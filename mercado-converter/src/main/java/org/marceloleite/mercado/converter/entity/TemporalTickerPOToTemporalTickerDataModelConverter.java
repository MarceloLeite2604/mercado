package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerPOToTemporalTickerDataModelConverter implements Converter<TemporalTickerPO, TemporalTickerDataModel>{

	@Override
	public TemporalTickerDataModel convertTo(TemporalTickerPO temporalTickerPO) {
		TemporalTickerDataModel temporalTickerDataModel = new TemporalTickerDataModel();
		TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
		temporalTickerDataModel.setCurrency(temporalTickerIdPO.getCurrency());
		temporalTickerDataModel.setStart(temporalTickerIdPO.getStart());
		temporalTickerDataModel.setEnd(temporalTickerIdPO.getEnd());
		temporalTickerDataModel.setOrders(temporalTickerPO.getOrders());
		temporalTickerDataModel.setBuyOrders(temporalTickerPO.getBuyOrders());
		temporalTickerDataModel.setSellOrders(temporalTickerPO.getSellOrders());
		temporalTickerDataModel.setBuy(temporalTickerPO.getBuy());
		temporalTickerDataModel.setPreviousBuy(temporalTickerPO.getPreviousBuy());
		temporalTickerDataModel.setSell(temporalTickerPO.getSell());
		temporalTickerDataModel.setPreviousSell(temporalTickerPO.getPreviousSell());
		temporalTickerDataModel.setLastPrice(temporalTickerPO.getLast());
		temporalTickerDataModel.setPreviousLastPrice(temporalTickerPO.getPreviousLast());
		temporalTickerDataModel.setFirstPrice(temporalTickerPO.getFirst());
		temporalTickerDataModel.setHighestPrice(temporalTickerPO.getHigh());
		temporalTickerDataModel.setLowestPrice(temporalTickerPO.getLow());
		temporalTickerDataModel.setAveragePrice(temporalTickerPO.getAverage());
		temporalTickerDataModel.setTimeDuration(temporalTickerPO.getDuration());
		temporalTickerDataModel.setVolumeTrades(temporalTickerPO.getVol());
		return temporalTickerDataModel;
	}

	@Override
	public TemporalTickerPO convertFrom(TemporalTickerDataModel temporalTickerDataModel) {
		TemporalTickerPO temporalTickerPO = new TemporalTickerPO();
		
		TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
		temporalTickerIdPO.setCurrency(temporalTickerDataModel.getCurrency());
		temporalTickerIdPO.setStart(temporalTickerDataModel.getStart());
		temporalTickerIdPO.setEnd(temporalTickerDataModel.getEnd());
		temporalTickerPO.setTemporalTickerIdPO(temporalTickerIdPO);
		
		temporalTickerPO.setOrders(temporalTickerDataModel.getOrders());
		temporalTickerPO.setBuyOrders(temporalTickerDataModel.getBuyOrders());
		temporalTickerPO.setSellOrders(temporalTickerDataModel.getSellOrders());
		temporalTickerPO.setBuy(temporalTickerDataModel.getBuy());
		temporalTickerPO.setPreviousBuy(temporalTickerDataModel.getPreviousBuy());
		temporalTickerPO.setSell(temporalTickerDataModel.getSell());
		temporalTickerPO.setPreviousSell(temporalTickerDataModel.getPreviousSell());
		temporalTickerPO.setLast(temporalTickerDataModel.getLastPrice());
		temporalTickerPO.setPreviousLast(temporalTickerDataModel.getPreviousLastPrice());
		temporalTickerPO.setFirst(temporalTickerDataModel.getFirstPrice());
		temporalTickerPO.setHigh(temporalTickerDataModel.getHighestPrice());
		temporalTickerPO.setLow(temporalTickerDataModel.getLowestPrice());
		temporalTickerPO.setAverage(temporalTickerDataModel.getAveragePrice());
		temporalTickerPO.setDuration(temporalTickerDataModel.getTimeDuration());
		return temporalTickerPO;
	}

}
