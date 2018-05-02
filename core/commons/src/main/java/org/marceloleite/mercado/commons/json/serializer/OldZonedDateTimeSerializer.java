package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OldZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

	private static final long serialVersionUID = 1L;

	public OldZonedDateTimeSerializer() {
		super(ZonedDateTime.class);
	}

	@Override
	public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializer)
			throws IOException {
		jsonGenerator.writeString(ZonedDateTimeUtils.format(zonedDateTime));
	}

}
