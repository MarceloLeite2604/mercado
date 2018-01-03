package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.CurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlCurrencyAmount;

public class CurrencyAmountXmlConverter implements XmlConverter<XmlCurrencyAmount, CurrencyAmount> {

	@Override
	public XmlCurrencyAmount convertToXml(CurrencyAmount currencyAmount) {
		Currency currency = currencyAmount.getCurrency();
		double amount = currencyAmount.getAmount();
		return new XmlCurrencyAmount(currency, amount);
	}

	@Override
	public CurrencyAmount convertToObject(XmlCurrencyAmount xmlCurrencyAmount) {
		Currency currency = xmlCurrencyAmount.getCurrency();
		double amount = xmlCurrencyAmount.getAmount();
		return new CurrencyAmount(currency, amount);
	}

}
