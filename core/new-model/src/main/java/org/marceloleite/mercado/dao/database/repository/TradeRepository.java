package org.marceloleite.mercado.dao.database.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.model.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TradeRepository extends CrudRepository<Trade, Long> {

	public List<Trade> findByCurrencyAndTimeBetween(Currency currency, ZonedDateTime start, ZonedDateTime end);

	public Trade findTopByOrderByTimeAsc();

	public Trade findTopByOrderByTimeDesc();

	public Trade findTopByCurrencyAndTimeLessThanOrderByTimeDesc(Currency currency, ZonedDateTime time);

	@Query(value = "SELECT * FROM TRADES WHERE id = (SELECT max(id) FROM TRADES WHERE time = ( SELECT max(time) FROM TRADES WHERE type = :type AND currency = :currency AND time <= :time) ) AND currency = :currency", nativeQuery = true)
	public Trade findFirstOfCurrencyAndTypeAndOlderThan(@Param("currency") Currency currency,
			@Param("type") TradeType type, @Param("time") ZonedDateTime time);
}
