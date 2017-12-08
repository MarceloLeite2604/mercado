package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.interfaces.Formatter;

public class LocalDateTimeToString implements Formatter<LocalDateTime, String> {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String format(LocalDateTime time) {
		return DateTimeFormatter.ofPattern(DATE_FORMAT).format(time);
	}	
}
