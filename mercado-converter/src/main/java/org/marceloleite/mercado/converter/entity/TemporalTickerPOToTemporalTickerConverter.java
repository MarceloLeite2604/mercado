package org.marceloleite.mercado.converter.entity;

import java.math.BigDecimal;
import java.time.Duration;

import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerPO;

public class TemporalTickerPOToTemporalTickerConverter implements Converter<TemporalTickerPO, TemporalTicker>{

	@Override
	public TemporalTicker convertTo(TemporalTickerPO temporalTickerPO) {
		TemporalTicker temporalTicker = new TemporalTicker();
		TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
		temporalTicker.setCurrency(temporalTickerIdPO.getCurrency());
		temporalTicker.setStart(temporalTickerIdPO.getStartTime());
		temporalTicker.setEnd(temporalTickerIdPO.getEndTime());
		temporalTicker.setOrders(temporalTickerPO.getOrders().longValue());
		temporalTicker.setBuyOrders(temporalTickerPO.getBuyOrders().longValue());
		temporalTicker.setSellOrders(temporalTickerPO.getSellOrders().longValue());
		temporalTicker.setBuy(temporalTickerPO.getBuy());
		temporalTicker.setPreviousBuy(temporalTickerPO.getPreviousBuy());
		temporalTicker.setSell(temporalTickerPO.getSell());
		temporalTicker.setPreviousSell(temporalTickerPO.getPreviousSell());
		temporalTicker.setLastPrice(temporalTickerPO.getLastPrice());
		temporalTicker.setPreviousLastPrice(temporalTickerPO.getPreviousLastPrice());
		temporalTicker.setFirstPrice(temporalTickerPO.getFirstPrice());
		temporalTicker.setHighestPrice(temporalTickerPO.getHighestPrice());
		temporalTicker.setLowestPrice(temporalTickerPO.getLowestPrice());
		temporalTicker.setAveragePrice(temporalTickerPO.getAveragePrice());
		Duration timeDuration = Duration.ofSeconds(temporalTickerPO.getTimeDuration().longValue());
		temporalTicker.setTimeDuration(timeDuration);
		temporalTicker.setVolumeTrades(temporalTickerPO.getVolumeTraded());
		return temporalTicker;
	}

	@Override
	public TemporalTickerPO convertFrom(TemporalTicker temporalTicker) {
		TemporalTickerPO temporalTickerPO = new TemporalTickerPO();
		
		TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
		temporalTickerIdPO.setCurrency(temporalTicker.getCurrency());
		temporalTickerIdPO.setStartTime(temporalTicker.getStart());
		temporalTickerIdPO.setEndTime(temporalTicker.getEnd());
		temporalTickerPO.setId(temporalTickerIdPO);
		
		temporalTickerPO.setOrders(new BigDecimal(temporalTicker.getOrders()));
		temporalTickerPO.setBuyOrders(new BigDecimal(temporalTicker.getBuyOrders()));
		temporalTickerPO.setSellOrders(new BigDecimal(temporalTicker.getSellOrders()));
		temporalTickerPO.setBuy(temporalTicker.getBuy());
		temporalTickerPO.setPreviousBuy(temporalTicker.getPreviousBuy());
		temporalTickerPO.setSell(temporalTicker.getSell());
		temporalTickerPO.setPreviousSell(temporalTicker.getPreviousSell());
		temporalTickerPO.setLastPrice(temporalTicker.getLastPrice());
		temporalTickerPO.setPreviousLastPrice(temporalTicker.getPreviousLastPrice());
		temporalTickerPO.setFirstPrice(temporalTicker.getFirstPrice());
		temporalTickerPO.setHighestPrice(temporalTicker.getHighestPrice());
		temporalTickerPO.setLowestPrice(temporalTicker.getLowestPrice());
		temporalTickerPO.setAveragePrice(temporalTicker.getAveragePrice());
		
		long seconds = temporalTicker.getTimeDuration().getSeconds();
		temporalTickerPO.setTimeDuration(new BigDecimal(seconds));
		return temporalTickerPO;
	}

}
