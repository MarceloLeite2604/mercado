package org.marceloleite.mercado.checker;

import org.marceloleite.mercado.ValidTemporalTickerDuration;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.interfaces.Checker;

public class ValidTimeIntervalForTemporalTickerChecker implements Checker<TimeInterval> {

	private static ValidTimeIntervalForTemporalTickerChecker instance;

	private ValidTimeIntervalForTemporalTickerChecker() {

	}

	public static ValidTimeIntervalForTemporalTickerChecker getInstance() {
		if (instance == null) {
			instance = new ValidTimeIntervalForTemporalTickerChecker();
		}
		return instance;
	}

	@Override
	public boolean check(TimeInterval timeInterval) {
		
		if (!ValidDurationForTemporalTickerChecker.getInstance().check(timeInterval.getDuration())) {
			return false;
		}
		
		if ( nonZeroSeconds(timeInterval)) {
			return false;
		}
		
		if (timeInterval.getDuration().getSeconds() == 60) {
			return true;
		}
		
		ValidTemporalTickerDuration highestValidDuration = ValidTemporalTickerDuration.ONE_MINUTE;
		
		if ( checkRemainingMinutes(timeInterval, 5))  {
			highestValidDuration = ValidTemporalTickerDuration.FIVE_MINUTES;
		}
		
		if ( checkRemainingMinutes(timeInterval, 10) ) {
			highestValidDuration = ValidTemporalTickerDuration.TEN_MINUTES;
		}
		
		if ( checkRemainingMinutes(timeInterval, 15) ) {
			highestValidDuration = ValidTemporalTickerDuration.FIFTEEN_MINUTES;
		}
		
		if ( checkRemainingMinutes(timeInterval, 20) ) {
			highestValidDuration = ValidTemporalTickerDuration.TWENTY_MINUTES;
		}
		
		if ( checkRemainingMinutes(timeInterval, 30) ) {
			highestValidDuration = ValidTemporalTickerDuration.THIRTY_MINUTES;
		}
		
		if ( checkRemainingHours(timeInterval, 1) ) {
			highestValidDuration = ValidTemporalTickerDuration.ONE_HOUR;
		}
		
		if ( checkRemainingHours(timeInterval, 2) ) {
			highestValidDuration = ValidTemporalTickerDuration.TWO_HOURS;
		}
		
		if ( checkRemainingHours(timeInterval, 4) ) {
			highestValidDuration = ValidTemporalTickerDuration.FOUR_HOURS;
		}
		
		if ( checkRemainingHours(timeInterval, 6) ) {
			highestValidDuration = ValidTemporalTickerDuration.SIX_HOURS;
		}
		
		if ( checkRemainingHours(timeInterval, 8) ) {
			highestValidDuration = ValidTemporalTickerDuration.EIGHT_HOURS;
		}
		
		if ( checkRemainingHours(timeInterval, 12) ) {
			highestValidDuration = ValidTemporalTickerDuration.TWELVE_HOURS;
		}
		
		if ( checkRemainingDays(timeInterval, 1) ) {
			highestValidDuration = ValidTemporalTickerDuration.ONE_DAY;
		}
		
		if ( checkRemainingDays(timeInterval, 7) ) {
			highestValidDuration = ValidTemporalTickerDuration.ONE_WEEK;
		}
		
		return timeInterval.getDuration().compareTo(highestValidDuration.getDuration()) <= 0;
	}

	private boolean checkRemainingDays(TimeInterval timeInterval, int divisor) {
		return (zeroHours(timeInterval) && ((timeInterval.getStart()
				.getDayOfMonth() % divisor == 0
				|| timeInterval.getEnd()
						.getDayOfMonth() % divisor == 0)));
	}

	private boolean zeroHours(TimeInterval timeInterval) {
		return (timeInterval.getStart()
				.getHour() == 0
				&& timeInterval.getEnd()
						.getHour() == 0);
	}

	private boolean checkRemainingHours(TimeInterval timeInterval, int divisor) {
		return (zeroMinutes(timeInterval) && ((timeInterval.getStart()
				.getHour() % divisor == 0
				|| timeInterval.getEnd()
						.getHour() % divisor == 0)));
	}

	private boolean zeroMinutes(TimeInterval timeInterval) {
		return (timeInterval.getStart()
				.getMinute() == 0
				&& timeInterval.getEnd()
						.getMinute() == 0);
	}

	private boolean checkRemainingMinutes(TimeInterval timeInterval, int divisor) {
		return (timeInterval.getStart()
				.getMinute() % divisor == 0
				|| timeInterval.getEnd()
						.getMinute() % divisor == 0);
	}

	private boolean nonZeroSeconds(TimeInterval timeInterval) {
		return (timeInterval.getStart()
				.getSecond() != 0
				|| timeInterval.getEnd()
						.getSecond() != 0);
	}
}
