package org.marceloleite.mercado.modeler.persistence.model;

public enum Tables {
	TRADE("Trades", Trade.class),
	TICKER("Tickers", Ticker.class);
	
	private String name;
	
	private Class<?> clazz;

	private Tables(String name, Class<?> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
