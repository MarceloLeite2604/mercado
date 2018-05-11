package org.marceloleite.mercado.commons.utils.creator;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.json.deserializer.CurrencyDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.DurationDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.OrderStatusDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.OrderTypeDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeDeserializer;
import org.marceloleite.mercado.commons.json.serializer.CurrencySerializer;
import org.marceloleite.mercado.commons.json.serializer.DurationSerializer;
import org.marceloleite.mercado.commons.json.serializer.OrderStatusSerializer;
import org.marceloleite.mercado.commons.json.serializer.OrderTypeSerializer;
import org.marceloleite.mercado.commons.json.serializer.ZonedDateTimeSerializer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class SimpleModuleCreator {

	private static final Version SIMPLE_MODULE_VERSION = new Version(1, 0, 0, null, null, null);
	private static final String SIMPLE_MODULE_NAME = "simpleModule";

	private SimpleModuleCreator() {
	}

	public static SimpleModule create() {
		SimpleModule simpleModule = new SimpleModule(SIMPLE_MODULE_NAME, SIMPLE_MODULE_VERSION);
		addDefaultSerializers(simpleModule);
		addDefaultDeserializers(simpleModule);
		return simpleModule;
	}

	private static SimpleModule addDefaultSerializers(SimpleModule simpleModule) {
		simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
		simpleModule.addSerializer(Duration.class, new DurationSerializer());
		simpleModule.addSerializer(OrderType.class, new OrderTypeSerializer());
		simpleModule.addSerializer(OrderStatus.class, new OrderStatusSerializer());
		simpleModule.addSerializer(Currency.class, new CurrencySerializer());
		return simpleModule;
	}

	private static SimpleModule addDefaultDeserializers(SimpleModule simpleModule) {
		simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
		simpleModule.addDeserializer(Duration.class, new DurationDeserializer());
		simpleModule.addDeserializer(OrderType.class, new OrderTypeDeserializer());
		simpleModule.addDeserializer(OrderStatus.class, new OrderStatusDeserializer());
		simpleModule.addDeserializer(Currency.class, new CurrencyDeserializer());
		return simpleModule;
	}
}
