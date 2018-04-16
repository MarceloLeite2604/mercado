package org.marceloleite.mercado.model.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.TradeType;

public class TradeTypeXmlAdapter extends XmlAdapter<String, TradeType> {

	@Override
	public TradeType unmarshal(String value) throws Exception {

		return TradeType.getByValue(value);
	}

	@Override
	public String marshal(TradeType tradeType) throws Exception {
		return tradeType.getValue();
	}

}
