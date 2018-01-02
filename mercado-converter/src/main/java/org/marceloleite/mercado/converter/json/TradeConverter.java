package org.marceloleite.mercado.converter.json;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.TradeIdPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class TradeConverter implements Converter<JsonTrade, TradePO> {

	@Override
	public TradePO convert(JsonTrade jsonTrade) {
		TradeIdPO tradeIdPO = new TradeIdPO(Long.valueOf(jsonTrade.getTid()), jsonTrade.getCurrency());
		TradePO trade = new TradePO();
		trade.setTradeIdPO(tradeIdPO);
		trade.setDate(new LongToLocalDateTimeConverter().convert(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

}