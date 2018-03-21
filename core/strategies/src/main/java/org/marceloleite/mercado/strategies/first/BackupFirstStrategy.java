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
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.utils.MathUtils;
import org.marceloleite.mercado.data.TemporalTicker;
import org.marceloleite.mercado.strategies.AbstractStrategy;

public class BackupFirstStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(BackupFirstStrategy.class);

	private Long buySteps;

	private Long sellSteps;

	private Currency currency;

	private BuySellStep buySellStep;

	private TemporalTicker baseTemporalTicker;

	private CurrencyAmount baseRealAmount;

	private double growthPercentageThreshold;

	private double shrinkPercentageThreshold;

	private boolean skipIfLowerThanMinimalValue;

	public BackupFirstStrategy(Currency currency) {
		super(FirstStrategyParameter.class, FirstStrategyVariable.class);
		this.currency = currency;
	}

	public BackupFirstStrategy() {
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
			if (account.getBalance().hasPositiveBalance(currency)) {
				LOGGER.debug(new ZonedDateTimeToStringConverter().convertTo(simulationTimeInterval.getEnd())
						+ ": Shrink threshold reached.");
				Order order = createSellOrder(simulationTimeInterval, account, house);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
				}
			} else {
				LOGGER.debug("No " + currency + " balance remaining to create a sell order. Cancelling.");
			}
		}

	}

	private Order createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		OrderProperties orderProperties = new OrderProperties(OrderType.SELL);
		orderProperties.setFirst(calculareCurrencyAmountBaseValue(orderProperties.getOrderType()));
		orderProperties.setUnitPrice(calculateCurrencyAmountUnitPrice(house));
		orderProperties
				.setSecond(calculateCurrencyAmountToSell(orderProperties.getFirst(), orderProperties.getUnitPrice()));

		checkMinimalAmount(orderProperties);
		if (orderProperties.isCancelled()) {
			return null;
		}

		if (orderProperties.isSecondAdjusted()) {
			orderProperties.setFirst(
					updateCurrencyAmountToReceive(orderProperties.getSecond(), orderProperties.getUnitPrice()));
		}

		if (!account.getBalance().hasBalance(orderProperties.getSecond())) {
			LOGGER.debug("No balance to execute sell order of " + orderProperties.getSecond() + ".");
			if (orderProperties.isSecondAdjusted()) {
				LOGGER.debug("Amount to sell has already been adjusted. Cancelling order.");
			} else {
				if (!account.getBalance().hasPositiveBalance(orderProperties.getSecond().getCurrency())) {
					LOGGER.debug("No " + orderProperties.getSecond().getCurrency()
							+ " ballance to create sell order. Cancelling.");
					return null;
				} else {
					orderProperties.getSecond()
							.setAmount(account.getBalance().get(orderProperties.getSecond().getCurrency()).getAmount());
					LOGGER.debug("Selling the remaining value of " + orderProperties.getSecond() + ".");
					orderProperties.setFirst(
							updateCurrencyAmountToReceive(orderProperties.getSecond(), orderProperties.getUnitPrice()));
					checkMinimalAmount(orderProperties);
					if (orderProperties.isCancelled()) {
						return null;
					}
				}
			}
		}

		Order order = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.selling(orderProperties.getSecond()).receivingUnitPriceOf(orderProperties.getUnitPrice()).build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private void checkMinimalAmount(OrderProperties orderProperties) {
		CurrencyAmount currencyAmount = orderProperties.getSecond();
		if (MinimalAmounts.isAmountLowerThanMinimal(currencyAmount)) {
			if (!orderProperties.isFirstAdjusted()) {
				if (skipIfLowerThanMinimalValue) {
					LOGGER.debug(currencyAmount + " is lower than minimal value. Cancelling order.");
					buySellStep.updateStep(orderProperties.getOrderType());
					orderProperties.setCancelled(true);
				} else {
					Double minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currencyAmount.getCurrency());
					LOGGER.debug(currencyAmount + " is lower than minimal value. Increasing it to "
							+ new DigitalCurrencyFormatter().format(minimalAmount));
					currencyAmount.setAmount(minimalAmount);
					orderProperties.setSecondAdjusted(true);
				}
			} else {
				String action = (orderProperties.getOrderType() == OrderType.BUY ? "pay" : "receive");
				LOGGER.debug("Currency amount to " + action + " already adjusted. Cancelling order.");
				orderProperties.setCancelled(true);
			}
		}
		;
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
			if (account.getBalance().hasPositiveBalance(Currency.REAL)) {
				Order order = createBuyOrder(simulationTimeInterval, house, account);
				if (order != null) {
					executeOrder(order, account, house);
					updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
				}
			} else {
				LOGGER.debug("No " + Currency.REAL + " balance remaining to create a sell order. Cancelling.");
			}
		}
	}

	private Order createBuyOrder(TimeInterval simulationTimeInterval, House house, Account account) {
		OrderProperties orderProperties = new OrderProperties(OrderType.BUY);
		orderProperties.setFirst(calculareCurrencyAmountBaseValue(orderProperties.getOrderType()));
		orderProperties.setUnitPrice(calculateCurrencyAmountUnitPrice(house));

		orderProperties
				.setSecond(calculateCurrencyAmountToBuy(orderProperties.getFirst(), orderProperties.getUnitPrice()));
		checkMinimalAmount(orderProperties);
		if (orderProperties.isCancelled()) {
			return null;
		}

		if (orderProperties.isSecondAdjusted()) {
			orderProperties
					.setFirst(updateCurrencyAmountToPay(orderProperties.getSecond(), orderProperties.getUnitPrice()));
		}

		if (!account.getBalance().hasBalance(orderProperties.getFirst())) {
			LOGGER.debug("Not enough balance to execute an order paying " + orderProperties.getFirst() + ".");

			if (orderProperties.isSecondAdjusted()) {
				LOGGER.debug("Amount to buy has already been adjusted. Cancelling order.");
				return null;
			} else {
				if (!account.getBalance().hasPositiveBalance(orderProperties.getFirst().getCurrency())) {
					LOGGER.debug("No remaining " + orderProperties.getFirst().getCurrency()
							+ " ballance to create buy order. Cancelling.");
					return null;
				} else {
					orderProperties.getFirst()
							.setAmount(account.getBalance().get(orderProperties.getFirst().getCurrency()).getAmount());
					LOGGER.debug("Paying the remaining value of " + orderProperties.getFirst() + ".");
				}
				Double balanceAmount = account.getBalance().get(orderProperties.getFirst().getCurrency()).getAmount();
				LOGGER.debug("Descreasing paying amount to " + new NonDigitalCurrencyFormatter().format(balanceAmount));
				orderProperties.getFirst().setAmount(balanceAmount);
				orderProperties.setSecond(
						calculateCurrencyAmountToBuy(orderProperties.getFirst(), orderProperties.getUnitPrice()));
				checkMinimalAmount(orderProperties);
				if (orderProperties.isCancelled()) {
					return null;
				}
			}
		}

		Order order = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
				.buying(orderProperties.getSecond()).payingUnitPriceOf(orderProperties.getUnitPrice()).build();
		LOGGER.debug("Order created is " + order + ".");
		return order;
	}

	private CurrencyAmount updateCurrencyAmountToReceive(CurrencyAmount currencyAmountToSell,
			CurrencyAmount currencyAmountUnitPrice) {
		double amountToReceive = currencyAmountUnitPrice.getAmount() * currencyAmountToSell.getAmount();
		CurrencyAmount currencyAmountToReceive = new CurrencyAmount(Currency.REAL, amountToReceive);
		LOGGER.debug("Currency amount to receive updated to " + currencyAmountToReceive + ".");
		return currencyAmountToReceive;
	}

	private CurrencyAmount updateCurrencyAmountToPay(CurrencyAmount currencyAmountToBuy,
			CurrencyAmount currencyAmountUnitPrice) {
		double amountToPay = currencyAmountUnitPrice.getAmount() * currencyAmountToBuy.getAmount();
		CurrencyAmount currencyAmountToPay = new CurrencyAmount(Currency.REAL, amountToPay);
		LOGGER.debug("Currency amount to pay updated to " + currencyAmountToPay + ".");
		return currencyAmountToPay;
	}

	private CurrencyAmount calculareCurrencyAmountBaseValue(OrderType orderType) {

		double operationPercentage = calculateOperationPercentage(orderType);
		double baseValueAmount = baseRealAmount.getAmount() * operationPercentage;

		CurrencyAmount currencyAmountBaseValue = new CurrencyAmount(Currency.REAL, baseValueAmount);
		LOGGER.debug("Base value is: " + currencyAmountBaseValue);
		return currencyAmountBaseValue;
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
		// Double restoredValue = quantity * currencyAmountUnitPrice.getAmount();
		// Double difference = restoredValue - currencyAmountToPay.getAmount();
		// if (difference > 0) {
		// quantity -= difference / currencyAmountUnitPrice.getAmount();
		// }
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
		Double amountToSell = currencyAmountToReceive.getAmount() / currencyAmountUnitPrice.getAmount();
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

	private double calculateOperationPercentage(OrderType orderType) {
		double result;
		long checkStep = buySellStep.calculateStep(orderType);
		if (checkStep > 0) {
			result = (double) MathUtils.factorial(checkStep) / (double) MathUtils.factorial(buySteps);
		} else {
			result = (double) MathUtils.factorial(Math.abs(checkStep)) / (double) MathUtils.factorial(sellSteps);
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
			growthPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SELL_STEP_FACTORIAL_NUMBER:
			sellSteps = Long.parseLong(parameter.getValue());
			break;
		case SHRINK_PERCENTAGE_THRESHOLD:
			shrinkPercentageThreshold = Double.parseDouble(parameter.getValue());
			break;
		case SKIP_IF_LOWER_THAN_MINIMAL_VALUE:
			skipIfLowerThanMinimalValue = Boolean.parseBoolean(parameter.getValue());
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
