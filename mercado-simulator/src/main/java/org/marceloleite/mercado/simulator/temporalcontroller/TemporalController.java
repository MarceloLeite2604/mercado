package org.marceloleite.mercado.simulator.temporalcontroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemporalController<T extends TimedObject> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TemporalController() {
		super();
	}

	public List<T> retrieve(LocalDateTime time) {
		List<T> timedObjects = stream().filter(timedObject -> timedObject.isTime(time)).collect(Collectors.toList());

		for (TimedObject timedObject : timedObjects) {
			remove(timedObject);
		}

		return timedObjects;
	}
}
