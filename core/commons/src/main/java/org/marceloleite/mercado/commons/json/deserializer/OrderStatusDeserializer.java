package org.marceloleite.mercado.commons.json.deserializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.OrderStatus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class OrderStatusDeserializer extends StdDeserializer<OrderStatus> {

	private static final long serialVersionUID = 1L;
	
	public OrderStatusDeserializer() {
		super(OrderStatus.class);
	}

	@Override
	public OrderStatus deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		return OrderStatus.getByValue(parser.readValueAs(Integer.class));
	}
}
