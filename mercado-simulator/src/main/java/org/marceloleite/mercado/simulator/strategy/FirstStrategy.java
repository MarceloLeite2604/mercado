package org.marceloleite.mercado.simulator.strategy;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.Balance;
import org.marceloleite.mercado.simulator.BuyOrder;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.House;
import org.marceloleite.mercado.simulator.MathUtils;
import org.marceloleite.mercado.simulator.TemporalTickerVariation;
import org.marceloleite.mercado.simulator.converter.BuyOrderToStringConverter;
import org.marceloleite.mercado.simulator.converter.CurrencyAmountToStringConverter;

public class FirstStrategy implements Strategy {

	private static final Logger LOGGER = LogManager.getLogger(FirstStrategy.class);

	private static final double GROWTH_PERCENTAGE_THRESHOLD = 0.03;

	private static final double SHRINK_PERCENTAGE_THRESHOLD = -0.03;

	private static final long TOTAL_BUY_DIVISIONS = MathUtils.factorial(3);

	private static final long TOTAL_SELL_DIVISIONS = MathUtils.factorial(5);

	private Currency currency;

	private long buySellStep = 0l;

	private TemporalTickerPO baseTemporalTickerPO;

	private CurrencyAmount baseCurrencyAmount;

	@Override
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		setBase(account, house);
		TemporalTickerVariation temporalTickerVariation = generateTemporalTickerVariations(house);
		LOGGER.debug("Average variation is " + temporalTickerVariation.getAverageVariation());
		if (temporalTickerVariation.getAverageVariation() >= GROWTH_PERCENTAGE_THRESHOLD) {
			informBuyIntent();
			CurrencyAmount currencyAmountToInvest = calculateOperationCurrencyAmount(account, Currency.REAL);
			if (currencyAmountToInvest.getAmount() > 0.0) {
				CurrencyAmount currencyAmountToBuy = new CurrencyAmount(currency, null);
				BuyOrder buyOrder = new BuyOrder(simulationTimeInterval.getEnd(), currencyAmountToBuy,
						currencyAmountToInvest);
				LOGGER.debug("Buy order created is " + new BuyOrderToStringConverter().convertTo(buyOrder));
				account.getBuyOrdersTemporalController().add(buyOrder);
			}
		} else if (temporalTickerVariation.getAverageVariation() <= SHRINK_PERCENTAGE_THRESHOLD) {
			informSellIntent();
			CurrencyAmount currencyAmountToInvest = calculateOperationCurrencyAmount(account, Currency.BITCOIN);
			if (currencyAmountToInvest.getAmount() > 0.0) {
				CurrencyAmount currencyAmountToBuy = new CurrencyAmount(Currency.REAL, null);
				BuyOrder buyOrder = new BuyOrder(simulationTimeInterval.getEnd(), currencyAmountToBuy,
						currencyAmountToInvest);
				LOGGER.debug("Buy order created is " + new BuyOrderToStringConverter().convertTo(buyOrder));
				account.getBuyOrdersTemporalController().add(buyOrder);
			}
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

	private CurrencyAmount calculateOperationCurrencyAmount(Account account, Currency currency) {
		CurrencyAmount currentCurrencyAmount = account.getBalance().get(currency);
		double operationPercentage = calculateOperationPercentage();
		double operationAmount;

		if (baseCurrencyAmount.getAmount() * operationPercentage > currentCurrencyAmount.getAmount()) {
			operationAmount = currentCurrencyAmount.getAmount();
		} else {
			operationAmount = baseCurrencyAmount.getAmount() * operationPercentage;
		}

		CurrencyAmount currencyAmount = new CurrencyAmount(Currency.REAL, operationAmount);
		LOGGER.debug("Amount of currency for operation is "
				+ new CurrencyAmountToStringConverter().convertTo(currencyAmount) + ".");
		return currencyAmount;
	}

	private void setBaseBalance(Balance balance) {
		if (baseCurrencyAmount == null) {
			LOGGER.debug("Setting base balance for " + currency + " currency.");
			baseCurrencyAmount = balance.get(currency);
		}

	}

	private double calculateOperationPercentage() {
		double result;
		if (buySellStep > 0) {
			result = MathUtils.factorial(buySellStep) / TOTAL_BUY_DIVISIONS;
		} else {
			result = MathUtils.factorial(Math.abs(buySellStep)) / TOTAL_SELL_DIVISIONS;
		}
		LOGGER.debug("Operation percentage is " + result);
		return result;
	}

	private void informBuyIntent() {
		if (buySellStep <= 0l) {
			buySellStep = 1l;
		} else {
			if (Math.abs(buySellStep) < TOTAL_BUY_DIVISIONS) {
				buySellStep++;
			}
		}
		LOGGER.debug("Buy/sell step is " + buySellStep);
	}

	private void informSellIntent() {
		if (buySellStep >= 0l) {
			buySellStep = -1l;
		} else {
			if (Math.abs(buySellStep) < TOTAL_SELL_DIVISIONS) {
				buySellStep--;
			}
		}
		LOGGER.debug("Buy/sell step is " + buySellStep);
	}

	private TemporalTickerVariation generateTemporalTickerVariations(House house) {
		Map<Currency, TemporalTickerPO> temporalTickers = house.getTemporalTickers();
		TemporalTickerPO currentTemporalTickerPO = temporalTickers.get(currency);
		TemporalTickerVariation temporalTickerVariation = new TemporalTickerVariation(baseTemporalTickerPO,
				currentTemporalTickerPO);
		LOGGER.debug(
				"Temporal ticker variation is:\n" + new ObjectToJsonConverter().convertTo(temporalTickerVariation));
		return temporalTickerVariation;
	}

}
