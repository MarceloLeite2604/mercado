package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;

public class TimeInterval {

	private ZonedDateTime start;

	private ZonedDateTime end;

	private Duration duration;

	public TimeInterval(ZonedDateTime start, ZonedDateTime end) {
		super();
		this.start = start;
		this.end = end;
		this.duration = Duration.between(start, end);
	}

	public TimeInterval(ZonedDateTime start, Duration duration) {
		super();
		this.start = start;
		this.duration = duration;
		this.end = start.plus(duration);
	}

	public TimeInterval(Duration duration, ZonedDateTime end) {
		super();
		this.duration = duration;
		this.end = end;
		this.start = end.minus(duration);
	}

	public ZonedDateTime getStart() {
		return start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
	}

	public ZonedDateTime getEnd() {
		return end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter();
		return zonedDateTimeToStringConverter.convertTo(start) + " to " + zonedDateTimeToStringConverter.convertTo(end);
	}
}
