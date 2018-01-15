package org.marceloleite.mercado.api.negotiation.methods.listorders;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethodParameters;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listorders.CurrencyPair;
import org.marceloleite.mercado.negotiationapi.model.listorders.OrderStatus;

public class ListOrdersMethod extends AbstractTapiMethod<ListOrdersMethodResponse> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(ListOrdersMethod.class);

	public ListOrdersMethodResponse execute(CurrencyPair coinPair) {
		return execute(coinPair, null, null, null, null, null, null);
	}

	public ListOrdersMethodResponse execute(CurrencyPair coinPair, OrderType orderType,
			List<OrderStatus> orderStatusList, Boolean hasFills, Long fromId, Long toId, LocalDateTime toTimestamp) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(coinPair, orderType, orderStatusList,
				hasFills, fromId, toId, toTimestamp);
		return connectAndReadResponse(tapiMethodParameters);
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.LIST_ORDER;
	}
	
	@Override
	protected ListOrdersMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new ListOrdersMethodResponse(jsonTapiResponse);
	}

	private TapiMethodParameters generateTapiMethodParameters(CurrencyPair coinPair, OrderType orderType,
			List<OrderStatus> orderStatusList, Boolean hasFills, Long fromId, Long toId, LocalDateTime toTimestamp) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		tapiMethodParameters.put(ListOrdersParameters.COIN_PAIR.toString(), coinPair);

		Long orderTypeValue;
		if (orderType == null) {
			orderTypeValue = null;
		} else {
			orderTypeValue = orderType.getValue();
		}
		tapiMethodParameters.put(ListOrdersParameters.ORDER_TYPE.toString(), orderTypeValue);
		tapiMethodParameters.put(ListOrdersParameters.STATUS_LIST.toString(),
				generateOrderStatusListParameterValue(orderStatusList));
		tapiMethodParameters.put(ListOrdersParameters.HAS_FILLS.toString(), hasFills);
		tapiMethodParameters.put(ListOrdersParameters.FROM_ID.toString(), fromId);
		tapiMethodParameters.put(ListOrdersParameters.TO_ID.toString(), toId);
		tapiMethodParameters.put(ListOrdersParameters.TO_ID.toString(), toTimestamp);

		return tapiMethodParameters;
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

	
}
