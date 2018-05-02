package org.marceloleite.mercado.api.negotiation.method;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;

public class ListOrders extends TapiMethodTemplate<TapiResponse<List<Order>>> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "order_type", "status_list", "has_fulls",
			"from_id", "to_id", "from_timestamp", "to_timestamp"};

	public ListOrders(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.LIST_ORDERS, PARAMETER_NAMES);
	}

	public TapiResponse<List<Order>> execute(CurrencyPair coinPair, OrderType orderType,
			List<OrderStatus> orderStatusList, Boolean hasFills, Long fromId, Long toId, ZonedDateTime toTimestamp, ZonedDateTime fromTimeStamp) {
		Long orderTypeValue = generateOrderTypeParameterValue(orderType);
		String statusList = generateOrderStatusListParameterValue(orderStatusList);
		return executeMethod(coinPair, orderTypeValue, statusList, hasFills, fromId, toId, toTimestamp, fromTimeStamp);
	}
	
	public TapiResponse<List<Order>> execute(CurrencyPair coinPair) {
		return execute(coinPair, null, null, null, null, null, null, null);
	}

	

	private String generateOrderStatusListParameterValue(List<OrderStatus> orderStatusList) {
		String result = null;
		if (orderStatusList != null && !orderStatusList.isEmpty()) {
			result = "[";
			for (OrderStatus orderStatus : orderStatusList) {
				result += orderStatus.getValue() + ",";
			}
			result = result.substring(0, result.length() - 1);
			result = "]";
		}
		return result;
	}	
	
	private Long generateOrderTypeParameterValue(OrderType orderType) {
		Long orderTypeValue = null;
		if (orderType != null) {
			orderTypeValue = orderType.getValue();
		}
		return orderTypeValue;
	}
}
