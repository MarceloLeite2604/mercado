package org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.BalanceApi;

public class JsonBalanceToBalanceApiConverter implements Converter<JsonBalance, BalanceApi> {

	@Override
	public BalanceApi convertTo(JsonBalance jsonBalance) {
		BalanceApi balance = new BalanceApi();
		if (jsonBalance != null && !jsonBalance.isEmpty()) {
			JsonCurrencyAvaliableToCurrencyAvaliableConverter jsonCurrencyAvaliableToCurrencyAvaliableConverter = new JsonCurrencyAvaliableToCurrencyAvaliableConverter();
			for (Currency currency : jsonBalance.keySet()) {
				JsonCurrencyAvailable jsonCurrencyAvailable = jsonBalance.get(currency);
				balance.put(currency,
						jsonCurrencyAvaliableToCurrencyAvaliableConverter.convertTo(jsonCurrencyAvailable));
			}
		}
		return balance;
	}

	@Override
	public JsonBalance convertFrom(BalanceApi balance) {
		throw new UnsupportedOperationException();
	}

}
