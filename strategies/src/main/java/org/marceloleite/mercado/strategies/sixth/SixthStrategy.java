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

	private static final String SIMULATION_GRAPHIC_EMAIL_SUBJECT = "Simulation graphic";

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);
	
	private SixthStrategyParametersReader parametersReader;

	private SixthStrategyStatus status;

	private BigDecimal workingAmountCurrency;
	
	private SixthStrategyThresholds thresholds;

	private SixthStrategyStatistics statistics;

	private SixthStrategyGraphic graphic;
	
	private SixthStrategyGraphicSender graphicSender;

	private Schedule generateDailyGraphicSchedule;

	private StatusAnalysers statusAnalysers;

	private boolean firstRun;

	public SixthStrategy(Strategy strategy) {
		super(strategy);
		this.thresholds = parametersReader.getSixthStrategyThresholds();
		this.statistics = parametersReader.getSixthStrategyStatistics();
		this.graphic = parametersReader.getSixthStrategyGraphic();
		this.status = parametersReader.getStatus();
		this.workingAmountCurrency = parametersReader.getWorkingAmountCurrency();
		this.statusAnalysers = createStatusAnalysers();
		this.graphicSender = createGraphicSender();
		this.generateDailyGraphicSchedule = createGenerateDailyGraphicSchedule();
		this.firstRun = true;
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
		firstRun = false;
	}

	private void addInformation(TimeInterval timeInterval, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		statistics.addInformation(temporalTicker, timeInterval, getCurrency());
		if (graphic != null) {
			graphic.addInformation(temporalTicker);
			addLimitPointsOnFirstRun(temporalTicker);
		}
	}

	private void addLimitPointsOnFirstRun(TemporalTicker temporalTicker) {
		if (firstRun) {
			graphic.addLimitPointsOnGraphicData(temporalTicker.getStart());
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
	}

	private void checkSendDailyGraphic(Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		if (generateDailyGraphicSchedule != null) {
			generateDailyGraphicSchedule.check(temporalTicker.getStart());
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (firstRun) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			// baseTemporalTicker = temporalTicker;
			statistics.getLastPriceStatistics()
					.setBase(temporalTicker.getCurrentOrPreviousLast()
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
		if (parametersReader.getCreateGraphicAtEndOfExecution() && graphic != null) {
			graphic.addLimitPointsOnLastTimeAvailable();
			graphicSender.setEmailSubject(SIMULATION_GRAPHIC_EMAIL_SUBJECT);
			graphicSender.run();
		}
	}

	@Override
	protected void setParameter(String name, Object object) {
		createParameterReaderIfNull();
		parametersReader.setParameter(name, object);
	}

	private void createParameterReaderIfNull() {
		if (parametersReader == null) {
			parametersReader = new SixthStrategyParametersReader();
		}
	}

	private Schedule createGenerateDailyGraphicSchedule() {
		Schedule schedule = null;
		if (parametersReader.getCreateDailyGraphic()) {
			Alarm alarm = DailyAlarm.builder()
					.ringsAt(parametersReader.getDailyGraphicTime())
					.build();

			schedule = new Schedule(alarm, graphicSender);
		}
		return schedule;
	}

	private SixthStrategyGraphicSender createGraphicSender() {
		SixthStrategyGraphicSender sixthStrategyGraphicSender = null;
		if (graphic != null) {
			sixthStrategyGraphicSender = new SixthStrategyGraphicSender(getStrategy().getAccount()
					.getEmail(), graphic);
		}
		return sixthStrategyGraphicSender;
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (SixthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			// baseTemporalTicker = new TemporalTicker();
			TemporalTicker temporalTicker = (TemporalTicker) object;
			statistics.getLastPriceStatistics()
					.setBase(temporalTicker.getCurrentOrPreviousLast()
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
			result = statistics.getLastPriceStatistics()
					.getBase();
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
