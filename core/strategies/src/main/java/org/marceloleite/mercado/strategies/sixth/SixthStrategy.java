package org.marceloleite.mercado.strategies.sixth;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerCreator;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.orderanalyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.strategies.sixth.graphic.SixStrategyGraphic;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphicData;
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

	private Statistics lastPriceStatistics;

	private Statistics averagePriceStatistics;

	private Integer circularArraySize;

	private Integer nextValueSteps;

	private SixStrategyGraphic sixStrategyGraphic;

	private TradeDAO tradeDAO;

	private TemporalTickerCreator temporalTickerCreator;

	public SixthStrategy(Strategy strategy) {
		super(strategy);
		this.status = null;
		this.circularArraySize = null;
		this.nextValueSteps = null;

		this.tradeDAO = CDI.current()
				.select(TradeDAO.class)
				.get();

		this.temporalTickerCreator = CDI.current()
				.select(TemporalTickerCreator.class)
				.get();
	}

	public SixthStrategy() {
		this(null);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		setBaseIfNull(temporalTicker);
		lastTemporalTicker = temporalTicker;
		addToStatistics(temporalTicker, timeInterval);
		addToGraphicData(temporalTicker);

		if (Double.isFinite(lastPriceStatistics.getNext())) {
			Order order = null;
			switch (status) {
			case UNDEFINED:
				if (lastPriceStatistics.getRatio() > 0) {
					updateBase(temporalTicker);
					order = createBuyOrder(timeInterval, account, house);
				}
				break;
			case SAVED:
				if (lastPriceStatistics.getRatio() < 0) {
					if (temporalTicker.getCurrentOrPreviousLast()
							.doubleValue() < lastPriceStatistics.getBase()) {
						updateBase(temporalTicker);
					}
				} else if (lastPriceStatistics.getRatio() >= growthPercentageThreshold
						&& averagePriceStatistics.getVariation() > 0) {
					updateBase(temporalTicker);
					order = createBuyOrder(timeInterval, account, house);
				}
				break;
			case APPLIED:
				if (lastPriceStatistics.getRatio() > 0) {
					if (temporalTicker.getCurrentOrPreviousLast()
							.doubleValue() > lastPriceStatistics.getBase()) {
						updateBase(temporalTicker);
					}
				} else if (lastPriceStatistics.getRatio() <= shrinkPercentageThreshold
						&& averagePriceStatistics.getVariation() < 0) {
					updateBase(temporalTicker);
					order = createSellOrder(timeInterval, account, house);
				}
				break;
			}
			if (order != null) {
				executeOrder(order, account, house);
			}
		}
		checkSendDailyGraphic(temporalTicker, account);
	}

	private void checkSendDailyGraphic(TemporalTicker temporalTicker, Account account) {

		if (sixStrategyGraphic != null) {
			ZonedDateTime time = temporalTicker.getEnd();
			if (sixStrategyGraphic.isTimeToSendGraphic(time)) {
				addLimitsToGraphicDatas(time);
				sixStrategyGraphic.sendDailyGraphic(time, account.getEmail());
			}
		}
	}

	private void addToGraphicData(TemporalTicker temporalTicker) {
		double lastPrice = temporalTicker.getCurrentOrPreviousLast()
				.doubleValue();
		double averageLastPrice = lastPriceStatistics.getAverage();
		sixStrategyGraphic.getGraphicDatas()
				.get(SixthStrategyGraphicData.LAST_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), lastPrice);
		sixStrategyGraphic.getGraphicDatas()
				.get(SixthStrategyGraphicData.AVERAGE_LAST_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), averageLastPrice);
		sixStrategyGraphic.getGraphicDatas()
				.get(SixthStrategyGraphicData.BASE_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), lastPriceStatistics.getBase());
	}

	private void addToStatistics(TemporalTicker temporalTicker, TimeInterval timeInterval) {
		lastPriceStatistics.add(temporalTicker.getCurrentOrPreviousLast()
				.doubleValue());
		averagePriceStatistics.add(lastPriceStatistics.getAverage());
		CircularArray<Double> circularArray = lastPriceStatistics.getCircularArray();
		if (!circularArray.isFilled()) {
			LOGGER.debug("Filling last price statistics circular array.");
			Duration stepTime = timeInterval.getDuration();
			ZonedDateTime endTime = timeInterval.getStart();
			Duration duration = stepTime.multipliedBy(circularArray.getVacantPositions());
			TimeInterval timeIntervalToRetrieve = new TimeInterval(duration, endTime);

			List<Trade> trades = tradeDAO.findByCurrencyAndTimeBetween(getCurrency(), timeIntervalToRetrieve.getStart(),
					timeIntervalToRetrieve.getEnd());
			TimeDivisionController timeDivisionController = new TimeDivisionController(timeIntervalToRetrieve,
					stepTime);
			List<TimeInterval> retrievedTimeIntervals = timeDivisionController.geTimeIntervals();
			for (TimeInterval retrievedTimeInterval : retrievedTimeIntervals) {
				List<Trade> tradesOnTimeInterval = trades.stream()
						.filter(trade -> ZonedDateTimeUtils.isBetween(trade.getTime(), retrievedTimeInterval))
						.collect(Collectors.toList());

				TemporalTicker temporalTickerForTimeInterval = temporalTickerCreator.create(getCurrency(),
						retrievedTimeInterval, tradesOnTimeInterval);
				lastPriceStatistics.add(temporalTickerForTimeInterval.getCurrentOrPreviousLast()
						.doubleValue());
			}
			LOGGER.debug("Filling concluded.");
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
			lastPriceStatistics.setBase(baseTemporalTicker.getCurrentOrPreviousLast()
					.doubleValue());
			addLimitsToGraphicDatas(temporalTicker.getStart());
		}
	}

	private void updateBase(TemporalTicker temporalTicker) {
		addLimitsToGraphicDatas(temporalTicker.getStart());
		lastPriceStatistics.setBase(temporalTicker.getCurrentOrPreviousLast()
				.doubleValue());
		addLimitsToGraphicDatas(temporalTicker.getEnd());
	}

	private void addLimitsToGraphicDatas(ZonedDateTime zonedDateTime) {
		double baseLastPrice = lastPriceStatistics.getBase();
		Double upperLimit = baseLastPrice * (1.0 + growthPercentageThreshold);
		Double lowerLimit = baseLastPrice * (1.0 + shrinkPercentageThreshold);
		sixStrategyGraphic.getGraphicDatas()
				.get(SixthStrategyGraphicData.UPPER_LIMIT.getTitle())
				.addValue(zonedDateTime, upperLimit);
		sixStrategyGraphic.getGraphicDatas()
				.get(SixthStrategyGraphicData.LOWER_LIMIT.getTitle())
				.addValue(zonedDateTime, lowerLimit);
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
		LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
				.convertTo(simulationTimeInterval.getStart()) + ": Created " + order + ".");
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
		LOGGER.info(ZonedDateTimeToStringConverter.getInstance()
				.convertTo(simulationTimeInterval.getStart()) + ": Created " + order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderAnalyser orderAnalyser, Account account) {
		Currency currency = null;
		BigDecimal amount = null;
		switch (orderAnalyser.getOrderType()) {
		case BUY:
			currency = Currency.REAL;
			if (workingAmountCurrency.compareTo(BigDecimal.ZERO) > 0) {
				if (account.getBalanceFor(currency)
						.compareTo(workingAmountCurrency) > 0) {
					amount = workingAmountCurrency;
				} else {
					amount = new BigDecimal(account.getBalanceFor(currency)
							.toString());
				}
			} else {
				amount = new BigDecimal(account.getBalanceFor(currency)
						.toString());
			}
			break;
		case SELL:
			currency = getCurrency();
			amount = new BigDecimal(account.getBalanceFor(currency)
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

	private SixStrategyGraphic createSixStrategyGraphic(boolean generate) {

		SixStrategyGraphic sixStrategyGraphic = null;
		if (generate) {
			sixStrategyGraphic = new SixStrategyGraphic();
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
		if (lastTemporalTicker != null) {
			addLimitsToGraphicDatas(lastTemporalTicker.getStart());
		}
		sixStrategyGraphic.createGraphicFile();
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
			workingAmountCurrency = (BigDecimal) object;
			break;
		case CIRCULAR_ARRAY_SIZE:
			circularArraySize = (Integer) object;
			break;
		case INITIAL_STATUS:
			status = (SixthStrategyStatus) object;
			break;
		case GENERATE_DAILY_GRAPHIC:
			this.sixStrategyGraphic = createSixStrategyGraphic((Boolean) object);
			break;
		case NEXT_VALUE_STEPS:
			nextValueSteps = (Integer) object;
		}

		if (circularArraySize != null && nextValueSteps != null) {
			lastPriceStatistics = new Statistics(circularArraySize, nextValueSteps);
			averagePriceStatistics = new Statistics(circularArraySize, nextValueSteps);
		}
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (SixthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = (TemporalTicker) object;
			lastPriceStatistics.setBase(baseTemporalTicker.getCurrentOrPreviousLast()
					.doubleValue());
			break;
		case STATUS:
			status = (SixthStrategyStatus) object;
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = (BigDecimal) object;
			break;
		case GENERATE_DAILY_GRAPHIC:
			this.sixStrategyGraphic = createSixStrategyGraphic((Boolean) object);
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
			result = new Boolean(sixStrategyGraphic != null);
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}

		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}
}
