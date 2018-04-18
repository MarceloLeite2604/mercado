package org.marceloleite.mercado.commons.converter;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

@Component
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
		throw new UnsupportedOperationException();
	}
}
