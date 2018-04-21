package org.marceloleite.mercado.checker;

import java.time.Duration;

import org.marceloleite.mercado.ValidTemporalTickerDuration;
import org.marceloleite.mercado.commons.interfaces.Checker;

public class ValidDurationForTemporalTickerChecker implements Checker<Duration> {

	private static ValidDurationForTemporalTickerChecker instance;

	private ValidDurationForTemporalTickerChecker() {
	}

	@Override
	public boolean check(Duration duration) {
		for (ValidTemporalTickerDuration validTemporalTickerDuration : ValidTemporalTickerDuration.values()) {
			if (validTemporalTickerDuration.getDuration()
					.compareTo(duration) == 0) {
				return true;
			}
		}
		return false;
	}

	public static ValidDurationForTemporalTickerChecker getInstance() {
		if (instance == null) {
			instance = new ValidDurationForTemporalTickerChecker();
		}
		return instance;
	}

}
