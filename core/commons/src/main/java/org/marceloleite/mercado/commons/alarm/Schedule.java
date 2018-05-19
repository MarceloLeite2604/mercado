package org.marceloleite.mercado.commons.alarm;

import java.time.ZonedDateTime;

public class Schedule {

	private Alarm alarm;

	private Runnable runnable;

	public Schedule(Alarm alarm, Runnable runnable) {
		this.alarm = alarm;
		this.runnable = runnable;
	}

	public void check(ZonedDateTime time) {
		if (alarm.isRinging(time)) {
			runnable.run();
		}
	}
}
