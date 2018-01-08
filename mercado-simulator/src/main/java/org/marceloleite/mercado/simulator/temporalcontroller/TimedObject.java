package org.marceloleite.mercado.simulator.temporalcontroller;

import java.time.LocalDateTime;

public interface TimedObject {

	LocalDateTime getTime();
	
	boolean isTime(LocalDateTime time);
}
