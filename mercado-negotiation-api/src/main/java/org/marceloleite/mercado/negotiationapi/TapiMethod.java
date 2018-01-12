package org.marceloleite.mercado.negotiationapi;

public enum TapiMethod {

	LIST_ORDER("list_orders");
	
	private String methodName;

	private TapiMethod(String methodName) {
		this.methodName = methodName;
	}
	
	@Override
	public String toString() {
		return methodName;
	}
}