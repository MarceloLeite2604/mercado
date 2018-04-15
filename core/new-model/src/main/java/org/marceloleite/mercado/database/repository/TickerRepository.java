package org.marceloleite.mercado.database.repository;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Ticker;
import org.springframework.data.repository.CrudRepository;

public interface TickerRepository extends CrudRepository<Ticker, Long> {

	public Ticker findByCurrencyAndTickerTime(Currency currency, ZonedDateTime tickerTime);
}
