package org.marceloleite.mercado.api.negotiation.methods;

public enum TapiMethod {

	LIST_ORDERS("list_orders"),
	LIST_SYSTEM_MESSAGES("list_system_messages"),
	GET_ACCOUNT_INFO("get_account_info"),
	GET_ORDER("get_order"),
	LIST_ORDERBOOK("list_orderbook"),
	PLACE_BUY_ORDER("place_buy_order"),
	PLACE_SELL_ORDER("place_sell_order"),
	CANCEL_ORDER("cancel_order"),
	GET_WITHDRAWAL("get_withdrawal");
	
	private String methodName;

	private TapiMethod(String methodName) {
		this.methodName = methodName;
	}
	
	@Override
	public String toString() {
		return methodName;
	}
}
