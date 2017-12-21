package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.Ticker;
import org.marceloleite.mercado.databasemodel.TickerId;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;
import org.marceloleite.mercado.siteretriever.model.JsonTickerValues;

public class TickerConverter implements Converter<JsonTicker, Ticker> {

	@Override
	public Ticker convert(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		
		TickerId tickerId = new TickerId();
		tickerId.setCurrency(jsonTicker.getCurrency());
		tickerId.setTime(new LongToLocalDateTimeConverter().convert(jsonTickerValues.getDate()));
		Ticker ticker = new Ticker();
		ticker.setHigh(jsonTickerValues.getHigh());
		ticker.setLow(jsonTickerValues.getLow());
		ticker.setVol(jsonTickerValues.getVol());
		ticker.setLast(jsonTickerValues.getLast());
		ticker.setBuy(jsonTickerValues.getBuy());
		ticker.setSell(jsonTickerValues.getSell());
		ticker.setTickerId(tickerId);
		
		return ticker;
	}

}
