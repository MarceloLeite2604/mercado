package org.marceloleite.mercado.commons.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.deserializer.CurrencyDeserializer;
import org.marceloleite.mercado.commons.deserializer.DurationDeserializer;
import org.marceloleite.mercado.commons.deserializer.OrderTypeDeserializer;
import org.marceloleite.mercado.commons.deserializer.ZonedDateTimeDeserializer;
import org.marceloleite.mercado.commons.serializer.CurrencySerializer;
import org.marceloleite.mercado.commons.serializer.DurationSerializer;
import org.marceloleite.mercado.commons.serializer.OrderStatusSerializer;
import org.marceloleite.mercado.commons.serializer.OrderTypeSerializer;
import org.marceloleite.mercado.commons.serializer.ZonedDateTimeSerializer;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

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
		addDefaultSerializers();
		addDefaultDeserializers();
		objectMapper.setDateFormat(new SimpleDateFormat(ZonedDateTimeUtils.DATE_FORMAT));
	}

	private void addDefaultSerializers() {
		simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
		simpleModule.addSerializer(Duration.class, new DurationSerializer());
		simpleModule.addSerializer(OrderType.class, new OrderTypeSerializer());
		simpleModule.addSerializer(OrderStatus.class, new OrderStatusSerializer());
		simpleModule.addSerializer(Currency.class, new CurrencySerializer());
	}

	private void addDefaultDeserializers() {
		simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
		simpleModule.addDeserializer(Duration.class, new DurationDeserializer());
		simpleModule.addDeserializer(OrderType.class, new OrderTypeDeserializer());
		simpleModule.addDeserializer(Currency.class, new CurrencyDeserializer());
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
		addDefaultSerializers();
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
		this.clazz = clazz;
		return (T) convertFrom(json);
	}

	@SuppressWarnings("unchecked")
	public <T> T convertFromToObject(String json, T object) {
		return (T) convertFromToObject(json, object.getClass());
	}

}
