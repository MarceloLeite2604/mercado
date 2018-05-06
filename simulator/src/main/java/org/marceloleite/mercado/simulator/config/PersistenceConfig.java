package org.marceloleite.mercado.simulator.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.marceloleite.mercado.commons.utils.PropertiesUtils;
import org.marceloleite.mercado.simulator.property.PersistenceProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

	@Inject
	private SimulatorProperties simulatorProperties;

	private Properties properties;

	@Bean
	public LocalContainerEntityManagerFactoryBean createEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(createDataSource());
		entityManagerFactoryBean.setPackagesToScan(new String[] { "org.baeldung.persistence.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

		return entityManagerFactoryBean;
	}

	@Bean(destroyMethod = "close")
	public DataSource createDataSource() {
		Properties properties = getProperties();
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.USER));
		dataSource.setPassword(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.PASSWORD));
		dataSource.setUrl(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.URL));
		dataSource.setDriverClassName(PropertiesUtils.retrieveProperty(properties, PersistenceProperty.DRIVER));
		return dataSource;
	}

	// @Bean
	// public PlatformTransactionManager transactionManager(EntityManagerFactory
	// entityManagerFactory) {
	// JpaTransactionManager transactionManager = new JpaTransactionManager();
	// transactionManager.setEntityManagerFactory(entityManagerFactory);
	// return transactionManager;
	// }

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	private Properties getHibernateProperties() {
		return PropertiesUtils.getPropertiesStartingWith(getProperties(), "hibernate");
	}

	private Properties getProperties() {
		System.out.println("Persistence properties file: \""+simulatorProperties.getPersistencePropertiesFile()+"\".");
		if (properties == null) {
			properties = PropertiesUtils.retrieveProperties(simulatorProperties.getPersistencePropertiesFile());
		}
		return properties;
	}
}
