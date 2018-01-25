package org.marceloleite.mercado.commons.util.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.ZonedDateTimeSerializer;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectToJsonConverter implements Converter<Object, String> {

	private Class<?> clazz;

	private ObjectWriter objectWritter;

	private ObjectMapper objectMapper;

	private SimpleModule simpleModule;

	public ObjectToJsonConverter() {
		super();
		this.clazz = null;
		objectMapper = new ObjectMapper();
		String simpleModuleName;
		if (this.clazz != null) {
			simpleModuleName = this.clazz.getName();
		} else {
			simpleModuleName = "ObjectToJsonConverter";
		}
		simpleModule = new SimpleModule(simpleModuleName, new Version(1, 0, 0, null, null, null));
		simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
		objectMapper.setDateFormat(new SimpleDateFormat(ZonedDateTimeUtils.DATE_FORMAT));
	}

	public ObjectToJsonConverter(Class<?> clazz) {
		super();
		this.clazz = clazz;
		String simpleModuleName;
		if (this.clazz != null) {
			simpleModuleName = this.clazz.getName();
		} else {
			simpleModuleName = "ObjectToJsonConverter";
		}
		objectMapper = new ObjectMapper();
		simpleModule = new SimpleModule(simpleModuleName, new Version(1, 0, 0, null, null, null));
		simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
		objectMapper.setDateFormat(new SimpleDateFormat(ZonedDateTimeUtils.DATE_FORMAT));
	}

	@Override
	public String convertTo(Object object) {
		objectMapper.registerModule(simpleModule);
		this.objectWritter = objectMapper.writerWithDefaultPrettyPrinter();
		try {
			return objectWritter.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object convertFrom(String json) {
		objectMapper.registerModule(simpleModule);
		if (clazz == null) {
			throw new IllegalStateException("Deserialization class is not defined.");
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException exception) {
			throw new RuntimeException("Error while deserializing JSON to " + clazz.getName() + " class.", exception);
		}
	}

	public <T> void addDeserializer(Class<T> clazz, StdDeserializer<? extends T> deserializer) {
		simpleModule.addDeserializer(clazz, deserializer);
	}

	public <T> void addSerializer(Class<? extends T> clazz, StdSerializer<T> serializer) {
		simpleModule.addSerializer(clazz, serializer);
	}

	@SuppressWarnings("unchecked")
	public <T> T convertFromToObject(String json, Class<T> clazz) {
		return (T) convertFrom(json);
	}
}
