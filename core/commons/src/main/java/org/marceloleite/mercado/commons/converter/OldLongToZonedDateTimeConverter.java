package org.marceloleite.mercado.commons.converter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.interfaces.Converter;

public class OldLongToZonedDateTimeConverter implements Converter<Long, ZonedDateTime> {

	private OldLongToZonedDateTimeConverter() {
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
