package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.xml.structures.XmlOrder;

public class ListOrderXmlConverter implements XmlConverter<List<XmlOrder>, List<OrderData>> {

	@Override
	public List<XmlOrder> convertToXml(List<OrderData> orderDatas) {
		List<XmlOrder> xmlOrders = new ArrayList<>();
		if (orderDatas != null && !orderDatas.isEmpty()) {
			OrderXmlConverter orderXmlConverter = new OrderXmlConverter();
			for (OrderData orderData : orderDatas) {
				xmlOrders.add(orderXmlConverter.convertToXml(orderData));
			}
		}
		return xmlOrders;
	}

	@Override
	public List<OrderData> convertToObject(List<XmlOrder> xmlOrders) {
		List<OrderData> orderDatas = new ArrayList<>();
		if (xmlOrders != null && !xmlOrders.isEmpty()) {
			OrderXmlConverter orderXmlConverter = new OrderXmlConverter();
			for (XmlOrder xmlOrder : xmlOrders) {
				orderDatas.add(orderXmlConverter.convertToObject(xmlOrder));
			}
		}
		return orderDatas;
	}

}
