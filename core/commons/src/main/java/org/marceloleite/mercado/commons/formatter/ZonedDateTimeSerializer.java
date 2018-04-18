package org.marceloleite.mercado.commons.formatter;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZonedDateTimeSerializer() {
		this(null);
	}

	public ZonedDateTimeSerializer(Class<ZonedDateTime> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(ZonedDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializer)
			throws IOException {
		jsonGenerator.writeString(ZonedDateTimeToStringConverter.getInstance().convertTo(localDateTime));
	}

}
