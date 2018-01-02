package org.marceloleite.mercado.tickergenerator;

import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

/**
 * Hello world!
 *
 */
public class Main {
	public static void main(String[] args) {
		temporalTickersGenerator();
	}

	private static void temporalTickersGenerator() {
		try {
			TemporalTickersGenerator temporalTickersGenerator = new TemporalTickersGenerator();

			temporalTickersGenerator.generate();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
