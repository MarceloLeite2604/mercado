package org.marceloleite.mercado.simulator.structure;

import java.util.EnumMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.marceloleite.mercado.commons.Currency;

@XmlRootElement
public class BalanceData extends EnumMap<Currency, CurrencyAmount> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceData() {
		super(Currency.class);
	}	
}
