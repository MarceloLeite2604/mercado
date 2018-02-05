package org.marceloleite.mercado.databasemodel;

public enum Entity {
	TRADE("TRADES", TradePO.class),
	TICKER("TICKERS", TickerPO.class),
	PROPERTY("PROPERTIES", PropertyPO.class),
	TEMPORAL_TICKER("TEMPORAL_TICKERS", TemporalTickerPO.class);
	
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
