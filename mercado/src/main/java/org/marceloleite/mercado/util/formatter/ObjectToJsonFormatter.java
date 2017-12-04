package org.marceloleite.mercado.util.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectToJsonFormatter implements Formatter<Object, String> {

	private ObjectWriter objectWritter;

	public ObjectToJsonFormatter() {
		super();
		this.objectWritter = new ObjectMapper().writerWithDefaultPrettyPrinter();
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
