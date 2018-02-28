package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.Trade;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class ListTradePOToListTradeConverter implements Converter<List<TradePO>, List<Trade>>{

	@Override
	public List<Trade> convertTo(List<TradePO> tradePOs) {
		TradePOToTradeConverter tradePOToTrade = new TradePOToTradeConverter();
		List<Trade> trades = new ArrayList<>();
		for (TradePO tradePO : tradePOs) {
			trades.add(tradePOToTrade.convertTo(tradePO));
		}
		return trades;
	}

	@Override
	public List<TradePO> convertFrom(List<Trade> trades) {
		TradePOToTradeConverter tradePOToTrade = new TradePOToTradeConverter();
		List<TradePO> tradePOs = new ArrayList<>();
		for (Trade trade : trades) {
			tradePOs.add(tradePOToTrade.convertFrom(trade));
		}
		return tradePOs;
	}

}