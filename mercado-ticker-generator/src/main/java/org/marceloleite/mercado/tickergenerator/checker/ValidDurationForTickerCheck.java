package org.marceloleite.mercado.tickergenerator.checker;

import java.time.Duration;

import org.marceloleite.mercado.commons.interfaces.Check;

public class ValidDurationForTickerCheck implements Check<Duration> {

	@Override
	public boolean check(Duration object) {
		
		return false;
	}

}
