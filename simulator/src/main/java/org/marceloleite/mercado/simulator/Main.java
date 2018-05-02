package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

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

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Main {

	@Inject
	private SimulatorProperties simulatorProperties;

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Transactional
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			System.out.println(simulatorProperties.getStartTime());
			// simulator();
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
	private static void simulator() {
		new Simulator().runSimulation();
	}
}
