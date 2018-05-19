package org.marceloleite.mercado.commons.alarm;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class SingleAlarm implements Alarm {

	public static final Duration DEFAULT_RINGING_DURATION = Duration.ofMinutes(1);

	private TimeInterval ringingTimeInterval;

	private boolean armed;

	public SingleAlarm(ZonedDateTime alarmTime, Duration ringingDuration) {
		super();
		this.ringingTimeInterval = new TimeInterval(alarmTime, ringingDuration);
		this.armed = true;
	}

	public SingleAlarm(ZonedDateTime alarmTime) {
		this(alarmTime, DEFAULT_RINGING_DURATION);
	}

	@Override
	public boolean isRinging(ZonedDateTime time) {
		return (armed && isOnPeriod(time));
	}

	private boolean isOnPeriod(ZonedDateTime time) {
		return ZonedDateTimeUtils.isBetween(time, ringingTimeInterval);
	}

	public void disarm() {
		armed = false;
	}
}
