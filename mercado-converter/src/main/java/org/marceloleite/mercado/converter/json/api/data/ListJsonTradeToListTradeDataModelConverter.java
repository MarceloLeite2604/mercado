package org.marceloleite.mercado.converter.json.api.data;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class ListJsonTradeToListTradeDataModelConverter implements Converter<List<JsonTrade>, List<TradeDataModel>> {

	@Override
	public List<TradeDataModel> convertTo(List<JsonTrade> jsonTrades) {
		JsonTradeToTradeDataModelConverter jsonTradeToTradeDataModelConverter = new JsonTradeToTradeDataModelConverter();
		List<TradeDataModel> tradeDataModels = new ArrayList<>();
		for (JsonTrade jsonTrade : jsonTrades) {
			tradeDataModels.add(jsonTradeToTradeDataModelConverter.convertTo(jsonTrade));
		}
		return tradeDataModels;
	}

	@Override
	public List<JsonTrade> convertFrom(List<TradeDataModel> tradeDataModels) {
		throw new UnsupportedOperationException();
	}

}
