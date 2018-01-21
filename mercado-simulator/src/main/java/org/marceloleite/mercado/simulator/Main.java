package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.simulator.strategy.second.CircularArray;

public class Main {

	public static void main(String[] args) {
		simulator();
		// circularArrayList();
	}

	private static void circularArrayList() {
		CircularArray<String> circularArrayList = new CircularArray<String>(4);
		circularArrayList.add("First");
		circularArrayList.add("Second");
		circularArrayList.add("Third");
		circularArrayList.add("Fourth");
		circularArrayList.add("Fifth");
		circularArrayList.add("Sixth");
		circularArrayList.add("Seventh");
		circularArrayList.add("Eighth");
		System.out.println(circularArrayList.get(0));
		System.out.println(circularArrayList.get(1));
		System.out.println(circularArrayList.get(2));
		System.out.println(circularArrayList.get(3));
	}

	@SuppressWarnings("unused")
	private static void timeDivisionController() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minus(Duration.ofDays(10));
		Duration divisionDuration = Duration.ofHours(10);
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, divisionDuration);
		timeDivisionController.geTimeIntervals().stream().forEach(System.out::println);
	}

	@SuppressWarnings("unused")
	private static void simulator() {

		try {
			new Simulator().runSimulation();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}	
}
