package org.marceloleite.mercado.simulator.temporalcontroller;
import java.time.LocalDateTime;

public abstract class AbstractTimedObject implements TimedObject {

	@Override
	public boolean isTime(LocalDateTime time) {
		return time.isBefore(getTime()) || time.isEqual(getTime());
	}

}
