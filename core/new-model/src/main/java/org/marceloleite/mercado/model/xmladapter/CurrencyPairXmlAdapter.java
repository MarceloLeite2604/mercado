package org.marceloleite.mercado.model.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.CurrencyPair;

public class CurrencyPairXmlAdapter extends XmlAdapter<String, CurrencyPair> {

	@Override
	public CurrencyPair unmarshal(String currencyPairAcronym) throws Exception {
		
		return CurrencyPair.retrieveByPairAcronym(currencyPairAcronym);
	}

	@Override
	public String marshal(CurrencyPair currencyPair) throws Exception {
		return currencyPair.toString();
	}

}
