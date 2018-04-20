package org.marceloleite.mercado.dao.database.repository;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.data.repository.CrudRepository;

public interface TemporalTickerRepository extends CrudRepository<TemporalTicker, Long> {

	public TemporalTicker findByCurrencyAndStartAndEnd(Currency currency, ZonedDateTime start, ZonedDateTime end);
}
