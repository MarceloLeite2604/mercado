package org.marceloleite.mercado.commons.utils;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeUtils {

	public static final String DATE_FORMAT = "HH:mm";

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	public static boolean isBetween(LocalTime timeToCheck, LocalTime startTime, Duration duration) {
		LocalTime endTime = startTime.plus(duration);
		return ((timeToCheck.equals(startTime) || timeToCheck.isAfter(startTime)) && timeToCheck.isBefore(endTime));
	}

	public static LocalTime parse(String value) {
		return LocalTime.parse(value, DATE_TIME_FORMATTER);
	}

	public static String format(LocalTime localTime) {
		return DATE_TIME_FORMATTER.format(localTime);
	}

}
