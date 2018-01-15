package org.marceloleite.mercado.converter.json.getaccountinfo;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.Balance;

public class JsonBalanceToBalanceConverter implements Converter<JsonBalance, Balance> {

	@Override
	public Balance convertTo(JsonBalance jsonBalance) {
		Balance balance = new Balance();
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
	public JsonBalance convertFrom(Balance balance) {
		throw new UnsupportedOperationException();
	}

}
