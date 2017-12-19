package org.marceloleite.mercado.consumer.util.formatter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.consumer.model.JsonTrade;

public class OldMapJsonTradeFormatter implements Converter<List<JsonTrade>, Map<Integer, JsonTrade>> {

	@Override
	public Map<Integer, JsonTrade> convert(List<JsonTrade> jsonTrades) {
		return jsonTrades.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(JsonTrade::getTid, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
