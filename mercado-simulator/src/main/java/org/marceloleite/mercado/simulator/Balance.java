package org.marceloleite.mercado.simulator;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.commons.Currency;

public class Balance extends EnumMap<Currency, CurrencyAmount> {

	private static final Logger LOGGER = LogManager.getLogger(Balance.class);

	private static final long serialVersionUID = 1L;

	public Balance() {
		super(Currency.class);
	}

	public Balance(List<BalanceData> balanceDatas) {
		super(Currency.class);

		for (BalanceData balanceData : balanceDatas) {
			CurrencyAmount currencyAmount = new CurrencyAmount(balanceData.getCurrency(), balanceData.getAmount());
			put(balanceData.getCurrency(), currencyAmount);
		}
	}

	public Balance(Balance balance) {
		super(balance);
	}

	public void deposit(CurrencyAmount currencyAmount) {
		LOGGER.debug("Depositing " + currencyAmount + ".");
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = getOrDefault(currency, new CurrencyAmount(currency, 0.0));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() + currencyAmount.getAmount());
		LOGGER.debug("New balance is " + retrievedCurrency + ".");
		put(currency, retrievedCurrency);
	}

	public void withdraw(CurrencyAmount currencyAmount) {
		LOGGER.debug("Withdrawing " + currencyAmount + ".");
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = Optional.ofNullable(get(currency)).orElse(new CurrencyAmount(currency, 0.0));
		LOGGER.debug("Previous balance was " + retrievedCurrency + ".");
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() - currencyAmount.getAmount());
		LOGGER.debug("New balance is " + retrievedCurrency + ".");
		put(currency, retrievedCurrency);
	}
}
