package org.marceloleite.mercado.commons;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class Alarm {

	private int toleranceMinutes;
	
	private TimeInterval timeInterval;

	private boolean armed;

	public Alarm(ZonedDateTime startTime, int toleranceMinutes) {
		super();
		this.toleranceMinutes = toleranceMinutes;
		setStartTime(startTime);
	}

	public void setStartTime(ZonedDateTime startTime) {
		timeInterval = new TimeInterval(startTime, startTime.plusMinutes(toleranceMinutes));
		armed = true;
	}

	public boolean isRinging(ZonedDateTime time) {
		return (armed && isOnPeriod(time));
	}

	private boolean isOnPeriod(ZonedDateTime time) {
		return ZonedDateTimeUtils.isBetween(time, timeInterval);
	}

	public void disarm() {
		armed = false;
	}
}
