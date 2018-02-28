package org.marceloleite.mercado.retriever;

import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.base.model.Trade;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.daos.TradeDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.retriever.filter.TradeTypeFilter;

public class TemporalTickerCreator {
	
	public TemporalTicker create(Currency currency, TimeInterval timeInterval,
			Map<Long, Trade> trades) {

		TemporalTicker temporalTicker = null;

		double high = 0.0;
		double average = 0.0;
		double low = 0.0;
		double vol = 0.0;
		double first = 0.0;
		double last = 0.0;
		double previousLast = 0.0;
		double buy = 0.0;
		double previousBuy = 0.0;
		double sell = 0.0;
		double previousSell = 0.0;
		long buyOrders = 0;
		long sellOrders = 0;

		if (trades.size() > 0) {
			Map<Long, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			sellOrders = buyingTrades.size();

			Map<Long, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);
			buyOrders = sellingTrades.size();

			high = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(Trade::getPrice).max().orElse(0.0);

			average = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(Trade::getPrice).average().orElse(0.0);

			low = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(Trade::getPrice).min().orElse(0.0);

			vol = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(Trade::getAmount).sum();

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(trade -> trade.getId()).max().orElse(0);
			if (lastTradeId != 0) {
				Trade lastTrade = trades.get(lastTradeId);
				last = lastTrade.getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).min().orElse(0);
			if (firstTradeId != 0) {
				Trade firstTrade = trades.get(firstTradeId);
				first = firstTrade.getPrice();
			}

			long lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).max().orElse(0);
			if (lastSellingTradeId != 0) {
				buy = trades.get(lastSellingTradeId).getPrice();
			} else {
				TradePO previousBuyingTrade = new TradeDAO().retrievePreviousTrade(currency, TradeType.SELL,
						timeInterval.getStart());
				if (previousBuyingTrade != null) {
					previousBuy = previousBuyingTrade.getPrice();
				}
			}

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).max().orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = trades.get(lastBuyingTradeId).getPrice();
			} else {
				TradePO previousSellingTrade = new TradeDAO().retrievePreviousTrade(currency, TradeType.BUY,
						timeInterval.getStart());
				if (previousSellingTrade != null) {
					previousSell = previousSellingTrade.getPrice();
				}
			}
		} else {
			TradeDAO tradeDAO = new TradeDAO();
			TradePO previousBuyingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.SELL,
					timeInterval.getStart());
			if (previousBuyingTrade != null) {
				previousBuy = previousBuyingTrade.getPrice();
			}
			TradePO previousSellingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.BUY,
					timeInterval.getStart());
			if (previousSellingTrade != null) {
				previousSell = previousSellingTrade.getPrice();
			}

			if (previousSellingTrade == null) {
				if (previousBuyingTrade != null) {
					previousLast = previousBuyingTrade.getPrice();
				}
			} else {
				if (previousBuyingTrade != null) {
					if (previousBuyingTrade.getId().getTradeId() > previousSellingTrade.getId().getTradeId()) {
						previousLast = previousBuyingTrade.getPrice();
					} else {
						previousLast = previousSellingTrade.getPrice();
					}
				} else {
					previousLast = previousSellingTrade.getPrice();
				}
			}
		}

		temporalTicker = new TemporalTicker();
		temporalTicker.setStart(timeInterval.getStart());
		temporalTicker.setEnd(timeInterval.getEnd());
		temporalTicker.setCurrency(currency);
		temporalTicker.setOrders(new Long(trades.size()));
		temporalTicker.setHighestPrice(high);
		temporalTicker.setAveragePrice(average);
		temporalTicker.setLowestPrice(low);
		temporalTicker.setVolumeTrades(vol);
		temporalTicker.setFirstPrice(first);
		temporalTicker.setLastPrice(last);
		temporalTicker.setPreviousLastPrice(previousLast);
		temporalTicker.setBuy(buy);
		temporalTicker.setPreviousBuy(previousBuy);
		temporalTicker.setSell(sell);
		temporalTicker.setPreviousSell(previousSell);
		temporalTicker.setBuyOrders(buyOrders);
		temporalTicker.setSellOrders(sellOrders);
		temporalTicker.setTimeDuration(timeInterval.getDuration());

		return temporalTicker;
	}
}
