package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String convert(LocalDateTime time) {
		return DateTimeFormatter.ofPattern(DATE_FORMAT).format(time);
	}	
}
