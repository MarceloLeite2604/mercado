package org.marceloleite.mercado.commons.alarm;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.LocalTimeUtils;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class DailyAlarm implements Alarm {

	private static final Duration DEFAULT_DURATION = Duration.ofMinutes(1);

	private LocalTime alarmTime;

	private Duration ringingDuration;

	private boolean armed;

	private ZonedDateTime rearmTime;

	private DailyAlarm() {
	}

	private DailyAlarm(Builder builder) {
		this.alarmTime = builder.alarmTime;
		this.ringingDuration = builder.ringingDuration;
		this.armed = true;
	}

	@Override
	public boolean isRinging(ZonedDateTime time) {
		armed = isArmed(time);
		boolean result = armed && isOnPeriod(time);
		if (result) {
			disarm(time);
		}
		return result;
	}

	private void disarm(ZonedDateTime time) {
		armed = false;
		rearmTime = createRearmTime(time);
	}

	private boolean isArmed(ZonedDateTime time) {
		boolean result = true;
		if (!armed) {
			createRearmTimeIfNull(time);
			result = (time.isEqual(rearmTime) || time.isAfter(rearmTime)); 
		}
		return result;
	}

	private void createRearmTimeIfNull(ZonedDateTime time) {
		if (rearmTime == null ) {
			rearmTime = createRearmTime(time);
		}
	}

	private ZonedDateTime createRearmTime(ZonedDateTime time) {
		return ZonedDateTime.of(time.toLocalDate(), alarmTime, ZonedDateTimeUtils.DEFAULT_ZONE_ID)
				.plus(ringingDuration);
	}

	private boolean isOnPeriod(ZonedDateTime time) {
		return LocalTimeUtils.isBetween(time.toLocalTime(), alarmTime, ringingDuration);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private LocalTime alarmTime;

		private Duration ringingDuration = DEFAULT_DURATION;

		public Builder ringsAt(LocalTime alarmTime) {
			this.alarmTime = alarmTime;
			return this;
		}

		public Builder keepsRingingFor(Duration ringingDuration) {
			this.ringingDuration = ringingDuration;
			return this;
		}

		public DailyAlarm build() {
			return new DailyAlarm(this);
		}
	}
}
