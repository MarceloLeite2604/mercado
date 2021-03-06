package org.marceloleite.mercado.base.model;

import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.data.TemporalTicker;

public interface House {
	
	void updateTemporalTickers(TimeInterval timeInterval);
	
	Map<Currency, TemporalTicker> getTemporalTickers();
	
	OrderExecutor getOrderExecutor();
	
	public double getComissionPercentage();
	
	Map<String, Balance> getComissionBalance();
}
