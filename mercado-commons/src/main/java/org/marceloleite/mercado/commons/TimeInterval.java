package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeInterval {

	private LocalDateTime start;

	private LocalDateTime end;

	private Duration duration;

	public TimeInterval(LocalDateTime start, LocalDateTime end) {
		super();
		this.start = start;
		this.end = end;
		this.duration = Duration.between(start, end);
	}

	public TimeInterval(LocalDateTime start, Duration duration) {
		super();
		this.start = start;
		this.duration = duration;
		this.end = start.plus(duration);
	}

	public TimeInterval(Duration duration, LocalDateTime end) {
		super();
		this.duration = duration;
		this.end = end;
		this.start = end.minus(duration);
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
}
