package org.marceloleite.mercado.commons.converter;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public final class OldEpochSecondsToZonedDateTimeConveter {

	private OldEpochSecondsToZonedDateTimeConveter() {
	}

	public Long convertToEpochSeconds(ZonedDateTime zonedDateTime) {
		return zonedDateTime.toEpochSecond();
	}

	public ZonedDateTime convertToZonedDateTime(Long epochTime) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZonedDateTimeUtils.DEFAULT_ZONE_ID);
	}
}
