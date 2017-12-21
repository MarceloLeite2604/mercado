package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LocalDateTimeToStringConverter.DATE_FORMAT);
		return  LocalDateTime.parse(string, dateTimeFormatter);
	}

}
