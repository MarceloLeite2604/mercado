package org.marceloleite.mercado.dao.interfaces;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TemporalTicker;

public interface TemporalTickerDAO extends BaseDAO<TemporalTicker> {
	
	public TemporalTicker findByCurrencyAndStartAndEnd(Currency currency, ZonedDateTime startTime, ZonedDateTime endTime);

}
