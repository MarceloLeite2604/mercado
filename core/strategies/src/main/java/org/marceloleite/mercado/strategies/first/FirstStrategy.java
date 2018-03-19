package org.marceloleite.mercado.strategies.first;

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
import org.marceloleite.mercado.base.model.order.MinimalAmounts;
import org.marceloleite.mercado.base.model.order.SellOrderBuilder;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
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

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	public FirstStrategy(Currency currency) {
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
			Double lastVariation = temporalTickerVariation.getLastVariation();
			if (lastVariation != null && lastVariation != Double.POSITIVE_INFINITY) {
				checkGrowthPercentage(simulationTimeInterval, account, house, lastVariation);
				checkShrinkPercentage(simulationTimeInterval, account, house, temporalTickerVariation);
			}
		}
	}

	private void checkShrinkPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		Double lastVariation = temporalTickerVariation.getLastVariation();
		if (lastVariation <= shrinkPercentageThreshold) {
			ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
			LOGGER.debug(zonedDateTimeToStringConverter.convertTo(simulationTimeInterval.getEnd())
					+ ": Shrink threshold reached.");
			Order order = createSellOrder(simulationTimeInterval, account, house);
			if (order != null) {
				executeOrder(order, account, house);
			}
			updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
		}

	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountBaseValue = calculareCurrencyAmountBaseValue(OrderType.SELL, house);
		CurrencyAmount currencyAmountToSell = calculateOrderAmount(OrderType.SELL, currencyAmountBaseValue, house,
				account);
		if (currencyAmountToSell == null) {
			return null;
		}
		CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
		if (!hasBalanceFor(currencyAmountToSell, account)) {
			if (!buySellStep.isOnMinSellStep()) {
				LOGGER.debug("Decreasing sell step an creating a new order.");
				buySellStep.updateStep(OrderType.SELL);
				return createSellOrder(simulationTimeInterval, account, house);
			} else {
				LOGGER.debug("Sell step is on min. Cancelling order creation.");
				return null;
			}
		}
		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(currencyAmountToSell).receivingUnitPriceOf(currencyAmountUnitPrice).build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private boolean hasBalanceFor(CurrencyAmount currencyAmount, Account account) {
		CurrencyAmount currencyAmountBalance = account.getBalance().get(currencyAmount.getCurrency());
		return (currencyAmountBalance.getAmount() > currencyAmount.getAmount());
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
			Double lastVariation) {
		if (lastVariation >= growthPercentageThreshold) {
			LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getEnd())
					+ ": Growth threshold reached.");
			Order order = createBuyOrder(simulationTimeInterval, house, account);
			if (order != null) {
				executeOrder(order, account, house);
			}
			updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
		}
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, House house, Account account) {
		CurrencyAmount currencyAmountBaseValue = calculareCurrencyAmountBaseValue(OrderType.BUY, house);
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, currencyAmountBaseValue, house,
				account);

		if (currencyAmountToPay == null) {
			return null;
		}

		CurrencyAmount currencyAmountUnitPrice = calculateCurrencyAmountUnitPrice(house);
		CurrencyAmount currencyAmountToBuy = calculateCurrencyAmountToBuy(currencyAmountToPay, currencyAmountUnitPrice);
		if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmountToBuy)) {
			LOGGER.debug("The amount of " + currency + " currency to buy is lower than the minimal limit.");
			if (!buySellStep.isOnMaxBuyStep()) {
				LOGGER.debug("Increasing buy step and creating a new order.");
				buySellStep.updateStep(OrderType.BUY);
				// return createBuyOrder(simulationTimeInterval, house, account);
			} else {
				LOGGER.debug("Buying step is on max. Cancelling order creation.");
				return null;
			}
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart()).buying(currencyAmountToBuy)
				.payingUnitPriceOf(currencyAmountUnitPrice).build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private CurrencyAmount calculareCurrencyAmountBaseValue(OrderType orderType, House house) {
		double operationPercentage = calculateOperationPercentage(orderType);
		Double baseValueAmount = null;
		Currency baseValueCurrency = null;
		switch (orderType) {
		case BUY:
			baseValueAmount = baseRealAmount.getAmount() * operationPercentage;
			baseValueCurrency = Currency.REAL;
			break;
		case SELL:
			TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
			Double lastPrice = temporalTicker.getLastPrice();
			if (lastPrice == null || lastPrice == 0.0) {
				lastPrice = temporalTicker.getPreviousLastPrice();
			}
			baseValueAmount = (baseRealAmount.getAmount() * operationPercentage) / lastPrice;
			baseValueCurrency = currency;
			break;
		}
		CurrencyAmount currencyAmount = new CurrencyAmount(baseValueCurrency, baseValueAmount);
		LOGGER.debug("Base value is: " + currencyAmount);
		return currencyAmount;
	}

	private CurrencyAmount calculateCurrencyAmountUnitPrice(House house) {
		TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
		Double lastPrice = temporalTicker.getLastPrice();
		if (lastPrice == null || lastPrice == 0.0) {
			lastPrice = temporalTicker.getPreviousLastPrice();
		}
		CurrencyAmount currencyAmountUnitPrice = new CurrencyAmount(Currency.REAL, lastPrice);
		return currencyAmountUnitPrice;
	}

	private CurrencyAmount calculateCurrencyAmountToBuy(CurrencyAmount currencyAmountToPay,
			CurrencyAmount currencyAmountUnitPrice) {
		Double quantity = currencyAmountToPay.getAmount() / currencyAmountUnitPrice.getAmount();
		Double restoredValue = quantity * currencyAmountUnitPrice.getAmount();
		Double difference = restoredValue - currencyAmountToPay.getAmount();
		if (difference > 0) {
			quantity -= difference / currencyAmountUnitPrice.getAmount();
		}
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, quantity);
		LOGGER.debug("Currency amount to buy is: " + currencyAmountToBuy);
		return currencyAmountToBuy;
	}

	private CurrencyAmount checkMinimalAmount(CurrencyAmount currencyAmount, Account account) {
		if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmount)) {
			Double minimalRequired = MinimalAmounts.retrieveMinimalAmountFor(currency);
			CurrencyAmount minimalCurrencyAmountRequired = new CurrencyAmount(currencyAmount.getCurrency(),
					minimalRequired);
			if (hasBalanceFor(minimalCurrencyAmountRequired, account)) {
				LOGGER.debug("The amount of " + currencyAmount + " is lower than minimal of " + minimalRequired
						+ " required for an operation. Updating it.");
				currencyAmount.setAmount(minimalRequired);
			} else {
				LOGGER.debug("Not enough balance for execute the minimal operation. Cancelling order execution.");
				return null;
			}
		}
		return currencyAmount;
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

	private CurrencyAmount calculateOrderAmount(OrderType orderType, CurrencyAmount currencyAmountBase, House house,
			Account account) {

		if (!hasPositiveBalance(currencyAmountBase.getCurrency(), account)) {
			LOGGER.debug("No " + currency + " balance to execute an order.");
			return null;
		}

		// CurrencyAmount currencyAmountForOperation =
		// calculateCurrencyAmountInitialOperation(currency, orderType, house);
		CurrencyAmount currencyAmountForOperation = new CurrencyAmount(currencyAmountBase);
		currencyAmountForOperation = checkBalance(currencyAmountForOperation, account);
		currencyAmountForOperation = checkMinimalAmount(currencyAmountForOperation, account);

		if (currencyAmountForOperation != null) {
			LOGGER.debug("Order amount is " + currencyAmountForOperation + ".");
		}
		return currencyAmountForOperation;
	}

	private boolean hasPositiveBalance(Currency currency, Account account) {
		CurrencyAmount currencyAmountBalance = account.getBalance().get(currency);
		LOGGER.debug("Balance: " + currencyAmountBalance + ".");
		return (currencyAmountBalance.getAmount() > 0);
	}

	private CurrencyAmount checkBalance(CurrencyAmount currencyAmount, Account account) {
		CurrencyAmount currencyAmountBalance = account.getBalance().get(currencyAmount.getCurrency());
		if (currencyAmountBalance.getAmount() <= currencyAmount.getAmount()) {
			LOGGER.debug("Not enough " + currencyAmount.getCurrency()
					+ " on balance to complete the initial amount. Using the remaining " + currencyAmountBalance + ".");
			return new CurrencyAmount(currencyAmountBalance);
		}
		return currencyAmount;
	}

	private CurrencyAmount calculateCurrencyAmountInitialOperation(Currency currency, OrderType orderType,
			House house) {
		Double operationAmount = null;
		double operationPercentage = calculateOperationPercentage(orderType);
		switch (orderType) {
		case BUY:
			operationAmount = baseRealAmount.getAmount() * operationPercentage;
			break;
		case SELL:
			TemporalTicker temporalTicker = house.getTemporalTickers().get(currency);
			Double lastPrice = temporalTicker.getLastPrice();
			if (lastPrice == null || lastPrice == 0.0) {
				lastPrice = temporalTicker.getPreviousLastPrice();
			}
			operationAmount = (baseRealAmount.getAmount() * operationPercentage) / lastPrice;
			break;
		}
		CurrencyAmount currencyAmountInitialOperation = new CurrencyAmount(currency, operationAmount);
		LOGGER.debug("Initial operation amount: " + currencyAmountInitialOperation + ".");

		return currencyAmountInitialOperation;
	}

	private void setBaseBalance(Balance balance) {
		if (baseRealAmount == null) {
			baseRealAmount = new CurrencyAmount(balance.get(Currency.REAL));
			LOGGER.debug("Base is " + baseRealAmount + ".");
		}

	}

	private double calculateOperationPercentage(OrderType orderType) {
		double result;
		long checkStep = buySellStep.calculateStep(orderType);
		if (checkStep > 0) {
			result = (double) checkStep / (double) buySteps;
		} else {
			result = (double) Math.abs(checkStep) / (double) sellSteps;
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
			buySteps = MathUtils.factorial(Long.parseLong(parameter.getValue()));
			break;
		case GROWTH_PERCENTAGE_THRESHOLD:
			growthPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SELL_STEP_FACTORIAL_NUMBER:
			sellSteps = MathUtils.factorial(Long.parseLong(parameter.getValue()));
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = Double.parseDouble(parameter.getValue());
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
