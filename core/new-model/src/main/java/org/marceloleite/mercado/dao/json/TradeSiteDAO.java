package org.marceloleite.mercado.dao.json;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.comparator.TradeComparatorByIdDesc;
import org.marceloleite.mercado.dao.interfaces.TradeDAO;
import org.marceloleite.mercado.dao.json.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.springframework.util.CollectionUtils;

@Named("TradeSiteDAO")
public class TradeSiteDAO implements TradeDAO {
	
	private static final int FIND_PREVIOUS_TRADE_LIMIT_MINUTES = 60*24*7;

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
	public Trade findPreviousTradeOfSameType(Trade trade) {
		Trade previousTrade = null;
		int minutesToSubtract = 1;
		ZonedDateTime end = ZonedDateTime.from(trade.getTime());
		while (previousTrade == null && minutesToSubtract < FIND_PREVIOUS_TRADE_LIMIT_MINUTES) {
			minutesToSubtract *= 2;
			ZonedDateTime start = end.minusMinutes(minutesToSubtract);
			List<Trade> trades = findByCurrencyAndTimeBetween(trade.getCurrency(), start, end);
			if (!CollectionUtils.isEmpty(trades)) {
				previousTrade = findPreviousTradeOnList(trades, trade);
			}
			end = start;
		}
		return trade;
	}

	private Trade findPreviousTradeOnList(List<Trade> trades, Trade trade) {
		Trade previousTrade;
		previousTrade = trades.stream()
				.filter(tradeToInspect -> tradeToInspect.getType()
						.equals(trade.getType()))
				.sorted(TradeComparatorByIdDesc.getInstance())
				.findFirst()
				.orElse(null);
		return previousTrade;
	}

	@Override
	public Trade findTopByOrderByTimeAsc() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Trade findTopByOrderByTimeDesc() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Trade> findAll() {
		throw new UnsupportedOperationException();
	}

}
