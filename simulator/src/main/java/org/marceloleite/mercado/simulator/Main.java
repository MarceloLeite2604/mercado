package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.marceloleite.mercado.NewModelMain;
import org.marceloleite.mercado.PersistenceContextConfiguration;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
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
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "org.marceloleite.mercado", excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = PersistenceContextConfiguration.class),
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = NewModelMain.class) })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Main {

	@Inject
	private Simulator simulator;

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			simulator();
			// circularArrayList();
		};
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
