package org.marceloleite.mercado.commons.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public class ZonedDateTimeUtils {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static final String DATE_FORMAT_WITH_TIMEZONE = "dd/MM/yyyy HH:mm:ss z";

	public static final String DEFAULT_TIME_ZONE = "UTC";

	public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE);

	public static ZonedDateTime now() {
		return ZonedDateTime.now(DEFAULT_ZONE_ID);
	}

	public static boolean isBetween(ZonedDateTime date, ZonedDateTime startInclusive, ZonedDateTime endExclusive) {
		return ((date.isAfter(startInclusive) || date.equals(startInclusive)) && date.isBefore(endExclusive));
	}
	
	public static boolean isBetween(ZonedDateTime date, TimeInterval timeInterval) {
		return isBetween(date, timeInterval.getStart(), timeInterval.getEnd());
	}
}
