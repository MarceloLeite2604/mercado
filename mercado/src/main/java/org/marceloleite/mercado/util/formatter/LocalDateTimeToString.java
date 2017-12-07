package org.marceloleite.mercado.util.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToString implements Formatter<LocalDateTime, String> {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String format(LocalDateTime time) {
		return DateTimeFormatter.ofPattern(DATE_FORMAT).format(time);
	}	
}
