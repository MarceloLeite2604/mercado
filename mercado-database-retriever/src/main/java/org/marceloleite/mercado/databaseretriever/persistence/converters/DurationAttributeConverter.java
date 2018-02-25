package org.marceloleite.mercado.databaseretriever.persistence.converters;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DurationAttributeConverter implements AttributeConverter<Duration, Long> {

	@Override
	public Long convertToDatabaseColumn(Duration duration) {
		if (duration == null) {
			return null;
		} else {
			return duration.getSeconds();
		}
	}

	@Override
	public Duration convertToEntityAttribute(Long seconds) {
		if (seconds == null) {
			return null;
		} else {
			return Duration.ofSeconds(seconds);
		}
	}
}
