package org.marceloleite.mercado.siteretriever.util.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class OldJsonTradeListToMapConverter implements Converter<List<JsonTrade>, Map<Long, JsonTrade>> {

	@Override
	public Map<Long, JsonTrade> convertTo(List<JsonTrade> jsonTrades) {
		return jsonTrades.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(JsonTrade::getTid, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

	@Override
	public List<JsonTrade> convertFrom(Map<Long, JsonTrade> object) {
		throw new UnsupportedOperationException();
	}

}
