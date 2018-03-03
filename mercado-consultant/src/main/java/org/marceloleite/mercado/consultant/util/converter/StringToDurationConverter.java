package org.marceloleite.mercado.consultant.util.converter;

import java.time.Duration;

import org.marceloleite.mercado.commons.converter.Converter;

public class StringToDurationConverter implements Converter<String, Duration> {

	@Override
	public Duration convertTo(String stringSeconds) {
		long seconds = Long.parseLong(stringSeconds);
		return Duration.ofSeconds(seconds);
	}

	@Override
	public String convertFrom(Duration object) {
		throw new UnsupportedOperationException();
	}

}
