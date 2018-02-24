package org.marceloleite.mercado.converter.xml;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.data.BalanceData;
import org.marceloleite.mercado.simulator.data.CurrencyAmountData;
import org.marceloleite.mercado.xml.structures.XmlBalances;

public class BalanceXmlConverter implements XmlConverter<XmlBalances, BalanceData> {

	@Override
	public XmlBalances convertToXml(BalanceData balanceData) {
		XmlBalances xmlBalances = new XmlBalances();
		for (Currency currency : balanceData.keySet()) {
			xmlBalances.put(currency, balanceData.get(currency).getAmount());
		}
		return xmlBalances;
	}

	@Override
	public BalanceData convertToObject(XmlBalances xmlBalances) {
		BalanceData balanceData = new BalanceData();
		for (Currency currency : Currency.values()) {
			Double amount = xmlBalances.getOrDefault(currency, 0.0);
			CurrencyAmountData currencyAmount = new CurrencyAmountData(currency, amount);
			balanceData.put(currency, currencyAmount);
		}
		return balanceData;
	}
}
