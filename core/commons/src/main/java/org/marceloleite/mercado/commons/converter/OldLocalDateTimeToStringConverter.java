package org.marceloleite.mercado.commons.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.interfaces.Converter;

public class OldLocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	private OldLocalDateTimeToStringConverter() {
	}

	@Override
	public String convertTo(LocalDateTime time) {
		return DateTimeFormatter.ofPattern(DATE_FORMAT).format(time);
	}

	@Override
	public LocalDateTime convertFrom(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return LocalDateTime.parse(string, dateTimeFormatter);
	}
}