package org.marceloleite.mercado.converter.json.getaccountinfo;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonWithdrawalLimits;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.WithdrawalLimits;

public class JsonWithdrawalLimitsToWithdrawalLimitsConverter
		implements Converter<JsonWithdrawalLimits, WithdrawalLimits> {

	@Override
	public WithdrawalLimits convertTo(JsonWithdrawalLimits jsonWithdrawalLimits) {
		WithdrawalLimits withdrawalLimits = new WithdrawalLimits();
		if (jsonWithdrawalLimits != null && !jsonWithdrawalLimits.isEmpty()) {
			JsonCurrencyAvaliableToCurrencyAvaliableConverter jsonCurrencyAvaliableToCurrencyAvaliableConverter = new JsonCurrencyAvaliableToCurrencyAvaliableConverter();
			for (Currency currency : jsonWithdrawalLimits.keySet()) {
				JsonCurrencyAvailable jsonCurrencyAvailable = jsonWithdrawalLimits.get(currency);
				withdrawalLimits.put(currency,
						jsonCurrencyAvaliableToCurrencyAvaliableConverter.convertTo(jsonCurrencyAvailable));
			}
		}
		return withdrawalLimits;
	}

	@Override
	public JsonWithdrawalLimits convertFrom(WithdrawalLimits withdrawalLimits) {
		throw new UnsupportedOperationException();
	}

}
