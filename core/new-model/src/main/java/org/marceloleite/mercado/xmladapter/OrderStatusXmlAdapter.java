package org.marceloleite.mercado.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.OrderStatus;

public class OrderStatusXmlAdapter extends XmlAdapter<Long, OrderStatus> {

	@Override
	public OrderStatus unmarshal(Long orderStatusValue) throws Exception {
		
		return OrderStatus.getByValue(orderStatusValue);
	}

	@Override
	public Long marshal(OrderStatus orderStatus) throws Exception {
		return orderStatus.getValue();
	}

}
