package org.marceloleite.mercado.negotiationapi;

public enum TapiMethod {

	LIST_ORDER("list_orders"),
	LIST_SYSTEM_MESSAGES("list_system_messages"),
	GET_ACCOUNT_INFO("get_account_info");
	
	private String methodName;

	private TapiMethod(String methodName) {
		this.methodName = methodName;
	}
	
	@Override
	public String toString() {
		return methodName;
	}
}
