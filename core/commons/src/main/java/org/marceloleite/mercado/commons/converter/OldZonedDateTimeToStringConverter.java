package org.marceloleite.mercado.commons.converter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class OldZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {

	private OldZonedDateTimeToStringConverter() {
	}

	@Override
	public String convertTo(ZonedDateTime time) {
		return DateTimeFormatter.ofPattern(ZonedDateTimeUtils.DATE_FORMAT).format(time);
	}

	@Override
	public ZonedDateTime convertFrom(String string) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ZonedDateTimeUtils.DATE_FORMAT_WITH_TIMEZONE);
		return ZonedDateTime.parse(string + " " + ZonedDateTimeUtils.DEFAULT_TIME_ZONE, dateTimeFormatter);
	}
}
