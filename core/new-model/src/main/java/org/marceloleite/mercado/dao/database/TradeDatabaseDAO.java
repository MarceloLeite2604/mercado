package org.marceloleite.mercado.dao.database;

import java.time.ZonedDateTime;
import java.util.List;
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

	private static TimeInterval cachedTimeIntervalAvailable;

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

	public TimeInterval retrieveTimeIntervalAvailable(boolean retrieveFromCache) {
		if (retrieveFromCache) {
			if (cachedTimeIntervalAvailable == null) {
				cachedTimeIntervalAvailable = retrieveTimeIntervalAvailableFromDatabase();
			}
			return cachedTimeIntervalAvailable;
		} else {
			return retrieveTimeIntervalAvailableFromDatabase();
		}
	}

	public TimeInterval retrieveTimeIntervalAvailable() {
		return retrieveTimeIntervalAvailable(true);
	}

	private TimeInterval retrieveTimeIntervalAvailableFromDatabase() {
		Trade oldestTrade = findTopByOrderByTimeAsc();
		Trade newestTrade = findTopByOrderByTimeDesc();

		if (oldestTrade != null && newestTrade != null) {
			TimeInterval timeInterval = new TimeInterval(oldestTrade.getTime(), newestTrade.getTime());
			return timeInterval;
		} else {
			return null;
		}
	}

	@Override
	public Trade findTopByOrderByTimeAsc() {
		return tradeRepository.findTopByOrderByTimeAsc();
	}

	@Override
	public Trade findTopByOrderByTimeDesc() {
		return tradeRepository.findTopByOrderByTimeDesc();
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
