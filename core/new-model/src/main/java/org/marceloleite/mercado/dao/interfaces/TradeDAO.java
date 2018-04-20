package org.marceloleite.mercado.dao.interfaces;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.model.Trade;

public interface TradeDAO extends BaseDAO<Trade> {
	
	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end);
	
	public Trade findTopByOrderByTimeAsc();
	
	public Trade findTopByOrderByTimeDesc();
	
	public Trade findFirstTradeOfCurrencyAndTypeAndOlderThan(Currency currency, TradeType type, ZonedDateTime time);
}
