package org.marceloleite.mercado.negotiationapi.listsystemmessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.AbstractTapiMethod;
import org.marceloleite.mercado.negotiationapi.TapiMethod;
import org.marceloleite.mercado.negotiationapi.TapiMethodParameters;
import org.marceloleite.mercado.negotiationapi.model.systemmessage.SystemMessageLevel;

public class ListSystemMessagesMethod extends AbstractTapiMethod<ListSystemMessagesMethodResponse> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(ListSystemMessagesMethod.class);

	public ListSystemMessagesMethodResponse execute(SystemMessageLevel systemMessageLevel) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(systemMessageLevel);
			return connectAndReadResponse(tapiMethodParameters);
	}

	private TapiMethodParameters generateTapiMethodParameters(SystemMessageLevel systemMessageLevel) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		if (systemMessageLevel != null) {
			tapiMethodParameters.put(ListSystemMessagesParameters.LEVEL.toString(), systemMessageLevel.toString());
		}
		return tapiMethodParameters;
	}

	public ListSystemMessagesMethodResponse execute() {
		return execute(null);
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.LIST_SYSTEM_MESSAGES;
	}

	@Override
	protected ListSystemMessagesMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new ListSystemMessagesMethodResponse(jsonTapiResponse);
	}

}
