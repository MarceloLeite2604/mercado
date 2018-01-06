package org.marceloleite.mercado.commons.util.converter;

import org.marceloleite.mercado.commons.TimeInterval;

public class TimeIntervalToStringConverter implements Converter<TimeInterval, String> {

	@Override
	public String convertTo(TimeInterval timeInterval) {
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		return localDateTimeToStringConverter.convertTo(timeInterval.getStart()) + " to "
				+ localDateTimeToStringConverter.convertTo(timeInterval.getEnd());
	}

	@Override
	public TimeInterval convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
