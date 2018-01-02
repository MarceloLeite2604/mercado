package org.marceloleite.mercado.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.Currency;

public class CurrencyXmlAdapter extends XmlAdapter<String, Currency> {

	@Override
	public Currency unmarshal(String currencyAcronym) throws Exception {
		
		return Currency.getByAcronym(currencyAcronym);
	}

	@Override
	public String marshal(Currency currency) throws Exception {
		return currency.getAcronym();
	}

}
