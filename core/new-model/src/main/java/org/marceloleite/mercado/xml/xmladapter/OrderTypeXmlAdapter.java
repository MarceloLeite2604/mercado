package org.marceloleite.mercado.xml.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.OrderType;

public class OrderTypeXmlAdapter extends XmlAdapter<Long, OrderType> {

	@Override
	public OrderType unmarshal(Long orderTypeValue) throws Exception {
		
		return OrderType.getByValue(orderTypeValue);
	}

	@Override
	public Long marshal(OrderType orderType) throws Exception {
		return orderType.getValue();
	}

}
