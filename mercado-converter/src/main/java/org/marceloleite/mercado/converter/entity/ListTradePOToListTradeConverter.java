package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.simulator.Trade;

public class ListTradePOToListTradeConverter implements Converter<List<TradePO>, List<Trade>>{

	@Override
	public List<Trade> convertTo(List<TradePO> tradePOs) {
		TradePOToTrade tradePOToTrade = new TradePOToTrade();
		List<Trade> trades = new ArrayList<>();
		for (TradePO tradePO : tradePOs) {
			trades.add(tradePOToTrade.convertTo(tradePO));
		}
		return trades;
	}

	@Override
	public List<TradePO> convertFrom(List<Trade> trades) {
		TradePOToTrade tradePOToTrade = new TradePOToTrade();
		List<TradePO> tradePOs = new ArrayList<>();
		for (Trade trade : trades) {
			tradePOs.add(tradePOToTrade.convertFrom(trade));
		}
		return tradePOs;
	}

}
