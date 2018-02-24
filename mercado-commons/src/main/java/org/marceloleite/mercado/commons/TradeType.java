package org.marceloleite.mercado.commons;

public enum TradeType {

	BUY("buy"),
	SELL("sell");

	private String value;

	private TradeType(String value) {
		this.value = value.toUpperCase();
	}

	public String getValue() {
		return value.toUpperCase();
	}

	@Override
	public String toString() {
		return value.toUpperCase();
	}

	public static TradeType retrieve(String value) {
		for (TradeType tradeType : values()) {
			if (tradeType.getValue().equals(value.toUpperCase())) {
				return tradeType;
			}
		}

		throw new IllegalArgumentException("Trade type not recognized.");
	}

	public static TradeType getByValue(String value) {
		for (TradeType tradeType : values()) {
			if (tradeType.value.equals(value.toUpperCase())) {
				return tradeType;
			}
		}
		throw new RuntimeException("Could not find a trade type with value \"" + value + "\".");
	}

}
