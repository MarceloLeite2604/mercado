package org.marceloleite.mercado.strategies.sixth;

import java.awt.Dimension;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
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
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.graphic.Graphic;
import org.marceloleite.mercado.commons.graphic.GraphicData;
import org.marceloleite.mercado.commons.graphic.MercadoRangeAxis;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.retriever.TemporalTickerCreator;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class SixthStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private TemporalTicker baseTemporalTicker;

	private MercadoBigDecimal growthPercentageThreshold;

	private MercadoBigDecimal shrinkPercentageThreshold;

	private Currency currency;

	private SixthStrategyStatus status;

	private MercadoBigDecimal workingAmountCurrency;

	private Statistics lastPriceStatistics;
	private Statistics buyOrdersStatistics;
	private Statistics sellOrdersStatistics;
	private Statistics orderStatistics;

	private Integer circularArraySize;

	private Integer nextValueSteps;

	private Graphic graphic;

	private GraphicData lastPriceGraphicData;
	
	private GraphicData averageLastPriceGraphicData;

	public SixthStrategy(Currency currency) {
		super(SixthStrategyParameter.class, SixthStrategyVariable.class);
		this.currency = currency;
		this.status = null;
		this.circularArraySize = null;
		this.nextValueSteps = null;
		this.graphic = new Graphic();
		this.graphic.setDimension(new Dimension(8192, 768));
		this.lastPriceGraphicData = new GraphicData("Last price", StandardXYItemRenderer.LINES,
				MercadoRangeAxis.CURRENCY_REAL);
		this.averageLastPriceGraphicData = new GraphicData("Average last price", StandardXYItemRenderer.LINES,
				MercadoRangeAxis.CURRENCY_REAL);
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
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval,
				house);
		if (temporalTickerVariation != null) {
			addToStatistics(temporalTicker, simulationTimeInterval);
			addToGraphic(temporalTicker);

			MercadoBigDecimal baseLastPrice = lastPriceStatistics.getBase();
			MercadoBigDecimal lastPrice = temporalTicker.retrieveCurrentOrPreviousLastPrice();

			StringBuilder stringBuilderDebug = new StringBuilder();
			stringBuilderDebug
					.append(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + " ");
			stringBuilderDebug.append(new NonDigitalCurrencyFormatter().format(lastPrice) + " ");
			stringBuilderDebug.append(lastPriceStatistics);
			// LOGGER.debug(stringBuilderDebug.toString());

			if (lastPriceStatistics.getNext().compareTo(MercadoBigDecimal.NOT_A_NUMBER) != 0) {
				Order order = null;
				switch (status) {
				case UNDEFINED:
					if (lastPriceStatistics.getNext().compareTo(MercadoBigDecimal.ZERO) > 0) {
						updateBase(temporalTicker);
						order = createBuyOrder(simulationTimeInterval, account, house);
					}
					break;
				case SAVED:
					if (lastPriceStatistics.getRatio().compareTo(baseLastPrice) < 0) {
						updateBase(temporalTicker);
					} else if (lastPriceStatistics.getRatio().compareTo(growthPercentageThreshold) >= 0) {
						updateBase(temporalTicker);
						order = createBuyOrder(simulationTimeInterval, account, house);
					}
					break;
				case APPLIED:
					if (lastPriceStatistics.getRatio().compareTo(baseLastPrice) > 0) {
						updateBase(temporalTicker);
					} else if (lastPriceStatistics.getRatio().compareTo(shrinkPercentageThreshold) <= 0) {
						updateBase(temporalTicker);
						order = createSellOrder(simulationTimeInterval, account, house);
					}
					break;
				}
				if (order != null) {
					executeOrder(order, account, house);
				}
			}
		}
	}

	private void addToGraphic(TemporalTicker temporalTicker) {
		Double lastPrice = temporalTicker.retrieveCurrentOrPreviousLastPrice().doubleValue();
//		Double buyOrders = buyOrdersStatistics.getAverage().doubleValue();
//		Double sellOrders = sellOrdersStatistics.getAverage().doubleValue();
		MercadoBigDecimal averageLastPrice = lastPriceStatistics.getAverage();
//		Double averageLastPrice = average.doubleValue();
//		MercadoBigDecimal variation = lastPriceStatistics.getVariation();
//		Double nextLastPrice = average.add(variation.multiply(new MercadoBigDecimal(nextValueSteps))).doubleValue();
		lastPriceGraphicData.addValue(temporalTicker.getStart(), lastPrice.doubleValue());
		averageLastPriceGraphicData.addValue(temporalTicker.getStart(), averageLastPrice.doubleValue());
	}

	private void addToStatistics(TemporalTicker temporalTicker, TimeInterval timeInterval) {
		lastPriceStatistics.add(temporalTicker.retrieveCurrentOrPreviousLastPrice());
		MercadoBigDecimal buy = new MercadoBigDecimal(temporalTicker.getBuyOrders());
		buyOrdersStatistics.add(buy);
		MercadoBigDecimal sell = new MercadoBigDecimal(temporalTicker.getSellOrders());
		sellOrdersStatistics.add(sell);
		orderStatistics.add(buy.add(sell));
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
				MercadoBigDecimal temporaryBuy = new MercadoBigDecimal(temporalTickerForTimeInterval.getBuyOrders());
				buyOrdersStatistics.add(temporaryBuy);
				MercadoBigDecimal temporarySell = new MercadoBigDecimal(temporalTickerForTimeInterval.getSellOrders());
				sellOrdersStatistics.add(temporarySell);
				orderStatistics.add(temporaryBuy.add(temporarySell));
			}
		}
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
			lastPriceStatistics.setBase(baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
		}
	}

	private void updateBase(TemporalTicker temporalTicker) {
		baseTemporalTicker = temporalTicker;
		lastPriceStatistics.setBase(baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
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

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickers().get(currency);
		if (currentTemporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}

		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker,
				currentTemporalTicker);
		/* LOGGER.debug("Variation : " + temporalTickerVariation + "."); */
		return temporalTickerVariation;
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
		case NEXT_VALUE_STEPS:
			nextValueSteps = Integer.parseInt(parameter.getValue());
		}

		if (circularArraySize != null && nextValueSteps != null) {
			lastPriceStatistics = new Statistics(circularArraySize, nextValueSteps);
			buyOrdersStatistics = new Statistics(circularArraySize, nextValueSteps);
			sellOrdersStatistics = new Statistics(circularArraySize, nextValueSteps);
			orderStatistics = new Statistics(circularArraySize, nextValueSteps);
		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		MercadoBigDecimal lastPrice = house.getTemporalTickers().get(currency).retrieveCurrentOrPreviousLastPrice();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	@Override
	public void afterFinish() {
		graphic.put(lastPriceGraphicData);
		graphic.put(averageLastPriceGraphicData);
		graphic.writeGraphicToFile("graphic.png");
	}
}
