package org.marceloleite.mercado.commons.json.serializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.OrderType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OrderTypeSerializer extends StdSerializer<OrderType> {

	public OrderTypeSerializer() {
		super(OrderType.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(OrderType orderType, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(orderType.getValue().toString());
	}

}
