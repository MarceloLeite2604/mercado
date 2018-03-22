package org.marceloleite.mercado.xml.converters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.xml.structures.XmlBalances;

public class BalanceXmlConverter implements XmlConverter<XmlBalances, List<BalanceData>> {

	@Override
	public XmlBalances convertToXml(List<BalanceData> balanceDatas) {
		XmlBalances xmlBalances = new XmlBalances();
		for (BalanceData balanceData : balanceDatas) {
			xmlBalances.put(balanceData.getCurrency(), balanceData.getAmount());
		}
		return xmlBalances;
	}

	@Override
	public List<BalanceData> convertToObject(XmlBalances xmlBalances) {
		List<BalanceData> balanceDatas = new ArrayList<>();
		for (Currency currency : Currency.values()) {
			BigDecimal amount = xmlBalances.getOrDefault(currency, new BigDecimal("0.0"));
			BalanceData balanceData = new BalanceData();
			balanceData.setCurrency(currency);
			balanceData.setAmount(amount);
			balanceDatas.add(balanceData);
		}
		return balanceDatas;
	}
}
