package org.marceloleite.mercado.dao.database.repository;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.data.repository.CrudRepository;

public interface TradeStartTimeRepository extends CrudRepository<TradeStartTime, Long> {

	public TradeStartTime findByCurrency(Currency currency);
}
