package org.marceloleite.mercado.xml.structures;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="balance")
public class XmlBalanceEntryList {

	private List<XmlCurrencyAmount> currencyAmounts;

	public XmlBalanceEntryList() {
		this.currencyAmounts = new ArrayList<>();
	}

	@XmlElement(name="balance")
	public List<XmlCurrencyAmount> getCurrencyAmounts() {
		return currencyAmounts;
	}

	public void setCurrencyAmounts(List<XmlCurrencyAmount> balanceEntries) {
		this.currencyAmounts = balanceEntries;
	}

}
