package org.marceloleite.mercado.commons.utils;

import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;

public class ObjectUtils {

	@SuppressWarnings("unchecked")
	public static <T> T makeDeepCopy(T object) {
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		return (T) objectToJsonConverter.convertFromToObject(objectToJsonConverter.convertTo(object), object.getClass());
	}
}
