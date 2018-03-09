package org.marceloleite.mercado.api.negotiation.methods.listsystemmessages;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonSystemMessagesToListSystemMessageConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListSystemMessagesResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessage;

public class ListSystemMessagesMethodResponse extends AbstractTapiResponse<JsonListSystemMessagesResponse, List<SystemMessage>> {

	public ListSystemMessagesMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonListSystemMessagesResponse.class, new JsonSystemMessagesToListSystemMessageConverter());
	}
}
