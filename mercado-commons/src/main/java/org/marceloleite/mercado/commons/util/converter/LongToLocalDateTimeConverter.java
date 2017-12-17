package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.marceloleite.mercado.commons.interfaces.Converter;

public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

	@Override
	public LocalDateTime format(Long value) {
		return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC);
	}
}
