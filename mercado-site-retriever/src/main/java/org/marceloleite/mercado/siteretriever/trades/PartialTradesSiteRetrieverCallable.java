package org.marceloleite.mercado.siteretriever.trades;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.datamodel.ListToMapTradeDataModelConverter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.siteretriever.util.checker.MaxTradesReachedCheck;

class PartialTradesSiteRetrieverCallable implements Callable<Map<Long, TradeDataModel>> {

	private Currency currency;

	private TimeInterval timeInterval;

	public PartialTradesSiteRetrieverCallable(Currency currency, TimeInterval timeInterval) {
		super();

		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public Map<Long, TradeDataModel> call() throws Exception {
		List<TradeDataModel> tradeDataModels = new PartialTradesSiteRetriever(currency).retrieve(timeInterval);
		Map<Long, TradeDataModel> result;
		if (new MaxTradesReachedCheck().check(tradeDataModels)) {
			System.err.println("Warning: Maximum trades exceeded from " + timeInterval + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapTradeDataModelConverter().convertTo(tradeDataModels);
		}
		return result;
	}

	public Map<Long, TradeDataModel> splitExecution() {
		TradesSiteRetriever tradesRetriever = new TradesSiteRetriever(currency, timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(timeInterval);
	}

}
