package org.marceloleite.mercado.controller.utils;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

public class TimeIntervalUtils {

	private static TimeIntervalUtils instance;

	public TimeInterval retrievePreviousMinuteInterval() {
		ZonedDateTime now = ZonedDateTimeUtils.now();
		ZonedDateTime endTime = now.minusSeconds(now.getSecond())
				.minusMinutes(1);
		endTime = endTime.minusNanos(endTime.getNano());
		ZonedDateTime startTime = endTime.minusMinutes(1);
		return new TimeInterval(startTime, endTime);
	}

	public static TimeIntervalUtils getInstance() {
		if (instance == null) {
			instance = new TimeIntervalUtils();
		}
		return instance;
	}
}
