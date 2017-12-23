package org.marceloleite.mercado.additional;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.additional.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.additional.converter.TimeIntervalToTemporalTickerIdConverter;
import org.marceloleite.mercado.additional.filter.TradeTypeFilter;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTicker;
import org.marceloleite.mercado.databasemodel.TemporalTickerId;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class TemporalTickersCallable implements Callable<TemporalTicker> {

	private Currency currency;
	private TimeInterval timeInterval;

	public TemporalTickersCallable(Currency currency, TimeInterval timeInterval) {
		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public TemporalTicker call() throws Exception {
		TradesRetriever tradesRetriever = new TradesRetriever();
		List<Trade> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd());
		Map<Long, Trade> tradesMap = new ListToMapTradeConverter().convert(trades);
		return generateTemporalTickerFromTrades(tradesMap);
	}

	private TemporalTicker generateTemporalTickerFromTrades(Map<Long, Trade> trades) {

		double high = 0.0;
		double average = 0.0;
		double low = 0.0;
		double vol = 0.0;
		double first = 0.0;
		double last = 0.0;
		double buy = 0.0;
		double sell = 0.0;

		if (trades.size() > 0) {
			Map<Long, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			Map<Long, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

			high = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToDouble(Trade::getPrice).max()
					.orElse(0.0);

			average = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToDouble(Trade::getPrice)
					.average().orElse(0.0);

			low = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToDouble(Trade::getPrice).min()
					.orElse(0.0);

			vol = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToDouble(Trade::getAmount).sum();

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToLong(Trade::getId)
					.max().orElse(0);
			if (lastTradeId != 0) {
				last = trades.get(lastTradeId).getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue).mapToLong(Trade::getId)
					.min().orElse(0);
			if (firstTradeId != 0) {
				first = trades.get(firstTradeId).getPrice();
			}

			long lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(Trade::getId).max().orElse(0);
			if (lastSellingTradeId != 0) {
				buy = trades.get(lastSellingTradeId).getPrice();
			}

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(Trade::getId).max().orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = trades.get(lastBuyingTradeId).getPrice();
			}
		}

		TemporalTicker temporalTicker = new TemporalTicker();
		TemporalTickerId temporalTickerId = new TimeIntervalToTemporalTickerIdConverter().convert(timeInterval);
		temporalTicker.setTemporalTickerId(temporalTickerId);
		temporalTicker.setOrders(trades.size());
		temporalTicker.setHigh(high);
		temporalTicker.setAverage(average);
		temporalTicker.setLow(low);
		temporalTicker.setVol(vol);
		temporalTicker.setFirst(first);
		temporalTicker.setLast(last);
		temporalTicker.setBuy(buy);
		temporalTicker.setSell(sell);

		return temporalTicker;
	}

}
