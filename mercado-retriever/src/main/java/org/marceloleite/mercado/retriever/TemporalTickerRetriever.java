package org.marceloleite.mercado.retriever;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.retriever.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.retriever.converter.TimeIntervalToTemporalTickerIdConverter;
import org.marceloleite.mercado.retriever.filter.TradeTypeFilter;

public class TemporalTickerRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = false;

	private static final int FIRST_CALL = 0;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerRetriever.class);

	private TemporalTickerDAO temporalTickerDAO;

	public TemporalTickerRetriever() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
	}

	public TemporalTickerPO retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValueOnDatabsase) {
		return retrieve(currency, timeInterval, ignoreValueOnDatabsase, FIRST_CALL);
	}

	private TemporalTickerPO retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValueOnDatabsase,
			int calls) {

		TemporalTickerPO temporalTickerPO = null;
		if (!ignoreValueOnDatabsase) {

			TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
			temporalTickerIdPO.setCurrency(currency);
			temporalTickerIdPO.setStart(timeInterval.getStart());
			temporalTickerIdPO.setEnd(timeInterval.getEnd());
			TemporalTickerPO temporalTickerPOForEnquirement = new TemporalTickerPO();
			temporalTickerPOForEnquirement.setTemporalTickerIdPO(temporalTickerIdPO);
			temporalTickerPO = temporalTickerDAO.findById(temporalTickerPOForEnquirement);

		}

		if (temporalTickerPO == null) {
			TradesRetriever tradesRetriever = new TradesRetriever();

			List<TradePO> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(),
					IGNORE_DATABASE_VALUES);
			Map<Long, TradePO> tradesMap = new ListToMapTradeConverter().convertTo(trades);
			temporalTickerPO = create(currency, timeInterval, tradesMap);
			if (temporalTickerPO != null) {
				temporalTickerDAO.merge(temporalTickerPO);
			}
		}

		return temporalTickerPO;
	}

	private TemporalTickerPO create(Currency currency, TimeInterval timeInterval, Map<Long, TradePO> trades) {

		TemporalTickerPO temporalTickerPO = null;

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
			Map<Long, TradePO> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
			sellOrders = buyingTrades.size();

			Map<Long, TradePO> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);
			buyOrders = sellingTrades.size();

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
			} else {
				TradePO previousBuyingTrade = new TradeDAO().retrievePreviousTrade(currency, TradeType.SELL,
						timeInterval.getStart());
				if (previousBuyingTrade != null) {
					previousBuy = previousBuyingTrade.getPrice();
				}
			}

			long lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).max().orElse(0);
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

		temporalTickerPO = new TemporalTickerPO();
		TemporalTickerIdPO temporalTickerIdPO = new TimeIntervalToTemporalTickerIdConverter().convertTo(timeInterval);
		temporalTickerIdPO.setCurrency(currency);
		temporalTickerPO.setTemporalTickerIdPO(temporalTickerIdPO);
		temporalTickerPO.setOrders(trades.size());
		temporalTickerPO.setHigh(high);
		temporalTickerPO.setAverage(average);
		temporalTickerPO.setLow(low);
		temporalTickerPO.setVol(vol);
		temporalTickerPO.setFirst(first);
		temporalTickerPO.setLast(last);
		temporalTickerPO.setPreviousLast(previousLast);
		temporalTickerPO.setBuy(buy);
		temporalTickerPO.setPreviousBuy(previousBuy);
		temporalTickerPO.setSell(sell);
		temporalTickerPO.setPreviousSell(previousSell);
		temporalTickerPO.setBuyOrders(buyOrders);
		temporalTickerPO.setSellOrders(sellOrders);
		temporalTickerPO.setDuration(timeInterval.getDuration());

		return temporalTickerPO;
	}

	public List<TemporalTickerPO> bulkRetrieve(Currency currency, TimeDivisionController timeDivisionController) {
		List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency, timeDivisionController);
		temporalTickerPOs = retrieveTemporalTickersNotFoundOnBulk(temporalTickerPOs, currency, timeDivisionController);
		return sortTemporalTickers(temporalTickerPOs);
	}

	public Map<TimeInterval, Map<Currency, TemporalTickerPO>> bulkRetrieve(
			TimeDivisionController timeDivisionController) {
		Map<Currency, List<TemporalTickerPO>> temporalTickerPOsByCurrency = new EnumMap<>(Currency.class);
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency,
						timeDivisionController);
				temporalTickerPOs = retrieveTemporalTickersNotFoundOnBulk(temporalTickerPOs, currency,
						timeDivisionController);
				temporalTickerPOsByCurrency.put(currency, temporalTickerPOs);
			}
		}

		return elaborateTemporalTickerPOsByTimeInterval(temporalTickerPOsByCurrency);
	}

	private Map<TimeInterval, Map<Currency, TemporalTickerPO>> elaborateTemporalTickerPOsByTimeInterval(
			Map<Currency, List<TemporalTickerPO>> temporalTickerPOsByCurrency) {
		Map<TimeInterval, Map<Currency, TemporalTickerPO>> result = new TreeMap<>();
		for (Currency currency : temporalTickerPOsByCurrency.keySet()) {
			List<TemporalTickerPO> temporalTickerPOs = temporalTickerPOsByCurrency.get(currency);
			for (TemporalTickerPO temporalTickerPO : temporalTickerPOs) {
				TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
				TimeInterval timeInterval = new TimeInterval(temporalTickerIdPO.getStart(),
						temporalTickerIdPO.getEnd());
				Map<Currency, TemporalTickerPO> temporalTickerPOByCurrency = result.getOrDefault(timeInterval,
						new EnumMap<>(Currency.class));
				temporalTickerPOByCurrency.put(temporalTickerIdPO.getCurrency(), temporalTickerPO);
				result.put(timeInterval, temporalTickerPOByCurrency);
			}
		}
		return result;
	}

	private List<TemporalTickerPO> retrieveTemporalTickersNotFoundOnBulk(List<TemporalTickerPO> temporalTickerPOs,
			Currency currency, TimeDivisionController timeDivisionController) {
		List<TemporalTickerIdPO> temporalTickerIdPOsFromBulk = temporalTickerPOs.stream().map(TemporalTickerPO::getId)
				.collect(Collectors.toList());
		TemporalTickerIdPO temporalTickerIdPOtoCheck;
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			temporalTickerIdPOtoCheck = new TemporalTickerIdPO(currency, timeInterval.getStart(),
					timeInterval.getEnd());
			if (!temporalTickerIdPOsFromBulk.contains(temporalTickerIdPOtoCheck)) {
				TemporalTickerPO temporalTickerPOretrieved = retrieve(currency, timeInterval, IGNORE_DATABASE_VALUES);
				temporalTickerPOs.add(temporalTickerPOretrieved);
			}
		}
		return temporalTickerPOs;
	}

	private List<TemporalTickerPO> sortTemporalTickers(List<TemporalTickerPO> temporalTickerPOs) {
		Collections.sort(temporalTickerPOs, new Comparator<TemporalTickerPO>() {

			public int compare(TemporalTickerPO firstObject, TemporalTickerPO secondObject) {
				if (firstObject.getId().getStart().isBefore(secondObject.getId().getStart())) {
					return -1;
				} else if (firstObject.getId().getStart().isAfter(secondObject.getId().getStart())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return temporalTickerPOs;
	}

}
