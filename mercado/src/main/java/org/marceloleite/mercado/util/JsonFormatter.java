package org.marceloleite.mercado.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonFormatter {

	private ObjectWriter objectWritter;

	public JsonFormatter() {
		super();
		this.objectWritter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	}

	public String format(Object object) {
		try {
			return objectWritter.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
