package org.marceloleite.mercado.commons;

public enum OrderStatus {

	UNDEFINED(1),
	OPEN(2),
	CANCELLED(3),
	FILLED(4);
	
	private int value;

	private OrderStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static OrderStatus getByValue(long value) {
		for (OrderStatus orderStatus : values()) {
			if (orderStatus.getValue() == value) {
				return orderStatus;
			}
		}
		throw new IllegalArgumentException("Invalid order status value: " + value);
	}
}
