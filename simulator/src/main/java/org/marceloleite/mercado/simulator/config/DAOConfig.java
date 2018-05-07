package org.marceloleite.mercado.simulator.config;

import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.dao.mixed.AccountXMLDatabaseDAO;
import org.marceloleite.mercado.simulator.AccountsRetriever;
import org.marceloleite.mercado.simulator.Simulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOConfig {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

//	@Bean
//	@Named("AAA")
//	public AccountDAO createAccountDAO() {
//		LOGGER.debug("Creating AccountDAO.");
//		return new AccountXMLDatabaseDAO();
//	}
	
//	@Bean
//	public AccountsRetriever createAccountsRetriever() {
//		LOGGER.debug("Creating AccountSRetriever.");
//		return new AccountsRetriever();
//	}
	
//	@Bean
//	public AccountXMLDAO createAccountXMLDAO() {
//		LOGGER.debug("Creating AccountXMLDAO.");
//		return new AccountXMLDAO();
//	}
}
