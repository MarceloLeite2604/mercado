package org.marceloleite.mercado.properties;

public interface Property {

	String getName();

	void setName(String name);

	String getValue();

	void setValue(String value);

	boolean isRequired();
}
