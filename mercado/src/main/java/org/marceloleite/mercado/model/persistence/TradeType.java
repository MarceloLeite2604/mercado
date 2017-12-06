package org.marceloleite.mercado.model.persistence;

public enum TradeType {

	BUY("buy"), SELL("sell");

	private String value;

	private TradeType(String value) {
		this.value = value;
	}

	private String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static TradeType retrieve(String value) {
		for (TradeType tradeType : values()) {
			if (tradeType.getValue()
				.equals(value)) {
				return tradeType;
			}
		}

		throw new IllegalArgumentException("Trade type not recognized.");
	}

}
