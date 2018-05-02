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
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZonedDateTimeUtils.DEFAULT_ZONE_ID);
	}

	public static String format(ZonedDateTime zonedDateTime) {
		return DateTimeFormatter.ofPattern(ZonedDateTimeUtils.DATE_FORMAT)
				.format(zonedDateTime);
	}

	public static ZonedDateTime parse(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ZonedDateTimeUtils.DATE_FORMAT_WITH_TIMEZONE);
		return ZonedDateTime.parse(string + " " + ZonedDateTimeUtils.DEFAULT_TIME_ZONE, dateTimeFormatter);
	}

	public static Long formatAsEpochTime(ZonedDateTime zonedDateTime) {
		return zonedDateTime.toInstant().getEpochSecond();
	}
}
