package org.marceloleite.mercado.xml.structures;

import java.util.EnumMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;

@XmlRootElement(name = "balances")
public class XmlBalances extends EnumMap<Currency, MercadoBigDecimal> {

	private static final long serialVersionUID = 1L;
	
	public XmlBalances() {
		super(Currency.class);
	}
}
