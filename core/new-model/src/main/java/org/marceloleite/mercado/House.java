package org.marceloleite.mercado;

import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Balance;
import org.marceloleite.mercado.model.TemporalTicker;

public interface House {
	
	void updateTemporalTickers(TimeInterval timeInterval);
	
	TemporalTicker getTemporalTickerFor(Currency currency);
	
	OrderExecutor getOrderExecutor();
	
	public MercadoBigDecimal getComissionPercentage();
	
	Map<String, Balance> getComissionBalance();
}
