package org.marceloleite.mercado.converter.json.api.negotiation;

import java.math.BigDecimal;
import java.util.List;

import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOperation;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class JsonOrderToOrderDataConverter implements Converter<JsonOrder, OrderData> {

	@Override
	public OrderData convertTo(JsonOrder jsonOrder) {
		OrderData order = new OrderData();
		order.setId(jsonOrder.getOrderId());
		CurrencyPair currencyPair = CurrencyPair.retrieveByPairAcronym(jsonOrder.getCoinPair());
		order.setFirstCurrency(currencyPair.getFirstCurrency());
		order.setSecondCurrency(currencyPair.getSecondCurrency());
		order.setType(OrderType.getByValue(jsonOrder.getOrderType()));
		order.setStatus(OrderStatus.getByValue(jsonOrder.getStatus()));
		order.setHasFills(jsonOrder.getHasFills());
		order.setQuantity(new BigDecimal(jsonOrder.getQuantity()));
		order.setLimitPrice(new BigDecimal(jsonOrder.getLimitPrice()));
		order.setExecutedQuantity(new BigDecimal(jsonOrder.getExecutedQuantity()));
		order.setExecutedPriceAverage(new BigDecimal(jsonOrder.getExecutedPriceAvg()));
		order.setFee(new BigDecimal(jsonOrder.getFee()));
		LongToZonedDateTimeConverter longToZonedDateTimeConverter = new LongToZonedDateTimeConverter();
		long longCreatedTimestamp = Long.parseLong(jsonOrder.getCreatedTimestamp());
		order.setCreated(longToZonedDateTimeConverter.convertTo(longCreatedTimestamp));
		long longUpdatedTimestamp = Long.parseLong(jsonOrder.getUpdatedTimestamp());
		order.setUpdated(longToZonedDateTimeConverter.convertTo(longUpdatedTimestamp));

		List<JsonOperation> jsonOperations = jsonOrder.getOperations();
		List<OperationData> operationDatas = new ListJsonOperationToListOperationDataConverter()
				.convertTo(jsonOperations);
		order.setOperationDatas(operationDatas);
		return order;
	}

	@Override
	public JsonOrder convertFrom(OrderData orderData) {
		throw new UnsupportedOperationException();
	}

}
