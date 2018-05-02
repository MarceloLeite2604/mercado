package org.marceloleite.mercado.commons.converter;

import java.text.DecimalFormat;
import java.time.Duration;

public final class OldDurationToStringConverter {

	private static final String NUMBER_FORMAT = "#########.#########";

	private static final long NANOS_IN_A_SECOND = 1000000000l;

	private OldDurationToStringConverter() {
	}

	public static String convertToString(Duration duration) {
		double seconds = 0.0;
		seconds += (double) duration.getNano() / (double) NANOS_IN_A_SECOND;
		seconds += (double) duration.getSeconds();

		return new DecimalFormat(NUMBER_FORMAT).format(seconds);
	}

	public static Duration convertToDuration(String string) {
		double value = Double.parseDouble(string);
		long seconds = (long) Math.floor(value);
		long nanoseconds = ((long) (value - (double) seconds)) * NANOS_IN_A_SECOND;
		Duration duration = Duration.ofSeconds(seconds);
		duration.plusNanos(nanoseconds);
		return duration;
	}
}
