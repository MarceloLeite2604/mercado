package org.marceloleite.mercado.converter.json;

import java.io.IOException;

import org.marceloleite.mercado.commons.util.converter.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToClassObjectConverter<T> implements Converter<String, T> {

	private Class<?> clazz;

	public JsonToClassObjectConverter(Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T convertTo(String jsonString) {
		try {
			Object object = new ObjectMapper().readValue(jsonString, clazz);
			return (T) object;

		} catch (IOException exception) {
			throw new RuntimeException("Error while converting Json to class \"" + clazz.getName() + "\".", exception);
		}
	}

	@Override
	public String convertFrom(T object) {
		throw new UnsupportedOperationException();
	}

}
