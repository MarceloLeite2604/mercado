package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonSystemMessage;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessage;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessageLevel;

public class JsonSystemMessageToSystemMessageConverter implements Converter<JsonSystemMessage, SystemMessage> {

	@Override
	public SystemMessage convertTo(JsonSystemMessage jsonSystemMessage) {
		SystemMessage systemMessage = new SystemMessage();
		long longMsgDate = Long.parseLong(jsonSystemMessage.getMsgContent());
		systemMessage.setTime(LongToZonedDateTimeConverter.getInstance().convertTo(longMsgDate));
		systemMessage.setSystemMessageLevel(SystemMessageLevel.getByName(jsonSystemMessage.getLevel()));
		systemMessage.setMessageContent(jsonSystemMessage.getMsgContent());
		return systemMessage;
	}

	@Override
	public JsonSystemMessage convertFrom(SystemMessage systemMessage) {
		throw new UnsupportedOperationException();
	}

}
