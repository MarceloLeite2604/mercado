package org.marceloleite.mercado.dao.mixed;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.dao.database.TradeDatabaseDAO;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.site.TradeSiteDAO;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeDatabaseSiteDAO")
public class TradeDatabaseSiteDAO implements TradeDAO {

	private static final boolean DEFAULT_IGNORE_VALUES_ON_DATABASE = false;

	private static boolean ignoreValuesOnDatabase = DEFAULT_IGNORE_VALUES_ON_DATABASE;

	@Inject
	private TradeDatabaseDAO tradeDatabaseDAO;

	@Inject
	private TradeSiteDAO tradeSiteDAO;

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
		List<Trade> trades = null;
		if (!ignoreValuesOnDatabase) {
			trades = retrieveTradesUpdatingDatabase(currency, start, end);
		} else {
			trades = retrieveTradesFromDatabase(currency, start, end);
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

	private void retrieveUnavailableTradesOnDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval timeIntervalAvailable = tradeDatabaseDAO.retrieveTimeIntervalAvailable();
		if (timeIntervalAvailable != null) {
			if (start.isBefore(timeIntervalAvailable.getStart())) {
				ZonedDateTime endRetrieveTime = ZonedDateTime.from(timeIntervalAvailable.getStart())
						.minusSeconds(1);
				retrieveFromSiteAndSaveOnDatabase(currency, start, endRetrieveTime);
			}
			if (end.isAfter(timeIntervalAvailable.getEnd())) {
				ZonedDateTime startRetrieveTime = ZonedDateTime.from(timeIntervalAvailable.getEnd());
				retrieveFromSiteAndSaveOnDatabase(currency, startRetrieveTime, end);
			}
		} else {
			retrieveFromSiteAndSaveOnDatabase(currency, start, end);
		}
	}

	private void retrieveFromSiteAndSaveOnDatabase(Currency currency, ZonedDateTime start,
			ZonedDateTime endRetrieveTime) {
		List<Trade> trades = tradeSiteDAO.findByCurrencyAndTimeBetween(currency, start, endRetrieveTime);
		tradeDatabaseDAO.saveAll(trades);
	}

	@Override
	public Trade findTopByOrderByTimeAsc() {
		return tradeDatabaseDAO.findTopByOrderByTimeAsc();
	}

	@Override
	public Trade findTopByOrderByTimeDesc() {
		return tradeDatabaseDAO.findTopByOrderByTimeDesc();
	}

	@Override
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time) {
		Trade previousTrade = tradeDatabaseDAO.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, time);
		if (previousTrade == null) {
			previousTrade = tradeSiteDAO.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, time);
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
		Trade previousTrade = tradeDatabaseDAO.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, time);
		if (previousTrade == null) {
			previousTrade = tradeSiteDAO.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, time);
		}
		return previousTrade;
	}
}
