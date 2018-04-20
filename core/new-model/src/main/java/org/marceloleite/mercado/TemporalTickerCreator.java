package org.marceloleite.mercado;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.comparator.TradeComparatorById;
import org.marceloleite.mercado.comparator.TradeComparatorByIdDesc;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Component;

@Component
public class TemporalTickerCreator {

	@Inject
	private TradeDAO tradeDAO;

	public TemporalTicker create(Currency currency, TimeInterval timeInterval, Map<Long, Trade> trades) {

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

			Trade firstTrade = trades.values()
					.stream()
					.sorted(TradeComparatorById.getInstance())
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Could not find first trade on list."));

			Trade lastTrade = trades.values()
					.stream()
					.sorted(TradeComparatorByIdDesc.getInstance())
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Could not find last trade on list."));

			high = new MercadoBigDecimal(trades.values()
					.stream()
					.mapToDouble(trade -> trade.getPrice()
							.doubleValue())
					.max()
					.orElse(0.0));

			average = new MercadoBigDecimal(trades.values()
					.stream()
					.mapToDouble(trade -> trade.getPrice()
							.doubleValue())
					.average()
					.orElse(0.0));

			low = new MercadoBigDecimal(trades.values()
					.stream()
					.mapToDouble(trade -> trade.getPrice()
							.doubleValue())
					.min()
					.orElse(0.0));

			vol = new MercadoBigDecimal(trades.values()
					.stream()
					.mapToDouble(trade -> trade.getAmount()
							.doubleValue())
					.sum());

			last = new MercadoBigDecimal(lastTrade.getPrice());

			first = new MercadoBigDecimal(firstTrade.getPrice());

			Trade lastSellingTrade = sellingTrades.values()
					.stream()
					.sorted(TradeComparatorByIdDesc.getInstance())
					.findFirst()
					.orElse(null);

			if (lastSellingTrade != null) {
				buy = new MercadoBigDecimal(trades.get(lastSellingTrade.getId())
						.getPrice());
			} else {

				Trade previousBuyingTrade = tradeDAO.findFirstTradeOfCurrencyAndTypeAndOlderThan(currency,
						TradeType.SELL, timeInterval.getStart());
				if (previousBuyingTrade != null) {
					previousBuy = new MercadoBigDecimal(previousBuyingTrade.getPrice());
				}
			}

			long lastBuyingTradeId = buyingTrades.entrySet()
					.stream()
					.map(Entry<Long, Trade>::getValue)
					.mapToLong(tradePO -> tradePO.getId())
					.max()
					.orElse(0);
			if (lastBuyingTradeId != 0) {
				sell = new MercadoBigDecimal(trades.get(lastBuyingTradeId)
						.getPrice());
			} else {
				Trade previousSellingTrade = tradeDAO.findFirstTradeOfCurrencyAndTypeAndOlderThan(currency,
						TradeType.BUY, timeInterval.getStart());
				if (previousSellingTrade != null) {
					previousSell = new MercadoBigDecimal(previousSellingTrade.getPrice());
				}
			}
		} else {
			Trade previousBuyingTrade = tradeDAO.findFirstTradeOfCurrencyAndTypeAndOlderThan(currency, TradeType.SELL,
					timeInterval.getStart());
			if (previousBuyingTrade != null) {
				previousBuy = new MercadoBigDecimal(previousBuyingTrade.getPrice());
			}
			Trade previousSellingTrade = tradeDAO.findFirstTradeOfCurrencyAndTypeAndOlderThan(currency, TradeType.BUY,
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

		return TemporalTicker.builder()
				.currency(currency)
				.start(timeInterval.getStart())
				.end(timeInterval.getEnd())
				.duration(timeInterval.getDuration())
				.first(first)
				.last(last)
				.previousLast(previousLast)
				.highest(high)
				.lowest(low)
				.average(average)
				.buy(buy)
				.previousBuy(previousBuy)
				.sell(sell)
				.previousSell(previousSell)
				.orders(new Long(trades.size()))
				.buyOrders(buyOrders)
				.sellOrders(sellOrders)
				.volumeTraded(vol)
				.build();
	}

	public TemporalTicker create(Currency currency, TimeInterval timeInterval, List<Trade> trades) {
		return create(currency, timeInterval, ListToMapTradeConverter.getInstance()
				.convertTo(trades));
	}
}
