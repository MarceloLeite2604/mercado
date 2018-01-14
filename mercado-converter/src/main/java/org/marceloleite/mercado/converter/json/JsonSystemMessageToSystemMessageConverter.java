package org.marceloleite.mercado.converter.json;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.JsonSystemMessage;
import org.marceloleite.mercado.negotiationapi.model.SystemMessage;
import org.marceloleite.mercado.negotiationapi.model.SystemMessageLevel;

public class JsonSystemMessageToSystemMessageConverter implements Converter<JsonSystemMessage, SystemMessage> {

	@Override
	public SystemMessage convertTo(JsonSystemMessage jsonSystemMessage) {
		SystemMessage systemMessage = new SystemMessage();
		LongToLocalDateTimeConverter longToLocalDateTimeConverter = new LongToLocalDateTimeConverter();
		long longMsgDate = Long.parseLong(jsonSystemMessage.getMsgContent());
		systemMessage.setTime(longToLocalDateTimeConverter.convertTo(longMsgDate));
		systemMessage.setSystemMessageLevel(SystemMessageLevel.getByName(jsonSystemMessage.getLevel()));
		systemMessage.setMessageContent(jsonSystemMessage.getMsgContent());
		return systemMessage;
	}

	@Override
	public JsonSystemMessage convertFrom(SystemMessage systemMessage) {
		throw new UnsupportedOperationException();
	}

}
