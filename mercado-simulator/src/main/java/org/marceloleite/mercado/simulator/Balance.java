package org.marceloleite.mercado.simulator;

import java.util.EnumMap;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.BalanceData;
import org.marceloleite.mercado.simulator.structure.CurrencyAmountData;

public class Balance extends EnumMap<Currency, CurrencyAmount> {

	private static final Logger LOGGER = LogManager.getLogger(Balance.class);

	private static final long serialVersionUID = 1L;

	public Balance() {
		super(Currency.class);
	}

	public Balance(BalanceData balanceData) {
		super(Currency.class);

		for (CurrencyAmountData currencyAmount : balanceData.values()) {
			put(currencyAmount.getCurrency(), new CurrencyAmount(currencyAmount));
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
