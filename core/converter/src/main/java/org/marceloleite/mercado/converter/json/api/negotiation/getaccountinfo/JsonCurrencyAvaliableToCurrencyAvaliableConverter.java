package org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonCurrencyAvailable;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.CurrencyAvailable;

public class JsonCurrencyAvaliableToCurrencyAvaliableConverter
		implements Converter<JsonCurrencyAvailable, CurrencyAvailable> {

	@Override
	public CurrencyAvailable convertTo(JsonCurrencyAvailable jsonCurrencyAvailable) {
		CurrencyAvailable currencyAvailable = new CurrencyAvailable();
		currencyAvailable.setAvailable(jsonCurrencyAvailable.getAvaliable());
		currencyAvailable.setTotal(jsonCurrencyAvailable.getTotal());
		currencyAvailable.setAmountOpenOrders(jsonCurrencyAvailable.getAmountOpenOrders());
		return currencyAvailable;
	}

	@Override
	public JsonCurrencyAvailable convertFrom(CurrencyAvailable currencyAvailable) {
		throw new UnsupportedOperationException();
	}

}
