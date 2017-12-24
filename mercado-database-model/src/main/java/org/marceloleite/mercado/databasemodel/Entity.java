package org.marceloleite.mercado.databasemodel;

public enum Entity {
	TRADE("Trades", TradePO.class),
	TICKER("Tickers", TickerPO.class),
	PROPERTY("Properties", PropertyPO.class);
	
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
