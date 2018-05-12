package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.marceloleite.mercado.NewModelMain;
import org.marceloleite.mercado.PersistenceContextConfiguration;
import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.dao.database.repository.TradeRepository;
import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.dao.xml.XMLDAOConfiguration;
import org.marceloleite.mercado.model.Account;
import org.marceloleite.mercado.model.Strategy;
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

	@SuppressWarnings("unused")
	@Inject
	private MercadoApplicationContextAware mercadoApplicationContextAware;
	
	@Inject
	private Simulator simulator;
	
	@Inject
	private TradeRepository tradeRepository;
	
	@Inject
	@Named("AccountXMLDAO")
	private AccountDAO accountDAO;

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			simulator();
			// circularArrayList();
			// createFakeAccount();
		};
	}

	private void createFakeAccount() {
		Account account = new Account();
		account.setOwner("fake");
		Strategy strategy = new Strategy();
		strategy.setClassName("org.marceloleite.mercado");
		strategy.setCurrency(Currency.BGOLD);
		account.setStrategies(Arrays.asList(strategy));
		accountDAO.save(account);
	}

	@SuppressWarnings("unused")
	private static void timeDivisionController() {
		ZonedDateTime end = ZonedDateTimeUtils.now();
		ZonedDateTime start = end.minus(Duration.ofDays(10));
		Duration divisionDuration = Duration.ofHours(10);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		timeDivisionController.geTimeIntervals()
				.stream()
				.forEach(System.out::println);
	}

	@SuppressWarnings("unused")
	private void simulator() {
		simulator.runSimulation();
	}
}
