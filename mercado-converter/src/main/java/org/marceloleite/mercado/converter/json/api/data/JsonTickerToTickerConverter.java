package org.marceloleite.mercado.converter.json.api.data;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.data.Ticker;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTickerValues;

public class JsonTickerToTickerConverter implements Converter<JsonTicker, Ticker> {

	@Override
	public Ticker convertTo(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		
		Ticker ticker = new Ticker();
		ticker.setCurrency(jsonTicker.getCurrency());
		ticker.setTime(new LongToZonedDateTimeConverter().convertTo(jsonTickerValues.getDate()));
		ticker.setHigh(jsonTickerValues.getHigh());
		ticker.setLow(jsonTickerValues.getLow());
		ticker.setVolume(jsonTickerValues.getVol());
		ticker.setLast(jsonTickerValues.getLast());
		ticker.setBuy(jsonTickerValues.getBuy());
		ticker.setSell(jsonTickerValues.getSell());
		
		return ticker;
	}

	@Override
	public JsonTicker convertFrom(Ticker ticker) {
		throw new UnsupportedOperationException();
	}

}
