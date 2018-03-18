package org.marceloleite.mercado.xml.converters;

import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.xml.structures.XmlOrder;

public class OrderXmlConverter implements XmlConverter<XmlOrder, OrderData> {

	@Override
	public XmlOrder convertToXml(OrderData orderData) {
		XmlOrder xmlOrder = new XmlOrder();
		xmlOrder.setCreated(orderData.getCreated());
		xmlOrder.setExecutedPriceAverage(orderData.getExecutedPriceAverage());
		xmlOrder.setExecutedQuantity(orderData.getExecutedQuantity());
		xmlOrder.setFee(orderData.getFee());
		xmlOrder.setFirstCurrency(orderData.getFirstCurrency());
		xmlOrder.setHasFills(orderData.getHasFills());
		xmlOrder.setId(orderData.getId());
		xmlOrder.setIntended(orderData.getIntended());
		xmlOrder.setLimitPrice(orderData.getLimitPrice());
		xmlOrder.setQuantity(orderData.getQuantity());
		xmlOrder.setSecondCurrency(orderData.getSecondCurrency());
		xmlOrder.setStatus(orderData.getStatus());
		xmlOrder.setType(orderData.getType());
		xmlOrder.setUpdated(orderData.getUpdated());
		
		new XmlOperationConverter();
		xmlOrder.setXmlOperations();
		
		return null;
	}

	@Override
	public OrderData convertToObject(XmlOrder xmlOrder) {
		// TODO Auto-generated method stub
		return null;
	}

}
