package org.marceloleite.mercado;

import java.time.Duration;

public enum ValidTemporalTickerDuration {
	ONE_MINUTE(Duration.ofMinutes(1)),
	FIVE_MINUTES(Duration.ofMinutes(5)),
	TEN_MINUTES(Duration.ofMinutes(10)),
	FIFTEEN_MINUTES(Duration.ofMinutes(15)),
	TWENTY_MINUTES(Duration.ofMinutes(20)),
	THIRTY_MINUTES(Duration.ofMinutes(30)),
	ONE_HOUR(Duration.ofHours(1)),
	TWO_HOURS(Duration.ofHours(2)),
	FOUR_HOURS(Duration.ofHours(4)),
	SIX_HOURS(Duration.ofHours(6)),
	EIGHT_HOURS(Duration.ofHours(8)),
	TWELVE_HOURS(Duration.ofHours(12)),
	ONE_DAY(Duration.ofDays(1)),
	ONE_WEEK(Duration.ofDays(7));

	private Duration duration;

	private ValidTemporalTickerDuration(Duration duration) {
		this.duration = duration;
		;
	}

	public Duration getDuration() {
		return duration;
	}
}
