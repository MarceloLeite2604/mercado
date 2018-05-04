package org.marceloleite.mercado.commons.properties;

public interface Property {

	String getName();

	String getDefaultValue();

	boolean isRequired();
	
	boolean isEncrypted();
}
