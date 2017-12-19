package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeToStringConverterTest {
	
	@InjectMocks
	private LocalDateTimeToStringConverter localDateTimeToStringConverter; 
	
	@Test
	public void convertTest() {
		LocalDateTime localDateTime = LocalDateTime.of(2000, 04, 12, 14, 40, 12);
		String result = localDateTimeToStringConverter.convert(localDateTime);
		String expected = "12/04/2000 14:40:12";
		Assert.assertEquals(expected, result);
	}
}
