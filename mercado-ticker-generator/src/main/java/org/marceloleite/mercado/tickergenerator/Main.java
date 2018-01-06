package org.marceloleite.mercado.tickergenerator;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {
	public static void main(String[] args) {
		temporalTickersGenerator();
	}

	private static void temporalTickersGenerator() {
		try {
			new TickerGenerator().generate();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
