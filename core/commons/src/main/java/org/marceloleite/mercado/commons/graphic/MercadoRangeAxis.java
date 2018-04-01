package org.marceloleite.mercado.commons.graphic;

import org.marceloleite.mercado.commons.Currency;

public enum MercadoRangeAxis {
	CURRENCY_REAL(Currency.REAL.getAcronym()),
	ORDERS("Orders");
	
	private String title;

	private MercadoRangeAxis(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
}
