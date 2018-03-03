package org.marceloleite.mercado.commons.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeUtils {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static final String DATE_FORMAT_WITH_TIMEZONE = "dd/MM/yyyy HH:mm:ss z";

	public static final String DEFAULT_TIME_ZONE = "UTC";
	
	public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE);
	
	public static ZonedDateTime now() {
		return ZonedDateTime.now(DEFAULT_ZONE_ID);
	}
}
