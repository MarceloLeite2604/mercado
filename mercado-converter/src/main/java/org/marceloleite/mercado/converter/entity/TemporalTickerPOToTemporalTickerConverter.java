package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerPOToTemporalTickerConverter implements Converter<TemporalTickerPO, TemporalTicker>{

	@Override
	public TemporalTicker convertTo(TemporalTickerPO temporalTickerPO) {
		TemporalTicker temporalTicker = new TemporalTicker();
		TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
		temporalTicker.setCurrency(temporalTickerIdPO.getCurrency());
		temporalTicker.setStart(temporalTickerIdPO.getStart());
		temporalTicker.setEnd(temporalTickerIdPO.getEnd());
		temporalTicker.setOrders(temporalTickerPO.getOrders());
		temporalTicker.setBuyOrders(temporalTickerPO.getBuyOrders());
		temporalTicker.setSellOrders(temporalTickerPO.getSellOrders());
		temporalTicker.setBuy(temporalTickerPO.getBuy());
		temporalTicker.setPreviousBuy(temporalTickerPO.getPreviousBuy());
		temporalTicker.setSell(temporalTickerPO.getSell());
		temporalTicker.setPreviousSell(temporalTickerPO.getPreviousSell());
		temporalTicker.setLastPrice(temporalTickerPO.getLast());
		temporalTicker.setPreviousLastPrice(temporalTickerPO.getPreviousLast());
		temporalTicker.setFirstPrice(temporalTickerPO.getFirst());
		temporalTicker.setHighestPrice(temporalTickerPO.getHigh());
		temporalTicker.setLowestPrice(temporalTickerPO.getLow());
		temporalTicker.setAveragePrice(temporalTickerPO.getAverage());
		temporalTicker.setTimeDuration(temporalTickerPO.getDuration());
		temporalTicker.setVolumeTrades(temporalTickerPO.getVol());
		return temporalTicker;
	}

	@Override
	public TemporalTickerPO convertFrom(TemporalTicker temporalTicker) {
		TemporalTickerPO temporalTickerPO = new TemporalTickerPO();
		
		TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
		temporalTickerIdPO.setCurrency(temporalTicker.getCurrency());
		temporalTickerIdPO.setStart(temporalTicker.getStart());
		temporalTickerIdPO.setEnd(temporalTicker.getEnd());
		temporalTickerPO.setTemporalTickerIdPO(temporalTickerIdPO);
		
		temporalTickerPO.setOrders(temporalTicker.getOrders());
		temporalTickerPO.setBuyOrders(temporalTicker.getBuyOrders());
		temporalTickerPO.setSellOrders(temporalTicker.getSellOrders());
		temporalTickerPO.setBuy(temporalTicker.getBuy());
		temporalTickerPO.setPreviousBuy(temporalTicker.getPreviousBuy());
		temporalTickerPO.setSell(temporalTicker.getSell());
		temporalTickerPO.setPreviousSell(temporalTicker.getPreviousSell());
		temporalTickerPO.setLast(temporalTicker.getLastPrice());
		temporalTickerPO.setPreviousLast(temporalTicker.getPreviousLastPrice());
		temporalTickerPO.setFirst(temporalTicker.getFirstPrice());
		temporalTickerPO.setHigh(temporalTicker.getHighestPrice());
		temporalTickerPO.setLow(temporalTicker.getLowestPrice());
		temporalTickerPO.setAverage(temporalTicker.getAveragePrice());
		temporalTickerPO.setDuration(temporalTicker.getTimeDuration());
		return temporalTickerPO;
	}

}
