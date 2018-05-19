package org.marceloleite.mercado.commons.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.TimeInterval;

public final class ZonedDateTimeUtils {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static final String DATE_FORMAT_WITH_TIMEZONE = "dd/MM/yyyy HH:mm:ss z";

	public static final String DEFAULT_TIME_ZONE = "UTC";

	public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_WITH_TIMEZONE = DateTimeFormatter
			.ofPattern(DATE_FORMAT_WITH_TIMEZONE);

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

	private ZonedDateTimeUtils() {
	}

	public static ZonedDateTime now() {
		return ZonedDateTime.now(DEFAULT_ZONE_ID);
	}

	public static boolean isBetween(ZonedDateTime date, ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
		return ((date.isAfter(startInclusive) || date.equals(startInclusive)) && date.isBefore(endExclusive));
	}

	public static boolean isBetween(ZonedDateTime date, TimeInterval timeInterval) {
		return isBetween(date, timeInterval.getStart(), timeInterval.getEnd());
	}

	public static ZonedDateTime convertFromEpochTime(Long epochTime) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochTime), DEFAULT_ZONE_ID);
	}

	public static String format(ZonedDateTime zonedDateTime) {
		return DATE_TIME_FORMATTER.format(zonedDateTime);
	}

	public static ZonedDateTime parse(String string) {
		return ZonedDateTime.parse(string + " " + DEFAULT_TIME_ZONE, DATE_TIME_FORMATTER_WITH_TIMEZONE);
	}

	public static Long formatAsEpochTime(ZonedDateTime zonedDateTime) {
		return zonedDateTime.toInstant()
				.getEpochSecond();
	}

	public static ZonedDateTime toSystemDefaultZoneId(ZonedDateTime zonedDateTime) {
		return zonedDateTime.toLocalDateTime()
				.atZone(ZoneId.systemDefault());
	}
}
