package org.marceloleite.mercado.commons;

public enum OrderType {
	BUY(1l),
	SELL(2l);

	private long value;

	private OrderType(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public static OrderType getByValue(long value) {
		for (OrderType orderType : values()) {
			if (orderType.getValue() == value) {
				return orderType;
			}
		}
		throw new IllegalArgumentException("Invalid order type value: " + value);
	}
}
