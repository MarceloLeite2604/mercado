package org.marceloleite.mercado.simulator.temporalcontroller;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public interface TimedObject {

	LocalDateTime getTime();
	
	boolean isTime(TimeInterval timeInterval);
}
