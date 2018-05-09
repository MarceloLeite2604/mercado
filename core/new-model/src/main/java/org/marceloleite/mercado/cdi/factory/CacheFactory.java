package org.marceloleite.mercado.cdi.factory;

import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.cache.base.Cache;
import org.marceloleite.mercado.model.TradeStartTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheFactory {
	
	private static final Logger LOGGER = LogManager.getLogger(CacheFactory.class);

	@Bean
	@Named("TradeStartTimeCache")
	public Cache<Currency, TradeStartTime> createTradeStartTimeCache(){
		LOGGER.debug("Creating trade start time cache.");
		return new Cache<>();
	}
	
	@Bean
	@Named("TimeIntervalAvaliableCache")
	public Cache<Currency, TimeInterval> createTimeIntervalAvailableByCurrencyCache() {
		LOGGER.debug("Creating time interval avaliable by currency cache.");
		return new Cache<>();
	}
}
