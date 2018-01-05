package org.marceloleite.mercado.tickergenerator;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;
import org.marceloleite.mercado.retriever.TradesRetriever;
import org.marceloleite.mercado.tickergenerator.checker.ValidDurationForTickerCheck;
import org.marceloleite.mercado.tickergenerator.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.tickergenerator.converter.TimeIntervalToTemporalTickerIdConverter;
import org.marceloleite.mercado.tickergenerator.filter.TradeTypeFilter;
import org.marceloleite.mercado.tickergenerator.property.TickerGeneratorPropertiesRetriever;

public class TemporalTickersGenerator {

	private static final Logger LOGGER = LogManager.getLogger(TemporalTickersGenerator.class);

	// private TimeDivisionController timeDivisionController;

	// private boolean ignoreTemporalTickersOnDatabase;

	private TemporalTickerDAO temporalTickerDAO;

	private TradesRetriever tradesRetriever;

	private Map<Currency, TemporalTickerPO> previousTemporalTickers;

	public TemporalTickersGenerator() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
		this.tradesRetriever = new TradesRetriever();
		this.previousTemporalTickers = new HashMap<>();
	}

	public void generate() {
		TickerGeneratorPropertiesRetriever tickerGeneratorPropertiesRetriever = new TickerGeneratorPropertiesRetriever();
		TimeDivisionController timeDivisionController = tickerGeneratorPropertiesRetriever
				.retrieveTimeDivisionController();
		boolean ignoreTemporalTickersOnDatabase = tickerGeneratorPropertiesRetriever
				.retrieveIgnoreTemporalTickersOnDatabase();
		generate(timeDivisionController, ignoreTemporalTickersOnDatabase);
	}

	public void generate(TimeDivisionController timeDivisionController, boolean ignoreTemporalTickersOnDatabase) {

		checkTimeDivisionController(timeDivisionController);
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		LOGGER.info("Starting temporal tickers generator for period between "
				+ localDateTimeToStringConverter.convertTo(timeDivisionController.getStart()) + " and "
				+ localDateTimeToStringConverter.convertTo(timeDivisionController.getEnd()) + " with steps of "
				+ durationToStringConverter.convertTo(timeDivisionController.getDivisionDuration()) + ".");

		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
					LOGGER.info("Retrieving temporal ticker to " + currency + " currency for period between "
							+ localDateTimeToStringConverter.convertTo(timeInterval.getStart()) + " and "
							+ localDateTimeToStringConverter.convertTo(timeInterval.getEnd()) + ".");
					TemporalTickerPO temporalTickerPO = null;
					if (!ignoreTemporalTickersOnDatabase) {
						temporalTickerPO = retrieveTemporalTickerFromDatabase(currency, timeInterval);
					}
					if (ignoreTemporalTickersOnDatabase
							|| (!ignoreTemporalTickersOnDatabase && temporalTickerPO == null)) {
						temporalTickerPO = generateNewTemporalTicker(currency, timeInterval);
					}
					if (temporalTickerPO != null) {
						temporalTickerPO = adjustValues(temporalTickerPO);
						temporalTickerDAO.merge(temporalTickerPO);
					}
					previousTemporalTickers.put(currency, temporalTickerPO);
				}
			}
		}
		LOGGER.info("Finishing execution.");
	}

	private TemporalTickerPO generateNewTemporalTicker(Currency currency, TimeInterval timeInterval) {
		List<TradePO> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(),
				false);
		Map<Long, TradePO> tradesMap = new ListToMapTradeConverter().convertTo(trades);
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

	private void checkTimeDivisionController(TimeDivisionController timeDivisionController) {
		ValidDurationForTickerCheck validDurationForTickerCheck = new ValidDurationForTickerCheck();
		Duration divisionDuration = timeDivisionController.getDivisionDuration();
		if (!validDurationForTickerCheck.check(divisionDuration)) {
			DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
			throw new RuntimeException(durationToStringConverter.convertTo(divisionDuration)
					+ " is not a valid duration to elaborate temporal tickers.");
		}
	}

	private TemporalTickerPO adjustValues(TemporalTickerPO temporalTickerPO) {
		if (temporalTickerPO.getOrders() == 0) {
			TemporalTickerIdPO temporalTickerIdPO = temporalTickerPO.getId();
			TemporalTickerPO previousTemporalTicker = previousTemporalTickers.get(temporalTickerIdPO.getCurrency());
			if (previousTemporalTicker == null) {
				LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
				LOGGER.info(
						"No previous temporal ticker for " + temporalTickerIdPO.getCurrency() + " currency on period "
								+ localDateTimeToStringConverter.convertTo(temporalTickerIdPO.getStart()) + " to "
								+ localDateTimeToStringConverter.convertTo(temporalTickerIdPO.getEnd()));
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
