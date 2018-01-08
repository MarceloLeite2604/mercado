package org.marceloleite.mercado.commons.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.marceloleite.mercado.commons.util.converter.Converter;

public class EpochSecondsToLocalDateTimeConveter implements Converter<LocalDateTime, Long>  {
	
	private static final ZoneId ZONE_ID = ZoneId.of("UTC");
	
	@Override
	public Long convertTo(LocalDateTime localDateTime) {
		return  localDateTime.atZone(ZONE_ID).toEpochSecond();
	}

	@Override
	public LocalDateTime convertFrom(Long epochTime) {
		throw new UnsupportedOperationException();
	}
}
