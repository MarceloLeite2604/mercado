package org.marceloleite.mercado.dao.cache;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.dao.cache.base.Cache;
import org.marceloleite.mercado.dao.interfaces.TradeStartTimeDAO;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.stereotype.Component;

@Component
@Named("TradeStartTimeCacheDAO")
public class TradeStartTimeCacheDAO implements TradeStartTimeDAO {
	
	@Inject
	@Named("TradeStartTimeCache")
	private Cache<Currency, TradeStartTime> cache;

	@Override
	public TradeStartTime findByCurrency(Currency currency) {
		return cache.get(currency);
	}
	
	public void setDirty(Currency currency) {
		cache.setDirty(currency);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends TradeStartTime> S save(S tradeStartTime) {
		return (S) cache.put(tradeStartTime.getCurrency(), tradeStartTime);
	}
}
