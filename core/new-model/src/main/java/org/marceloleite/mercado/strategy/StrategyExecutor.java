package org.marceloleite.mercado.strategy;

import org.marceloleite.mercado.House;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Strategy;

public interface StrategyExecutor {

	void execute(TimeInterval timeInterval, Account account, House house);
	
	Strategy getStrategy();
	
	void beforeStart();
	
	void afterFinish();
}
