package org.marceloleite.mercado.simulator.temporalcontroller;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public abstract class AbstractTimedObject implements TimedObject {

	@Override
	public boolean isTime(TimeInterval timeInterval) {
		ZonedDateTime start = timeInterval.getStart();
		ZonedDateTime end = timeInterval.getEnd();
		if ( start.isBefore(getTime()) || start.isEqual(getTime())) {
			if ( end.isAfter(getTime())) {
				return true;
			}
		}
		return false; 
	}

}
