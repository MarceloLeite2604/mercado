package org.marceloleite.mercado.controller.properties;

import org.marceloleite.mercado.commons.properties.PropertyDefinition;

public enum PersistenceProperty implements PropertyDefinition {
	DRIVER("javax.persistence.jdbc.driver", false),
	URL("javax.persistence.jdbc.url", false),
	USER("javax.persistence.jdbc.user", true),
	PASSWORD("javax.persistence.jdbc.password", true),
	HBM2DDL_AUTO("hibernate.hbm2ddl.auto", false),
	SHOW_SQL("hibernate.show_sql", false, "false"),
	DIALECT("hibernate.dialect", false);

	private String name;

	private boolean encrypted;

	private String defaultValue;

	private PersistenceProperty(String name, boolean encrypted, String defaultValue) {
		this.name = name;
		this.encrypted = encrypted;
		this.defaultValue = defaultValue;
	}
	
	private PersistenceProperty(String name, boolean encrypted) {
		this(name, encrypted, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isRequired() {
		return (defaultValue == null);
	}

	@Override
	public boolean isEncrypted() {
		return encrypted;
	}
}
