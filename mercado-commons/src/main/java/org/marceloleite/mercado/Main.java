package org.marceloleite.mercado;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;

public class Main {

	public static void main(String[] args) {
		daylightSavingTime();
		// durationToStringConverter();
	}

	private static void daylightSavingTime() {
		ZonedDateTime start = ZonedDateTime.of(2017, 10, 14, 23, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime end = ZonedDateTime.of(2017, 10, 15, 03, 0, 0, 0, ZoneOffset.UTC);
		TimeDivisionController timeDivisionController = new TimeDivisionController(new TimeInterval(start, end), Duration.ofHours(1l));
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			System.out.println(timeInterval);	
		}
		
	}

	@SuppressWarnings("unused")
	private static void durationToStringConverter() {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		System.out.println(durationToStringConverter.convertTo(duration));
	}
}
