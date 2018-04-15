package org.marceloleite.mercado.database.repository;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.data.repository.CrudRepository;

public interface TemporalTickerRepository extends CrudRepository<TemporalTicker, Long> {

	public TemporalTicker findByCurrencyAndStartTimeAndEndTime(Currency currency, ZonedDateTime startTime, ZonedDateTime endTime);
}
