package org.marceloleite.mercado.retriever;

import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.retriever.filter.TradeTypeFilter;

public class TemporalTickerCreator {
	
	public TemporalTickerDataModel create(Currency currency, TimeInterval timeInterval,
			Map<Long, TradeDataModel> trades) {

		TemporalTickerDataModel temporalTickerDataModel = null;

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
			Map<Long, TradeDataModel> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			sellOrders = buyingTrades.size();

			Map<Long, TradeDataModel> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);
			buyOrders = sellingTrades.size();

			high = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToDouble(TradeDataModel::getPrice).max().orElse(0.0);

			average = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToDouble(TradeDataModel::getPrice).average().orElse(0.0);

			low = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToDouble(TradeDataModel::getPrice).min().orElse(0.0);

			vol = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToDouble(TradeDataModel::getAmount).sum();

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToLong(tradeDataModel -> tradeDataModel.getId()).max().orElse(0);
			if (lastTradeId != 0) {
				TradeDataModel lastTrade = trades.get(lastTradeId);
				last = lastTrade.getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).min().orElse(0);
			if (firstTradeId != 0) {
				TradeDataModel firstTrade = trades.get(firstTradeId);
				first = firstTrade.getPrice();
			}

			long lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
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

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, TradeDataModel>::getValue)
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
					if (previousBuyingTrade.getId().getId() > previousSellingTrade.getId().getId()) {
						previousLast = previousBuyingTrade.getPrice();
					} else {
						previousLast = previousSellingTrade.getPrice();
					}
				} else {
					previousLast = previousSellingTrade.getPrice();
				}
			}
		}

		temporalTickerDataModel = new TemporalTickerDataModel();
		temporalTickerDataModel.setStart(timeInterval.getStart());
		temporalTickerDataModel.setEnd(timeInterval.getEnd());
		temporalTickerDataModel.setCurrency(currency);
		temporalTickerDataModel.setOrders(new Long(trades.size()));
		temporalTickerDataModel.setHighestPrice(high);
		temporalTickerDataModel.setAveragePrice(average);
		temporalTickerDataModel.setLowestPrice(low);
		temporalTickerDataModel.setVolumeTrades(vol);
		temporalTickerDataModel.setFirstPrice(first);
		temporalTickerDataModel.setLastPrice(last);
		temporalTickerDataModel.setPreviousLastPrice(previousLast);
		temporalTickerDataModel.setBuy(buy);
		temporalTickerDataModel.setPreviousBuy(previousBuy);
		temporalTickerDataModel.setSell(sell);
		temporalTickerDataModel.setPreviousSell(previousSell);
		temporalTickerDataModel.setBuyOrders(buyOrders);
		temporalTickerDataModel.setSellOrders(sellOrders);
		temporalTickerDataModel.setTimeDuration(timeInterval.getDuration());

		return temporalTickerDataModel;
	}
}
