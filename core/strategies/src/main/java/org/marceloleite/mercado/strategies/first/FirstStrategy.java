package org.marceloleite.mercado.strategies.first;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.MathUtils;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;

public class FirstStrategy extends AbstractStrategyExecutor {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	private Long buySteps;

	private Long sellSteps;

	private BuySellStep buySellStep;

	private TemporalTicker baseTemporalTicker;

	private CurrencyAmount baseRealAmount;

	private Double growthPercentageThreshold;

	private Double shrinkPercentageThreshold;

	public FirstStrategy(Strategy strategy) {
		super(strategy);
	}

	@Override
	public void execute(TimeInterval timeInterval, Account account, House house) {
		setBase(account, house);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(timeInterval,
				house.getTemporalTickerFor(getCurrency()));

		Double lastVariation = temporalTickerVariation.getLastVariation();
		if (Double.isFinite(lastVariation)) {
			checkGrowthPercentage(timeInterval, account, house, lastVariation);
			checkShrinkPercentage(timeInterval, account, house, temporalTickerVariation);
		}

	}

	private void checkShrinkPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		double lastVariation = temporalTickerVariation.getLastVariation();
		if (lastVariation <= shrinkPercentageThreshold) {
			if (account.hasPositiveBalanceOf(getCurrency())) {
				/*
				 * LOGGER.debug(new
				 * ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getEnd()) +
				 * ": Shrink threshold reached.");
				 */
				Order order = createSellOrder(simulationTimeInterval, account, house);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickerFor(getCurrency()));
				}
			} else {
				/*
				 * LOGGER.debug("No " + currency +
				 * " balance remaining to create a sell order. Cancelling.");
				 */
			}
		}
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderValuesAnalyser = new OrderAnalyser(account, OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, getCurrency());

		try {
			orderValuesAnalyser.setFirst(calculareCurrencyAmountBaseValue(orderValuesAnalyser.getOrderType()));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			if (exception instanceof NoBalanceForMinimalValueOrderAnalyserException) {
				buySellStep.updateStep(orderValuesAnalyser.getOrderType());
			}
			return null;
		}

		try {
			orderValuesAnalyser.setSecond(
					calculateCurrencyAmountToSell(orderValuesAnalyser.getFirst(), orderValuesAnalyser.getUnitPrice()));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			if (exception instanceof NoBalanceForMinimalValueOrderAnalyserException) {
				buySellStep.updateStep(orderValuesAnalyser.getOrderType());
			}
			return null;
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderValuesAnalyser.getSecond())
				.receivingUnitPriceOf(orderValuesAnalyser.getUnitPrice())
				.build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
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
			buySellStep.updateStep(order.getType());
			break;
		case CANCELLED:
			LOGGER.info(order + " cancelled.");
			break;
		}
	}

	private void checkGrowthPercentage(TimeInterval timeInterval, Account account, House house, double lastVariation) {
		if (lastVariation >= growthPercentageThreshold) {
			// LOGGER.debug(ZonedDateTimeToStringConverter.getInstance().convertTo(timeInterval.getEnd())
			// + ": Growth threshold reached.");

			if (account.hasPositiveBalanceOf(Currency.REAL)) {
				Order order = createBuyOrder(timeInterval, house, account);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickerFor(getCurrency()));
				}
			} else {
				// LOGGER.debug("No " + Currency.REAL +
				// " balance remaining to create a sell order. Cancelling.");
			}
		}
	}

	private Order createBuyOrder(TimeInterval timeInterval, House house, Account account) {
		OrderAnalyser orderValuesAnalyser = new OrderAnalyser(account, OrderType.BUY,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, getCurrency());

		try {
			orderValuesAnalyser.setFirst(calculareCurrencyAmountBaseValue(orderValuesAnalyser.getOrderType()));
		} catch (NoBalanceForMinimalValueOrderAnalyserException | NoBalanceOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			if (exception instanceof NoBalanceForMinimalValueOrderAnalyserException) {
				buySellStep.updateStep(orderValuesAnalyser.getOrderType());
			}
			return null;
		}

		try {
			orderValuesAnalyser.setSecond(
					calculateCurrencyAmountToBuy(orderValuesAnalyser.getFirst(), orderValuesAnalyser.getUnitPrice()));
		} catch (NoBalanceOrderAnalyserException | NoBalanceForMinimalValueOrderAnalyserException exception) {
			LOGGER.warn(exception.getMessage());
			if (exception instanceof NoBalanceForMinimalValueOrderAnalyserException) {
				buySellStep.updateStep(orderValuesAnalyser.getOrderType());
			}
			return null;
		}

		Order order = new BuyOrderBuilder().toExecuteOn(timeInterval.getStart())
				.buying(orderValuesAnalyser.getSecond())
				.payingUnitPriceOf(orderValuesAnalyser.getUnitPrice())
				.build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private CurrencyAmount calculareCurrencyAmountBaseValue(OrderType orderType) {
		Double operationPercentage = calculateOperationPercentage(orderType);
		BigDecimal baseValueAmount = baseRealAmount.getAmount()
				.multiply(new BigDecimal(operationPercentage));

		CurrencyAmount currencyAmountBaseValue = new CurrencyAmount(Currency.REAL, baseValueAmount);
		LOGGER.debug("Base value is: " + currencyAmountBaseValue);
		return currencyAmountBaseValue;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		BigDecimal lastPrice = house.getTemporalTickerFor(getCurrency())
				.getCurrentOrPreviousLast();
		return new CurrencyAmount(Currency.REAL, lastPrice);
	}

	private CurrencyAmount calculateCurrencyAmountToBuy(CurrencyAmount currencyAmountToPay,
			CurrencyAmount currencyAmountUnitPrice) {
		BigDecimal quantity = currencyAmountToPay.getAmount()
				.divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(getCurrency(), quantity);
		LOGGER.debug("Currency amount to buy is: " + currencyAmountToBuy);
		return currencyAmountToBuy;
	}

	private void setBase(Account account, House house) {
		baseRealAmount = new CurrencyAmount(Currency.REAL, account.getBalanceFor(getCurrency()));
		LOGGER.debug("Base is " + baseRealAmount + ".");
		baseTemporalTicker = house.getTemporalTickerFor(getCurrency());
	}

	private CurrencyAmount calculateCurrencyAmountToSell(CurrencyAmount currencyAmountToReceive,
			CurrencyAmount currencyAmountUnitPrice) {
		BigDecimal amountToSell = currencyAmountToReceive.getAmount()
				.divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToSell = new CurrencyAmount(getCurrency(), amountToSell);
		LOGGER.debug("Currency amount to sell is " + currencyAmountToSell + ".");
		return currencyAmountToSell;
	}

	private Double calculateOperationPercentage(OrderType orderType) {
		Double result;
		long checkStep = buySellStep.calculateStep(orderType);
		if (checkStep > 0) {
			result = (double) MathUtils.factorial(checkStep) / (double) MathUtils.factorial(buySteps);
		} else {
			result = (double) MathUtils.factorial(Math.abs(checkStep)) / (double) MathUtils.factorial(sellSteps);
		}
		LOGGER.debug("Operation percentage is " + PercentageFormatter.getInstance()
				.format(result) + ".");
		return result;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval,
			TemporalTicker currentTemporalTicker) {
		TemporalTickerVariation temporalTickerVariation = null;
		if (currentTemporalTicker != null) {
			temporalTickerVariation = new TemporalTickerVariation(baseTemporalTicker, currentTemporalTicker);
			/*
			 * LOGGER.debug(simulationTimeInterval + ": Last variation is " + new
			 * PercentageFormatter().format(temporalTickerVariation.getLastVariation()));
			 */
		} else {
			LOGGER.debug("No ticker variation for period " + simulationTimeInterval + ".");
		}
		return temporalTickerVariation;
	}

	private void updateBaseTemporalTicker(TemporalTicker temporalTicker) {
		LOGGER.debug("Updating base temporal ticker.");
		this.baseTemporalTicker = temporalTicker;
	}

	@Override
	public Property retrieveVariable(String name) {
		FirstStrategyVariable firstStrategyVariable = FirstStrategyVariable.findByName(name);
		switch (firstStrategyVariable) {
		case BUY_SELL_STEP:
			firstStrategyVariable.setValue(Long.toString(buySellStep.getCurrentStep()));
			break;
		case BASE_TEMPORAL_TICKER:
			firstStrategyVariable.setName(new ObjectToJsonConverter().convertTo(baseTemporalTicker));
			break;
		}
		return firstStrategyVariable;
	}

	@Override
	public void defineParameter(Property parameter) {
		FirstStrategyParameter firstStrategyParameter = FirstStrategyParameter.findByName(parameter.getName());

		switch (firstStrategyParameter) {
		case BUY_STEP_FACTORIAL_NUMBER:
			buySteps = Long.parseLong(parameter.getValue());
			break;
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		case SELL_STEP_FACTORIAL_NUMBER:
			sellSteps = Long.parseLong(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = new MercadoBigDecimal(parameter.getValue());
			break;
		}

		if (buySteps != null && this.buySteps != null && this.sellSteps != null) {
			this.buySellStep = new BuySellStep(buySteps, sellSteps);
		}
	}

	@Override
	public void defineVariable(Property variable) {
		FirstStrategyVariable firstStrategyVariable = FirstStrategyVariable.findByName(variable.getName());

		switch (firstStrategyVariable) {
		case BUY_SELL_STEP:
			long currentStep = Long.parseLong(firstStrategyVariable.getValue());
			this.buySellStep.setCurrentStep(currentStep);
			break;
		case BASE_TEMPORAL_TICKER:
			this.baseTemporalTicker = new ObjectToJsonConverter().convertFromToObject(variable.getName(),
					baseTemporalTicker);
			break;
		}
	}

	@Override
	protected void setParameter(ParameterDefintion parameterDefinition, Object parameterObject) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setVariable(Object variableObject) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getVariable(String variableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Class<?>> getParameterDefinitions() {
		return Arrays.asList(FirstStrategyParameter.values())
				.stream()
				.collect(Collectors.toMap(FirstStrategyParameter::getName, FirstStrategyParameter::getClazz));
	}
}
