package org.marceloleite.mercado.simulator;
import java.time.LocalDateTime;

import org.marceloleite.mercado.simulator.TimedObject;

public abstract class AbstractTimedObject implements TimedObject {

	@Override
	public boolean isTime(LocalDateTime time) {
		return time.isBefore(getTime()) || time.isEqual(getTime());
	}

}
