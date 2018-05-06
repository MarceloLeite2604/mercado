package org.marceloleite.mercado.commons.properties;

public interface PropertyDefinition {

	String getName();

	String getDefaultValue();

	boolean isRequired();
	
	boolean isEncrypted();
}
