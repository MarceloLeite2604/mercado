package org.marceloleite.mercado.commons.util.converter;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class StringToLocalDateTimeConverterTest {
	
	@InjectMocks
	private StringToLocalDateTimeConverter stringToLocalDateTimeConverter; 
	
	@Test
	public void convertTest() {
		String expected = "12/04/2000 14:40:12";
		LocalDateTime localDateTime = stringToLocalDateTimeConverter.convert(expected);
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		String result = localDateTimeToStringConverter.convert(localDateTime);
		Assert.assertEquals(expected, result);
	}
}
