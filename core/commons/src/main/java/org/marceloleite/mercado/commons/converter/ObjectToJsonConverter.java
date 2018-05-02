package org.marceloleite.mercado.commons.converter;

import java.io.IOException;

import org.marceloleite.mercado.commons.utils.creator.ObjectMapperCreator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectToJsonConverter {

	private static ObjectMapper objectMapper = ObjectMapperCreator.create();

	private ObjectToJsonConverter() {
	}

	public static String writeAsJson(Object object) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(object);
		} catch (JsonProcessingException exception) {
			throw new RuntimeException("Error while writing \"" + object.getClass() + "\" object as Json.", exception);
		}
	}

	public static <T> T convertToObject(String json, Class<T> objectClass) {
		try {
			return objectMapper.readValue(json, objectClass);
		} catch (IOException exception) {
			throw new RuntimeException("Error while deserializing Json to \"" + objectClass.getName() + "\" class.", exception);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T convertToObject(String json, T object) {
		return (T) convertToObject(json, object.getClass());
	}

}
