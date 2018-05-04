package org.marceloleite.mercado.simulator;

import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.marceloleite.mercado.commons.utils.PropertiesUtils;
import org.marceloleite.mercado.simulator.property.PersistenceProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.util.StringUtils;

public class PersistenceConfig {

	@Inject
	private SimulatorProperties simulatorProperties;

	private Properties properties;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		// sessionFactory.setPackagesToScan(new String[] {
		// "org.baeldung.spring.persistence.model" });
		sessionFactory.setHibernateProperties(getHibernateProperties());

		return sessionFactory;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		Properties properties = getProperties();
		return DataSourceBuilder.create()
				.username(retrieveProperty(properties, PersistenceProperty.USER))
				.password(retrieveProperty(properties, PersistenceProperty.PASSWORD))
				.url(retrieveProperty(properties, PersistenceProperty.URL))
				.driverClassName(retrieveProperty(properties, PersistenceProperty.DRIVER))
				.build();
	}

	private String retrieveProperty(Properties properties, PersistenceProperty persistenceProperty) {
		String value = properties.getProperty(persistenceProperty.getName(), persistenceProperty.getDefaultValue());
		if (StringUtils.isEmpty(value) && persistenceProperty.isRequired()) {
			throw new RuntimeException(
					"Could not find persistence property \"" + persistenceProperty.getName() + "\".");
		}
		if (persistenceProperty.isEncrypted()) {
			value = EncryptUtils.decrypt(value);
		}
		return value;
	}

	private Properties getHibernateProperties() {
		return PropertiesUtils.getPropertiesStartingWith(getProperties(), "hibernate");
	}

	private Properties getProperties() {
		if (properties == null) {
			properties = PropertiesUtils.retrieveProperties(simulatorProperties.getPersistenceFileName());
		}
		return properties;
	}
}
