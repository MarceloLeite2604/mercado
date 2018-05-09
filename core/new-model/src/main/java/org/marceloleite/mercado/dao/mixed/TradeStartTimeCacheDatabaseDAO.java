package org.marceloleite.mercado.dao.mixed;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.dao.interfaces.TradeStartTimeDAO;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.stereotype.Repository;

@Repository
@Named("TradeStartTimeCacheDatabaseDAO")
public class TradeStartTimeCacheDatabaseDAO implements TradeStartTimeDAO {

	@Inject
	@Named("TradeStartTimeCacheDAO")
	TradeStartTimeDAO tradeStartTimeCacheDAO;

	@Inject
	@Named("TradeStartTimeDatabaseDAO")
	TradeStartTimeDAO tradeStartTimeDatabaseDAO;

	@Override
	public TradeStartTime findByCurrency(Currency currency) {
		TradeStartTime result = null;
		result = tradeStartTimeCacheDAO.findByCurrency(currency);
		if ( result == null ) {
			result = tradeStartTimeDatabaseDAO.findByCurrency(currency);
			if (result != null) {
				tradeStartTimeCacheDAO.save(result);
			}
		}
		return result;
	}

	@Override
	public <S extends TradeStartTime> S save(S tradeStartTime) {
		tradeStartTimeDatabaseDAO.save(tradeStartTime);
		tradeStartTimeCacheDAO.save(tradeStartTime);
		return tradeStartTime;
	}

}
