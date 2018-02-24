package org.marceloleite.mercado.retriever.filter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.interfaces.Filter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;

public class TradeTypeFilter implements Filter<Map<Long, TradeDataModel>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public Map<Long, TradeDataModel> filter(Map<Long, TradeDataModel> trades) {
		return trades.entrySet().stream().filter(entry -> type.equals(entry.getValue().getTradeType()))
				.map(Entry<Long, TradeDataModel>::getValue).collect(Collectors.toConcurrentMap(
						tradeDataModel -> tradeDataModel.getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
