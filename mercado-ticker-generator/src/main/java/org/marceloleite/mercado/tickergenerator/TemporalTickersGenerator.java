package org.marceloleite.mercado.tickergenerator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.tickergenerator.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.tickergenerator.filter.TradeTypeFilter;
import org.marceloleite.mercado.tickergenerator.property.TickerGeneratorPropertiesRetriever;

public class TemporalTickersGenerator {

	private static final Logger LOGGER = LogManager.getLogger(TemporalTickersGenerator.class);

	private TimeDivisionController timeDivisionController;

	private boolean ignoreTemporalTickersOnDatabase;

	private TemporalTickerDAO temporalTickerDAO;

	private TradesRetriever tradesRetriever;

	private Map<Currency, TemporalTickerPO> previousTemporalTickers;

	public TemporalTickersGenerator() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
		this.tradesRetriever = new TradesRetriever();
	}

	public void generate() {

		retrieveProperties();
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		LOGGER.info("Starting temporal tickers generator for period between "
				+ localDateTimeToStringConverter.convert(timeDivisionController.getStart()) + " and "
				+ localDateTimeToStringConverter.convert(timeDivisionController.getEnd()) + " with steps of " + );

		for (long step = 0; step < timeDivisionController.getDivisions(); step++) {
			TimeInterval nextTimeInterval = timeDivisionController.getNextTimeInterval();
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
					TemporalTickerPO temporalTickerPO;
					if (ignoreTemporalTickersOnDatabase) {
						temporalTickerPO = generateNewTemporalTicker(currency, nextTimeInterval);
					} else {
						temporalTickerPO = retrieveTemporalTickerFromDatabase(currency, nextTimeInterval);
					}
					if (temporalTickerPO != null) {
						temporalTickerPO = adjustValues(temporalTickerPO);
						temporalTickerDAO.merge(temporalTickerPO);
					}
					previousTemporalTickers.put(currency, temporalTickerPO);
				}
			}
		}
	}

	private TemporalTickerPO generateNewTemporalTicker(Currency currency, TimeInterval timeInterval) {
		List<TradePO> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(),
				false);
		Map<Long, TradePO> tradesMap = new ListToMapTradeConverter().convert(trades);
		return generateTemporalTickerFromTrades(currency, timeInterval, tradesMap);
	}

	private TemporalTickerPO retrieveTemporalTickerFromDatabase(Currency currency, TimeInterval nextTimeInterval) {
		TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
		temporalTickerIdPO.setCurrency(currency);
		temporalTickerIdPO.setStart(nextTimeInterval.getStart());
		temporalTickerIdPO.setEnd(nextTimeInterval.getEnd());
		TemporalTickerPO temporalTickerPOForEnquirement = new TemporalTickerPO();
		temporalTickerPOForEnquirement.setTemporalTickerIdPO(temporalTickerIdPO);
		TemporalTickerPO TemporalTickerPO = temporalTickerDAO.findById(temporalTickerPOForEnquirement);
		return TemporalTickerPO;
	}

	private void retrieveProperties() {
		TickerGeneratorPropertiesRetriever tickerGeneratorPropertiesRetriever = new TickerGeneratorPropertiesRetriever();
		timeDivisionController = tickerGeneratorPropertiesRetriever.retrieveTimeDivisionController();
		ignoreTemporalTickersOnDatabase = tickerGeneratorPropertiesRetriever.retrieveIgnoreTemporalTickersOnDatabase();
	}

	private TemporalTickerPO adjustValues(TemporalTickerPO temporalTickerPO) {
		if (temporalTickerPO.getOrders() == 0) {
			TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
			TemporalTickerPO previousTemporalTicker = previousTemporalTickers.get(temporalTickerIdPO.getCurrency());
			if (previousTemporalTicker == null) {
				LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
				LOGGER.info("No previous temporal ticker for " + temporalTickerIdPO.getCurrency()
						+ " currency on period " + localDateTimeToStringConverter.convert(temporalTickerIdPO.getStart())
						+ " to " + localDateTimeToStringConverter.convert(temporalTickerIdPO.getEnd()));
			} else {
				temporalTickerPO.setLast(previousTemporalTicker.getLast());
			}
		}

		return temporalTickerPO;
	}

	private TemporalTickerPO generateTemporalTickerFromTrades(Currency currency, TimeInterval timeInterval,
			Map<Long, TradePO> trades) {

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
				last = trades.get(lastTradeId).getPrice();
			}

			long firstTradeId = trades.entrySet().stream().map(Entry<Long, TradePO>::getValue)
					.mapToLong(tradePO -> tradePO.getTradeIdPO().getId()).min().orElse(0);
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
