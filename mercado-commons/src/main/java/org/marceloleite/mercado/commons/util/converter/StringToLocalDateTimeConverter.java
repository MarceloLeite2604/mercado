package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.interfaces.Converter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LocalDateTimeToStringConverter.DATE_FORMAT);
		return  LocalDateTime.parse(string, dateTimeFormatter);
	}

}
