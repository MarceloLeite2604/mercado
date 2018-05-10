package org.marceloleite.mercado.strategies.first;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.BuyOrderBuilder;
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.House;
import org.marceloleite.mercado.SellOrderBuilder;
import org.marceloleite.mercado.TemporalTickerVariation;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.MathUtils;
import org.marceloleite.mercado.commons.utils.formatter.PercentageFormatter;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.orderanalyser.OrderAnalyser;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.orderanalyser.exception.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.strategy.AbstractStrategyExecutor;
import org.marceloleite.mercado.strategy.ObjectDefinition;

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
			if (account.getWallet()
					.hasPositiveBalanceOf(getCurrency())) {
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

			if (account.getWallet()
					.hasPositiveBalanceOf(Currency.REAL)) {
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
		baseRealAmount = account.getWallet()
				.getBalanceFor(getCurrency())
				.asCurrencyAmount();
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
		LOGGER.debug("Operation percentage is " + PercentageFormatter.format(result) + ".");
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
	protected void setParameter(String name, Object object) {
		FirstStrategyParameter firstStrategyParameter = FirstStrategyParameter.findByName(name);

		switch (firstStrategyParameter) {
		case BUY_STEP_FACTORIAL_NUMBER:
			buySteps = (Long) object;
			break;
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = (Double) object;
			break;
		case SELL_STEP_FACTORIAL_NUMBER:
			sellSteps = (Long) object;
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = (Double) object;
			break;
		}

		if (buySteps != null && this.buySteps != null && this.sellSteps != null) {
			this.buySellStep = new BuySellStep(buySteps, sellSteps);
		}
	}

	@Override
	protected void setVariable(String name, Object object) {
		FirstStrategyVariable firstStrategyVariable = FirstStrategyVariable.findByName(name);

		switch (firstStrategyVariable) {
		case BUY_SELL_STEP:
			this.buySellStep.setCurrentStep((Long) object);
			break;
		case BASE_TEMPORAL_TICKER:
			this.baseTemporalTicker = (TemporalTicker) object;
			break;
		default:
			throw new IllegalStateException("Unrecognized variable \"" + name + "\".");
		}
	}

	@Override
	protected Object getVariable(String name) {
		Object result;
		FirstStrategyVariable firstStrategyVariable = FirstStrategyVariable.findByName(name);
		switch (firstStrategyVariable) {
		case BUY_SELL_STEP:
			result = Long.toString(buySellStep.getCurrentStep());
			break;
		case BASE_TEMPORAL_TICKER:
			result = baseTemporalTicker;
			break;
		default:
			throw new IllegalArgumentException("Unknown variable \"" + name + "\".");
		}
		return (Object) result;
	}

	@Override
	protected Map<String, ObjectDefinition> getParameterDefinitions() {
		return FirstStrategyParameter.getObjectDefinitions();
	}

	@Override
	protected Map<String, ObjectDefinition> getVariableDefinitions() {
		return FirstStrategyVariable.getObjectDefinitions();
	}
}
