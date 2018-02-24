package org.marceloleite.mercado.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.structures.XmlBalanceEntryList;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlCurrencyAmount;

public class XmlBalancesXmlAdapter extends XmlAdapter<XmlBalanceEntryList, XmlBalances> {

	@Override
	public XmlBalances unmarshal(XmlBalanceEntryList balanceEntryList) throws Exception {
		XmlBalances balance = new XmlBalances();
		for (XmlCurrencyAmount currencyAmount : balanceEntryList.getCurrencyAmounts()) {
			balance.put(currencyAmount.getCurrency(), currencyAmount.getAmount());
		}
		return balance;
	}

	@Override
	public XmlBalanceEntryList marshal(XmlBalances balances) throws Exception {
		XmlBalanceEntryList balanceEntryList = new XmlBalanceEntryList();
		for (Currency currency : balances.keySet()) {
			Double amount = balances.get(currency);
			balanceEntryList.getCurrencyAmounts().add(new XmlCurrencyAmount(currency, amount));
		}
		return balanceEntryList;
	}

}
