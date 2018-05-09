package org.marceloleite.mercado.dao.database;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.dao.database.repository.TradeStartTimeRepository;
import org.marceloleite.mercado.dao.interfaces.TradeStartTimeDAO;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeStartTimeDatabaseDAO")
public class TradeStartTimeDatabaseDAO implements TradeStartTimeDAO {
	
	@Inject
	private TradeStartTimeRepository tradeStartTimeRepository;

	@Override
	public TradeStartTime findByCurrency(Currency currency) {
		return tradeStartTimeRepository.findByCurrency(currency);
	}

	@Override
	public <S extends TradeStartTime> S save(S tradeStartTime) {
		return tradeStartTimeRepository.save(tradeStartTime);
	}	
}
