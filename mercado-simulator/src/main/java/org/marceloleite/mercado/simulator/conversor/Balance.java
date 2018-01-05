package org.marceloleite.mercado.simulator.conversor;

import java.util.EnumMap;
import java.util.Optional;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.structure.BalanceData;
import org.marceloleite.mercado.simulator.structure.CurrencyAmountData;

public class Balance extends EnumMap<Currency, CurrencyAmount> {

	/**
	 * 
	 */
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

	public void deposit(CurrencyAmount currencyAmount) {
		Currency currency = currencyAmount.getCurrency();
		CurrencyAmount retrievedCurrency = getOrDefault(currency, new CurrencyAmount(currency, 0.0));
		retrievedCurrency.setAmount(retrievedCurrency.getAmount() + currencyAmount.getAmount());
		put(currency, currencyAmount);
	}

	public void withdraw(CurrencyAmount currencyAmountToWithdraw) {
		CurrencyAmount currencyAmount = Optional.ofNullable(get(currencyAmountToWithdraw.getCurrency()))
				.orElse(new CurrencyAmount(currencyAmountToWithdraw.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() - currencyAmountToWithdraw.getAmount());
		put(currencyAmount.getCurrency(), currencyAmount);
	}
}
