package org.marceloleite.mercado.dao.interfaces;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.TemporalTicker;

public interface TemporalTickerDAO extends BaseDAO<TemporalTicker> {
	
	public TemporalTicker findByCurrencyAndStartAndEnd(Currency currency, ZonedDateTime startTime, ZonedDateTime endTime);

	public List<TemporalTicker> findByCurrencyAndDurationAndStartBetween(Currency currency, Duration duration, TimeInterval timeInterval);
}
