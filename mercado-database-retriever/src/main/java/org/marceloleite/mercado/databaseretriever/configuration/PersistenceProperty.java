package org.marceloleite.mercado.databaseretriever.configuration;

import org.marceloleite.mercado.properties.Property;

public enum PersistenceProperty implements Property {

	JDBC_DRIVER("javax.persistence.jdbc.driver", true),
	JDBC_URL("javax.persistence.jdbc.url", true),
	JDBC_USER("javax.persistence.jdbc.user", true),
	JDBC_PASSWORD("javax.persistence.jdbc.password", true),
	HIBERNATE_HBM2DDL_AUTO("hibernate.hbm2ddl.auto", false),
	HIBERNATE_SHOW_SQL("hibernate.show_sql", false),
	HIBERNATE_DIALECT("hibernate.dialect", false);

	private String name;

	private String value;

	private boolean required;

	private PersistenceProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
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
}
