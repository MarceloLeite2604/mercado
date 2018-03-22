package org.marceloleite.mercado.xml.structures;

import java.math.BigDecimal;
import java.util.EnumMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.marceloleite.mercado.commons.Currency;

@XmlRootElement(name = "balances")
public class XmlBalances extends EnumMap<Currency, BigDecimal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public XmlBalances() {
		super(Currency.class);
	}
}
