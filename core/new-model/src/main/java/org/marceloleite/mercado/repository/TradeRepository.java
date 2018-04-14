package org.marceloleite.mercado.repository;

import org.marceloleite.mercado.model.Trade;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {

}
