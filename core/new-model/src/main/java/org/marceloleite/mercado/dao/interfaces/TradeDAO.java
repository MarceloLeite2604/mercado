package org.marceloleite.mercado.dao.interfaces;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Trade;

public interface TradeDAO extends BaseDAO<Trade> {
	
	List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end);
}
