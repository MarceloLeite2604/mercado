package org.marceloleite.mercado.commons.util;

import java.text.SimpleDateFormat;

import org.marceloleite.mercado.commons.interfaces.Formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectToJsonFormatter implements Formatter<Object, String> {

	private ObjectWriter objectWritter;

	public ObjectToJsonFormatter() {
		super();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat(LocalDateTimeToString.DATE_FORMAT));
		this.objectWritter = objectMapper.writerWithDefaultPrettyPrinter();
	}

	@Override
	public String format(Object object) {
		try {
			return objectWritter.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
