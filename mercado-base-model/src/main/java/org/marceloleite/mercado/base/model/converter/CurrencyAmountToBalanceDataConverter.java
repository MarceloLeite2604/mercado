package org.marceloleite.mercado.base.model.converter;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.BalanceData;

public class CurrencyAmountToBalanceDataConverter implements Converter<CurrencyAmount, BalanceData> {

	@Override
	public BalanceData convertTo(CurrencyAmount currencyAmount) {
		BalanceData balanceData = new BalanceData();
		balanceData.setCurrency(currencyAmount.getCurrency());
		balanceData.setAmount(currencyAmount.getAmount());
		return balanceData;
	}

	@Override
	public CurrencyAmount convertFrom(BalanceData balanceData) {
		return new CurrencyAmount(balanceData.getCurrency(), balanceData.getAmount());
	}

}
