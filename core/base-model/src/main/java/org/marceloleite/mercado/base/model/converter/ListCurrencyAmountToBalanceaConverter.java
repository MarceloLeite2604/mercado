package org.marceloleite.mercado.base.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.Balance;
import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;

public class ListCurrencyAmountToBalanceaConverter
		implements Converter<List<CurrencyAmount>, Balance> {

	@Override
	public Balance convertTo(List<CurrencyAmount> currencyAmounts) {
		Balance balance = new Balance();
		if (currencyAmounts != null && !currencyAmounts.isEmpty()) {
			for (CurrencyAmount currencyAmount : currencyAmounts) {
				balance.put(currencyAmount.getCurrency(), new CurrencyAmount(currencyAmount));
			}
		}
		return balance;
	}

	@Override
	public List<CurrencyAmount> convertFrom(Balance balance) {
		List<CurrencyAmount> currencyAmounts = new ArrayList<>();
		if (balance != null && !balance.isEmpty()) {
			for (Currency currency : balance.keySet()) {
				CurrencyAmount currencyAmount = balance.get(currency);
				currencyAmounts.add(currencyAmount);
			}
		}
		return currencyAmounts;
	}

}
