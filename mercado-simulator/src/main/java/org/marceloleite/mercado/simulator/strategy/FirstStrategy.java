package org.marceloleite.mercado.simulator.strategy;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.Balance;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.MathUtils;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.converter.BuyOrderToStringConverter;
import org.marceloleite.mercado.simulator.converter.CurrencyAmountToStringConverter;
import org.marceloleite.mercado.simulator.converter.SellOrderToStringConverter;
import org.marceloleite.mercado.simulator.order.BuyOrder;
import org.marceloleite.mercado.simulator.order.SellOrder;

public class FirstStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.03;

	private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.03;

	private static final long TOTAL_BUY_STEPS = MathUtils.factorial(3);

	private static final long TOTAL_SELL_STEPS = MathUtils.factorial(5);

	private Currency currency;

	private BuySellStep buySellStep;

	private TemporalTickerPO baseTemporalTickerPO;

	private CurrencyAmount baseRealAmount;

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
		this.buySellStep = new BuySellStep(TOTAL_BUY_STEPS, TOTAL_SELL_STEPS);
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		setBase(account, house);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariation(house);

		checkGrowthPercentage(simulationTimeInterval, account, house, temporalTickerVariation);

		checkShrinkPercentage(simulationTimeInterval, account, house, temporalTickerVariation);
	}

	private void checkShrinkPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		if (temporalTickerVariation.getAverageVariation() <= SHRINK_PERCENTAGE_THRESHOLD) {
			LOGGER.debug("Shrink threshold reached.");
			if (checkBalanceForSellOrder(account)) {
				LOGGER.debug("Has balance for sell order.");
				buySellStep.updateStep(OrderType.SELL);
				createSellOrder(simulationTimeInterval, account, house);
				updateBaseTemporalTickerPO(house.getTemporalTickers().get(currency));
			}
		}
	}

	private void createSellOrder(TimeInterval simulationTimeInterval, Account account, House house) {
		CurrencyAmount currencyAmountToSell = calculateCurrencyAmountToSell(account, house);
		CurrencyAmount currencyAmountToRetrieve = new CurrencyAmount(Currency.REAL, null);
		SellOrder sellOrder = new SellOrder(simulationTimeInterval.getStart(), currencyAmountToSell,
				currencyAmountToRetrieve);
		LOGGER.debug("Sell order created is " + new SellOrderToStringConverter().convertTo(sellOrder));
		account.getSellOrdersTemporalController().add(sellOrder);
	}

	private boolean checkBalanceForSellOrder(Account account) {
		return (account.getBalance().get(currency).getAmount() > 0);
	}

	private void checkGrowthPercentage(TimeInterval simulationTimeInterval, Account account, House house,
			TemporalTickerVariation temporalTickerVariation) {
		if (temporalTickerVariation.getAverageVariation() >= GROWTH_PERCENTAGE_THRESHOLD) {
			LOGGER.debug("Growth threshold reached.");
			if (checkBalanceForBuyOrder(account)) {
				LOGGER.debug("Has balance for buy order.");
				buySellStep.updateStep(OrderType.BUY);
				createBuyOrder(simulationTimeInterval, account);
				updateBaseTemporalTickerPO(house.getTemporalTickers().get(currency));
			}
		}
	}

	private boolean checkBalanceForBuyOrder(Account account) {
		return (account.getBalance().get(Currency.REAL).getAmount() > 0);
	}

	private void createBuyOrder(TimeInterval simulationTimeInterval, Account account) {
		CurrencyAmount currencyAmountToInvest = calculateCurrencyAmountToBuy(account);
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
		BuyOrder buyOrder = new BuyOrder(simulationTimeInterval.getStart(), currencyAmountToBuy,
				currencyAmountToInvest);
		LOGGER.debug("Buy order created is " + new BuyOrderToStringConverter().convertTo(buyOrder));
		account.getBuyOrdersTemporalController().add(buyOrder);
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

	private CurrencyAmount calculateCurrencyAmountToBuy(Account account) {
		CurrencyAmount currentCurrencyAmount = account.getBalance().get(Currency.REAL);
		double operationPercentage = calculateOperationPercentage(OrderType.BUY);
		double operationAmount;

		CurrencyAmountToStringConverter currencyAmountToStringConverter = new CurrencyAmountToStringConverter();
		LOGGER.debug("Base currency amount: " + currencyAmountToStringConverter.convertTo(baseRealAmount));
		LOGGER.debug("Current currency amount: " + currencyAmountToStringConverter.convertTo(currentCurrencyAmount));
		if (baseRealAmount.getAmount() * operationPercentage > currentCurrencyAmount.getAmount()) {
			operationAmount = currentCurrencyAmount.getAmount();
		} else {
			operationAmount = baseRealAmount.getAmount() * operationPercentage;
		}

		CurrencyAmount currencyAmount = new CurrencyAmount(Currency.REAL, operationAmount);
		LOGGER.debug("Amount of currency to buy is " + currencyAmountToStringConverter.convertTo(currencyAmount) + ".");
		return currencyAmount;
	}

	private CurrencyAmount calculateCurrencyAmountToSell(Account account, House house) {
		CurrencyAmount currentCurrencyAmount = account.getBalance().get(currency);
		double operationPercentage = calculateOperationPercentage(OrderType.SELL);
		double operationAmount;

		CurrencyAmountToStringConverter currencyAmountToStringConverter = new CurrencyAmountToStringConverter();
		LOGGER.debug("Base " + Currency.REAL.getAcronym() + " amount: "
				+ currencyAmountToStringConverter.convertTo(baseRealAmount));
		LOGGER.debug("Current " + currency.getAcronym() + " amount: "
				+ currencyAmountToStringConverter.convertTo(currentCurrencyAmount));
		double realToRetrieve = baseRealAmount.getAmount() * operationPercentage;
		LOGGER.debug(Currency.REAL.getAcronym() + " amount to retrieve: " + realToRetrieve);
		double currencyAmountToSell = calculateCurrencyAmountToSell(realToRetrieve,
				house.getTemporalTickers().get(currency));
		LOGGER.debug(currency.getAcronym() + " equivalent: " + currencyAmountToSell);
		if (currencyAmountToSell > currentCurrencyAmount.getAmount()) {
			operationAmount = currentCurrencyAmount.getAmount();
		} else {
			operationAmount = currencyAmountToSell;
		}

		CurrencyAmount currencyAmount = new CurrencyAmount(currency, operationAmount);
		LOGGER.debug(
				"Amount of currency for sell is " + currencyAmountToStringConverter.convertTo(currencyAmount) + ".");
		return currencyAmount;
	}

	private double calculateCurrencyAmountToSell(double realToRetrieve, TemporalTickerPO temporalTickerPO) {
		return realToRetrieve / temporalTickerPO.getAverage();
	}

	private void setBaseBalance(Balance balance) {
		if (baseRealAmount == null) {
			baseRealAmount = new CurrencyAmount(balance.get(Currency.REAL));
			LOGGER.debug(
					"Base real amount is " + new CurrencyAmountToStringConverter().convertTo(baseRealAmount) + ".");
		}

	}

	private double calculateOperationPercentage(OrderType orderType) {
		double result;
		long checkStep = buySellStep.checkStep(orderType);
		if (checkStep > 0) {
			result = (double) MathUtils.factorial(checkStep) / (double) TOTAL_BUY_STEPS;
		} else {
			result = (double) MathUtils.factorial(Math.abs(checkStep)) / (double) TOTAL_SELL_STEPS;
		}
		LOGGER.debug("Operation percentage is " + result);
		return result;
	}

	private TemporalTickerVariation generateTemporalTickerVariation(House house) {
		Map<Currency, TemporalTickerPO> temporalTickers = house.getTemporalTickers();
		TemporalTickerPO currentTemporalTickerPO = temporalTickers.get(currency);
		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTickerPO,
				currentTemporalTickerPO);
		LOGGER.debug("Average variation is " + temporalTickerVariation.getAverageVariation());
		return temporalTickerVariation;
	}

	private void updateBaseTemporalTickerPO(TemporalTickerPO temporalTickerPO) {
		LOGGER.debug("Updating base temporal ticker.");
		this.baseTemporalTickerPO = new TemporalTickerPO(temporalTickerPO);
	}

}
