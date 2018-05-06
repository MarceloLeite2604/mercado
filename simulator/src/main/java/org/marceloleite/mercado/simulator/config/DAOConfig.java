package org.marceloleite.mercado.simulator.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.simulator.Simulator;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DAOConfig {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

//	@Bean
//	public AccountDAO createAccountDAO() {
//		LOGGER.debug("Creating AccountDAO.");
//		return new AccountXMLDatabaseDAO();
//	}
	
//	@Bean
//	public AccountXMLDAO createAccountXMLDAO() {
//		LOGGER.debug("Creating AccountXMLDAO.");
//		return new AccountXMLDAO();
//	}
}
