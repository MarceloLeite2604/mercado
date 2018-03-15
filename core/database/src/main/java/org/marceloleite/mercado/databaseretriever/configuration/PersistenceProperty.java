package org.marceloleite.mercado.databaseretriever.configuration;

import org.marceloleite.mercado.commons.properties.Property;

public enum PersistenceProperty implements Property {

	JDBC_DRIVER("javax.persistence.jdbc.driver"),
	JDBC_URL("javax.persistence.jdbc.url"),
	JDBC_USER("javax.persistence.jdbc.user", true),
	JDBC_PASSWORD("javax.persistence.jdbc.password", true),
	HIBERNATE_HBM2DDL_AUTO("hibernate.hbm2ddl.auto"),
	HIBERNATE_SHOW_SQL("hibernate.show_sql", "false"),
	HIBERNATE_DIALECT("hibernate.dialect");

	private String name;

	private String value;
	
	private String defaultValue;
	
	private boolean encrypted;

	private PersistenceProperty(String name, String defaultValue, boolean encrypted) {
		this.name = name;
		this.value = null;
		this.defaultValue = defaultValue;
		this.encrypted = encrypted;
	}
	
	private PersistenceProperty(String name, boolean encrypted) {
		this(name, null, encrypted);
	}
	
	private PersistenceProperty(String name, String defaultValue) {
		this(name, defaultValue, false);
	}
	
	private PersistenceProperty(String name) {
		this(name, null, false);
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return (defaultValue == null);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void setName(String name) {
		throw new IllegalAccessError();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isEncrypted() {
		return encrypted;
	}
}
