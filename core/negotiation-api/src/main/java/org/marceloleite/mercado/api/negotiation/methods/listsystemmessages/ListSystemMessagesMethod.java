package org.marceloleite.mercado.api.negotiation.methods.listsystemmessages;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.negotiationapi.model.listsystemmessages.SystemMessageLevel;

public class ListSystemMessagesMethod extends AbstractTapiMethod<ListSystemMessagesMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"level"};

	public ListSystemMessagesMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.LIST_SYSTEM_MESSAGES, ListSystemMessagesMethodResponse.class, PARAMETER_NAMES);
	}
	
	public ListSystemMessagesMethodResponse execute(SystemMessageLevel systemMessageLevel) {
		return executeMethod(systemMessageLevel);
	}

	public ListSystemMessagesMethodResponse execute() {
		return execute(null);
	}
}
