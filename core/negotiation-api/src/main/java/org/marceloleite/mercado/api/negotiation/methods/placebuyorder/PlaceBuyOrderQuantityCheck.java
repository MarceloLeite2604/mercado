package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Checker;

public class PlaceBuyOrderQuantityCheck implements Checker<Double> {

	private double MINIMAL_QUANTITY_FOR_BTC = 0.001;
	private double MINIMAL_QUANTITY_FOR_LTC = 0.009;

	private Currency currency;

	public PlaceBuyOrderQuantityCheck(Currency currency) {
		super();
		this.currency = currency;
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
