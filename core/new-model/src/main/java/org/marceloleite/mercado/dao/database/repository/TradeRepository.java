package org.marceloleite.mercado.dao.database.repository;

import org.marceloleite.mercado.model.Trade;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {

}
