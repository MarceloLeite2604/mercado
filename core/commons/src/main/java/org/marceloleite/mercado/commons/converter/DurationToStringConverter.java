package org.marceloleite.mercado.commons.converter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DurationToStringConverter implements Converter<Duration, String> {

	private static final String NANO_UNIT = "nanosecond";

	private static final String SECOND_UNIT = "second";

	private static final String MINUTE_UNIT = "minute";
	
	private static final String DAY_UNIT = "day";

	private static final String HOUR_UNIT = "hour";

	private static final long NANOS_IN_A_SECOND = 1000000000l;

	private static final long SECONDS_IN_A_MINUTE = 60l;

	private static final long MINUTES_IN_AN_HOUR = 60l;

	private static final long HOUR_IN_A_DAY = 24l;

	private List<String> strings;

	@Override
	public String convertTo(Duration duration) {
		long total;
		strings = new ArrayList<>();

		try {
			total = duration.toNanos();
			total = calculateValue(total, NANOS_IN_A_SECOND, NANO_UNIT);
			calculateValue(total, SECONDS_IN_A_MINUTE, SECOND_UNIT);
		} catch (ArithmeticException exception) {

		}

		total = duration.toMinutes();
		total = calculateValue(total, MINUTES_IN_AN_HOUR, MINUTE_UNIT);
		total = calculateValue(total, HOUR_IN_A_DAY, HOUR_UNIT);
		elaborateUnitText(total, DAY_UNIT);
		
		return elaborateText();

	}

	private long calculateValue(long total, long division, String unit) {
		long remainder = total % (division);

		elaborateUnitText(remainder, unit);
		
		return (total - remainder) / division;
	}

	private void elaborateUnitText(long value, String unit) {
		if (value > 0) {
			strings.add(new String(value + " " + unit + (value > 1 ? "s" : "")));
		}
	}

	private String elaborateText() {
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

	@Override
	public Duration convertFrom(String object) {
		throw new UnsupportedOperationException();
	}

}
