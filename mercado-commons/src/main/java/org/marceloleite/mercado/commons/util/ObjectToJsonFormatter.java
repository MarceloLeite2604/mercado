package org.marceloleite.mercado.commons.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.interfaces.Formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectToJsonFormatter implements Formatter<Object, String> {

	private ObjectWriter objectWritter;

	public ObjectToJsonFormatter() {
		super();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("LocalDateTimeSerializer", new Version(1, 0, 0, null, null, null));
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		objectMapper.registerModule(module);
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
