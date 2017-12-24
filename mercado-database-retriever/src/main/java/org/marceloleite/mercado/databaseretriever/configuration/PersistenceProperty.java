package org.marceloleite.mercado.databaseretriever.configuration;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public enum PersistenceProperty {

	JDBC_DRIVER("javax.persistence.jdbc.driver", true),
	JDBC_URL("javax.persistence.jdbc.url", true),
	JDBC_USER("javax.persistence.jdbc.user", true),
	JDBC_PASSWORD("javax.persistence.jdbc.password", true),
	HIBERNATE_HBM2DDL_AUTO("hibernate.hbm2ddl.auto", false),
	HIBERNATE_SHOW_SQL("hibernate.show_sql", false),
	HIBERNATE_DIALECT("hibernate.dialect", false);
	
	private String name;
	
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
	
	public Property toProperty() {
		return new StandardProperty(name, required);
	}
}
