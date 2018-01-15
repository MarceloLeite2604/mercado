package org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo;

import java.util.HashMap;

import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("withdrawal_limits")
public class JsonWithdrawalLimits extends HashMap<Currency, JsonCurrencyAvailable> {

	private static final long serialVersionUID = 1L;

}
