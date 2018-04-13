package org.marceloleite.mercado.commons.serializer;

import java.io.IOException;

import org.marceloleite.mercado.commons.OrderStatus;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class OrderStatusSerializer extends StdSerializer<OrderStatus> {

	public OrderStatusSerializer() {
		super(OrderStatus.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(OrderStatus orderStatus, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		generator.writeString(orderStatus.getValue().toString());
	}

}
