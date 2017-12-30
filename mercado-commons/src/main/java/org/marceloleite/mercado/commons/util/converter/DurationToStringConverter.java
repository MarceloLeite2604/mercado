package org.marceloleite.mercado.commons.util.converter;

import java.time.Duration;

public class DurationToStringConverter implements Converter<Duration, String> {

	private static final String[] units = { "second", "minute", "hour", "day", "month", "year" };
	
	private static final long NANOS_IN_A_SECOND = 1000000000l;
	
	private static final long SECONDS_IN_A_MINUTE = 60;
	
	private static final long MINUTES_IN_AN_HOUR = 60;
	
	private static final long HOUR_IN_A_DAY = 24;

	@Override
	public String convert(Duration duration) {
		boolean nextUnit;
		long value;
		long remaining;
		/* TODO: Parei aqui. */
		try {
			remaining = duration.toNanos();
		} catch (ArithmeticException exception) {
			nextUnit = true;
		}
		
		value = remaining%(NANOS_IN_A_SECOND);
		remaining = remaining - value;

		if (duration.o)
			return null;
	}

}
