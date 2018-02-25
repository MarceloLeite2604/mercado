package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.Ticker;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TickerIdPO;
import org.marceloleite.mercado.databasemodel.TickerPO;

public class TickerPOToTickerDataModelConverter implements Converter<TickerPO, Ticker>{

	@Override
	public Ticker convertTo(TickerPO tickerPO) {
		Ticker tickerDataModel = new Ticker();
		TickerIdPO tickerIdPO = tickerPO.getTickerIdPO();
		tickerDataModel.setCurrency(tickerIdPO.getCurrency());
		tickerDataModel.setTime(tickerIdPO.getTime());
		tickerDataModel.setBuy(tickerPO.getBuy());
		tickerDataModel.setHigh(tickerPO.getHigh());
		tickerDataModel.setLast(tickerPO.getLast());
		tickerDataModel.setLow(tickerPO.getLow());
		tickerDataModel.setSell(tickerPO.getSell());
		tickerDataModel.setVolume(tickerPO.getVol());
		return tickerDataModel;
	}

	@Override
	public TickerPO convertFrom(Ticker tickerDataModel) {
		TickerPO tickerPO = new TickerPO();
		TickerIdPO tickerIdPO = new TickerIdPO();
		tickerIdPO.setCurrency(tickerDataModel.getCurrency());
		tickerIdPO.setTime(tickerDataModel.getTime());
		tickerPO.setTickerIdPO(tickerIdPO);
		tickerPO.setBuy(tickerDataModel.getBuy());
		tickerPO.setHigh(tickerDataModel.getHigh());
		tickerPO.setLast(tickerDataModel.getLast());
		tickerPO.setLow(tickerDataModel.getLow());
		tickerPO.setSell(tickerDataModel.getSell());
		tickerPO.setVol(tickerDataModel.getVolume());
		return tickerPO;
	}

	
}
