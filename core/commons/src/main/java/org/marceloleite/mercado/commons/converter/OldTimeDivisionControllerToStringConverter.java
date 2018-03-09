package org.marceloleite.mercado.commons.converter;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;

public class OldTimeDivisionControllerToStringConverter implements Converter<TimeDivisionController, String> {

	@Override
	public String convertTo(TimeDivisionController timeDivisionController) {
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		TimeInterval timeInterval = new TimeInterval(timeDivisionController.getStart(),
				timeDivisionController.getEnd());
		return timeInterval + " with steps of "
				+ durationToStringConverter.convertTo(timeDivisionController.getDivisionDuration());
	}

	@Override
	public TimeDivisionController convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
