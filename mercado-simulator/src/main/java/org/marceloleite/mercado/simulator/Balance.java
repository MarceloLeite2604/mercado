package org.marceloleite.mercado.simulator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xmlstructures.CurrencyAmount;

public class Balance {

	private Map<Currency, CurrencyAmount> balances;

	public Balance() {
		balances = new EnumMap<>(Currency.class);
	}

	public void deposit(CurrencyAmount currencyAmountToDeposit) {
		CurrencyAmount currencyAmount = Optional.ofNullable(balances.get(currencyAmountToDeposit.getCurrency()))
			.orElse(new CurrencyAmount(currencyAmountToDeposit.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() + currencyAmountToDeposit.getAmount());
		balances.put(currencyAmount.getCurrency(), currencyAmount);
	}

	public void withdraw(CurrencyAmount currencyAmountToWithdraw) {
		CurrencyAmount currencyAmount = Optional.ofNullable(balances.get(currencyAmountToWithdraw.getCurrency()))
			.orElse(new CurrencyAmount(currencyAmountToWithdraw.getCurrency(), 0.0));
		currencyAmount.setAmount(currencyAmount.getAmount() - currencyAmountToWithdraw.getAmount());
		balances.put(currencyAmount.getCurrency(), currencyAmount);
	}

	public Map<Currency, CurrencyAmount> getBalances() {
		return balances;
	}
}
