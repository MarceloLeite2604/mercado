package org.marceloleite.mercado.strategies.sixth;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.OrderAnalyser;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.Statistics;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.retriever.TemporalTickerCreator;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.strategies.AbstractStrategy;
import org.marceloleite.mercado.strategies.sixth.graphic.SixStrategyGraphic;
import org.marceloleite.mercado.strategies.sixth.graphic.SixthStrategyGraphicData;

public class SixthStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private static final LocalTime DAILY_GRAPHIC_TIME = LocalTime.of(8, 0, 0);

	private TemporalTicker baseTemporalTicker;

	private TemporalTicker lastTemporalTicker;

	private MercadoBigDecimal growthPercentageThreshold;

	private MercadoBigDecimal shrinkPercentageThreshold;

	private Currency currency;

	private SixthStrategyStatus status;

	private MercadoBigDecimal workingAmountCurrency;

	private Statistics lastPriceStatistics;

	private Statistics averagePriceStatistics;

	private Integer circularArraySize;

	private Integer nextValueSteps;

	private SixStrategyGraphic sixStrategyGraphic;

	private boolean generateDailyGraphic;

	public SixthStrategy(Currency currency) {
		super(SixthStrategyParameter.class, SixthStrategyVariable.class);
		this.currency = currency;
		this.status = null;
		this.circularArraySize = null;
		this.nextValueSteps = null;
		this.sixStrategyGraphic = new SixStrategyGraphic();
	}

	public SixthStrategy() {
		this(null);
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		setBaseIfNull(temporalTicker);
		lastTemporalTicker = temporalTicker;
		addToStatistics(temporalTicker, simulationTimeInterval);
		addToGraphicData(temporalTicker);

		if (lastPriceStatistics.getNext().compareTo(MercadoBigDecimal.NOT_A_NUMBER) != 0) {
			Order order = null;
			switch (status) {
			case UNDEFINED:
				if (lastPriceStatistics.getRatio().compareTo(MercadoBigDecimal.ZERO) > 0) {
					updateBase(temporalTicker);
					order = createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case SAVED:
				if (lastPriceStatistics.getRatio().compareTo(MercadoBigDecimal.ZERO) < 0) {
					if (temporalTicker.retrieveCurrentOrPreviousLastPrice()
							.compareTo(lastPriceStatistics.getBase()) < 0) {
						updateBase(temporalTicker);
					}
				} else if (lastPriceStatistics.getRatio().compareTo(growthPercentageThreshold) >= 0
						&& averagePriceStatistics.getVariation().compareTo(MercadoBigDecimal.ZERO) > 0) {
					updateBase(temporalTicker);
					order = createBuyOrder(simulationTimeInterval, account, house);
				}
				break;
			case APPLIED:
				if (lastPriceStatistics.getRatio().compareTo(MercadoBigDecimal.ZERO) > 0) {
					if (temporalTicker.retrieveCurrentOrPreviousLastPrice()
							.compareTo(lastPriceStatistics.getBase()) > 0) {
						updateBase(temporalTicker);
					}
				} else if (lastPriceStatistics.getRatio().compareTo(shrinkPercentageThreshold) <= 0
						&& averagePriceStatistics.getVariation().compareTo(MercadoBigDecimal.ZERO) < 0) {
					updateBase(temporalTicker);
					order = createSellOrder(simulationTimeInterval, account, house);
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
		if (generateDailyGraphic && temporalTicker.getEnd().toLocalTime().equals(DAILY_GRAPHIC_TIME)) {
			addLimitsToGraphicDatas(temporalTicker.getEnd());
			sixStrategyGraphic.createGraphicFile();
			sixStrategyGraphic.sendGraphic(account.getEmail());
		}
	}

	private void addToGraphicData(TemporalTicker temporalTicker) {
		Double lastPrice = temporalTicker.retrieveCurrentOrPreviousLastPrice().doubleValue();
		MercadoBigDecimal averageLastPrice = lastPriceStatistics.getAverage();
		sixStrategyGraphic.getGraphicDatas().get(SixthStrategyGraphicData.LAST_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), lastPrice.doubleValue());
		sixStrategyGraphic.getGraphicDatas().get(SixthStrategyGraphicData.AVERAGE_LAST_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), averageLastPrice.doubleValue());
		sixStrategyGraphic.getGraphicDatas().get(SixthStrategyGraphicData.BASE_PRICE.getTitle())
				.addValue(temporalTicker.getStart(), lastPriceStatistics.getBase().doubleValue());
	}

	private void addToStatistics(TemporalTicker temporalTicker, TimeInterval timeInterval) {
		lastPriceStatistics.add(temporalTicker.retrieveCurrentOrPreviousLastPrice());
		averagePriceStatistics.add(lastPriceStatistics.getAverage());
		CircularArray<MercadoBigDecimal> circularArray = lastPriceStatistics.getCircularArray();
		if (!circularArray.isFilled()) {
			Duration stepTime = timeInterval.getDuration();
			ZonedDateTime endTime = timeInterval.getStart();
			Duration duration = stepTime.multipliedBy(circularArray.getVacantPositions());
			TimeInterval timeIntervalToRetrieve = new TimeInterval(duration, endTime);
			TradesRetriever tradesRetriever = new TradesRetriever();

			List<Trade> trades = tradesRetriever.retrieve(currency, timeIntervalToRetrieve, false);
			TimeDivisionController timeDivisionController = new TimeDivisionController(timeIntervalToRetrieve,
					stepTime);
			TemporalTickerCreator temporalTickerCreator = new TemporalTickerCreator();
			List<TimeInterval> retrievedTimeIntervals = timeDivisionController.geTimeIntervals();
			for (TimeInterval retrievedTimeInterval : retrievedTimeIntervals) {
				List<Trade> tradesOnTimeInterval = trades.stream()
						.filter(trade -> ZonedDateTimeUtils.isBetween(trade.getDate(), retrievedTimeInterval))
						.collect(Collectors.toList());

				TemporalTicker temporalTickerForTimeInterval = temporalTickerCreator.create(currency,
						retrievedTimeInterval, tradesOnTimeInterval);
				lastPriceStatistics.add(temporalTickerForTimeInterval.retrieveCurrentOrPreviousLastPrice());
			}
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
			lastPriceStatistics.setBase(baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
			addLimitsToGraphicDatas(temporalTicker.getStart());
		}
	}

	private void updateBase(TemporalTicker temporalTicker) {
		addLimitsToGraphicDatas(temporalTicker.getStart());
		lastPriceStatistics.setBase(temporalTicker.retrieveCurrentOrPreviousLastPrice());
		addLimitsToGraphicDatas(temporalTicker.getEnd());
	}

	private void addLimitsToGraphicDatas(ZonedDateTime zonedDateTime) {
		double baseLastPrice = lastPriceStatistics.getBase().doubleValue();
		Double upperLimit = baseLastPrice * (1.0 + growthPercentageThreshold.doubleValue());
		Double lowerLimit = baseLastPrice * (1.0 + shrinkPercentageThreshold.doubleValue());
		sixStrategyGraphic.getGraphicDatas().get(SixthStrategyGraphicData.UPPER_LIMIT.getTitle())
				.addValue(zonedDateTime, upperLimit);
		sixStrategyGraphic.getGraphicDatas().get(SixthStrategyGraphicData.LOWER_LIMIT.getTitle())
				.addValue(zonedDateTime, lowerLimit);
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account.getBalance(), OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);
		try {
			orderAnalyser.setSecond(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderAnalyser.getSecond()).receivingUnitPriceOf(orderAnalyser.getUnitPrice()).build();
		LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ order + ".");
		return order;
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderAnalyser = new OrderAnalyser(account.getBalance(), OrderType.BUY,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);

		try {
			orderAnalyser.setFirst(calculateOrderAmount(orderAnalyser, account));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderAnalyser.getSecond()).payingUnitPriceOf(orderAnalyser.getUnitPrice()).build();
		LOGGER.info(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + ": Created "
				+ order + ".");
		return order;
	}

	private CurrencyAmount calculateOrderAmount(OrderAnalyser orderAnalyser, Account account) {
		Currency currency = null;
		MercadoBigDecimal amount = null;
		switch (orderAnalyser.getOrderType()) {
		case BUY:
			currency = Currency.REAL;
			if (workingAmountCurrency.compareTo(MercadoBigDecimal.ZERO) > 0) {
				if (account.getBalance().get(currency).getAmount().compareTo(workingAmountCurrency) > 0) {
					amount = new MercadoBigDecimal(workingAmountCurrency);
				} else {
					amount = new MercadoBigDecimal(account.getBalance().get(currency).getAmount());
				}
			} else {
				amount = new MercadoBigDecimal(account.getBalance().get(currency).getAmount());
			}
			break;
		case SELL:
			currency = this.currency;
			amount = new MercadoBigDecimal(account.getBalance().get(currency).getAmount());
			break;
		}
		CurrencyAmount orderAmount = new CurrencyAmount(currency, amount);
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor().placeOrder(order, house, account);

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
	protected Property retrieveVariable(String name) {
		SixthStrategyVariable fifthStrategyVariable = SixthStrategyVariable.findByName(name);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		String json = null;
		switch (fifthStrategyVariable) {
		case BASE_TEMPORAL_TICKER:
			json = objectToJsonConverter.convertTo(baseTemporalTicker);
			break;
		case STATUS:
			json = objectToJsonConverter.convertTo(status);
			break;
		case WORKING_AMOUNT_CURRENCY:
			json = objectToJsonConverter.convertTo(workingAmountCurrency);
			break;
		}

		fifthStrategyVariable.setValue(json);
		return fifthStrategyVariable;
	}

	@Override
	protected void defineVariable(Property variable) {
		SixthStrategyVariable strategyVariable = SixthStrategyVariable.findByName(variable.getName());
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		switch (strategyVariable) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = new TemporalTicker();
			baseTemporalTicker = objectToJsonConverter.convertFromToObject(variable.getValue(), baseTemporalTicker);
			lastPriceStatistics.setBase(baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
			break;
		case STATUS:
			status = objectToJsonConverter.convertFromToObject(variable.getValue(), status);
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = new MercadoBigDecimal();
			workingAmountCurrency = objectToJsonConverter.convertFromToObject(variable.getValue(),
					workingAmountCurrency);
			break;
		}
	}

	@Override
	protected void defineParameter(Property parameter) {
		SixthStrategyParameter strategyParameter = SixthStrategyParameter.findByName(parameter.getName());
		switch (strategyParameter) {
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = new MercadoBigDecimal(parameter.getValue());
			break;
		case CIRCULAR_ARRAY_SIZE:
			circularArraySize = Integer.parseInt(parameter.getValue());
			break;
		case INITIAL_STATUS:
			status = SixthStrategyStatus
					.findByName(Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue()));
			break;
		case GENERATE_DAILY_GRAPHIC:
			generateDailyGraphic = Boolean
					.parseBoolean(Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue()));
			break;
		case NEXT_VALUE_STEPS:
			nextValueSteps = Integer.parseInt(parameter.getValue());
		}

		if (circularArraySize != null && nextValueSteps != null) {
			lastPriceStatistics = new Statistics(circularArraySize, nextValueSteps);
			averagePriceStatistics = new Statistics(circularArraySize, nextValueSteps);
		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		MercadoBigDecimal lastPrice = house.getTemporalTickers().get(currency).retrieveCurrentOrPreviousLastPrice();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	@Override
	public void afterFinish() {
		if (lastTemporalTicker != null) {
			addLimitsToGraphicDatas(lastTemporalTicker.getStart());
		}
		sixStrategyGraphic.createGraphicFile();
	}
}
