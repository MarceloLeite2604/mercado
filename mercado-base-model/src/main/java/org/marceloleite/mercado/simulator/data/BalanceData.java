package org.marceloleite.mercado.simulator.data;

import java.util.EnumMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.marceloleite.mercado.commons.Currency;

@XmlRootElement
public class BalanceData extends EnumMap<Currency, CurrencyAmountData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceData() {
		super(Currency.class);
	}	
}
