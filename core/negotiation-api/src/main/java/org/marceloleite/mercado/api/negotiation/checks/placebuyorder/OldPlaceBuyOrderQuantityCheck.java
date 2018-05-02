package org.marceloleite.mercado.api.negotiation.checks.placebuyorder;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Checker;

public class OldPlaceBuyOrderQuantityCheck {
	
	private static PlayBuyOrderQuantityCheck instance;

	private OldPlaceBuyOrderQuantityCheck() {
		
	}

	@Override
	public boolean check(Double quantity) {
		if (quantity != null) {
			switch (currency) {
			case BITCOIN:
				return (quantity >= MINIMAL_QUANTITY_FOR_BTC);
			case LITECOIN:
				return (quantity >= MINIMAL_QUANTITY_FOR_LTC);
			default:
				return true;
			}
		}
		return true;
	}

}
