package org.marceloleite.mercado.simulator.strategies.first;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.Balance;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.MathUtils;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.MinimalAmounts;
import org.marceloleite.mercado.simulator.order.OrderExecutor;
import org.marceloleite.mercado.simulator.order.OrderStatus;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.simulator.strategies.AbstractStrategy;

public class FirstStrategy extends AbstractStrategy {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	// private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.01;

	// private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.01;

	// private static final long TOTAL_BUY_STEPS = MathUtils.factorial(5);

	// private static final long TOTAL_SELL_STEPS = MathUtils.factorial(6);

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
				checkGrowthPercentage(simulationTimeInterval, account, house, temporalTickerVariation);
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
			if (hasBalance(account)) {
				LOGGER.debug("Has balance for sell order.");
				createSellOrder(simulationTimeInterval, account, house);
				updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
			}
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToReceive = calculateOrderAmount(OrderType.SELL, account);
		if (currencyAmountToReceive != null) {
			CurrencyAmount currencyAmountToSell = new CurrencyAmount(currency, null);
			SellOrder sellOrder = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell).receiving(currencyAmountToReceive).build();
			LOGGER.debug("Created " + sellOrder + ".");
			executeSellOrder(sellOrder, account, house);
			// account.getSellOrdersTemporalController().add(sellOrder);
		}
	}

	private void executeSellOrder(SellOrder sellOrder, Account account, House house) {
		new OrderExecutor().executeSellOrder(sellOrder, house, account);

		switch (sellOrder.getOrderStatus()) {
		case CREATED:
			throw new RuntimeException("Requested " + sellOrder + " execution, but its status returned as \""
					+ OrderStatus.CREATED + "\".");
		case EXECUTED:
			buySellStep.updateStep(OrderType.SELL);
			break;
		case CANCELLED:
			LOGGER.info(sellOrder + " cancelled.");
			break;
		}
	}

	private void executeBuyOrder(BuyOrder buyOrder, Account account, House house) {
		new OrderExecutor().executeBuyOrder(buyOrder, house, account);

		switch (buyOrder.getOrderStatus()) {
		case CREATED:
			throw new RuntimeException("Requested " + buyOrder + " execution, but its status returned as \""
					+ OrderStatus.CREATED + "\".");
		case EXECUTED:
			buySellStep.updateStep(OrderType.BUY);
			break;
		case CANCELLED:
			LOGGER.info(buyOrder + " cancelled.");
			break;
		}
	}

	private void checkGrowthPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		Double lastVariation = temporalTickerVariation.getLastVariation();
		if (lastVariation >= growthPercentageThreshold) {
			LOGGER.debug(zonedDateTimeToStringConverter.convertTo(simulationTimeInterval.getEnd())
					+ ": Growth threshold reached.");
			if (hasBalance(account)) {
				LOGGER.debug("Has balance for buy order.");
				createBuyOrder(simulationTimeInterval, house, account);
				updateBaseTemporalTicker(house.getTemporalTickers().get(currency));
			}
		}
	}

	private boolean hasBalance(Account account) {
		CurrencyAmount currencyAmountBalance = account.getBalance().get(Currency.REAL);
		Double balanceAmount = currencyAmountBalance.getAmount();
		LOGGER.debug("Balance amount: " + currencyAmountBalance);
		return (balanceAmount > 0.0 && balanceAmount > MinimalAmounts.retrieveMinimalAmountFor(Currency.REAL));
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, House house, Account account) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			BuyOrder buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(currencyAmountToBuy).paying(currencyAmountToPay).build();
			LOGGER.debug("Buy order created is " + buyOrder);
			// account.getBuyOrdersTemporalController().add(buyOrder);
			executeBuyOrder(buyOrder, account, house);
		}
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

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		/*
		 * Currency balanceCurrency = (orderType == OrderType.SELL ? this.currency :
		 * Currency.REAL);
		 */
		CurrencyAmount balance = account.getBalance().get(Currency.REAL);
		double balanceAmount = balance.getAmount();
		LOGGER.debug("Balance amount: " + balance + ".");
		double operationPercentage = calculateOperationPercentage(orderType);
		double operationAmount = baseRealAmount.getAmount() * operationPercentage;
		LOGGER.debug("Initial operation amount: " + new DigitalCurrencyFormatter().format(operationAmount) + ".");
		Double minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currency);

		if (orderType == OrderType.BUY && operationAmount > balanceAmount) {
			operationAmount = balanceAmount;
		}

		if (operationAmount < minimalAmount) {
			LOGGER.debug("No balance for minimal operation. Aborting order.");
			return null;
		}

		CurrencyAmount orderAmount = new CurrencyAmount(Currency.REAL, operationAmount);
		LOGGER.debug("Order amount is " + orderAmount + ".");
		return orderAmount;
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
