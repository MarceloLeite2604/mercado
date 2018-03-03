package org.marceloleite.mercado.commons.converter;

import java.time.ZonedDateTime;

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
