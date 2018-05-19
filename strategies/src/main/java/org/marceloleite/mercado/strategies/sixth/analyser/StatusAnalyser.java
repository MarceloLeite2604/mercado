package org.marceloleite.mercado.strategies.sixth.analyser;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;

public interface StatusAnalyser {
	
	public SixthStrategyStatus getStatus();

	public Order analyse(TimeInterval timeInterval, Account account, House house);
}
