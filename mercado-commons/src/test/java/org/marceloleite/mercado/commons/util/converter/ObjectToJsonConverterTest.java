package org.marceloleite.mercado.commons.util.converter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.marceloleite.mercado.commons.TestClass;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(MockitoJUnitRunner.class)
public class ObjectToJsonConverterTest {

	@InjectMocks
	private ObjectToJsonConverter objectToJsonConverter;

	@Test
	public void convertTest() throws JsonProcessingException {
		String name = "test";
		Long longValue = 2039204935l;
		Double doubleValue = 304912.9309350;
		TestClass testClass = new TestClass(name, longValue, doubleValue);

		String lineSeparator = System.getProperty("line.separator");
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{" + lineSeparator);
		stringBuffer.append("  \"name\" : \"" + name + "\"," + lineSeparator);
		stringBuffer.append("  \"longValue\" : " + longValue + "," + lineSeparator);
		stringBuffer.append("  \"doubleValue\" : " + doubleValue + lineSeparator);
		stringBuffer.append("}");
		String expected = stringBuffer.toString();
		
		String result = objectToJsonConverter.convertTo(testClass);

		Assert.assertEquals(expected, result);
	}
}
