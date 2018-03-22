package org.marceloleite.mercado.strategies.first;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Order;
import org.marceloleite.mercado.base.model.TemporalTickerVariation;
import org.marceloleite.mercado.base.model.order.BuyOrderBuilder;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceForMinimalValueOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.NoBalanceOrderAnalyserException;
import org.marceloleite.mercado.base.model.order.analyser.OrderAnalyser;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.MathUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class FirstStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	private Long buySteps;

	private Long sellSteps;

	private Currency currency;

	private BuySellStep buySellStep;

	private TemporalTicker baseTemporalTicker;

	private CurrencyAmount baseRealAmount;

	private BigDecimal growthPercentageThreshold;

	private BigDecimal shrinkPercentageThreshold;

	public FirstStrategy(Currency currency) {
		super(FirstStrategyParameter.class, FirstStrategyVariable.class);
		this.currency = currency;
	}

	public FirstStrategy() {
		this(null);
	}

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		setBase(account, house);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(simulationTimeInterval,
				house.getTemporalTickers().get(currency));

		if (temporalTickerVariation != null) {
			BigDecimal lastVariation = temporalTickerVariation.getLastVariation();
			/* TODO: Create a positive and negative infity BigDecimal. */
			if (lastVariation != null && lastVariation.compareTo(new BigDecimal("10E20")) != 0) {
				checkGrowthPercentage(simulationTimeInterval, account, house, lastVariation);
				checkShrinkPercentage(simulationTimeInterval, account, house, temporalTickerVariation);
			}
		}
	}

	private void checkShrinkPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		BigDecimal lastVariation = temporalTickerVariation.getLastVariation();
		if (lastVariation.compareTo(shrinkPercentageThreshold) <= 0) {
			if (account.getBalance().hasPositiveBalance(currency)) {
				/*LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getEnd())
						+ ": Shrink threshold reached.");*/
				Order order = createSellOrder(simulationTimeInterval, account, house);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
				}
			} else {
				/*LOGGER.debug("No " + currency + " balance remaining to create a sell order. Cancelling.");*/
			}
		}
	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderAnalyser orderValuesAnalyser = new OrderAnalyser(account.getBalance(), OrderType.SELL,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);
		
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
				.selling(orderValuesAnalyser.getSecond()).receivingUnitPriceOf(orderValuesAnalyser.getUnitPrice())
				.build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private void executeOrder(Order order, Account account, House house) {
		house.getOrderExecutor().placeOrder(order, house, account);

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

	private void checkGrowthPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			BigDecimal lastVariation) {
		if (lastVariation.compareTo(growthPercentageThreshold) >= 0) {
/*			LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getEnd())
					+ ": Growth threshold reached.");*/
			if (account.getBalance().hasPositiveBalance(Currency.REAL)) {
				Order order = createBuyOrder(simulationTimeInterval, house, account);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
				}
			} else {
				/*LOGGER.debug("No " + Currency.REAL + " balance remaining to create a sell order. Cancelling.");*/
			}
		}
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, House house, Account account) {
		OrderAnalyser orderValuesAnalyser = new OrderAnalyser(account.getBalance(), OrderType.BUY,
				calculateCurrencyAmountUnitPrice(house), Currency.REAL, currency);

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

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderValuesAnalyser.getSecond()).payingUnitPriceOf(orderValuesAnalyser.getUnitPrice()).build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private CurrencyAmount calculareCurrencyAmountBaseValue(OrderType orderType) {

		BigDecimal operationPercentage = calculateOperationPercentage(orderType);
		BigDecimal baseValueAmount = baseRealAmount.getAmount().multiply(operationPercentage);

		CurrencyAmount currencyAmountBaseValue = new CurrencyAmount(Currency.REAL, baseValueAmount);
		LOGGER.debug("Base value is: " + currencyAmountBaseValue);
		return currencyAmountBaseValue;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		BigDecimal lastPrice = temporalTicker.getLastPrice();
		if (lastPrice == null || lastPrice.compareTo(BigDecimal.ZERO) == 0) {
			lastPrice = temporalTicker.getPreviousLastPrice();
		}
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}

	private CurrencyAmount calculateCurrencyAmountToBuy(CurrencyAmount currencyAmountToPay,
			CurrencyAmount currencyAmountUnitPrice) {
		BigDecimal quantity = currencyAmountToPay.getAmount().divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, quantity);
		LOGGER.debug("Currency amount to buy is: " + currencyAmountToBuy);
		return currencyAmountToBuy;
	}

	private void setBase(Account account, House house) {
		setBaseBalance(account.getBalance());
		setBaseTemporalTickers(house.getTemporalTickers());
	}

	private void setBaseTemporalTickers(Map<Currency, TemporalTicker> temporalTickers) {
		if (baseTemporalTicker == null) {
			baseTemporalTicker = temporalTickers.get(currency);
		}
	}

	private CurrencyAmount calculateCurrencyAmountToSell(CurrencyAmount currencyAmountToReceive,
			CurrencyAmount currencyAmountUnitPrice) {
		BigDecimal amountToSell = currencyAmountToReceive.getAmount().divide(currencyAmountUnitPrice.getAmount());
		CurrencyAmount currencyAmountToSell = new CurrencyAmount(currency, amountToSell);
		LOGGER.debug("Currency amount to sell is " + currencyAmountToSell + ".");
		return currencyAmountToSell;
	}

	private void setBaseBalance(Balance balance) {
		if (baseRealAmount == null) {
			baseRealAmount = new CurrencyAmount(balance.get(Currency.REAL));
			LOGGER.debug("Base is " + baseRealAmount + ".");
		}

	}

	private BigDecimal calculateOperationPercentage(OrderType orderType) {
		BigDecimal result;
		long checkStep = buySellStep.calculateStep(orderType);
		if (checkStep > 0) {
			result = new BigDecimal((double) MathUtils.factorial(checkStep) / (double) MathUtils.factorial(buySteps));
		} else {
			result = new BigDecimal((double) MathUtils.factorial(Math.abs(checkStep)) / (double) MathUtils.factorial(sellSteps));
		}
		LOGGER.debug("Operation percentage is " + new PercentageFormatter().format(result) + ".");
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
		this.baseTemporalTicker = new TemporalTicker(temporalTicker);
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
			growthPercentageThreshold = new BigDecimal(parameter.getValue());
			break;
		case SELL_STEP_FACTORIAL_NUMBER:
			sellSteps = Long.parseLong(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = new BigDecimal(parameter.getValue());
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
}
