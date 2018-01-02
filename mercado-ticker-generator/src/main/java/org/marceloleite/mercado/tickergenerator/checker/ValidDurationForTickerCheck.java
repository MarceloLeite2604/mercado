package org.marceloleite.mercado.tickergenerator.checker;

import java.time.Duration;

import org.marceloleite.mercado.commons.interfaces.Check;

public class ValidDurationForTickerCheck implements Check<Duration> {

	private static final Duration TEN_SECONDS_DURATION = Duration.ofSeconds(10l);

	private static final Duration THIRTY_SECONDS_DURATION = Duration.ofSeconds(30l);

	private static final Duration ONE_MINUTE_DURATION = Duration.ofMinutes(1l);

	private static final Duration TEN_MINUTES_DURATION = Duration.ofMinutes(10l);

	private static final Duration FIFTEEN_MINUTES_DURATION = Duration.ofMinutes(15l);

	private static final Duration THIRTY_MINUTES_DURATION = Duration.ofMinutes(30l);

	private static final Duration ONE_HOUR_DURATION = Duration.ofHours(1l);

	private static final Duration TWO_HOURS_DURATION = Duration.ofHours(2l);

	private static final Duration FOUR_HOURS_DURATION = Duration.ofHours(4l);

	private static final Duration SIX_HOURS_DURATION = Duration.ofHours(6l);

	private static final Duration EIGHT_HOURS_DURATION = Duration.ofHours(8l);

	private static final Duration TWELVE_HOURS_DURATION = Duration.ofHours(12l);

	private static final Duration ONE_DAY_DURATION = Duration.ofDays(1l);

	private static final Duration ONE_WEEK_DURATION = Duration.ofDays(7l);

	private static final Duration[] validDurations = { TEN_SECONDS_DURATION, THIRTY_SECONDS_DURATION,
			ONE_MINUTE_DURATION, TEN_MINUTES_DURATION, FIFTEEN_MINUTES_DURATION, THIRTY_MINUTES_DURATION,
			ONE_HOUR_DURATION, TWO_HOURS_DURATION, FOUR_HOURS_DURATION, SIX_HOURS_DURATION, EIGHT_HOURS_DURATION,
			TWELVE_HOURS_DURATION, ONE_DAY_DURATION, ONE_WEEK_DURATION };

	@Override
	public boolean check(Duration duration) {
		for (Duration validDuration : validDurations) {
			if (validDuration.compareTo(duration) == 0) {
				return true;
			}
		}
		return false;
	}

}
