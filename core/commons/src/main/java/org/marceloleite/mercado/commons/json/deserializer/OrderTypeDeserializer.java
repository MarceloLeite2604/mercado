package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.OrderType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class OrderTypeDeserializer extends StdDeserializer<OrderType> {
	
	private static final long serialVersionUID = 1L;

	public OrderTypeDeserializer() {
		super(OrderType.class);
	}

	@Override
	public OrderType deserialize(JsonParser parser, DeserializationContext context)
			throws IOException {
		return OrderType.getByValue(parser.readValueAs(Integer.class));
	}
}
