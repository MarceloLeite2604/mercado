package org.marceloleite.mercado.base.model.order;

public enum OrderStatus {
	CREATED("created"),
	EXECUTED("executed"),
	CANCELLED("cancelled");
	
	private String value;

	private OrderStatus(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
