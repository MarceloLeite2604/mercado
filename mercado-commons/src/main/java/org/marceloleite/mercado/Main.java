package org.marceloleite.mercado;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;
import org.marceloleite.mercado.commons.util.converter.TimeIntervalToStringConverter;

public class Main {

	public static void main(String[] args) {
		daylightSavingTime();
		// durationToStringConverter();
	}

	private static void daylightSavingTime() {
		LocalDateTime start = LocalDateTime.of(2017, 10, 14, 23, 00);
		LocalDateTime end = LocalDateTime.of(2017, 10, 15, 03, 00);
		TimeDivisionController timeDivisionController = new TimeDivisionController(new TimeInterval(start, end), Duration.ofHours(1l));
		//System.out.println(new LocalDateTimeToStringConverter().convertTo(end));
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			System.out.println(new TimeIntervalToStringConverter().convertTo(timeInterval));	
		}
		
	}

	private static void durationToStringConverter() {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		System.out.println(durationToStringConverter.convertTo(duration));
	}
}
