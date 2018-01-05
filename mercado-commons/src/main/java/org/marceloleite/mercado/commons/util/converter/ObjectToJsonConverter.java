package org.marceloleite.mercado.commons.util.converter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.util.LocalDateTimeSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectToJsonConverter implements Converter<Object, String> {

	private ObjectWriter objectWritter;

	public ObjectToJsonConverter() {
		super();
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("LocalDateTimeSerializer", new Version(1, 0, 0, null, null, null));
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		objectMapper.registerModule(module);
		objectMapper.setDateFormat(new SimpleDateFormat(LocalDateTimeToStringConverter.DATE_FORMAT));

		this.objectWritter = objectMapper.writerWithDefaultPrettyPrinter();
	}

	@Override
	public String convertTo(Object object) {
		try {
			return objectWritter.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object convertFrom(String object) {
		throw new UnsupportedOperationException();
	}
}
