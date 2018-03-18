package org.marceloleite.mercado.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.OrderStatus;

public class OrderStatusXmlAdapter extends XmlAdapter<Integer, OrderStatus> {

	@Override
	public OrderStatus unmarshal(Integer orderStatusValue) throws Exception {
		
		return OrderStatus.getByValue(orderStatusValue);
	}

	@Override
	public Integer marshal(OrderStatus orderStatus) throws Exception {
		return orderStatus.getValue();
	}

}
