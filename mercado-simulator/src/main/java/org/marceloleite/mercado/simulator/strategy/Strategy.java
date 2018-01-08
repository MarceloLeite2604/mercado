package org.marceloleite.mercado.simulator.strategy;

import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;

public interface Strategy {

	void check(Account account, House house);
}
