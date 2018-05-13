package org.marceloleite.mercado;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.checker.ValidTimeIntervalForTemporalTickerChecker;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.comparator.TradeComparatorById;
import org.marceloleite.mercado.comparator.TradeComparatorByIdDesc;
import org.marceloleite.mercado.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TemporalTickerCreator {

	@Inject
	@Named("TradeDatabaseSiteDAO")
	private TradeDAO tradeDAO;

	public TemporalTicker create(Currency currency, TimeInterval timeInterval, List<Trade> trades) {
		return create(currency, timeInterval, ListToMapTradeConverter.fromListToMap(trades));
	}

	public TemporalTicker create(Currency currency, TimeInterval timeInterval, Map<Long, Trade> trades) {

		if (!ValidTimeIntervalForTemporalTickerChecker.getInstance()
				.check(timeInterval)) {
			throw new IllegalArgumentException("Time interval " + timeInterval + " is invalid for a Temporal Ticker.");
		}

		BigDecimal high;
		BigDecimal average;
		BigDecimal low;
		BigDecimal vol = new BigDecimal("0");
		BigDecimal first;
		BigDecimal last;
		BigDecimal previousLast;
		BigDecimal buy;
		BigDecimal previousBuy;
		BigDecimal sell;
		BigDecimal previousSell;
		long orders = 0;
		long buyOrders = 0;
		long sellOrders = 0;

		if (!CollectionUtils.isEmpty(trades)) {

			Map<Long, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			Map<Long, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

			Trade lastSellingTrade = retrieveLast(sellingTrades);
			Trade lastBuyingTrade = retrieveLast(buyingTrades);
			Trade firstTrade = retrieveFirst(trades);
			Trade lastTrade = retrieveLast(trades);

			high = retrieveHigh(trades);
			average = retrieveAverage(trades);
			low = retrieveLow(trades);
			vol = retrieveVolumeTraded(trades);
			first = createBigDecimalFromPrice(firstTrade);

			if (lastTrade != null) {
				last = new BigDecimal(lastTrade.getPrice()
						.toString());
				previousLast = null;
			} else {
				last = null;
				previousLast = retrievePrevious(currency, timeInterval.getStart());
			}

			if (lastSellingTrade != null) {
				buy = createBigDecimalFromPrice(lastSellingTrade);
				previousBuy = null;
			} else {
				buy = null;
				previousBuy = retrievePrevious(currency, TradeType.SELL, timeInterval.getStart());
			}

			if (lastBuyingTrade != null) {
				sell = createBigDecimalFromPrice(lastBuyingTrade);
				previousSell = null;
			} else {
				sell = null;
				previousSell = retrievePrevious(currency, TradeType.BUY, timeInterval.getStart());
			}

			orders = trades.size();
			buyOrders = sellingTrades.size();
			sellOrders = buyingTrades.size();

		} else {

			high = null;
			average = null;
			low = null;
			vol = new BigDecimal("0");
			first = null;
			last = null;
			buy = null;
			sell = null;
			
			if ( !timeInterval.getStart().isBefore(tradeDAO.retrieveTimeIntervalAvailable(currency).getStart())) {
				previousLast = retrievePrevious(currency, timeInterval.getStart());
				previousBuy = retrievePrevious(currency, TradeType.SELL, timeInterval.getStart());
				previousSell = retrievePrevious(currency, TradeType.BUY, timeInterval.getStart());
			} else {
				previousLast = null;
				previousBuy = null;
				previousSell = null;
			}
			
			orders = 0;
			buyOrders = 0;
			sellOrders = 0;
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
				.orders(orders)
				.buyOrders(buyOrders)
				.sellOrders(sellOrders)
				.volumeTraded(vol)
				.build();
	}

	private BigDecimal retrievePrevious(Currency currency, ZonedDateTime date) {
		Trade previousTrade = tradeDAO.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, date);
		return createBigDecimalFromPrice(previousTrade);
	}

	private BigDecimal retrievePrevious(Currency currency, TradeType type, ZonedDateTime date) {
		Trade previousTrade = tradeDAO.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, date);
		return createBigDecimalFromPrice(previousTrade);
	}

	private BigDecimal createBigDecimalFromPrice(Trade trade) {
		BigDecimal bigDecimal = null;
		if (trade != null) {
			bigDecimal = new BigDecimal(trade.getPrice()
					.toString());
		}
		return bigDecimal;
	}

	private BigDecimal retrieveVolumeTraded(Map<Long, Trade> trades) {
		BigDecimal vol;
		vol = new BigDecimal(trades.values()
				.stream()
				.mapToDouble(trade -> trade.getAmount()
						.doubleValue())
				.sum());
		return vol;
	}

	private BigDecimal retrieveLow(Map<Long, Trade> trades) {
		BigDecimal low;
		low = new BigDecimal(trades.values()
				.stream()
				.mapToDouble(trade -> trade.getPrice()
						.doubleValue())
				.min()
				.orElse(0.0));
		return low;
	}

	private BigDecimal retrieveAverage(Map<Long, Trade> trades) {
		BigDecimal average;
		average = new BigDecimal(trades.values()
				.stream()
				.mapToDouble(trade -> trade.getPrice()
						.doubleValue())
				.average()
				.orElse(0.0));
		return average;
	}

	private BigDecimal retrieveHigh(Map<Long, Trade> trades) {
		BigDecimal high;
		high = new BigDecimal(trades.values()
				.stream()
				.mapToDouble(trade -> trade.getPrice()
						.doubleValue())
				.max()
				.orElse(0.0));
		return high;
	}

	private Trade retrieveFirst(Map<Long, Trade> trades) {
		return retrieveFirstOrderingBy(trades, TradeComparatorById.getInstance());
	}

	private Trade retrieveLast(Map<Long, Trade> trades) {
		return retrieveFirstOrderingBy(trades, TradeComparatorByIdDesc.getInstance());
	}

	private Trade retrieveFirstOrderingBy(Map<Long, Trade> trades, Comparator<? super Trade> comparator) {
		return retrieveFirstOrderingBy(trades, comparator, null);
	}

	private Trade retrieveFirstOrderingBy(Map<Long, Trade> trades, Comparator<? super Trade> comparator,
			String message) {
		Trade lastTrade = trades.values()
				.stream()
				.sorted(comparator)
				.findFirst()
				.orElse(null);

		if (lastTrade == null && message != null) {
			throw new RuntimeException(message);
		}

		return lastTrade;
	}
}
