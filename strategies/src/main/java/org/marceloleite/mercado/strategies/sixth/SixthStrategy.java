package org.marceloleite.mercado.strategies.sixth;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphic;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphicData;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyStatistics;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class SixthStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private TemporalTicker baseTemporalTicker;

	private TemporalTicker lastTemporalTicker;

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private SixthStrategyStatus status;

	private BigDecimal workingAmountCurrency;

	private SixthStrategyStatistics sixthStrategyStatistics;

	private SixthStrategyGraphic sixthStrategyGraphic;

	public SixthStrategy(Strategy strategy) {
		super(strategy);
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
		sixthStrategyStatistics.addInformation(temporalTicker, timeInterval, getCurrency());
		sixthStrategyGraphic.addInformation(temporalTicker);
	}

	private void analyseStrategyAccortingToStatus(TimeInterval timeInterval, Account account, House house) {
		Order result = null;
		switch (status) {
		case UNDEFINED:
			result = analyseStrategyStatusUndefined(timeInterval, account, house);
			break;
		case SAVED:
			result = analyseStrategyStatusSaved(timeInterval, account, house);
			break;
		case APPLIED:
			result = analyseStrategyStatusApplied(timeInterval, account, house);
			break;
		}
		checkOrderCreated(account, house, result);
	}

	private void checkOrderCreated(Account account, House house, Order order) {
		if (order != null) {
			executeOrder(order, account, house);
		}
	}

	private Order analyseStrategyStatusApplied(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		Statistics lastPriceStatistics = sixthStrategyStatistics.getLastPriceStatistics();
		Order result = null;
		if (lastPriceStatistics.getRatio() > 0) {
			if (temporalTicker.getCurrentOrPreviousLast()
					.doubleValue() > lastPriceStatistics.getBase()) {
				updateBase(temporalTicker);
			}
		} else if (lastPriceStatistics.getRatio() <= shrinkPercentageThreshold
				&& sixthStrategyStatistics.getAveragePriceStatistics()
						.getVariation() < 0) {
			updateBase(temporalTicker);
			result = createSellOrder(timeInterval, account, house);
		}
		return result;
	}

	private Order analyseStrategyStatusSaved(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		Statistics lastPriceStatistics = sixthStrategyStatistics.getLastPriceStatistics();
		Order result = null;
		if (lastPriceStatistics.getRatio() < 0) {
			if (temporalTicker.getCurrentOrPreviousLast()
					.doubleValue() < lastPriceStatistics.getBase()) {
				updateBase(temporalTicker);
			}
		} else if (lastPriceStatistics.getRatio() >= growthPercentageThreshold
				&& sixthStrategyStatistics.getAveragePriceStatistics()
						.getVariation() > 0) {
			updateBase(temporalTicker);
			result = createBuyOrder(timeInterval, account, house);
		}
		return result;
	}

	private Order analyseStrategyStatusUndefined(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		Statistics lastPriceStatistics = sixthStrategyStatistics.getLastPriceStatistics();
		Order result = null;
		if (lastPriceStatistics.getRatio() > 0) {
			updateBase(temporalTicker);
			addLimitPointsOnGraphicFor(temporalTicker);
			result = createBuyOrder(timeInterval, account, house);
		}
		return result;
	}

	private void updateValues(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		setBaseIfNull(temporalTicker);
		lastTemporalTicker = temporalTicker;
	}

	private void checkSendDailyGraphic(Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());

		if (sixthStrategyGraphic != null) {
			ZonedDateTime time = temporalTicker.getEnd();
			if (sixthStrategyGraphic.isTimeToSendGraphic(time)) {
				addLimitsOnGraphicDatas(time);
				sixthStrategyGraphic.sendDailyGraphic(time, account.getEmail());
			}
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
			sixthStrategyStatistics.getLastPriceStatistics()
					.setBase(baseTemporalTicker.getCurrentOrPreviousLast()
							.doubleValue());
			// addLimitsOnGraphicDatas(temporalTicker.getStart());
		}
	}

	private void updateBase(TemporalTicker temporalTicker) {
		sixthStrategyStatistics.getLastPriceStatistics()
				.setBase(temporalTicker.getCurrentOrPreviousLast()
						.doubleValue());
	}

	private void addLimitPointsOnGraphicFor(TemporalTicker temporalTicker) {
		if (sixthStrategyGraphic != null) {
			addLimitsOnGraphicDatas(temporalTicker.getStart());
			// addLimitsOnGraphicDatas(temporalTicker.getEnd());
		}
	}

	private void addLimitsOnGraphicDatas(ZonedDateTime zonedDateTime) {
		if (sixthStrategyGraphic != null) {
			addUpperLimitPoinOnGraphic(zonedDateTime);
			addLowerLimitPointOnGraphic(zonedDateTime);
		}
	}

	private void addUpperLimitPoinOnGraphic(ZonedDateTime zonedDateTime) {
		sixthStrategyGraphic.addPointOnGraphicData(SixthStrategyGraphicData.UPPER_LIMIT, zonedDateTime,
				calculateLimitValue(growthPercentageThreshold));
	}

	private void addLowerLimitPointOnGraphic(ZonedDateTime zonedDateTime) {
		sixthStrategyGraphic.addPointOnGraphicData(SixthStrategyGraphicData.LOWER_LIMIT, zonedDateTime,
				calculateLimitValue(shrinkPercentageThreshold));
	}

	private Double calculateLimitValue(double percentageThreshold) {
		return sixthStrategyStatistics.getLastPriceStatistics()
				.getBase() * (1.0 + percentageThreshold);
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, getCurrency());
		try {
			orderAnalyser.setSecond(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderAnalyser.getSecond())
				.receivingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeUtils.format(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account, OrderType.BUY, calculateCurrencyAmountUnitPrice(house),
				Currency.REAL, getCurrency());

		try {
			orderAnalyser.setFirst(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderAnalyser.getSecond())
				.payingUnitPriceOf(orderAnalyser.getUnitPrice())
				.build();
		LOGGER.info(ZonedDateTimeUtils.format(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderAnalyser orderAnalyser, Account account) {
		Currency currency = null;
		BigDecimal amount = null;
		switch (orderAnalyser.getOrderType()) {
		case BUY:
			currency = Currency.REAL;
			if (workingAmountCurrency.compareTo(BigDecimal.ZERO) > 0) {
				if (account.getWallet()
						.getBalanceFor(currency)
						.getAmount()
						.compareTo(workingAmountCurrency) > 0) {
					amount = workingAmountCurrency;
				} else {
					amount = new BigDecimal(account.getWallet()
							.getBalanceFor(currency)
							.getAmount()
							.toString());
				}
			} else {
				amount = new BigDecimal(account.getWallet()
						.getBalanceFor(currency)
						.getAmount()
						.toString());
			}
			break;
		case SELL:
			currency = getCurrency();
			amount = new BigDecimal(account.getWallet()
					.getBalanceFor(currency)
					.getAmount()
					.toString());
			break;
		}
		CurrencyAmount orderAmount = new CurrencyAmount(currency, amount);
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
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

	private SixthStrategyGraphic createSixStrategyGraphic(boolean generate) {

		SixthStrategyGraphic sixStrategyGraphic = null;
		if (generate) {
			sixStrategyGraphic = new SixthStrategyGraphic();
		}
		return sixStrategyGraphic;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(getCurrency())
				.getCurrentOrPreviousLast();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	@Override
	public void afterFinish() {
		if (sixthStrategyGraphic != null) {
			if (lastTemporalTicker != null) {
				addLimitsOnGraphicDatas(lastTemporalTicker.getStart());
			}
			sixthStrategyGraphic.createGraphicFile();
		}
	}

	@Override
	protected void setParameter(String name, Object object) {
		switch (SixthStrategyParameter.findByName(name)) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = (Double) object;
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = (Double) object;
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = new BigDecimal((Double) object);
			break;
		case CIRCULAR_ARRAY_SIZE:
			sixthStrategyStatistics.setCircularArraySize((Integer) object);
			break;
		case INITIAL_STATUS:
			status = (SixthStrategyStatus) object;
			break;
		case GENERATE_DAILY_GRAPHIC:
			this.sixthStrategyGraphic = createSixStrategyGraphic((Boolean) object);
			break;
		case NEXT_VALUE_STEPS:
			sixthStrategyStatistics.setNextValueSteps((Integer) object);
		}

		sixthStrategyStatistics.checkFieldsAndCreateStatistics();
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (SixthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = (TemporalTicker) object;
			sixthStrategyStatistics.getLastPriceStatistics()
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
			this.sixthStrategyGraphic = createSixStrategyGraphic((Boolean) object);
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
			result = new Boolean(sixthStrategyGraphic != null);
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
