package org.marceloleite.mercado;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Component;

@Component
public class TemporalTickerCreator {
	
	@Inject
	private TradeDAO tradeDAO;
	
	public TemporalTicker create(Currency currency, TimeInterval timeInterval, Map<Long, Trade> trades) {

		TemporalTicker temporalTicker = null;

		MercadoBigDecimal high = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal average = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal low = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal vol = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal first = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal last = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal previousLast = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal buy = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal previousBuy = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal sell = MercadoBigDecimal.NOT_A_NUMBER;
		MercadoBigDecimal previousSell = MercadoBigDecimal.NOT_A_NUMBER;
		long buyOrders = 0;
		long sellOrders = 0;

		if (trades.size() > 0) {
			Map<Long, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			sellOrders = buyingTrades.size();

			Map<Long, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);
			buyOrders = sellingTrades.size();

			high = new MercadoBigDecimal(trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(trade -> trade.getPrice().doubleValue()).max().orElse(0.0));

			average = new MercadoBigDecimal(trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(trade -> trade.getPrice().doubleValue()).average().orElse(0.0));

			low = new MercadoBigDecimal(trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(trade -> trade.getPrice().doubleValue()).min().orElse(0.0));

			vol = new MercadoBigDecimal(trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToDouble(trade -> trade.getAmount().doubleValue()).sum());

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(trade -> trade.getId()).max().orElse(0);
			if (lastTradeId != 0) {
				Trade lastTrade = trades.get(lastTradeId);
				last = new MercadoBigDecimal(lastTrade.getPrice());
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).min().orElse(0);
			if (firstTradeId != 0) {
				Trade firstTrade = trades.get(firstTradeId);
				first = new MercadoBigDecimal(firstTrade.getPrice());
			}

			long lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).max().orElse(0);
			if (lastSellingTradeId != 0) {
				buy = new MercadoBigDecimal(trades.get(lastSellingTradeId).getPrice());
			} else {
				Trade previousBuyingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.SELL,
						timeInterval.getStart());
				if (previousBuyingTrade != null) {
					previousBuy = new MercadoBigDecimal(previousBuyingTrade.getPrice());
				}
			}

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId()).max().orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = new MercadoBigDecimal(trades.get(lastBuyingTradeId).getPrice());
			} else {
				Trade previousSellingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.BUY,
						timeInterval.getStart());
				if (previousSellingTrade != null) {
					previousSell = new MercadoBigDecimal(previousSellingTrade.getPrice());
				}
			}
		} else {
			Trade previousBuyingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.SELL,
					timeInterval.getStart());
			if (previousBuyingTrade != null) {
				previousBuy = new MercadoBigDecimal(previousBuyingTrade.getPrice());
			}
			Trade previousSellingTrade = tradeDAO.retrievePreviousTrade(currency, TradeType.BUY,
					timeInterval.getStart());
			if (previousSellingTrade != null) {
				previousSell = new MercadoBigDecimal(previousSellingTrade.getPrice());
			}

			if (previousSellingTrade == null) {
				if (previousBuyingTrade != null) {
					previousLast = new MercadoBigDecimal(previousBuyingTrade.getPrice());
				}
			} else {
				if (previousBuyingTrade != null) {
					if (previousBuyingTrade.getId() > previousSellingTrade.getId()) {
						previousLast = new MercadoBigDecimal(previousBuyingTrade.getPrice());
					} else {
						previousLast = new MercadoBigDecimal(previousSellingTrade.getPrice());
					}
				} else {
					previousLast = new MercadoBigDecimal(previousSellingTrade.getPrice());
				}
			}
		}

		temporalTicker = new TemporalTicker();
		temporalTicker.setStartTime(timeInterval.getStart());
		temporalTicker.setEndTime(timeInterval.getEnd());
		temporalTicker.setCurrency(currency);
		temporalTicker.setOrders(new Long(trades.size()));
		temporalTicker.setHighestPrice(high);
		temporalTicker.setAveragePrice(average);
		temporalTicker.setLowestPrice(low);
		temporalTicker.setVolumeTraded(vol);
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

	public TemporalTicker create(Currency currency, TimeInterval timeInterval, List<Trade> trades) {
		return create(currency, timeInterval, ListToMapTradeConverter.getInstance().convertTo(trades));
	}
}
