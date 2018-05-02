package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradeIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class TradePOToTradeConverter implements Converter<TradePO, Trade>{

	@Override
	public Trade convertTo(TradePO tradePO) {
		Trade trade = new Trade();
		TradeIdPO tradeIdPO = tradePO.getId();
		trade.setCurrency(tradeIdPO.getCurrency());
		trade.setId(tradeIdPO.getTradeId());
		trade.setAmount(new MercadoBigDecimal(tradePO.getAmount()));
		trade.setDate(tradePO.getTradeDate());
		trade.setPrice(new MercadoBigDecimal(tradePO.getPrice()));
		trade.setTradeType(tradePO.getTradeType());
		return trade;
	}

	@Override
	public TradePO convertFrom(Trade trade) {
		TradePO tradePO = new TradePO();
		
		TradeIdPO tradeIdPO = new TradeIdPO();
		tradeIdPO.setCurrency(trade.getCurrency());
		tradeIdPO.setTradeId(trade.getId());
		tradePO.setId(tradeIdPO);
		
		tradePO.setAmount(trade.getAmount());
		tradePO.setTradeDate(trade.getDate());
		tradePO.setPrice(trade.getPrice());
		tradePO.setTradeType(trade.getTradeType());
		return tradePO;
	}

	
}
