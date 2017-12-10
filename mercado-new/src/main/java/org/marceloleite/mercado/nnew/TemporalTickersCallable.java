package org.marceloleite.mercado.nnew;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.consumer.model.Cryptocoin;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.consumer.trades.TradesRetriever;
import org.marceloleite.mercado.modeler.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.modeler.persistence.TemporalTicker;
import org.marceloleite.mercado.modeler.persistence.Trade;
import org.marceloleite.mercado.modeler.persistence.TradeType;
import org.marceloleite.mercado.modeler.util.formatter.MapTradeFormatter;

public class TemporalTickersCallable implements Callable<TemporalTicker> {

	private Cryptocoin cryptocoin;
	private LocalDateTime initialTime;
	private Duration duration;

	public TemporalTickersCallable(Cryptocoin cryptocoin, LocalDateTime initialTime, Duration duration) {
		this.cryptocoin = cryptocoin;
		this.initialTime = initialTime;
		this.duration = duration;

	}

	@Override
	public TemporalTicker call() throws Exception {
		TradesRetriever tradesRetriever = new TradesRetriever();
		Map<Integer, JsonTrade> jsonTrades = tradesRetriever.retrieve(cryptocoin, initialTime, duration);
		return generateTemporalTickerFromJsonTrades(jsonTrades);
	}

	private TemporalTicker generateTemporalTickerFromJsonTrades(Map<Integer, JsonTrade> jsonTrades) {
		Map<Integer, Trade> trades = new MapTradeFormatter().format(jsonTrades);

		double high = 0.0;
		double average = 0.0;
		double low = 0.0;
		double vol = 0.0;
		double first = 0.0;
		double last = 0.0;
		double buy = 0.0;
		double sell = 0.0;

		if (trades.size() > 0) {
			Map<Integer, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			Map<Integer, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

			high = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getPrice).max()
					.orElse(0.0);

			average = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getPrice)
					.average().orElse(0.0);

			low = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getPrice).min()
					.orElse(0.0);

			vol = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getAmount).sum();

			int lastTradeId = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToInt(Trade::getId)
					.max().orElse(0);
			if (lastTradeId != 0) {
				last = trades.get(lastTradeId).getPrice();
			}
			
			int firstTradeId = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToInt(Trade::getId)
					.min().orElse(0);
			if (firstTradeId != 0) {
				first = trades.get(firstTradeId).getPrice();
			}

			int lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Integer, Trade>::getValue)
					.mapToInt(Trade::getId).max().orElse(0);
			if (lastSellingTradeId != 0) {
				buy = trades.get(lastSellingTradeId).getPrice();
			}

			int lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Integer, Trade>::getValue)
					.mapToInt(Trade::getId).max().orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = trades.get(lastBuyingTradeId).getPrice();
			}
		}

		TemporalTicker temporalTicker = new TemporalTicker();
		temporalTicker.setFrom(initialTime);
		temporalTicker.setTo(initialTime.plus(duration));
		temporalTicker.setOrders(jsonTrades.size());
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
