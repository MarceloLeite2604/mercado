package org.marceloleite.mercado.dao.interfaces;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.model.Trade;

public interface TradeDAO extends BaseDAO<Trade> {
	
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end);
	
	public Trade findTopByCurrencyOrderByTimeAsc(Currency currency);
	
	public Trade findTopByCurrencyOrderByTimeDesc(Currency currency);
	
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time);
	
	public Trade findTopByCurrencyAndTimeLessThanOrderByTimeDesc(Currency currency, ZonedDateTime time);
	
	public TimeInterval retrieveTimeIntervalAvailable(Currency currency);
}
