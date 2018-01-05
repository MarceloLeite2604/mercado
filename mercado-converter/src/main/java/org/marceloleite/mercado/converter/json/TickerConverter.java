package org.marceloleite.mercado.converter.json;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.JsonTicker;
import org.marceloleite.mercado.jsonmodel.JsonTickerValues;
import org.marceloleite.mercado.databasemodel.TickerIdPO;

public class TickerConverter implements Converter<JsonTicker, TickerPO> {

	@Override
	public TickerPO convertTo(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		
		TickerIdPO tickerId = new TickerIdPO();
		tickerId.setCurrency(jsonTicker.getCurrency());
		tickerId.setTime(new LongToLocalDateTimeConverter().convertTo(jsonTickerValues.getDate()));
		TickerPO ticker = new TickerPO();
		ticker.setHigh(jsonTickerValues.getHigh());
		ticker.setLow(jsonTickerValues.getLow());
		ticker.setVol(jsonTickerValues.getVol());
		ticker.setLast(jsonTickerValues.getLast());
		ticker.setBuy(jsonTickerValues.getBuy());
		ticker.setSell(jsonTickerValues.getSell());
		ticker.setTickerId(tickerId);
		
		return ticker;
	}

	@Override
	public JsonTicker convertFrom(TickerPO object) {
		throw new UnsupportedOperationException();
	}

}
