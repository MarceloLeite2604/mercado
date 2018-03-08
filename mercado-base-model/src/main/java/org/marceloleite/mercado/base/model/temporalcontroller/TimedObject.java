package org.marceloleite.mercado.base.model.temporalcontroller;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.TimeInterval;

public interface TimedObject {

	ZonedDateTime getTime();
	
	boolean isTime(TimeInterval timeInterval);
}
