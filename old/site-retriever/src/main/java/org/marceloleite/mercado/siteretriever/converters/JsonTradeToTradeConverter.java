package org.marceloleite.mercado.siteretriever.converters;

import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class JsonTradeToTradeConverter implements Converter<JsonTrade, Trade> {

	@Override
	public Trade convertTo(JsonTrade jsonTrade) {
		Trade trade = new Trade();
		trade.setAmount(new MercadoBigDecimal(jsonTrade.getAmount()));
		trade.setCurrency(jsonTrade.getCurrency());
		trade.setDate(LongToZonedDateTimeConverter.getInstance().convertTo(jsonTrade.getDate()));
		trade.setId(jsonTrade.getTid());
		trade.setPrice(new MercadoBigDecimal(jsonTrade.getPrice()));
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

	@Override
	public JsonTrade convertFrom(Trade trade) {
		throw new UnsupportedOperationException();
	}

}
