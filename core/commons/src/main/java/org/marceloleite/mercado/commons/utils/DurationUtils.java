package org.marceloleite.mercado.commons.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class DurationUtils {

	private static final String NANO_UNIT = "nanosecond";

	private static final String SECOND_UNIT = "second";

	private static final String MINUTE_UNIT = "minute";

	private static final String DAY_UNIT = "day";

	private static final String HOUR_UNIT = "hour";

	private static final long NANOS_IN_A_SECOND = 1000000000l;

	private static final long SECONDS_IN_A_MINUTE = 60l;

	private static final long MINUTES_IN_AN_HOUR = 60l;

	private static final long HOUR_IN_A_DAY = 24l;

	private DurationUtils() {
	}

	public static String formatAsSpelledNumber(Duration duration) {
		long total;
		List<String> strings = new ArrayList<>();

		try {
			total = duration.toNanos();
			total = calculateValue(strings, total, NANOS_IN_A_SECOND, NANO_UNIT);
			calculateValue(strings, total, SECONDS_IN_A_MINUTE, SECOND_UNIT);
		} catch (ArithmeticException exception) {

		}

		total = duration.toMinutes();
		total = calculateValue(strings, total, MINUTES_IN_AN_HOUR, MINUTE_UNIT);
		total = calculateValue(strings, total, HOUR_IN_A_DAY, HOUR_UNIT);
		elaborateUnitText(strings, total, DAY_UNIT);

		return elaborateText(strings);

	}

	private static long calculateValue(List<String> strings, long total, long division, String unit) {
		long remainder = total % (division);

		elaborateUnitText(strings, remainder, unit);

		return (total - remainder) / division;
	}

	private static void elaborateUnitText(List<String> strings, long value, String unit) {
		if (value > 0) {
			strings.add(new String(value + " " + unit + (value > 1 ? "s" : "")));
		}
	}

	private static String elaborateText(List<String> strings) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int counter = strings.size() - 1; counter >= 0; counter--) {
			String string = strings.get(counter);
			if (stringBuffer.length() > 0) {
				stringBuffer.append((counter > 0 ? ", " : " and "));
			}
			stringBuffer.append(string);
		}
		return stringBuffer.toString();
	}
	
	public static Double formatAsSeconds(Duration duration) {
		double seconds = 0.0;
		seconds += (double) duration.getNano() / (double) NANOS_IN_A_SECOND;
		seconds += (double) duration.getSeconds();

		return seconds;
	}
	
	public static Duration parseFromSeconds(Long seconds) {
		return Duration.ofSeconds(seconds);
	}
}
