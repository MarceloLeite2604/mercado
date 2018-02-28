package org.marceloleite.mercado.converter.json.old;

import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradeIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class OldTradeConverter implements Converter<JsonTrade, TradePO> {

	@Override
	public TradePO convertTo(JsonTrade jsonTrade) {
		TradeIdPO tradeIdPO = new TradeIdPO(Long.valueOf(jsonTrade.getTid()), jsonTrade.getCurrency());
		TradePO trade = new TradePO();
		trade.setTradeIdPO(tradeIdPO);
		trade.setDate(new LongToZonedDateTimeConverter().convertTo(jsonTrade.getDate()));
		trade.setPrice(jsonTrade.getPrice());
		trade.setAmount(jsonTrade.getAmount());
		trade.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return trade;
	}

	@Override
	public JsonTrade convertFrom(TradePO object) {
		throw new UnsupportedOperationException();
	}

}
