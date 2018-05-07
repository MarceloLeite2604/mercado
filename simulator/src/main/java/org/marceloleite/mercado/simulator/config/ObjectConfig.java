package org.marceloleite.mercado.simulator.config;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.simulator.AccountsRetriever;
import org.marceloleite.mercado.simulator.SimulationHouse;
import org.marceloleite.mercado.simulator.Simulator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectConfig {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	@Inject
	private AccountsRetriever accountsRetriever;

	@Bean
	public SimulationHouse createSimulatorHouse() {
		LOGGER.debug("Creating SimulatorHouse.");
		return SimulationHouse.builder()
				.accounts(accountsRetriever.retrieve())
				.build();
	}
}
