package org.marceloleite.mercado.tickergenerator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.tickergenerator.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.tickergenerator.converter.TimeIntervalToTemporalTickerIdConverter;
import org.marceloleite.mercado.tickergenerator.filter.TradeTypeFilter;

public class TemporalTickersCallable implements Callable<TemporalTickerPO> {

	private Currency currency;
	private TimeInterval timeInterval;

	public TemporalTickersCallable(Currency currency, TimeInterval timeInterval) {
		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public TemporalTickerPO call() throws Exception {
		TradesRetriever tradesRetriever = new TradesRetriever();
		List<TradePO> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(), false);
		Map<Long, TradePO> tradesMap = new ListToMapTradeConverter().convert(trades);
		return generateTemporalTickerFromTrades(tradesMap);
	}

	private TemporalTickerPO generateTemporalTickerFromTrades(Map<Long, TradePO> trades) {

		double high = 0.0;
		double average = 0.0;
		double low = 0.0;
		double vol = 0.0;
		double first = 0.0;
		double last = 0.0;
		double buy = 0.0;
		double sell = 0.0;

		if (trades.size() > 0) {
			Map<Long, TradePO> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			Map<Long, TradePO> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

			high = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToDouble(TradePO::getPrice).max()
					.orElse(0.0);

			average = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToDouble(TradePO::getPrice)
					.average().orElse(0.0);

			low = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToDouble(TradePO::getPrice).min()
					.orElse(0.0);

			vol = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToDouble(TradePO::getAmount).sum();

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToLong(tradePO -> tradePO.getTradeIdPO().getId())
					.max().orElse(0);
			if (lastTradeId != 0) {
				last = trades.get(lastTradeId).getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue).mapToLong(tradePO -> tradePO.getTradeIdPO().getId())
					.min().orElse(0);
			if (firstTradeId != 0) {
				first = trades.get(firstTradeId).getPrice();
			}

			long lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).max().orElse(0);
			if (lastSellingTradeId != 0) {
				buy = trades.get(lastSellingTradeId).getPrice();
			}

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).max().orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = trades.get(lastBuyingTradeId).getPrice();
			}
		}

		TemporalTickerPO temporalTicker = new TemporalTickerPO();
		TemporalTickerIdPO temporalTickerId = new TimeIntervalToTemporalTickerIdConverter().convert(timeInterval);
		temporalTicker.setTemporalTickerIdPO(temporalTickerId);
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
