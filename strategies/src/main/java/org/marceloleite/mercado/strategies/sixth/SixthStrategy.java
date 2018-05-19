package org.marceloleite.mercado.strategies.sixth;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.alarm.Alarm;
import org.marceloleite.mercado.commons.alarm.DailyAlarm;
import org.marceloleite.mercado.commons.alarm.Schedule;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategies.sixth.analyser.StatusAnalysers;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphic;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphicSender;
import org.marceloleite.mercado.strategies.sixth.parameter.SixthStrategyParameter;
import org.marceloleite.mercado.strategies.sixth.parameter.SixthStrategyParametersReader;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class SixthStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private TemporalTicker baseTemporalTicker;

	private TemporalTicker lastTemporalTicker;

	private SixthStrategyStatus status;

	private BigDecimal workingAmountCurrency;

	private SixthStrategyStatistics statistics;

	private SixthStrategyGraphic graphic;

	private SixthStrategyThresholds thresholds;

	private Schedule generateDailyGraphicSchedule;

	private SixthStrategyGraphicSender graphicSender;

	private SixthStrategyParametersReader parametersReader;

	private StatusAnalysers statusAnalysers;

	public SixthStrategy(Strategy strategy) {
		super(strategy);
		this.parametersReader = new SixthStrategyParametersReader();
		this.statusAnalysers = createStatusAnalysers();
	}

	private StatusAnalysers createStatusAnalysers() {
		return StatusAnalysers.builder()
				.strategy(this)
				.build();
	}

	public BigDecimal getWorkingAmountCurrency() {
		return workingAmountCurrency;
	}

	public SixthStrategyStatistics getStatistics() {
		return statistics;
	}

	public SixthStrategyGraphic getGraphic() {
		return graphic;
	}

	public SixthStrategyThresholds getThresholds() {
		return thresholds;
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		updateValues(house);
		addInformation(timeInterval, house);
		analyseStrategyAccortingToStatus(timeInterval, account, house);
		checkSendDailyGraphic(account, house);
	}

	private void addInformation(TimeInterval timeInterval, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		statistics.addInformation(temporalTicker, timeInterval, getCurrency());
		if (graphic != null) {
			graphic.addInformation(temporalTicker);
		}
	}

	private void analyseStrategyAccortingToStatus(TimeInterval timeInterval, Account account, House house) {
		Order result = statusAnalysers.getStatusAnalyser(status)
				.analyse(timeInterval, account, house);
		checkOrderCreated(account, house, result);
	}

	private void checkOrderCreated(Account account, House house, Order order) {
		if (order != null) {
			executeOrder(order, account, house);
		}
	}

	private void updateValues(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		setBaseIfNull(temporalTicker);
		lastTemporalTicker = temporalTicker;
	}

	private void checkSendDailyGraphic(Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		generateDailyGraphicSchedule.check(temporalTicker.getStart());
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
			statistics.getLastPriceStatistics()
					.setBase(baseTemporalTicker.getCurrentOrPreviousLast()
							.doubleValue());
		}
	}

	public void updateBase(TemporalTicker temporalTicker) {
		statistics.getLastPriceStatistics()
				.setBase(temporalTicker.getCurrentOrPreviousLast()
						.doubleValue());
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor()
				.placeOrder(order, house, account);

		switch (order.getStatus()) {
		case OPEN:
		case UNDEFINED:
			throw new RuntimeException(
					"Requested " + order + " execution, but its status returned as \"" + order.getStatus() + "\".");
		case FILLED:
			switch (order.getType()) {
			case BUY:
				status = SixthStrategyStatus.APPLIED;
				break;
			case SELL:
				status = SixthStrategyStatus.SAVED;
				break;
			}
			break;
		case CANCELLED:
			LOGGER.info(order + " cancelled.");
			break;
		}
	}

	@Override
	public void afterFinish() {
		if (graphic != null && lastTemporalTicker != null) {
			graphic.addLimitPointsOnGraphicData(lastTemporalTicker.getEnd());
			graphicSender.run();
		}
	}

	@Override
	protected void setParameter(String name, Object object) {
		parametersReader.setParameter(name, object);
		retrieveParameters();
	}

	private void retrieveParameters() {
		if (parametersReader.doneReading()) {
			thresholds = parametersReader.getSixthStrategyThresholds();
			statistics = parametersReader.getSixthStrategyStatistics();
			graphic = parametersReader.getSixthStrategyGraphic();
			status = parametersReader.getStatus();
			workingAmountCurrency = parametersReader.getWorkingAmountCurrency();
			generateDailyGraphicSchedule = createGenerateDailyGraphicSchedule();
		}
	}

	private Schedule createGenerateDailyGraphicSchedule() {
		Schedule schedule = null;
		if (parametersReader.getCreateDailyGraphic()) {
			Alarm alarm = DailyAlarm.builder()
					.ringsAt(parametersReader.getDailyGraphicTime())
					.build();
			this.graphicSender = new SixthStrategyGraphicSender(getStrategy().getAccount()
					.getEmail(), graphic);

			schedule = new Schedule(alarm, graphicSender);
		}
		return schedule;
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (SixthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = (TemporalTicker) object;
			statistics.getLastPriceStatistics()
					.setBase(baseTemporalTicker.getCurrentOrPreviousLast()
							.doubleValue());
			break;
		case STATUS:
			status = (SixthStrategyStatus) object;
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = (BigDecimal) object;
			break;
		case GENERATE_DAILY_GRAPHIC:
			// TODO How to fix this (perhaps create a variable reader)?
			// this.sixthStrategyGraphic = createSixStrategyGraphic((Boolean) object);
			break;
		}
	}

	@Override
	protected Object getVariable(String name) {
		Object result = null;
		switch (SixthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			result = baseTemporalTicker;
			break;
		case STATUS:
			result = status;
			break;
		case WORKING_AMOUNT_CURRENCY:
			result = workingAmountCurrency;
			break;
		case GENERATE_DAILY_GRAPHIC:
			result = new Boolean(graphic != null);
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}

		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return SixthStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return SixthStrategyVariable.getObjectDefinitions();
	}
}
