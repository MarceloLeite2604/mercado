package org.marceloleite.mercado.converter.json.api.data;

import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class JsonTradeToTradeDataModelConverter implements Converter<JsonTrade, TradeDataModel> {

	@Override
	public TradeDataModel convertTo(JsonTrade jsonTrade) {
		TradeDataModel tradeDataModel = new TradeDataModel();
		tradeDataModel.setAmount(jsonTrade.getAmount());
		tradeDataModel.setCurrency(jsonTrade.getCurrency());
		tradeDataModel.setDate(new LongToZonedDateTimeConverter().convertTo(jsonTrade.getDate()));
		tradeDataModel.setId(jsonTrade.getTid());
		tradeDataModel.setPrice(jsonTrade.getPrice());
		tradeDataModel.setTradeType(TradeType.retrieve(jsonTrade.getType()));
		return tradeDataModel;
	}

	@Override
	public JsonTrade convertFrom(TradeDataModel tradeDataModel) {
		throw new UnsupportedOperationException();
	}

}
