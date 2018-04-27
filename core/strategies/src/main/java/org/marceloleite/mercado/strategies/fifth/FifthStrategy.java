package org.marceloleite.mercado.strategies.fifth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.VariationCalculator;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public class FifthStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(FifthStrategy.class);

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private TemporalTicker baseTemporalTicker;

	private FifthStrategyStatus status;

	private BigDecimal workingAmountCurrency;

	private int circularArraySize;

	private CircularArray<TemporalTicker> temporalTickerCircularArray;

	public FifthStrategy(Strategy strategy) {
		super(strategy);
		this.status = FifthStrategyStatus.UNDEFINED;
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		setBaseIfNull(temporalTicker);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(timeInterval, house);
		if (temporalTickerVariation != null) {
			temporalTickerCircularArray = addToTemporalTickerCircularArray(temporalTickerCircularArray, temporalTicker);
			BigDecimal nextPrice = calculateNextPrice(temporalTicker);
			double lastVariation = VariationCalculator.getInstance()
					.calculate(nextPrice, baseTemporalTicker.getCurrentOrPreviousLast());
			StringBuilder stringBuilderDebug = new StringBuilder();
			stringBuilderDebug.append("Variation: " + PercentageFormatter.getInstance()
					.format(lastVariation));
			BigDecimal baseLastPrice = baseTemporalTicker.getCurrentOrPreviousLast();
			stringBuilderDebug.append(", base: " + NonDigitalCurrencyFormatter.getInstance()
					.format(baseLastPrice));
			BigDecimal lastPrice = temporalTicker.getCurrentOrPreviousLast();
			stringBuilderDebug.append(", last: " + NonDigitalCurrencyFormatter.getInstance()
					.format(lastPrice));
			stringBuilderDebug.append(", next: " + NonDigitalCurrencyFormatter.getInstance()
					.format(nextPrice));
			// MercadoBigDecimal lastVariation = retrieveLastVariation();
			/*
			 * LOGGER.debug( simulationTimeInterval + ": Last variation is " + new
			 * PercentageFormatter().format(lastVariation));
			 */
			Order order = null;
			switch (status) {
			case UNDEFINED:
				if (Double.isFinite(lastVariation) && lastVariation > 0) {
					updateBase(house);
					order = createBuyOrder(timeInterval, account, house);
				}
				break;
			case SAVED:
				if (Double.isFinite(lastVariation) && lastVariation < 0) {
					updateBase(house);
				} else if (Double.isFinite(lastVariation) && lastVariation >= growthPercentageThreshold) {
					updateBase(house);
					order = createBuyOrder(timeInterval, account, house);
				}
				break;
			case APPLIED:
				if (Double.isFinite(lastVariation) && lastVariation > 0) {
					updateBase(house);
				} else if (Double.isFinite(lastVariation) && lastVariation <= shrinkPercentageThreshold) {
					updateBase(house);
					order = createSellOrder(timeInterval, account, house);
				}
				break;
			}
			if (order != null) {
				LOGGER.debug(stringBuilderDebug.toString());
				executeOrder(order, account, house);
			}
		}
	}

	private CircularArray<TemporalTicker> addToTemporalTickerCircularArray(
			CircularArray<TemporalTicker> temporalTickerCircularArray, TemporalTicker temporalTicker) {
		temporalTickerCircularArray.add(temporalTicker);
		if (temporalTickerCircularArray.getOccupiedPositions() < circularArraySize) {
			for (int counter = temporalTickerCircularArray
					.getOccupiedPositions(); counter < circularArraySize; counter++) {
				temporalTickerCircularArray.add(temporalTicker);
			}
		}
		return temporalTickerCircularArray;
	}

	private BigDecimal calculateNextPrice(TemporalTicker currentTemporalTicker) {
		List<TemporalTicker> temporalTickersList = temporalTickerCircularArray.asList();
		List<BigDecimal> lasts = temporalTickersList.stream()
				.map(temporalTicker -> temporalTicker.getCurrentOrPreviousLast())
				.collect(Collectors.toList());
		List<BigDecimal> derivativeLasts = new ArrayList<>();
		BigDecimal previousLast = new BigDecimal("0");
		BigDecimal sumLast = new BigDecimal("0");
		for (int counter = 0; counter < lasts.size(); counter++) {
			if (counter == 0) {
				previousLast = baseTemporalTicker.getCurrentOrPreviousLast();
			} else {
				previousLast = lasts.get(counter - 1);
			}
			BigDecimal currentLast = lasts.get(counter);

			derivativeLasts.add(currentLast.subtract(previousLast));
			sumLast = sumLast.add(currentLast);
		}

		double sum = derivativeLasts.parallelStream()
				.mapToDouble(derivativeLast -> derivativeLast.doubleValue())
				.sum();
		BigDecimal derivativeLastSum = new BigDecimal(sum);

		BigDecimal arraySize = new BigDecimal(temporalTickerCircularArray.getOccupiedPositions());
		BigDecimal variation = derivativeLastSum.divide(arraySize);

		BigDecimal averageLast = sumLast.divide(arraySize);

		return averageLast.add(variation);
	}

	private void setBaseIfNull(TemporalTicker temporalTicker) {
		if (baseTemporalTicker == null) {
			LOGGER.debug("Setting base temporal ticker as: " + temporalTicker + ".");
			baseTemporalTicker = temporalTicker;
		}
	}

	private void updateBase(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickerFor(getCurrency());
		if (temporalTicker != null) {
			baseTemporalTicker = temporalTicker;
		}
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
		Currency currency = (orderAnalyser.getOrderType() == OrderType.SELL ? getCurrency() : Currency.REAL);
		CurrencyAmount orderAmount = new CurrencyAmount(currency, account.getBalanceFor(currency));
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval, House house) {
		TemporalTicker currentTemporalTicker = house.getTemporalTickerFor(getCurrency());
		if (currentTemporalTicker == null) {
			throw new RuntimeException("No temporal ticker for time interval " + simulationTimeInterval);
		}

		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker,
				currentTemporalTicker);
		/* LOGGER.debug("Variation : " + temporalTickerVariation + "."); */
		return temporalTickerVariation;
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
				status = FifthStrategyStatus.APPLIED;
				break;
			case SELL:
				status = FifthStrategyStatus.SAVED;
				break;
			}
			break;
		case CANCELLED:
			LOGGER.info(order + " cancelled.");
			break;
		}
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(getCurrency())
				.getCurrentOrPreviousLast();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	@Override
	protected void setParameter(String name, Object object) {
		switch (FifthStrategyParameter.findByName(name)) {
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
			temporalTickerCircularArray = new CircularArray<>((Integer) object);
			break;
		default:
			throw new IllegalArgumentException("Unknown parameter \"" + name + "\".");
		}
	}

	@Override
	protected void setVariable(String name, Object object) {
		switch (FifthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			baseTemporalTicker = (TemporalTicker) object;
			break;
		case STATUS:
			status = (FifthStrategyStatus) object;
			break;
		case WORKING_AMOUNT_CURRENCY:
			workingAmountCurrency = (BigDecimal) object;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}
	}

	@Override
	protected Object getVariable(String name) {
		Object result = null;
		switch (FifthStrategyVariable.findByName(name)) {
		case BASE_TEMPORAL_TICKER:
			result = baseTemporalTicker;
			break;
		case STATUS:
			result = status;
			break;
		case WORKING_AMOUNT_CURRENCY:
			result = workingAmountCurrency;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}
		return result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return FifthStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}
}
