package org.marceloleite.mercado.converter.json.api.negotiation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.BalanceApi;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.CurrencyAvailable;

public class BalanceApiToListBalanceDataConverter implements Converter<BalanceApi, List<BalanceData>> {

	@Override
	public List<BalanceData> convertTo(BalanceApi balanceApi) {
		List<BalanceData> balanceDatas = new ArrayList<>();
		if ( balanceApi != null && !balanceApi.isEmpty()) {
			for ( Currency currency : balanceApi.keySet()) {
				CurrencyAvailable currencyAvailable = balanceApi.get(currency);
				BalanceData balanceData = new BalanceData();
				balanceData.setCurrency(currency);
				balanceData.setAmount(new BigDecimal(currencyAvailable.getAvailable().toString()));
				balanceDatas.add(balanceData);
			}
		}
		return balanceDatas;
	}

	@Override
	public BalanceApi convertFrom(List<BalanceData> balanceDatas) {
		throw new UnsupportedOperationException();
	}

}
