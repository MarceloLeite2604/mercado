package org.marceloleite.mercado.dao.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseDAOConfiguration {

	@Bean
	public AccountDatabaseDAO accountDatabsaseDAO() {
		return new AccountDatabaseDAO();
	}
	
	@Bean
	public PropertyDatabaseDAO propertyDatabsaseDAO() {
		return new PropertyDatabaseDAO();
	}
}
