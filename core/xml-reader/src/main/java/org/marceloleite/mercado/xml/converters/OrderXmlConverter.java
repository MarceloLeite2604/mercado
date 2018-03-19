package org.marceloleite.mercado.xml.converters;

import java.util.List;

import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.xml.structures.XmlOperation;
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
		
		ListOperationXmlConverter listOperationXmlConverter = new ListOperationXmlConverter();
		List<OperationData> operationDatas = orderData.getOperationDatas();
		List<XmlOperation> xmlOperations = listOperationXmlConverter.convertToXml(operationDatas);
		xmlOrder.setXmlOperations(xmlOperations);
		
		return xmlOrder;
	}

	@Override
	public OrderData convertToObject(XmlOrder xmlOrder) {
		OrderData orderData = new OrderData();
		orderData.setCreated(xmlOrder.getCreated());
		orderData.setExecutedPriceAverage(xmlOrder.getExecutedPriceAverage());
		orderData.setExecutedQuantity(xmlOrder.getExecutedQuantity());
		orderData.setFee(xmlOrder.getFee());
		orderData.setFirstCurrency(xmlOrder.getFirstCurrency());
		orderData.setHasFills(xmlOrder.getHasFills());
		orderData.setId(xmlOrder.getId());
		orderData.setIntended(xmlOrder.getIntended());
		orderData.setLimitPrice(xmlOrder.getLimitPrice());
		orderData.setQuantity(xmlOrder.getQuantity());
		orderData.setSecondCurrency(xmlOrder.getSecondCurrency());
		orderData.setStatus(xmlOrder.getStatus());
		orderData.setType(xmlOrder.getType());
		orderData.setUpdated(xmlOrder.getUpdated());
		
		ListOperationXmlConverter listOperationXmlConverter = new ListOperationXmlConverter();
		List<XmlOperation> xmlOperations = xmlOrder.getXmlOperations();
		List<OperationData> operationDatas = listOperationXmlConverter.convertToObject(xmlOperations);
		orderData.setOperationDatas(operationDatas);
		return orderData;
	}

}
