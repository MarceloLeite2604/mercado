package org.marceloleite.mercado.converter.json;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.JsonSystemMessage;
import org.marceloleite.mercado.jsonmodel.JsonListSystemMessagesResponse;
import org.marceloleite.mercado.negotiationapi.model.SystemMessage;

public class JsonSystemMessagesToListSystemMessageConverter
		implements Converter<JsonListSystemMessagesResponse, List<SystemMessage>> {

	@Override
	public List<SystemMessage> convertTo(JsonListSystemMessagesResponse jsonSystemMessages) {
		List<SystemMessage> systemMessages = new ArrayList<>();
		List<JsonSystemMessage> jsonSystemMessageList = jsonSystemMessages.getMessages();
		if (jsonSystemMessageList != null && !jsonSystemMessageList.isEmpty()) {
			JsonSystemMessageToSystemMessageConverter jsonSystemMessageToSystemMessageConverter = new JsonSystemMessageToSystemMessageConverter();
			for (JsonSystemMessage jsonSystemMessage : jsonSystemMessages.getMessages()) {
				systemMessages.add(jsonSystemMessageToSystemMessageConverter.convertTo(jsonSystemMessage));
			}
		}
		return systemMessages;
	}

	@Override
	public JsonListSystemMessagesResponse convertFrom(List<SystemMessage> systemMessages) {
		throw new UnsupportedOperationException();
	}

}
