package org.marceloleite.mercado.dao.site;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.comparator.TradeComparatorByIdDesc;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.site.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@Named("TradeSiteDAO")
public class TradeSiteDAO implements TradeDAO {

	private static final int FIND_PREVIOUS_TRADE_LIMIT_MINUTES = 60 * 24 * 7;

	@Inject
	private TradeSiteRetriever tradeSiteRetriever;

	@Override
	public <S extends Trade> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Trade> Iterable<S> saveAll(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		return tradeSiteRetriever.retrieve(currency, new TimeInterval(start, end));
	}

	@Override
	public Trade findTopByCurrencyAndTimeLessThanOrderByTimeDesc(Currency currency, ZonedDateTime time) {
		Trade previousTrade = null;
		int minutesToSubtract = 1;
		ZonedDateTime end = ZonedDateTime.from(time);
		while (previousTrade == null && minutesToSubtract < FIND_PREVIOUS_TRADE_LIMIT_MINUTES) {
			minutesToSubtract *= 2;
			ZonedDateTime start = end.minusMinutes(minutesToSubtract);
			List<Trade> trades = findByCurrencyAndTimeBetween(currency, start, end);
			if (!CollectionUtils.isEmpty(trades)) {
				previousTrade = trades.stream()
						.sorted(TradeComparatorByIdDesc.getInstance())
						.findFirst()
						.orElse(null);
			}
			end = start;
		}
		return previousTrade;
	}

	@Override
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time) {
		Trade previousTrade = null;
		int minutesToSubtract = 1;
		ZonedDateTime end = ZonedDateTime.from(time);
		while (previousTrade == null && minutesToSubtract < FIND_PREVIOUS_TRADE_LIMIT_MINUTES) {
			minutesToSubtract *= 2;
			ZonedDateTime start = end.minusMinutes(minutesToSubtract);
			List<Trade> trades = findByCurrencyAndTimeBetween(currency, start, end);
			if (!CollectionUtils.isEmpty(trades)) {
				previousTrade = findPreviousTradeOnList(trades, type);
			}
			end = start;
		}
		return previousTrade;
	}

	private Trade findPreviousTradeOnList(List<Trade> trades, TradeType type) {
		Trade previousTrade;
		previousTrade = trades.stream()
				.filter(tradeToInspect -> tradeToInspect.getType()
						.equals(type))
				.sorted(TradeComparatorByIdDesc.getInstance())
				.findFirst()
				.orElse(null);
		return previousTrade;
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeAsc(Currency currency) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Trade findTopByCurrencyOrderByTimeDesc(Currency currency) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Trade> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Trade> Optional<S> findById(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TimeInterval retrieveTimeIntervalAvailable(Currency currency) {
		throw new UnsupportedOperationException();
	}

}
