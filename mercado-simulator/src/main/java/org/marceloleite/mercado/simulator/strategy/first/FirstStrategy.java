package org.marceloleite.mercado.simulator.strategy.first;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.util.PercentageFormatter;
import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.Balance;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.MathUtils;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;
import org.marceloleite.mercado.simulator.order.MinimalAmounts;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;
import org.marceloleite.mercado.simulator.strategy.Strategy;

public class FirstStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.01;

	private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.01;

	private static final long TOTAL_BUY_STEPS = MathUtils.factorial(5);

	private static final long TOTAL_SELL_STEPS = MathUtils.factorial(6);

	private Currency currency;

	private BuySellStep buySellStep;

	private TemporalTickerPO baseTemporalTickerPO;

	private CurrencyAmount baseRealAmount;

	public FirstStrategy(Currency currency) {
		this.currency = currency;
		this.buySellStep = new BuySellStep(TOTAL_BUY_STEPS, TOTAL_SELL_STEPS);
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
		if (lastVariation <= SHRINK_PERCENTAGE_THRESHOLD) {
			ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
			LOGGER.debug(zonedDateTimeToStringConverter.convertTo(simulationTimeInterval.getEnd())
					+ ": Shrink threshold reached.");
			if (hasBalance(account)) {
				LOGGER.debug("Has balance for sell order.");
				createSellOrder(simulationTimeInterval, account, house);
				updateBaseTemporalTickerPO(house.getTemporalTickers().get(currency));
			}
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = calculateOrderAmount(OrderType.SELL, account);
		if (currencyAmountToSell != null) {
			CurrencyAmount currencyAmountToReceive = new CurrencyAmount(Currency.REAL, null);
			SellOrder sellOrder = new SellOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.selling(currencyAmountToSell)
					.receiving(currencyAmountToReceive).build();
			LOGGER.debug("Created " + sellOrder + ".");
			account.getSellOrdersTemporalController().add(sellOrder);
		}
	}

	private void checkGrowthPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		Double lastVariation = temporalTickerVariation.getLastVariation();
		if (lastVariation >= GROWTH_PERCENTAGE_THRESHOLD) {
			LOGGER.debug(zonedDateTimeToStringConverter.convertTo(simulationTimeInterval.getEnd())
					+ ": Growth threshold reached.");
			if (hasBalance(account)) {
				LOGGER.debug("Has balance for buy order.");
				createBuyOrder(simulationTimeInterval, account);
				updateBaseTemporalTickerPO(house.getTemporalTickers().get(currency));
			}
		}
	}

	private boolean hasBalance(Account account) {
		Double balanceAmount = account.getBalance().get(Currency.REAL).getAmount();
		return (balanceAmount > 0 && balanceAmount > MinimalAmounts.retrieveMinimalAmountFor(Currency.REAL));
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account) {
		CurrencyAmount currencyAmountToPay = calculateOrderAmount(OrderType.BUY, account);
		if (currencyAmountToPay != null) {
			CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
			BuyOrder buyOrder = new BuyOrderBuilder().toExecuteOn(simulationTimeInterval.getStart())
					.buying(currencyAmountToBuy).paying(currencyAmountToPay).build();
			LOGGER.debug("Buy order created is " + buyOrder);
			account.getBuyOrdersTemporalController().add(buyOrder);
		}
	}

	private void setBase(Account account, House house) {
		setBaseBalance(account.getBalance());
		setBaseTemporalTickers(house.getTemporalTickers());
	}

	private void setBaseTemporalTickers(Map<Currency, TemporalTickerPO> temporalTickers) {
		if (baseTemporalTickerPO == null) {
			baseTemporalTickerPO = temporalTickers.get(currency);
		}
	}

	private CurrencyAmount calculateOrderAmount(OrderType orderType, Account account) {
		Currency currency = (orderType == OrderType.SELL ? this.currency : Currency.REAL);
		CurrencyAmount balance = account.getBalance().get(currency);
		LOGGER.debug("Balance amount: " + balance + ".");
		double operationPercentage = calculateOperationPercentage(orderType);
		LOGGER.debug("Operation percentage: " + new PercentageFormatter().format(operationPercentage) + ".");
		double operationAmount = baseRealAmount.getAmount() * operationPercentage;
		LOGGER.debug("Initial operation amount: " + new DigitalCurrencyFormatter().format(operationAmount) + ".");
		Double minimalAmount = MinimalAmounts.retrieveMinimalAmountFor(currency);

		if (operationAmount <= balance.getAmount() && operationAmount < minimalAmount) {
			LOGGER.debug("Has balance for inital operation amount, but it is lower than the minimal order value.");
			operationAmount = minimalAmount;
		} else {
			LOGGER.debug("No balance for initial operation amount.");
			operationAmount = balance.getAmount();
			if (operationAmount < minimalAmount) {
				LOGGER.debug("No balance for minimal operation. Aborting order.");
				return null;
			}
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
		long checkStep = buySellStep.updateStep(orderType);
		if (checkStep > 0) {
			result = (double) checkStep / (double) TOTAL_BUY_STEPS;
		} else {
			result = (double) Math.abs(checkStep) / (double) TOTAL_SELL_STEPS;
		}
		LOGGER.debug("Operation percentage is " + new PercentageFormatter().format(result) + ".");
		return result;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(TimeInterval simulationTimeInterval,
			TemporalTickerPO currentTemporalTickerPO) {
		TemporalTickerVariation temporalTickerVariation = null;
		if (currentTemporalTickerPO != null) {
			temporalTickerVariation = new TemporalTickerVariation(baseTemporalTickerPO, currentTemporalTickerPO);
			LOGGER.debug(simulationTimeInterval + ": Last variation is "
					+ new PercentageFormatter().format(temporalTickerVariation.getLastVariation()));
		} else {
			LOGGER.debug("No ticker variation for period " + simulationTimeInterval + ".");
		}
		return temporalTickerVariation;
	}

	private void updateBaseTemporalTickerPO(TemporalTickerPO temporalTickerPO) {
		LOGGER.debug("Updating base temporal ticker.");
		this.baseTemporalTickerPO = new TemporalTickerPO(temporalTickerPO);
	}

}
