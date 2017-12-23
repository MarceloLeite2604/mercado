package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class TradeConverter implements Converter<JsonTrade, Trade> {

	@Override
	public Trade convert(JsonTrade jsonTrade) {
		Trade trade = new Trade();
		trade.setId(Long.valueOf(jsonTrade.getTid()));
		trade.setDate(new LongToLocalDateTimeConverter().convert(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		trade.setCurrency(jsonTrade.getCurrency());
		return trade;
	}

}
