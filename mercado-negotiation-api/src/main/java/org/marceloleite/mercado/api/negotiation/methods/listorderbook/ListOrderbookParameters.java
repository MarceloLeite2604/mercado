package org.marceloleite.mercado.api.negotiation.methods.listorderbook;

public enum ListOrderbookParameters {

	COIN_PAIR("coin_pair"),
	FULL("full");
	
	private String name;


	ListOrderbookParameters(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
