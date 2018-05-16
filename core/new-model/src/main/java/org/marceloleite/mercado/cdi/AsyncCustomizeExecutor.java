package org.marceloleite.mercado.cdi;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncCustomizeExecutor extends AsyncConfigurerSupport {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCustomizeExecutor.class);
	
	public static int DEFAULT_CORE_POOL_SIZE = 2;
	public static String THREAD_NAME_PREFIX = "tsr";
	
	@Override
	public Executor getAsyncExecutor() {
		LOGGER.debug("Retrieving Executor.");
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
		threadPoolTaskExecutor.setMaxPoolSize(DEFAULT_CORE_POOL_SIZE);
		threadPoolTaskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
		return threadPoolTaskExecutor;
	}
}
