package org.marceloleite.mercado.modeler.util.converter;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.consumer.model.JsonTicker;
import org.marceloleite.mercado.consumer.model.JsonTickerValues;
import org.marceloleite.mercado.modeler.persistence.model.Ticker;
import org.marceloleite.mercado.modeler.persistence.model.TickerId;

public class TickerConverter implements Converter<JsonTicker, Ticker> {

	@Override
	public Ticker format(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		
		TickerId tickerId = new TickerId();
		tickerId.setCurrency(jsonTicker.getCurrency());
		tickerId.setTime(new LongToLocalDateTimeConverter().format(jsonTickerValues.getDate()));
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
