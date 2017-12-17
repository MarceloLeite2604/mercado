package org.marceloleite.mercado.modeler.util.converter;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.modeler.persistence.model.Trade;
import org.marceloleite.mercado.modeler.persistence.model.TradeType;

public class TradeConverter implements Converter<JsonTrade, Trade> {

	@Override
	public Trade format(JsonTrade jsonTrade) {
		Trade trade = new Trade();
		trade.setId(jsonTrade.getTid());
		trade.setDate(new LongToLocalDateTimeConverter().format(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

}
