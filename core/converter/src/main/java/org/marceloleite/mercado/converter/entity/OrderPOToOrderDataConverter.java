package org.marceloleite.mercado.converter.entity;

import java.math.BigDecimal;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OperationPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OrderIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OrderPO;

public class OrderPOToOrderDataConverter implements Converter<OrderPO, OrderData> {

	@Override
	public OrderData convertTo(OrderPO orderPO) {
		OrderData orderData = new OrderData();

		orderData.setId(orderPO.getId().getId().longValue());
		orderData.setCreated(orderPO.getCreated());
		orderData.setUpdated(orderPO.getUpdated());
		orderData.setExecutedPriceAverage(new BigDecimal(orderPO.getExecutedPriceAverage().toString()));
		orderData.setExecutedQuantity(new BigDecimal(orderPO.getExecutedQuantity().toString()));
		orderData.setFee(new BigDecimal(orderPO.getFee().toString()));
		orderData.setFirstCurrency(orderPO.getFirstCurrency());
		orderData.setSecondCurrency(orderPO.getSecondCurrency());
		orderData.setStatus(orderPO.getStatus());
		orderData.setType(orderPO.getType());
		orderData.setHasFills(orderPO.getHasFills());
		orderData.setLimitPrice(new BigDecimal(orderPO.getLimitPrice().toString()));
		orderData.setQuantity(new BigDecimal(orderPO.getQuantity().toString()));

		List<OperationPO> operationPOs = orderPO.getOperationPOs();
		List<OperationData> operationDatas = new ListOperationPOToListOperationDataConverter().convertTo(operationPOs);
		operationDatas.forEach(operationData -> operationData.setOrderData(orderData));
		orderData.setOperationDatas(operationDatas);

		return orderData;
	}

	@Override
	public OrderPO convertFrom(OrderData orderData) {
		OrderPO orderPO = new OrderPO();
		OrderIdPO orderIdPO = new OrderIdPO();
		
		orderIdPO.setId(new BigDecimal(orderData.getId()));
		
		orderPO.setId(orderIdPO);
		orderPO.setCreated(orderData.getCreated());
		orderPO.setUpdated(orderData.getUpdated());
		orderPO.setExecutedPriceAverage(new BigDecimal(orderData.getExecutedPriceAverage().toString()));
		orderPO.setExecutedQuantity(new BigDecimal(orderData.getExecutedQuantity().toString()));
		orderPO.setFee(new BigDecimal(orderData.getFee().toString()));
		orderPO.setFirstCurrency(orderData.getFirstCurrency());
		orderPO.setSecondCurrency(orderData.getSecondCurrency());
		orderPO.setStatus(orderData.getStatus());
		orderPO.setType(orderData.getType());
		orderPO.setHasFills(orderData.getHasFills());
		orderPO.setLimitPrice(new BigDecimal(orderData.getLimitPrice().toString()));
		orderPO.setQuantity(new BigDecimal(orderData.getQuantity().toString()));
		
		List<OperationPO> operationPOs = new ListOperationPOToListOperationDataConverter().convertFrom(orderData.getOperationDatas());
		operationPOs.forEach(operationPO -> operationPO.setOrder(orderPO));
		orderPO.setOperationPOs(operationPOs);

		return orderPO;
	}

}
