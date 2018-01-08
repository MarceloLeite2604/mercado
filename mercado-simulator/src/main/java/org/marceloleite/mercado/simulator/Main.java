package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		simulator();
	}

	@SuppressWarnings("unused")
	private static void timeDivisionController() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minus(Duration.ofDays(10));
		Duration divisionDuration = Duration.ofHours(10);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		timeDivisionController.geTimeIntervals().stream().forEach(System.out::println);
	}

	private static void simulator() {

		try {
			new Simulator().runSimulation();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}	
}
