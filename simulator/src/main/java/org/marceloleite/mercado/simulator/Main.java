package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.marceloleite.mercado.NewModelMain;
import org.marceloleite.mercado.PersistenceContextConfiguration;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.site.siteretriever.trade.TradeSiteRetriever;
import org.marceloleite.mercado.dao.xml.XMLDAOConfiguration;
import org.marceloleite.mercado.model.Trade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "org.marceloleite.mercado", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = PersistenceContextConfiguration.class),
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewModelMain.class),
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = XMLDAOConfiguration.class)})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@DependsOn("MercadoApplicationContextAware")
public class Main {

	@Inject
	private Simulator simulator;
	
	@Inject
	private TradeSiteRetriever tradeSiteRetriever;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			simulator();
			// testTradeSiteRetriever();
		};
	}

	@SuppressWarnings("unused")
	private void testTradeSiteRetriever() {
		TradeSiteRetriever.setConfiguredStepDuration(Duration.ofDays(1));
		ZonedDateTime start = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		ZonedDateTime end = ZonedDateTime.of(2017, 1, 10, 0, 0, 0, 0, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		List<Trade> trades = tradeSiteRetriever.retrieve(Currency.BITCOIN, new TimeInterval(start, end));
		System.out.println(trades.size() + " trade(s) retrieved.");
	}

	@SuppressWarnings("unused")
	private void simulator() {
		simulator.run();
	}
}
