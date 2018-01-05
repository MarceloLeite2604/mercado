package org.marceloleite.mercado.simulator.conversor;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		// timeDivisionController();
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

		Simulator simulator = new Simulator();

		try {
			simulator.runSimulation();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}

	/*
	 * private static void calculateRealValueOf(CurrencyAmount currencyAmount) {
	 * double currentPrice; if (currencyAmount.getCurrency() != Currency.REAL) {
	 * currentPrice = new PriceRetriever().retrieve(currencyAmount.getCurrency(),
	 * LocalDateTime.now()); } else { currentPrice = 1; }
	 * 
	 * CurrencyAmount realCurrencyAmount = new CurrencyAmount(Currency.REAL,
	 * currencyAmount.getAmount() * currentPrice);
	 * System.out.println(realCurrencyAmount); }
	 */

}
