package org.marceloleite.mercado.simulator.strategies;

import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;

public interface Strategy {

	void check(TimeInterval simulationTimeInterval, Account account, House house);
	
	void setCurrency(Currency currency);
	
	List<Property> getParameters();
	
	void setParameters(List<Property> parameters);
	
	List<Property> getVariables();
	
	void setVariables(List<Property> variables);
}
