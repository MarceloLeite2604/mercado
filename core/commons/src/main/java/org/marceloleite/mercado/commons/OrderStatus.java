package org.marceloleite.mercado.commons;

public enum OrderStatus {

	UNDEFINED(1L),
	OPEN(2L),
	CANCELLED(3L),
	FILLED(4L);
	
	private Long value;

	private OrderStatus(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
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
