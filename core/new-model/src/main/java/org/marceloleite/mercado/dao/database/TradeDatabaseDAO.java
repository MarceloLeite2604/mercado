package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.dao.database.repository.TradeRepository;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeDatabaseDAO")
public class TradeDatabaseDAO implements TradeDAO {

	private static Map<Currency, TimeInterval> CACHED_TIME_INTERVAL_AVAILABLE_BY_CURRENCY = null;

	@Inject
	private TradeRepository tradeRepository;

	@Override
	public <S extends Trade> S save(S trade) {
		return tradeRepository.save(trade);
	}

	@Override
	public <S extends Trade> Iterable<S> saveAll(Iterable<S> trades) {
		return tradeRepository.saveAll(trades);
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeRepository.findByCurrencyAndTimeBetween(currency, start, end);
	}

	@Override
	public Iterable<Trade> findAll() {
		return tradeRepository.findAll();
	}

	public TimeInterval retrieveTimeIntervalAvailable(Currency currency, boolean retrieveFromCache) {
		if (retrieveFromCache) {
			if (CACHED_TIME_INTERVAL_AVAILABLE_BY_CURRENCY == null) {
				CACHED_TIME_INTERVAL_AVAILABLE_BY_CURRENCY = retrieveTimeIntervalAvailableFromDatabase();
			}
			return CACHED_TIME_INTERVAL_AVAILABLE_BY_CURRENCY.get(currency);
		} else {
			Map<Currency, TimeInterval> timeIntervalAvailableFromDatabase = retrieveTimeIntervalAvailableFromDatabase();
			return timeIntervalAvailableFromDatabase.get(currency);
		}
	}

	public TimeInterval retrieveTimeIntervalAvailable(Currency currency) {
		return retrieveTimeIntervalAvailable(currency, true);
	}

	private Map<Currency, TimeInterval> retrieveTimeIntervalAvailableFromDatabase() {
		Map<Currency,TimeInterval> timeIntervalAvailableByCurrency = new EnumMap<>(Currency.class);
		for(Currency currency : Currency.values()) {
			Trade oldestTrade = findTopByCurrencyOrderByTimeAsc(currency);
			Trade newestTrade = findTopByCurrencyOrderByTimeDesc(currency);

			if (oldestTrade != null && newestTrade != null) {
				TimeInterval timeInterval = new TimeInterval(oldestTrade.getTime(), newestTrade.getTime());
				timeIntervalAvailableByCurrency.put(currency, timeInterval);
			} else {
				timeIntervalAvailableByCurrency.put(currency, null);
			}
		}
		return timeIntervalAvailableByCurrency;
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeAsc(Currency currency) {
		return tradeRepository.findTopByCurrencyOrderByTimeAsc(currency);
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeDesc(Currency currency) {
		return tradeRepository.findTopByCurrencyOrderByTimeDesc(currency);
	}

	@Override
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time) {
		return tradeRepository.findFirstOfCurrencyAndTypeAndOlderThan(currency, type, time);

	}

	@Override
	public Trade findTopByCurrencyAndTimeLessThanOrderByTimeDesc(Currency currency, ZonedDateTime time) {
		return tradeRepository.findTopByCurrencyAndTimeLessThanOrderByTimeDesc(currency, time);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Trade> Optional<S> findById(Long id) {
		return (Optional<S>) tradeRepository.findById(id);
	}
}
