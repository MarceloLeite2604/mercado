package org.marceloleite.mercado.simulator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.marceloleite.mercado.consumer.model.Currency;

public class Balance {

	private Map<Currency, CurrencyAmount> balances;

	public Balance() {
		balances = new EnumMap<Currency, CurrencyAmount>(Currency.class);
	}

	public void add(Currency currency, double amount) {
		CurrencyAmount currencyAmount = Optional.ofNullable(balances.get(currency))
			.orElse(new CurrencyAmount(currency, 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() + amount);
		balances.put(currency, currencyAmount);
	}
	
	public Map<Currency, CurrencyAmount> getBalances() {
		return balances;
	}
}
