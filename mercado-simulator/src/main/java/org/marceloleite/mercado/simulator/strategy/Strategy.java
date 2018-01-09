package org.marceloleite.mercado.simulator.strategy;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;

public interface Strategy {

	void check(TimeInterval simulationTimeInterval, Account account, House house);
	
	void setCurrency(Currency currency);
}
