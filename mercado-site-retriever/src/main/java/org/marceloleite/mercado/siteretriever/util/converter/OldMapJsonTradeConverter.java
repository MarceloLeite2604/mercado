package org.marceloleite.mercado.siteretriever.util.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class OldMapJsonTradeConverter implements Converter<List<JsonTrade>, Map<Integer, JsonTrade>> {

	@Override
	public Map<Integer, JsonTrade> convert(List<JsonTrade> jsonTrades) {
		return jsonTrades.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(JsonTrade::getTid, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}