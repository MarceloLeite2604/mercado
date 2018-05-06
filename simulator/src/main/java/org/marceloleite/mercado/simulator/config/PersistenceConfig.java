package org.marceloleite.mercado.simulator.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.utils.PropertiesUtils;
import org.marceloleite.mercado.simulator.Simulator;
import org.marceloleite.mercado.simulator.property.PersistenceProperty;
import org.marceloleite.mercado.simulator.property.SimulatorProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.marceloleite.mercado", entityManagerFactoryRef = "createEntityManagerFactory")
@EntityScan("org.marceloleite.mercado.model")
public class PersistenceConfig {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	@Inject
	private SimulatorProperties simulatorProperties;

	private Properties properties;

	@Bean
	public LocalContainerEntityManagerFactoryBean createEntityManagerFactory() {
		LOGGER.debug("Creating EntityManagerFactory.");
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(createDataSource());
		entityManagerFactoryBean.setPackagesToScan(new String[] { "org.baeldung.persistence.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

		return entityManagerFactoryBean;
	}

	@Bean
	public DataSource createDataSource() {
		LOGGER.debug("Creating DataSource.");
		Properties properties = getProperties();
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.USER));
		dataSource.setPassword(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.PASSWORD));
		dataSource.setUrl(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.URL));
		dataSource.setDriverClassName(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.DRIVER));
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		LOGGER.debug("Creating TransactionManager.");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor createPersistenceExceptionTranslationPostProcessor() {
		LOGGER.debug("Creating PersistenceExceptionTranslationPostProcessor.");
		return new PersistenceExceptionTranslationPostProcessor();
	}

	private Properties getHibernateProperties() {
		LOGGER.debug("Retrieving hibernate properties.");
		return PropertiesUtils.getPropertiesStartingWith(getProperties(), "hibernate");
	}

	private Properties getProperties() {
		LOGGER.debug("Persistence properties file: \"" + simulatorProperties.getPersistencePropertiesFile() + "\".");
		if (properties == null) {
			properties = PropertiesUtils.retrieveProperties(simulatorProperties.getPersistencePropertiesFile());
		}
		return properties;
	}
}