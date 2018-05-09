package org.marceloleite.mercado.dao.interfaces;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TradeStartTime;

public interface TradeStartTimeDAO {

	public TradeStartTime findByCurrency(Currency currency);
	
	public <S extends TradeStartTime> S save(S tradeStartTime);
	
}
