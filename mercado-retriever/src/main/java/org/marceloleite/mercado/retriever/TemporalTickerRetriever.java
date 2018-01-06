package org.marceloleite.mercado.retriever;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;
import org.marceloleite.mercado.retriever.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.retriever.converter.TimeIntervalToTemporalTickerIdConverter;
import org.marceloleite.mercado.retriever.exception.NoTemporalTickerForPeriodException;
import org.marceloleite.mercado.retriever.filter.TradeTypeFilter;

public class TemporalTickerRetriever {

	private static final int FIRST_CALL = 0;

	private static final int MAX_CALLS = 3;

	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerRetriever.class);

	private TemporalTickerDAO temporalTickerDAO;

	public TemporalTickerRetriever() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
	}

	public TemporalTickerPO retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValueOnDatabsase) throws NoTemporalTickerForPeriodException {
		return retrieve(currency, timeInterval, ignoreValueOnDatabsase, FIRST_CALL);
	}

	private TemporalTickerPO retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValueOnDatabsase,
			int calls) throws NoTemporalTickerForPeriodException {

		TemporalTickerPO temporalTickerPO = null;
		if (!ignoreValueOnDatabsase) {
			TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
			temporalTickerIdPO.setStart(timeInterval.getStart());
			temporalTickerIdPO.setEnd(timeInterval.getEnd());
			TemporalTickerPO temporalTickerPOForEnquirement = new TemporalTickerPO();
			temporalTickerPOForEnquirement.setTemporalTickerIdPO(temporalTickerIdPO);
			temporalTickerPO = temporalTickerDAO.findById(temporalTickerPOForEnquirement);
		}

		if (temporalTickerPO == null) {
			TradesRetriever tradesRetriever = new TradesRetriever();
			List<TradePO> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(),
					false);
			Map<Long, TradePO> tradesMap = new ListToMapTradeConverter().convertTo(trades);
			temporalTickerPO = create(currency, timeInterval, tradesMap);
			temporalTickerPO = adjustValues(temporalTickerPO, currency, timeInterval, ignoreValueOnDatabsase, calls);
			if (temporalTickerPO != null) {
				temporalTickerDAO.merge(temporalTickerPO);
			} else {
				if (calls == FIRST_CALL) {
					throw new NoTemporalTickerForPeriodException(currency, timeInterval);
				}
			}
		}

		return temporalTickerPO;
	}

	private TemporalTickerPO adjustValues(TemporalTickerPO temporalTickerPO, Currency currency,
			TimeInterval timeInterval, boolean ignoreValueOnDatabase, int calls) throws NoTemporalTickerForPeriodException {
		if (temporalTickerPO.getOrders() == 0) {
			LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
			if (calls < MAX_CALLS) {
				LOGGER.info("Temporal ticker for currency " + currency.getAcronym() + " on period "
						+ localDateTimeToStringConverter.convertTo(timeInterval.getStart()) + " to "
						+ localDateTimeToStringConverter.convertTo(timeInterval.getEnd()) + " has no orders.");
				LocalDateTime previousStartTime = timeInterval.getStart().minus(timeInterval.getDuration());
				TimeInterval previousTimeInterval = new TimeInterval(previousStartTime, timeInterval.getDuration());
				TemporalTickerPO previousTemporalTickerPO = new TemporalTickerRetriever().retrieve(currency,
						previousTimeInterval, ignoreValueOnDatabase, ++calls);
				if (previousTemporalTickerPO != null) {
					temporalTickerPO.setLast(previousTemporalTickerPO.getLast());
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		return temporalTickerPO;
	}

	private TemporalTickerPO create(Currency currency, TimeInterval timeInterval, Map<Long, TradePO> trades) {
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

			long lastTradeId = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).max().orElse(0);
			if (lastTradeId != 0) {
				TradePO lastTradePO = trades.get(lastTradeId);
				last = lastTradePO.getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).min().orElse(0);
			if (firstTradeId != 0) {
				TradePO firstTradePO = trades.get(firstTradeId);
				first = firstTradePO.getPrice();
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
		TemporalTickerIdPO temporalTickerId = new TimeIntervalToTemporalTickerIdConverter().convertTo(timeInterval);
		temporalTickerId.setCurrency(currency);
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
