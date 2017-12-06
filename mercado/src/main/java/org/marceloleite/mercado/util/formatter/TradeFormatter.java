package org.marceloleite.mercado.util.formatter;

import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.model.persistence.Trade;
import org.marceloleite.mercado.model.persistence.TradeType;

public class TradeFormatter implements Formatter<JsonTrade, Trade> {

	@Override
	public Trade format(JsonTrade jsonTrade) {
		Trade trade = new Trade();
		trade.setId(jsonTrade.getTid());
		trade.setDate(new LongToCalendarFormatter().format(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

}
