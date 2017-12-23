package org.marceloleite.mercado.databasemodel;

public enum Entity {
	TRADE("Trades", Trade.class),
	TICKER("Tickers", Ticker.class),
	PROPERTY("Properties", Property.class);
	
	private String name;
	
	private Class<?> entityClass;

	private Entity(String name, Class<?> clazz) {
		this.name = name;
		this.entityClass = clazz;
	}

	public String getName() {
		return name;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
}
