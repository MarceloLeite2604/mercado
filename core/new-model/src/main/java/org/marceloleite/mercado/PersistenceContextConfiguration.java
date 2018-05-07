package org.marceloleite.mercado;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.marceloleite.mercado.commons.utils.EncryptUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = { "org.marceloleite.mercado" })
public class PersistenceContextConfiguration {

	@Bean
	DataSource dataSource(Environment environment) {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(environment.getRequiredProperty("javax.persistence.jdbc.driver"));
		dataSourceConfig.setJdbcUrl(environment.getRequiredProperty("javax.persistence.jdbc.url"));
		dataSourceConfig.setUsername(EncryptUtils.decrypt(environment.getRequiredProperty("javax.persistence.jdbc.user")));
		dataSourceConfig.setPassword(EncryptUtils.decrypt(environment.getRequiredProperty("javax.persistence.jdbc.password")));

		return new HikariDataSource(dataSourceConfig);
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment environment) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("org.marceloleite.mercado");

		Properties jpaProperties = new Properties();

		jpaProperties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		jpaProperties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		/*jpaProperties.put("hibernate.ejb.naming_strategy",
				environment.getRequiredProperty("hibernate.ejb.naming_strategy"));*/
		jpaProperties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		jpaProperties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));

		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}
