package org.marceloleite.mercado.strategy;

public interface VariableDefinition {

	public String getName();
	
	public Class<?> getObjectClass();
	
	public boolean isRequired();
}
