package org.marceloleite.mercado.tickergenerator;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {
	public static void main(String[] args) {
		// oldTemporalTickersGenerator();
		// checkTimeIntervals();
		temporalTickersGenerator();
	}

	private static void temporalTickersGenerator() {
		new TemporalTickersGenerator().generate();
	}

	@SuppressWarnings("unused")
	private static void checkTimeIntervals() {
		ZonedDateTime time = ZonedDateTime.of(2017, 12, 30, 01, 47, 0, 0, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		System.out.println("Has seconds? " + (time.getSecond() != 0));
		System.out.println("One minute? " + (time.getMinute()%5 != 0));
		System.out.println("Five minutes? " + (time.getMinute()%5 == 0));
		System.out.println("Ten minutes? " + (time.getMinute()%10 == 0));
		System.out.println("Fifteen minutes? " + (time.getMinute()%15 == 0));
		System.out.println("Thirty minutes? " + (time.getMinute()%30 == 0));
		boolean hasMinutes = time.getMinute() != 0;
		System.out.println("Has minutes? " + hasMinutes);
		System.out.println("One hour? " + !hasMinutes);
	}

	@SuppressWarnings("unused")
	private static void oldTemporalTickersGenerator() {
		try {
			new OldTickerGenerator().generate();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}
}
