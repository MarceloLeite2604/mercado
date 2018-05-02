package org.marceloleite.mercado.commons.utils;

import java.io.IOException;

import org.marceloleite.mercado.commons.utils.creator.ObjectMapperCreator;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectUtils {

	private static final ObjectMapper objectMapper = ObjectMapperCreator.create();

	private ObjectUtils() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T makeDeepCopy(T object) {
		try {
			return (T) objectMapper.readValue(objectMapper.writeValueAsString(object), object.getClass());
		} catch (IOException exception) {
			throw new RuntimeException("Error while creating deep copy of \"" + object.getClass() + "\" object.",
					exception);
		}
	}
}
