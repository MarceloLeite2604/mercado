package org.marceloleite.mercado.commons;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.utils.DurationUtils;

public class TimeDivisionController {

	private ZonedDateTime start;

	private ZonedDateTime end;

	private Duration divisionDuration;

	private long totalDivisions;

	private List<TimeInterval> timeIntervals;

	public TimeDivisionController(ZonedDateTime start, ZonedDateTime end, Duration divisionDuration) {
		super();
		if (end.isBefore(start) || end.isEqual(start)) {
			throw new IllegalArgumentException("Start time cannot be before or equal end time.");
		}
		this.start = start;
		this.end = end;
		this.divisionDuration = divisionDuration;
		this.totalDivisions = calculateTotalDivisions();
		this.timeIntervals = createTimeIntervals();
	}

	public TimeDivisionController(TimeInterval timeInterval, Duration divisionDuration) {
		this(timeInterval.getStart(), timeInterval.getEnd(), divisionDuration);
	}

	public TimeDivisionController(ZonedDateTime start, ZonedDateTime end, Long divisions) {
		super();
		this.start = start;
		this.end = end;
		this.divisionDuration = calculateDivisionDuration();
	}

	public ZonedDateTime getStart() {
		return ZonedDateTime.from(start);
	}

	public ZonedDateTime getEnd() {
		return ZonedDateTime.from(end);
	}

	public Duration getDivisionDuration() {
		return Duration.from(divisionDuration);
	}

	public List<TimeInterval> getTimeIntervals() {
		return new ArrayList<>(timeIntervals);
	}

	public long getTotalDivisions() {
		return totalDivisions;
	}

	private List<TimeInterval> createTimeIntervals() {
		List<TimeInterval> timeIntervals = new ArrayList<>();
		TimeInterval nextTimeInterval = null;
		nextTimeInterval = elaborateNextTimeInterval(nextTimeInterval);
		timeIntervals.add(nextTimeInterval);
		while (!nextTimeInterval.getEnd()
				.isEqual(end)) {
			nextTimeInterval = elaborateNextTimeInterval(nextTimeInterval);
			timeIntervals.add(nextTimeInterval);
		}
		return timeIntervals;
	}

	/*
	 * public TimeInterval getNextTimeInterval() { TimeInterval result =
	 * nextTimeInterval; if (currentDivision < divisions) { currentDivision++;
	 * nextTimeInterval = elaborateNextTimeInterval(); } return result; }
	 */

	private TimeInterval elaborateNextTimeInterval(TimeInterval previousTimeInterval) {
		Duration timeIntervalDuration = calculateTimeIntervalDuration(previousTimeInterval);
		ZonedDateTime time;
		if (previousTimeInterval != null) {
			time = ZonedDateTime.from(previousTimeInterval.getEnd());
		} else {
			time = start;
		}
		return new TimeInterval(time, Duration.from(timeIntervalDuration));
	}

	private Duration calculateTimeIntervalDuration(TimeInterval previousTimeInterval) {
		Duration timeIntervalDuration;
		if (previousTimeInterval != null) {
			timeIntervalDuration = Duration.between(previousTimeInterval.getEnd(), end);
		} else {
			timeIntervalDuration = Duration.between(start, end);
		}

		if (divisionDuration.compareTo(timeIntervalDuration) < 0) {
			timeIntervalDuration = Duration.from(divisionDuration);
		}
		return timeIntervalDuration;
	}

	private long calculateTotalDivisions() {
		Duration duration = Duration.between(start, end);
		return (long) Math.ceil((double) duration.getSeconds() / (double) divisionDuration.getSeconds());
	}

	private Duration calculateDivisionDuration() {
		Duration duration = Duration.between(start, end);
		return duration.dividedBy(totalDivisions);
	}

	public TimeInterval getTotalTimeInterval() {
		return new TimeInterval(start, end);
	}

	@Override
	public String toString() {
		TimeInterval timeInterval = new TimeInterval(start, end);
		return timeInterval + " with steps of " + DurationUtils.formatAsSpelledNumber(divisionDuration);
	}
}
