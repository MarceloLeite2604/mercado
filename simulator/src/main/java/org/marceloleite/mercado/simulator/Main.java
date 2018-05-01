package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class Main {

	public static void main(String[] args) {
		simulator();
		// circularArrayList();
	}	

	@SuppressWarnings("unused")
	private static void timeDivisionController() {
		ZonedDateTime end = ZonedDateTimeUtils.now();
		ZonedDateTime start = end.minus(Duration.ofDays(10));
		Duration divisionDuration = Duration.ofHours(10);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		timeDivisionController.geTimeIntervals().stream().forEach(System.out::println);
	}

	@SuppressWarnings("unused")
	private static void simulator() {
		new Simulator().runSimulation();
	}	
}
