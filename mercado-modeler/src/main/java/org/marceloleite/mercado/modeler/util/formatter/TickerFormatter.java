package org.marceloleite.mercado.modeler.util.formatter;

import org.marceloleite.mercado.commons.interfaces.Formatter;
import org.marceloleite.mercado.commons.util.LongToLocalDateTimeFormatter;
import org.marceloleite.mercado.consumer.model.JsonTicker;
import org.marceloleite.mercado.consumer.model.JsonTickerValues;
import org.marceloleite.mercado.modeler.persistence.Ticker;

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
		ticker.setDate(new LongToLocalDateTimeFormatter().format(jsonTickerValues.getDate()));

		return ticker;
	}

}
