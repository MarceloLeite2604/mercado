package org.marceloleite.mercado.dao.database.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Trade;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {
	
	public <S extends Account> S save(S account);

	List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end);
}
