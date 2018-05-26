package org.marceloleite.mercado.controller.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.utils.PropertiesUtils;
import org.marceloleite.mercado.controller.properties.ControllerProperties;
import org.marceloleite.mercado.controller.properties.PersistenceProperty;
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
public class PersistenceConfig {

	private static final Logger LOGGER = LogManager.getLogger(PersistenceConfig.class);
	
	private static final String[] PACKAGES_TO_SCAN = { "org.marceloleite.mercado.model" };

	@Inject
	private ControllerProperties controllerProperties;

	private Properties properties;

	@Bean
	public LocalContainerEntityManagerFactoryBean createEntityManagerFactory() {
		LOGGER.debug("Creating EntityManagerFactory.");
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(createDataSource());
		entityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);

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
		LOGGER.debug("Persistence properties file: \"" + controllerProperties.getPersistencePropertiesFile() + "\".");
		if (properties == null) {
			properties = PropertiesUtils.retrieveProperties(controllerProperties.getPersistencePropertiesFile());
		}
		return properties;
	}
}
