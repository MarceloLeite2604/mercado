package org.marceloleite.mercado.commons.properties;

public interface Property {

	String getName();

	void setName(String name);

	String getValue();
	
	String getDefaultValue();

	void setValue(String value);

	boolean isRequired();
	
	boolean isEncrypted();
}
