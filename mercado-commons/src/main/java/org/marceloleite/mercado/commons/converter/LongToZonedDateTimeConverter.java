package org.marceloleite.mercado.commons.converter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LongToZonedDateTimeConverter implements Converter<Long, ZonedDateTime> {

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
