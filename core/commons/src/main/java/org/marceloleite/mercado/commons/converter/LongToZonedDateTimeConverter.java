package org.marceloleite.mercado.commons.converter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LongToZonedDateTimeConverter implements Converter<Long, ZonedDateTime> {

	private static LongToZonedDateTimeConverter instance;

	private LongToZonedDateTimeConverter() {
	}

	public static LongToZonedDateTimeConverter getInstance() {
		if (instance == null) {
			instance = new LongToZonedDateTimeConverter();
		}
		return instance;
	}

	@Override
	public ZonedDateTime convertTo(Long value) {
		Instant instant = Instant.ofEpochSecond(value);
		return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
	}

	@Override
	public Long convertFrom(ZonedDateTime zonedDateTime) {
		throw new UnsupportedOperationException();
	}
}
