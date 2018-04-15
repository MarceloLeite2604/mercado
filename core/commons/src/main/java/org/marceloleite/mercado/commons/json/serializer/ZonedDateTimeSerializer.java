package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {
	
	private static final ZonedDateTimeToStringConverter CONVERTER = new ZonedDateTimeToStringConverter();

	public ZonedDateTimeSerializer() {
		super(ZonedDateTime.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(ZonedDateTime zonedDateTime, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(CONVERTER.convertTo(zonedDateTime));
	}

}
