package org.marceloleite.mercado.base.model.temporalcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.TimeInterval;

public class TemporalController<T extends TimedObject> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TemporalController() {
		super();
	}

	public TemporalController(TemporalController<T> temporalController) {
		super(temporalController);
	}

	public List<T> retrieve(TimeInterval timeInterval) {
		List<T> timedObjects = stream().filter(timedObject -> timedObject.isTime(timeInterval))
				.collect(Collectors.toList());

		for (TimedObject timedObject : timedObjects) {
			remove(timedObject);
		}

		return timedObjects;
	}
}
