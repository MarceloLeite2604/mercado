package org.marceloleite.mercado.modeler.util.formatter;

import org.marceloleite.mercado.commons.interfaces.Formatter;
import org.marceloleite.mercado.commons.util.LongToLocalDateTimeFormatter;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.modeler.persistence.Trade;
import org.marceloleite.mercado.modeler.persistence.TradeType;

public class TradeFormatter implements Formatter<JsonTrade, Trade> {

	@Override
	public Trade format(JsonTrade jsonTrade) {
		Trade trade = new Trade();
		trade.setId(jsonTrade.getTid());
		trade.setDate(new LongToLocalDateTimeFormatter().format(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

}
