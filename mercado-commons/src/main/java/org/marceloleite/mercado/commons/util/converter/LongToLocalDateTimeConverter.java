package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

	@Override
	public LocalDateTime convertTo(Long value) {
		return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC);
	}

	@Override
	public Long convertFrom(LocalDateTime localDateTime) {
		throw new UnsupportedOperationException();
	}
}
