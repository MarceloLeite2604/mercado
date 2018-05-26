package org.marceloleite.mercado.controller.config;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.AccountsRetriever;
import org.marceloleite.mercado.controller.ControllerHouse;
import org.marceloleite.mercado.controller.properties.ControllerPropertiesRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ObjectConfig {

	private static final Logger LOGGER = LogManager.getLogger(ObjectConfig.class);
	
	private static final int THREAD_POOL_SIZE = 4; 
	
	@Inject
	private ControllerPropertiesRetriever controllerPropertiesRetriever;

	@Inject
	private AccountsRetriever accountsRetriever;

	@Bean
	public ControllerHouse createControllerHouse() {
		LOGGER.debug("Creating controller house.");
		
		return ControllerHouse.builder()
				.accounts(accountsRetriever.retrieve())
				.comissionPercentage(controllerPropertiesRetriever.retrieveTradeComission())
				.build();
	}
	
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
