package org.marceloleite.mercado.commons.converter;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class EpochSecondsToZonedDateTimeConveter implements Converter<ZonedDateTime, Long> {

	private static EpochSecondsToZonedDateTimeConveter instance;

	private EpochSecondsToZonedDateTimeConveter() {
	}

	public static EpochSecondsToZonedDateTimeConveter getInstance() {
		if (instance == null) {
			instance = new EpochSecondsToZonedDateTimeConveter();
		}
		return instance;
	}

	@Override
	public Long convertTo(ZonedDateTime zonedDateTime) {
		return zonedDateTime.toEpochSecond();
	}

	@Override
	public ZonedDateTime convertFrom(Long epochTime) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZonedDateTimeUtils.DEFAULT_ZONE_ID);
	}
}
