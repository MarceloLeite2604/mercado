package org.marceloleite.mercado.api.negotiation.methods.listsystemmessages;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonSystemMessagesToListSystemMessageConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListSystemMessagesResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessage;

public class ListSystemMessagesMethodResponse extends AbstractTapiResponse<JsonListSystemMessagesResponse, List<SystemMessage>> {

	private List<SystemMessage> systemMessages;	
	
	public ListSystemMessagesMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse);
		this.systemMessages = getFormattedResponseData();
	}
	
	public List<SystemMessage> getSystemMessages() {
		return systemMessages;
	}

	public void setSystemMessages(List<SystemMessage> systemMessages) {
		this.systemMessages = systemMessages;
	}

	@Override
	protected Class<?> getJsonResponseDataClass() {
		return JsonListSystemMessagesResponse.class;
	}

	@Override
	protected Converter<JsonListSystemMessagesResponse, List<SystemMessage>> getConverter() {
		return new JsonSystemMessagesToListSystemMessageConverter();
	}
}
