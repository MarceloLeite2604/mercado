package org.marceloleite.mercado.api.negotiation.method;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.model.SystemMessage;
import org.marceloleite.mercado.api.negotiation.model.SystemMessageLevel;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class ListSystemMessages extends TapiMethodTemplate<TapiResponse<List<SystemMessage>>> {
	
	private static final String[] PARAMETER_NAMES = { "level" };

	public ListSystemMessages(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.LIST_SYSTEM_MESSAGES, PARAMETER_NAMES, new ParameterizedTypeReference<TapiResponse<List<SystemMessage>>>(){});
	}

	public TapiResponse<List<SystemMessage>> execute(SystemMessageLevel systemMessageLevel) {
		return executeMethod(systemMessageLevel);
	}

	public TapiResponse<List<SystemMessage>> execute() {
		return execute(null);
	}
}
