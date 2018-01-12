package org.marceloleite.mercado.converter.json;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.JsonOperation;
import org.marceloleite.mercado.jsonmodel.JsonOrder;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.Operation;
import org.marceloleite.mercado.negotiationapi.model.Order;
import org.marceloleite.mercado.negotiationapi.model.OrderStatus;

public class JsonOrderToOrderConverter implements Converter<JsonOrder, Order> {

	@Override
	public Order convertTo(JsonOrder jsonOrder) {
		Order order = new Order();
		order.setId(jsonOrder.getOrderId());
		order.setCurrencyPair(CurrencyPair.retrieveByPairAcronym(jsonOrder.getCoinPair()));
		order.setOrderType(OrderType.getByValue(jsonOrder.getOrderType()));
		order.setOrderStatus(OrderStatus.getByValue(jsonOrder.getStatus()));
		order.setHasFills(jsonOrder.getHasFills());
		order.setQuantity(Double.parseDouble(jsonOrder.getQuantity()));
		order.setLimitPrice(Double.parseDouble(jsonOrder.getLimitPrice()));
		order.setExecutedQuantity(Double.parseDouble(jsonOrder.getExecutedQuantity()));
		order.setExecutedPriceAverage(Double.parseDouble(jsonOrder.getExecutedPriceAvg()));
		order.setFee(Double.parseDouble(jsonOrder.getFee()));
		LongToLocalDateTimeConverter longToLocalDateTimeConverter = new LongToLocalDateTimeConverter();
		long longCreatedTimestamp = Long.parseLong(jsonOrder.getCreatedTimestamp());
		order.setCreatedTimestamp(longToLocalDateTimeConverter.convertTo(longCreatedTimestamp));
		long longUpdatedTimestamp = Long.parseLong(jsonOrder.getUpdatedTimestamp());
		order.setUpdatedTimestamp(longToLocalDateTimeConverter.convertTo(longUpdatedTimestamp));
		
		List<JsonOperation> jsonOperations = jsonOrder.getOperations();
		List<Operation> operations = null;
		JsonOperationToOperationConverter jsonOperationToOperationConverter = new JsonOperationToOperationConverter();
		if ( jsonOperations != null && !jsonOperations.isEmpty()) {
			operations = new ArrayList<>();
			for (JsonOperation jsonOperation : jsonOrder.getOperations()) {
				operations.add(jsonOperationToOperationConverter.convertTo(jsonOperation));
			}
		}
		order.setOperations(operations);
		return order;
	}

	@Override
	public JsonOrder convertFrom(Order Order) {
		throw new UnsupportedOperationException();
	}

}