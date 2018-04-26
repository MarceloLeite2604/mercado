package org.marceloleite.mercado.strategy;

public interface ObjectDefinition {

	public String getName();

	public Class<?> getObjectClass();

	public String getDefaultValue();

	public boolean isRequired();
}
