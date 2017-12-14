package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemporalController<T> {

	List<TemporalToken<T>> temporalTokens;
	
	public TemporalController() {
		super();
		temporalTokens = new ArrayList<>();
	}

	public void add(LocalDateTime time, T object) {
		temporalTokens.add(new TemporalToken<>(time, object));
	}

	public List<T> retrieve(LocalDateTime time) {
		List<TemporalToken<T>> temporalTokensToConsume = temporalTokens.stream()
				.filter(temporalToken -> temporalToken.isTime(time)).collect(Collectors.toList());
		for (TemporalToken<T> temporalToken : temporalTokensToConsume) {
			temporalTokens.remove(temporalToken);
		}

		return temporalTokensToConsume.stream().map(TemporalToken::getObject).collect(Collectors.toList());
	}
}
