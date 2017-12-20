package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public class TimeDivisionController {

	private LocalDateTime start;

	private LocalDateTime end;

	private Duration divisionDuration;

	private long divisions;

	private long currentDivision;

	private TimeInterval nextTimeInterval;

	public TimeDivisionController(LocalDateTime start, LocalDateTime end, Duration divisionDuration) {
		super();
		if ( end.isBefore(start) ) {
			throw new IllegalArgumentException("Start time cannot be after end time.");
		}
		this.start = start;
		this.end = end;
		this.divisionDuration = divisionDuration;
		this.divisions = calculateDivisions();
		this.currentDivision = 0;
		this.nextTimeInterval = elaborateNextTimeInterval();
	}

	public TimeDivisionController(LocalDateTime start, LocalDateTime end, Long divisions) {
		super();
		this.start = start;
		this.end = end;
		this.divisions = divisions;
		this.divisionDuration = calculateDivisionDuration();
		this.currentDivision = 0;
		this.nextTimeInterval = elaborateNextTimeInterval();
	}

	public LocalDateTime getStart() {
		return LocalDateTime.from(start);
	}

	public LocalDateTime getEnd() {
		return LocalDateTime.from(end);
	}

	public Duration getDivisionDuration() {
		return Duration.from(divisionDuration);
	}
	
	public long getDivisions() {
		return divisions;
	}

	public TimeInterval getNextTimeInterval() {
		TimeInterval result = nextTimeInterval;
		if (currentDivision < divisions) {
			nextTimeInterval = elaborateNextTimeInterval();
		}
		return result;
	}

	private TimeInterval elaborateNextTimeInterval() {
		Duration timeIntervalDuration = calculateTimeIntervalDuration();
		LocalDateTime time;
		if (nextTimeInterval != null ) {
			time = LocalDateTime.from(nextTimeInterval.getStart());
		} else {
			time = start;
		}
		return new TimeInterval(time, Duration.from(timeIntervalDuration));
	}

	private Duration calculateTimeIntervalDuration() {
		Duration timeIntervalDuration;
		if (nextTimeInterval != null) {
			timeIntervalDuration = Duration.between(nextTimeInterval.getStart(), end);
		} else {
			timeIntervalDuration = Duration.between(start, end);
		}

		if (divisionDuration.compareTo(timeIntervalDuration) < 0) {
			timeIntervalDuration = Duration.from(divisionDuration);
		}
		return timeIntervalDuration;
	}

	private long calculateDivisions() {
		Duration duration = Duration.between(start, end);
		return (long) Math.ceil((double) duration.getSeconds() / (double) divisionDuration.getSeconds());
	}

	private Duration calculateDivisionDuration() {
		Duration duration = Duration.between(start, end);
		return duration.dividedBy(divisions);
	}
}
