package org.marceloleite.mercado.xml.converters;

import org.marceloleite.mercado.base.model.data.CurrencyAmountData;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.structures.XmlCurrencyAmount;

public class CurrencyAmountXmlConverter implements XmlConverter<XmlCurrencyAmount, CurrencyAmountData> {

	@Override
	public XmlCurrencyAmount convertToXml(CurrencyAmountData currencyAmount) {
		Currency currency = currencyAmount.getCurrency();
		double amount = currencyAmount.getAmount();
		return new XmlCurrencyAmount(currency, amount);
	}

	@Override
	public CurrencyAmountData convertToObject(XmlCurrencyAmount xmlCurrencyAmount) {
		Currency currency = xmlCurrencyAmount.getCurrency();
		double amount = xmlCurrencyAmount.getAmount();
		return new CurrencyAmountData(currency, amount);
	}

}
