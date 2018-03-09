package org.marceloleite.mercado.base.model;

import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.ClassData;

public interface Strategy {

	void check(TimeInterval timeInterval, Account account, House house);
	
	void setCurrency(Currency currency);
	
	List<Property> getParameters();
	
	void setParameters(List<Property> parameters);
	
	List<Property> getVariables();
	
	void setVariables(List<Property> variables);
	
	ClassData retrieveData();
}
