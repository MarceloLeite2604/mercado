package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemporalController<T> extends ArrayList<TemporalToken<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TemporalController() {
		super();
	}

	public void add(LocalDateTime time, T object) {
		add(new TemporalToken<>(time, object));
	}

	public List<T> retrieve(LocalDateTime time) {
		List<TemporalToken<T>> temporalTokensToConsume = stream().filter(temporalToken -> temporalToken.isTime(time))
				.collect(Collectors.toList());
		for (TemporalToken<T> temporalToken : temporalTokensToConsume) {
			remove(temporalToken);
		}

		return temporalTokensToConsume.stream().map(TemporalToken::getObject).collect(Collectors.toList());
	}
}
