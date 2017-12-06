package org.marceloleite.mercado.util.formatter;

import org.marceloleite.mercado.model.json.JsonTicker;
import org.marceloleite.mercado.model.json.JsonTickerValues;
import org.marceloleite.mercado.model.persistence.Ticker;

public class TickerFormatter implements Formatter<JsonTicker, Ticker> {

	@Override
	public Ticker format(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		Ticker ticker = new Ticker();
		ticker.setHigh(jsonTickerValues.getHigh());
		ticker.setLow(jsonTickerValues.getLow());
		ticker.setVol(jsonTickerValues.getVol());
		ticker.setLast(jsonTickerValues.getLast());
		ticker.setBuy(jsonTickerValues.getBuy());
		ticker.setSell(jsonTickerValues.getSell());
		ticker.setDate(new LongToCalendarFormatter().format(jsonTickerValues.getDate()));

		return ticker;
	}

}
