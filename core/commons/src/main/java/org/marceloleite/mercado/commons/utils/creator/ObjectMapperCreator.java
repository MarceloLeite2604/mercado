package org.marceloleite.mercado.commons.utils.creator;

import java.text.SimpleDateFormat;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperCreator {

	private ObjectMapperCreator() {
	}

	public static ObjectMapper create() {
		ObjectMapper objectMapper = new ObjectMapper();
		configure(objectMapper);
		return objectMapper;
	}

	private static ObjectMapper configure(ObjectMapper objectMapper) {
		objectMapper.registerModule(SimpleModuleCreator.create());
		objectMapper.setDateFormat(new SimpleDateFormat(ZonedDateTimeUtils.DATE_FORMAT));
		return objectMapper;
	}
}
