package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.Ticker;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TickerIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TickerPO;

public class TickerPOToTickerConverter implements Converter<TickerPO, Ticker>{

	@Override
	public Ticker convertTo(TickerPO tickerPO) {
		Ticker ticker = new Ticker();
		TickerIdPO tickerIdPO = tickerPO.getId();
		ticker.setCurrency(tickerIdPO.getCurrency());
		ticker.setTime(tickerIdPO.getTickerTime());
		ticker.setBuy(tickerPO.getBuy());
		ticker.setHigh(tickerPO.getHigh());
		ticker.setLast(tickerPO.getLast());
		ticker.setLow(tickerPO.getLow());
		ticker.setSell(tickerPO.getSell());
		ticker.setVolume(tickerPO.getVolume());
		return ticker;
	}

	@Override
	public TickerPO convertFrom(Ticker tickerDataModel) {
		TickerPO tickerPO = new TickerPO();
		TickerIdPO tickerIdPO = new TickerIdPO();
		tickerIdPO.setCurrency(tickerDataModel.getCurrency());
		tickerIdPO.setTickerTime(tickerDataModel.getTime());
		tickerPO.setId(tickerIdPO);
		tickerPO.setBuy(tickerDataModel.getBuy());
		tickerPO.setHigh(tickerDataModel.getHigh());
		tickerPO.setLast(tickerDataModel.getLast());
		tickerPO.setLow(tickerDataModel.getLow());
		tickerPO.setSell(tickerDataModel.getSell());
		tickerPO.setVolume(tickerDataModel.getVolume());
		return tickerPO;
	}

	
}
