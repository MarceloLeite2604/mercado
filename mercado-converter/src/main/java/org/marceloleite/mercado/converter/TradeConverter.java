package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class TradeConverter implements Converter<JsonTrade, TradePO> {

	@Override
	public TradePO convert(JsonTrade jsonTrade) {
		TradePO trade = new TradePO();
		trade.setId(Long.valueOf(jsonTrade.getTid()));
		trade.setDate(new LongToLocalDateTimeConverter().convert(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		trade.setCurrency(jsonTrade.getCurrency());
		return trade;
	}

}
