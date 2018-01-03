package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.Balance;
import org.marceloleite.mercado.simulator.structure.CurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlBalances;

public class BalanceXmlConverter implements XmlConverter<XmlBalances, Balance> {

	@Override
	public XmlBalances convertToXml(Balance balance) {
		XmlBalances xmlBalances = new XmlBalances();
		for (Currency currency : balance.keySet()) {
			xmlBalances.put(currency, balance.get(currency).getAmount());
		}
		return xmlBalances;
	}

	@Override
	public Balance convertToObject(XmlBalances xmlBalances) {
		Balance balance = new Balance();
		for (Currency currency : xmlBalances.keySet()) {
			Double amount = xmlBalances.get(currency);
			CurrencyAmount currencyAmount = new CurrencyAmount(currency, amount);
			balance.put(currency, currencyAmount);
		}
		return balance;
	}

}
