package org.marceloleite.mercado.converter.entity;

import java.time.Duration;

import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.TemporalTicker;
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
		temporalTicker.setBuy(new MercadoBigDecimal(temporalTickerPO.getBuy()));
		temporalTicker.setPreviousBuy(new MercadoBigDecimal(temporalTickerPO.getPreviousBuy()));
		temporalTicker.setSell(new MercadoBigDecimal(temporalTickerPO.getSell()));
		temporalTicker.setPreviousSell(new MercadoBigDecimal(temporalTickerPO.getPreviousSell()));
		temporalTicker.setLastPrice(new MercadoBigDecimal(temporalTickerPO.getLastPrice()));
		temporalTicker.setPreviousLastPrice(new MercadoBigDecimal(temporalTickerPO.getPreviousLastPrice()));
		temporalTicker.setFirstPrice(new MercadoBigDecimal(temporalTickerPO.getFirstPrice()));
		temporalTicker.setHighestPrice(new MercadoBigDecimal(temporalTickerPO.getHighestPrice()));
		temporalTicker.setLowestPrice(new MercadoBigDecimal(temporalTickerPO.getLowestPrice()));
		temporalTicker.setAveragePrice(new MercadoBigDecimal(temporalTickerPO.getAveragePrice()));
		Duration timeDuration = Duration.ofSeconds(temporalTickerPO.getTimeDuration().longValue());
		temporalTicker.setTimeDuration(timeDuration);
		temporalTicker.setVolumeTrades(new MercadoBigDecimal(temporalTickerPO.getVolumeTraded()));
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
		
		temporalTickerPO.setOrders(new MercadoBigDecimal(temporalTicker.getOrders()));
		temporalTickerPO.setBuyOrders(new MercadoBigDecimal(temporalTicker.getBuyOrders()));
		temporalTickerPO.setSellOrders(new MercadoBigDecimal(temporalTicker.getSellOrders()));
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
		temporalTickerPO.setVolumeTraded(temporalTicker.getVolumeTrades());
		
		long seconds = temporalTicker.getTimeDuration().getSeconds();
		temporalTickerPO.setTimeDuration(new MercadoBigDecimal(seconds));
		return temporalTickerPO;
	}

}
