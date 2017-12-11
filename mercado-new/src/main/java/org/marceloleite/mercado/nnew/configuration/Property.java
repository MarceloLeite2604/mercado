package org.marceloleite.mercado.nnew.configuration;

public enum Property {

	BITCOIN_BUYING_DATE("bitcoin.buying_date"),
	BITCOIN_AMOUNT("bitcoin.amount"),
	BITCOIN_VALUE("bitcoin.value");

	private String name;

	private Property(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
