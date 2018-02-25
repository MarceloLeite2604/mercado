package org.marceloleite.mercado.databaseretriever.persistence.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) {
			return null;
		} else {
			return Timestamp.from(zonedDateTime.toInstant());
		}
	}

	@Override
	public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		} else {
			Instant instant = timestamp.toInstant();
			return ZonedDateTime.ofInstant(instant, ZonedDateTimeUtils.DEFAULT_ZONE_ID);
		}
	}
}
