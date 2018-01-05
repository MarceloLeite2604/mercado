package org.marceloleite.mercado;

import java.time.Duration;

import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;

public class Main {

	public static void main(String[] args) {
		Duration duration = Duration.ofDays(10l);
		duration = duration.minus(Duration.ofHours(7l));
		duration = duration.minus(Duration.ofMinutes(93l));
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		System.out.println(durationToStringConverter.convertTo(duration));
	}
}
