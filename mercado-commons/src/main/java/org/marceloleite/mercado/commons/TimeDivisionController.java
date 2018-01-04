package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeDivisionController {

	private LocalDateTime start;

	private LocalDateTime end;

	private Duration divisionDuration;

	private long divisions;

	// private long currentDivision;

	// private TimeInterval nextTimeInterval;
	
	private List<TimeInterval> timeIntervals;

	public TimeDivisionController(LocalDateTime start, LocalDateTime end, Duration divisionDuration) {
		super();
		if ( end.isBefore(start) ) {
			throw new IllegalArgumentException("Start time cannot be after end time.");
		}
		this.start = start;
		this.end = end;
		this.divisionDuration = divisionDuration;
		this.divisions = calculateDivisions();
		// this.currentDivision = 0;
		// this.nextTimeInterval = elaborateNextTimeInterval();
		this.timeIntervals = createTimeIntervals();
	}

	public TimeDivisionController(LocalDateTime start, LocalDateTime end, Long divisions) {
		super();
		this.start = start;
		this.end = end;
		// this.divisions = divisions;
		this.divisionDuration = calculateDivisionDuration();
		// this.currentDivision = 0;
		// this.nextTimeInterval = elaborateNextTimeInterval();
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
	
	public List<TimeInterval> geTimeIntervals() {
		return new ArrayList<>(timeIntervals);
	}
	
	/*public long getDivisions() {
		return divisions;
	}*/
	
	private List<TimeInterval> createTimeIntervals() {
		List<TimeInterval> timeIntervals = new ArrayList<>();
		TimeInterval nextTimeInterval = null;
		nextTimeInterval = elaborateNextTimeInterval(nextTimeInterval);
		timeIntervals.add(nextTimeInterval);
		while (!nextTimeInterval.getEnd().isEqual(end)) {
			nextTimeInterval = elaborateNextTimeInterval(nextTimeInterval);
			timeIntervals.add(nextTimeInterval);
		}
		return timeIntervals;
	}	

	/*public TimeInterval getNextTimeInterval() {
		TimeInterval result = nextTimeInterval;
		if (currentDivision < divisions) {
			currentDivision++;
			nextTimeInterval = elaborateNextTimeInterval();
		}
		return result;
	}*/

	private TimeInterval elaborateNextTimeInterval(TimeInterval previousTimeInterval) {
		Duration timeIntervalDuration = calculateTimeIntervalDuration(previousTimeInterval);
		LocalDateTime time;
		if (previousTimeInterval != null ) {
			time = LocalDateTime.from(previousTimeInterval.getEnd());
		} else {
			time = start;
		}
		return new TimeInterval(time, Duration.from(timeIntervalDuration));
	}

	private Duration calculateTimeIntervalDuration(TimeInterval previousTimeInterval) {
		Duration timeIntervalDuration;
		if (previousTimeInterval != null) {
			timeIntervalDuration = Duration.between(previousTimeInterval.getStart(), end);
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
