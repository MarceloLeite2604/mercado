package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.interfaces.Formatter;

public class StringToLocalDateTimeFormatter implements Formatter<String, LocalDateTime> {

	@Override
	public LocalDateTime format(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LocalDateTimeToString.DATE_FORMAT);
		return  LocalDateTime.parse(string, dateTimeFormatter);
	}

}
