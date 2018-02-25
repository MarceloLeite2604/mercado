package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.Trade;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradeIdPO;
import org.marceloleite.mercado.databasemodel.TradePO;

public class TradePOToTrade implements Converter<TradePO, Trade>{

	@Override
	public Trade convertTo(TradePO tradePO) {
		Trade trade = new Trade();
		TradeIdPO tradeIdPO = tradePO.getTradeIdPO();
		trade.setCurrency(tradeIdPO.getCurrency());
		trade.setId(tradeIdPO.getId());
		trade.setAmount(tradePO.getAmount());
		trade.setDate(tradePO.getDate());
		trade.setPrice(tradePO.getPrice());
		trade.setTradeType(tradePO.getTradeType());
		return null;
	}

	@Override
	public TradePO convertFrom(Trade trade) {
		TradePO tradePO = new TradePO();
		
		TradeIdPO tradeIdPO = new TradeIdPO();
		tradeIdPO.setCurrency(trade.getCurrency());
		tradeIdPO.setId(trade.getId());
		tradePO.setTradeIdPO(tradeIdPO);
		
		tradePO.setAmount(trade.getAmount());
		tradePO.setDate(trade.getDate());
		tradePO.setPrice(trade.getPrice());
		tradePO.setTradeType(trade.getTradeType());
		return tradePO;
	}

	
}
