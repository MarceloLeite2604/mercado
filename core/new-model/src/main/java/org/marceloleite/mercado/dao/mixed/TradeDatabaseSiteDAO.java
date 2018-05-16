package org.marceloleite.mercado.dao.mixed;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.interfaces.TradeStartTimeDAO;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeDatabaseSiteDAO")
public class TradeDatabaseSiteDAO implements TradeDAO {

	private static final Logger LOGGER = LogManager.getLogger(TradeDatabaseSiteDAO.class);

	private static final boolean DEFAULT_IGNORE_VALUES_ON_DATABASE = false;

	private static boolean ignoreValuesOnDatabase = DEFAULT_IGNORE_VALUES_ON_DATABASE;

	@Inject
	@Named("TradeStartTimeCacheDatabaseDAO")
	private TradeStartTimeDAO tradeStartTimeCacheDatabaseDAO;

	@Inject
	@Named("TradeDatabaseDAO")
	private TradeDAO tradeDatabaseDAO;

	@Inject
	@Named("TradeSiteDAO")
	private TradeDAO tradeSiteDAO;

	@Inject
	@Named("TradeStartTimeDatabaseDAO")
	private TradeStartTimeDAO tradeStartTimeDAO;

	@Override
	public <S extends Trade> S save(S trade) {
		return tradeDatabaseDAO.save(trade);
	}

	@Override
	public <S extends Trade> Iterable<S> saveAll(Iterable<S> entities) {
		return tradeDatabaseDAO.saveAll(entities);
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		LOGGER.debug("Retrieving trades for {} currency and time interval {}", currency, new TimeInterval(start, end));
		List<Trade> trades = null;

		if (isTimeAfterTradeStartTime(currency, start)) {
			if (ignoreValuesOnDatabase) {
				trades = retrieveAllTradesFromSite(currency, start, end);
			} else {
				trades = retrieveTradesUpdatingDatabase(currency, start, end);
			}
		}
		return trades;
	}

	private List<Trade> retrieveTradesUpdatingDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		retrieveUnavailableTradesOnDatabase(currency, start, end);
		return retrieveTradesFromDatabase(currency, start, end);

	}

	private List<Trade> retrieveTradesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeDatabaseDAO.findByCurrencyAndTimeBetween(currency, start, end);
	}
	
	private List<Trade> retrieveAllTradesFromSite(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<Trade> trades = tradeSiteDAO.findByCurrencyAndTimeBetween(currency, start, end);
		if ( trades != null ) {
			tradeDatabaseDAO.saveAll(trades);
		}
		return trades;
	}

	private void retrieveUnavailableTradesOnDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval timeIntervalAvailable = tradeDatabaseDAO.retrieveTimeIntervalAvailable(currency);
		TimeInterval retrievalTime = defineRetrievalTime(start, end, timeIntervalAvailable);
		if (retrievalTime != null ) {
			retrieveFromSiteAndSaveOnDatabase(currency, retrievalTime);
		}
	}

	private TimeInterval defineRetrievalTime(ZonedDateTime start, ZonedDateTime end,
			TimeInterval timeIntervalAvailable) {
		TimeInterval result;
		if (timeIntervalAvailable == null) {
			result = new TimeInterval(start, end);
		} else {
			if (start.isBefore(timeIntervalAvailable.getStart())) {
				if (end.isBefore(timeIntervalAvailable.getStart())) {
					result = new TimeInterval(start, end);
				} else {
					result = new TimeInterval(start, ZonedDateTime.from(timeIntervalAvailable.getStart()));
				}
			} else {
				if (end.isAfter(timeIntervalAvailable.getEnd())) {
					if (start.isAfter(timeIntervalAvailable.getEnd())) {
						result = new TimeInterval(start, end);
					} else {
						result = new TimeInterval(ZonedDateTime.from(timeIntervalAvailable.getEnd()), end);
					}
				} else {
					// Database already has trades for required period.
					result = null;
				}
			}
		}
		return result;
	}

	private void retrieveFromSiteAndSaveOnDatabase(Currency currency, TimeInterval timeInterval) {
		List<Trade> trades = tradeSiteDAO.findByCurrencyAndTimeBetween(currency, timeInterval.getStart(),
				timeInterval.getEnd());
		tradeDatabaseDAO.saveAll(trades);
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeAsc(Currency currency) {
		return tradeDatabaseDAO.findTopByCurrencyOrderByTimeAsc(currency);
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeDesc(Currency currency) {
		return tradeDatabaseDAO.findTopByCurrencyOrderByTimeDesc(currency);
	}

	@Override
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time) {
		Trade previousTrade = null;

		if (isTimeAfterTradeStartTime(currency, time)) {
			previousTrade = tradeDatabaseDAO.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, time);
			if (previousTrade == null) {
				previousTrade = tradeSiteDAO.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, time);
			}
		}
		return previousTrade;
	}

	@Override
	public Iterable<Trade> findAll() {
		throw new UnsupportedOperationException();
	}

	public static void setIgnoreValuesOnDatabase(boolean ignoreValuesOnDatabase) {
		TradeDatabaseSiteDAO.ignoreValuesOnDatabase = ignoreValuesOnDatabase;
	}

	@Override
	public Trade findTopByCurrencyAndTimeLessThanOrderByTimeDesc(Currency currency, ZonedDateTime time) {
		Trade previousTrade = null;

		if (isTimeAfterTradeStartTime(currency, time)) {
			previousTrade = tradeDatabaseDAO.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, time);
			if (previousTrade == null) {
				previousTrade = tradeSiteDAO.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, time);
			}
		}
		return previousTrade;
	}

	@Override
	public <S extends Trade> Optional<S> findById(Long id) {
		return tradeDatabaseDAO.findById(id);
	}

	@Override
	public TimeInterval retrieveTimeIntervalAvailable(Currency currency) {
		return tradeDatabaseDAO.retrieveTimeIntervalAvailable(currency);
	}

	private boolean isTimeAfterTradeStartTime(Currency currency, ZonedDateTime time) {
		TradeStartTime tradeStartTime = tradeStartTimeCacheDatabaseDAO.findByCurrency(currency);
		if (tradeStartTime == null) {
			throw new RuntimeException("Unable to find trade start time for \"" + currency + "\" currency.");
		}

		return (time.isAfter(tradeStartTime.getStartTime()) || time.isEqual(tradeStartTime.getStartTime()));
	}
}
