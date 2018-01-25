package org.marceloleite.mercado.simulator.temporalcontroller;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public interface TimedObject {

	ZonedDateTime getTime();
	
	boolean isTime(TimeInterval timeInterval);
}
