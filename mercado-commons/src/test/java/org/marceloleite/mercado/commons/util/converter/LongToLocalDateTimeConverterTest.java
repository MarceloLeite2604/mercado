package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LongToLocalDateTimeConverterTest {

	@InjectMocks
	private OldLongToLocalDateTimeConverter longToLocalDateTimeConverter;

	@Test
	public void convertTest() {
		long epochTime = 955550412l;
		LocalDateTime expectedLocalDateTime = LocalDateTime.of(2000, 04, 12, 14, 40, 12);
		LocalDateTime resultLocalDateTime = longToLocalDateTimeConverter.convertTo(epochTime);
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		Assert.assertEquals(localDateTimeToStringConverter.convertTo(expectedLocalDateTime),
				localDateTimeToStringConverter.convertTo(resultLocalDateTime));
	}
}
