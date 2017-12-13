package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;

public class TemporalToken<T> {
	
	private LocalDateTime time;
	
	private T object;

	public TemporalToken(LocalDateTime time, T object) {
		super();
		this.time = time;
		this.object = object;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public T getObject() {
		return object;
	}
	
	public boolean isTime(LocalDateTime time) {
		return (time.isAfter(time) || time.isEqual(time));
	}
}
