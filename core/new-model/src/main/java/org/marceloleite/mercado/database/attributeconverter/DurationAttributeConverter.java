package org.marceloleite.mercado.database.attributeconverter;

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
	public Duration convertToEntityAttribute(Long value) {
		if (value == null) {
			return null;
		} else {
			return Duration.ofSeconds(value);
		}
	}
}
