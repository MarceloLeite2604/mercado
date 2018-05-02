package org.marceloleite.mercado.api.negotiation.model.serializer;

import java.io.IOException;

import org.marceloleite.mercado.api.negotiation.model.WithdrawalStatus;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class WithdrawalStatusSerializer extends StdSerializer<WithdrawalStatus> {

	private static final long serialVersionUID = 1L;
	
	public WithdrawalStatusSerializer() {
		this(null);
	}

	public WithdrawalStatusSerializer(Class<WithdrawalStatus> clazz) {
		super(clazz);
	}

	@Override
	public void serialize(WithdrawalStatus withdrawalStatus, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeString(withdrawalStatus.getValue().toString());
	}

}
