package org.marceloleite.mercado.converter.json.api.data;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.databasemodel.TickerIdPO;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTickerValues;

public class TickerConverter implements Converter<JsonTicker, TickerPO> {

	@Override
	public TickerPO convertTo(JsonTicker jsonTicker) {

		JsonTickerValues jsonTickerValues = jsonTicker.getTicker();
		
		TickerIdPO tickerId = new TickerIdPO();
		tickerId.setCurrency(jsonTicker.getCurrency());
		tickerId.setTime(new LongToZonedDateTimeConverter().convertTo(jsonTickerValues.getDate()));
		TickerPO ticker = new TickerPO();
		ticker.setHigh(jsonTickerValues.getHigh());
		ticker.setLow(jsonTickerValues.getLow());
		ticker.setVol(jsonTickerValues.getVol());
		ticker.setLast(jsonTickerValues.getLast());
		ticker.setBuy(jsonTickerValues.getBuy());
		ticker.setSell(jsonTickerValues.getSell());
		ticker.setTickerIdPO(tickerId);
		
		return ticker;
	}

	@Override
	public JsonTicker convertFrom(TickerPO object) {
		throw new UnsupportedOperationException();
	}

}
