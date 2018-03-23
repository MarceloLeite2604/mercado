package org.marceloleite.mercado.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class MercadoBigDecimalXmlAdapter extends XmlAdapter<String, MercadoBigDecimal> {

	@Override
	public MercadoBigDecimal unmarshal(String content) throws Exception {
		return new MercadoBigDecimal(content);
	}

	@Override
	public String marshal(MercadoBigDecimal mercadoBigDecimal) throws Exception {
		return mercadoBigDecimal.toString();
	}

}
