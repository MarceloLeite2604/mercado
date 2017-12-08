package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.marceloleite.mercado.commons.interfaces.Formatter;

public class LongToLocalDateTimeFormatter implements Formatter<Long, LocalDateTime> {

	@Override
	public LocalDateTime format(Long value) {
		return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC);
	}
}
