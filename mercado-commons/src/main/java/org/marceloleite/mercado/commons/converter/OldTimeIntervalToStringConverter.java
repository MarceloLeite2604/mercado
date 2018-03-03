package org.marceloleite.mercado.commons.converter;

import org.marceloleite.mercado.commons.TimeInterval;

public class OldTimeIntervalToStringConverter implements Converter<TimeInterval, String> {

	@Override
	public String convertTo(TimeInterval timeInterval) {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		return zonedDateTimeToStringConverter.convertTo(timeInterval.getStart()) + " to "
				+ zonedDateTimeToStringConverter.convertTo(timeInterval.getEnd());
	}

	@Override
	public TimeInterval convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
