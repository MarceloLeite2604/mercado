package org.marceloleite.mercado.strategy;

public interface ParameterDefinition {

	public String getName();
	
	public Class<?> getObjectClass();
	
	public boolean isRequired();
}
