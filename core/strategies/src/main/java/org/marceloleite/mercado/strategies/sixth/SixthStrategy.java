package org.marceloleite.mercado.strategies.sixth;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.marceloleite.mercado.base.model.util.VariationCalculator;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.retriever.TemporalTickerCreator;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class SixthStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(SixthStrategy.class);

	private MercadoBigDecimal growthPercentageThreshold;

	private MercadoBigDecimal shrinkPercentageThreshold;

	private Currency currency;

	private TemporalTicker baseTemporalTicker;

	private SixthStrategyStatus status;

	private MercadoBigDecimal workingAmountCurrency;

	private int circularArraySize;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	public SixthStrategy(Currency currency) {
		super(SixthStrategyParameter.class, SixthStrategyVariable.class);
		this.currency = currency;
		this.status = null;
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
			temporalTickerCircularArray = addToTemporalTickerCircularArray(temporalTickerCircularArray, temporalTicker,
					simulationTimeInterval);
			MercadoBigDecimal nextPrice = calculateNextPrice(temporalTicker);
			MercadoBigDecimal lastVariation = new VariationCalculator().calculate(nextPrice,
					baseTemporalTicker.retrieveCurrentOrPreviousLastPrice());
			MercadoBigDecimal baseLastPrice = baseTemporalTicker.retrieveCurrentOrPreviousLastPrice();
			MercadoBigDecimal lastPrice = temporalTicker.retrieveCurrentOrPreviousLastPrice();

			StringBuilder stringBuilderDebug = new StringBuilder();
			stringBuilderDebug
					.append(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getStart()) + " ");
			stringBuilderDebug.append("Variation: " + new PercentageFormatter().format(lastVariation));
			NonDigitalCurrencyFormatter nonDigitalCurrencyFormatter = new NonDigitalCurrencyFormatter();
			stringBuilderDebug.append(", base: " + nonDigitalCurrencyFormatter.format(baseLastPrice));
			stringBuilderDebug.append(", last: " + nonDigitalCurrencyFormatter.format(lastPrice));
			stringBuilderDebug.append(", next: " + nonDigitalCurrencyFormatter.format(nextPrice));
			// LOGGER.debug(stringBuilderDebug.toString());

			if (lastVariation.compareTo(MercadoBigDecimal.NOT_A_NUMBER) != 0) {
				Order order = null;
				switch (status) {
				case UNDEFINED:
					if (lastVariation.compareTo(MercadoBigDecimal.ZERO) > 0) {
						updateBase(house);
						order = createBuyOrder(simulationTimeInterval, account, house);
					}
					break;
				case SAVED:
					if (lastVariation.compareTo(MercadoBigDecimal.ZERO) < 0) {
						updateBase(house);
					} else if (lastVariation.compareTo(growthPercentageThreshold) >= 0) {
						updateBase(house);
						order = createBuyOrder(simulationTimeInterval, account, house);
					}
					break;
				case APPLIED:
					if (lastVariation.compareTo(MercadoBigDecimal.ZERO) > 0) {
						updateBase(house);
					} else if (lastVariation.compareTo(shrinkPercentageThreshold) <= 0) {
						updateBase(house);
						order = createSellOrder(simulationTimeInterval, account, house);
					}
					break;
				}
				if (order != null) {
					executeOrder(order, account, house);
//					temporalTickerCircularArray.clear();
				}
			}
		}
	}

	private CircularArray<TemporalTicker> addToTemporalTickerCircularArray(
			CircularArray<TemporalTicker> temporalTickerCircularArray, TemporalTicker temporalTicker,
			TimeInterval timeInterval) {
		if (temporalTickerCircularArray.getSize() < circularArraySize) {
			Duration stepTime = timeInterval.getDuration();
			ZonedDateTime endTime = timeInterval.getStart().minus(stepTime);
			Duration duration = stepTime.multipliedBy(circularArraySize - temporalTickerCircularArray.getSize());
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
				temporalTickerCircularArray.add(temporalTickerForTimeInterval);
			}

			/*for (int counter = temporalTickerCircularArray.getSize(); counter < circularArraySize; counter++) {
				temporalTickerCircularArray.add(temporalTicker);
			}*/
		}

		temporalTickerCircularArray.add(temporalTicker);
		return temporalTickerCircularArray;
	}

	private MercadoBigDecimal calculateNextPrice(TemporalTicker currentTemporalTicker) {
		List<TemporalTicker> temporalTickersList = temporalTickerCircularArray.asList();
		List<MercadoBigDecimal> lasts = temporalTickersList.stream()
				.map(temporalTicker -> temporalTicker.retrieveCurrentOrPreviousLastPrice())
				.collect(Collectors.toList());
		List<MercadoBigDecimal> derivativeLasts = new ArrayList<>(lasts.size());
		MercadoBigDecimal previousLast = null;
		MercadoBigDecimal sumLast = new MercadoBigDecimal("0");
		for (int counter = 0; counter < lasts.size(); counter++) {
			if (counter == 0) {
				previousLast = lasts.get(counter);
			} else {
				previousLast = lasts.get(counter - 1);
			}
			MercadoBigDecimal currentLast = lasts.get(counter);

			MercadoBigDecimal subtract = currentLast.subtract(previousLast);
			// LOGGER.debug("Subtract: " + new DigitalCurrencyFormatter().format(subtract));
			derivativeLasts.add(subtract);
			sumLast = sumLast.add(currentLast);
		}
		// LOGGER.debug("Sumlast: " + new DigitalCurrencyFormatter().format(sumLast));

		double sum = derivativeLasts.parallelStream().mapToDouble(derivativeLast -> derivativeLast.doubleValue()).sum();
		MercadoBigDecimal derivativeLastSum = new MercadoBigDecimal(sum);
		// LOGGER.debug("Derivative last sum: " + new
		// DigitalCurrencyFormatter().format(derivativeLastSum));

		MercadoBigDecimal arraySize = new MercadoBigDecimal(temporalTickerCircularArray.getSize());
		// LOGGER.debug("Array size: " + new
		// DigitalCurrencyFormatter().format(arraySize));
		MercadoBigDecimal variation = derivativeLastSum.divide(arraySize);
		// LOGGER.debug("Variation: " + new
		// DigitalCurrencyFormatter().format(variation));

		MercadoBigDecimal averageLast = sumLast.divide(arraySize);

		// return averageLast.add(variation.multiply(new MercadoBigDecimal("72")));
		return averageLast.add(variation);
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
		}
	}

	private void updateBase(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
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
			temporalTickerCircularArray = new CircularArray<>(circularArraySize);
			break;
		case INITIAL_STATUS:
			status = SixthStrategyStatus
					.findByName(Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue()));
			break;
		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		MercadoBigDecimal lastPrice = house.getTemporalTickers().get(currency).retrieveCurrentOrPreviousLastPrice();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}
}
