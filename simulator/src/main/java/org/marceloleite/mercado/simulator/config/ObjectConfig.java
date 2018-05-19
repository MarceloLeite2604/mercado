package org.marceloleite.mercado.simulator.config;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.simulator.AccountsRetriever;
import org.marceloleite.mercado.simulator.SimulationHouse;
import org.marceloleite.mercado.simulator.Simulator;
import org.marceloleite.mercado.simulator.SimulatorHouseThread;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ObjectConfig {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);
	
	private static final int THREAD_POOL_SIZE = 4; 
	
	@Inject
	private SimulatorPropertiesRetriever simulatorPropertiesRetriever;

	@Inject
	private AccountsRetriever accountsRetriever;

	@Bean
	public SimulationHouse createSimulatorHouse() {
		LOGGER.debug("Creating simulator house.");
		
		return SimulationHouse.builder()
				.accounts(accountsRetriever.retrieve())
				.comissionPercentage(simulatorPropertiesRetriever.retrieveTradeComission())
				.build();
	}
	
	@Bean
	public SimulatorHouseThread createSimulationHouseThread(SimulationHouse simulationHouse) {
		LOGGER.debug("Creating simulator house thread.");
		return new SimulatorHouseThread(simulationHouse);
	}
	
//	@Bean
//	public ExecutorService createExecutorSerivice() {
//		LOGGER.debug("Creating executor service.");
//	    return Executors.newFixedThreadPool(simulatorPropertiesRetriever.retrieveThreadPoolSize());
//	}
	
	@Bean
	public TaskExecutor createThreadPoolTaskExecutor() {
		LOGGER.debug("Creating thread pool task executor.");
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(THREAD_POOL_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(THREAD_POOL_SIZE);
        threadPoolTaskExecutor.setThreadNamePrefix("simulator");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
	}
}
