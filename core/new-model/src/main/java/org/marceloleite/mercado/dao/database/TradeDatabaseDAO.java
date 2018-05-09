package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.dao.cache.base.Cache;
import org.marceloleite.mercado.dao.database.repository.TradeRepository;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeDatabaseDAO")
public class TradeDatabaseDAO implements TradeDAO {

	@Inject
	@Named("TimeIntervalAvaliableCache")
	private Cache<Currency, TimeInterval> cachedTimeIntervalAvailableByCurrency;

	@Inject
	private TradeRepository tradeRepository;

	@Override
	public <S extends Trade> S save(S trade) {
		S result = tradeRepository.save(trade);
		cachedTimeIntervalAvailableByCurrency.setDirty(result.getCurrency());
		return result;
	}

	@Override
	public <S extends Trade> Iterable<S> saveAll(Iterable<S> trades) {
		Iterable<S> result = tradeRepository.saveAll(trades);
		retrieveDistinctCurrencies(result).forEach(cachedTimeIntervalAvailableByCurrency::setDirty);
		return result;
	}

	private <S extends Trade> Set<Currency> retrieveDistinctCurrencies(Iterable<S> result) {
		Set<Currency> currencies = new HashSet<>();
		for (S trade : result) {
			if (!currencies.contains(trade.getCurrency())) {
				currencies.add(trade.getCurrency());
			}
		}
		return currencies;
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeRepository.findByCurrencyAndTimeBetween(currency, start, end);
	}

	@Override
	public Iterable<Trade> findAll() {
		return tradeRepository.findAll();
	}

	public TimeInterval retrieveTimeIntervalAvailable(Currency currency) {
		TimeInterval timeInterval = cachedTimeIntervalAvailableByCurrency.get(currency);
		if (timeInterval == null) {
			timeInterval = retrieveTimeIntervalAvailableFromDatabase(currency);

			if (timeInterval != null) {
				cachedTimeIntervalAvailableByCurrency.put(currency, timeInterval);
			}
		}
		return timeInterval;
	}

	private TimeInterval retrieveTimeIntervalAvailableFromDatabase(Currency currency) {
		TimeInterval timeInterval = null;

		Trade oldestTrade = findTopByCurrencyOrderByTimeAsc(currency);
		Trade newestTrade = findTopByCurrencyOrderByTimeDesc(currency);

		if (oldestTrade != null && newestTrade != null) {
			timeInterval = new TimeInterval(oldestTrade.getTime(), newestTrade.getTime());
		}
		return timeInterval;
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
