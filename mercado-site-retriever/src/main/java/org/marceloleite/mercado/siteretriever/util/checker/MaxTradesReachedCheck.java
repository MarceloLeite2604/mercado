package org.marceloleite.mercado.siteretriever.util.checker;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Check;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;

public class MaxTradesReachedCheck implements Check<List<TradeDataModel>>{
	
	private static int MAXIMUM_TRADES = 1000;

	@Override
	public boolean check(List<TradeDataModel> tradeDataModels) {
		return (tradeDataModels.size() >= MAXIMUM_TRADES);
	}

}
