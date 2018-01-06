package org.marceloleite.mercado.commons.util.converter;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;

public class TimeDivisionControllerToStringConverter implements Converter<TimeDivisionController, String> {

	@Override
	public String convertTo(TimeDivisionController timeDivisionController) {
		TimeIntervalToStringConverter timeIntervalToStringConverter = new TimeIntervalToStringConverter();
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		TimeInterval timeInterval = new TimeInterval(timeDivisionController.getStart(),
				timeDivisionController.getEnd());
		return timeIntervalToStringConverter.convertTo(timeInterval) + " with steps of "
				+ durationToStringConverter.convertTo(timeDivisionController.getDivisionDuration());
	}

	@Override
	public TimeDivisionController convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
