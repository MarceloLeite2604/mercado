package org.marceloleite.mercado.commons.util;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.converter.Converter;

public class EpochSecondsToZonedDateTimeConveter implements Converter<ZonedDateTime, Long>  {
	
	@Override
	public Long convertTo(ZonedDateTime zonedDateTime) {
		return  zonedDateTime.toEpochSecond();
	}

	@Override
	public ZonedDateTime convertFrom(Long epochTime) {
		throw new UnsupportedOperationException();
	}
}
