package org.marceloleite.mercado.xml.structures;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;

@XmlRootElement(name="currencyAmount")
@XmlType(propOrder= {"currency", "amount"})
public class XmlCurrencyAmount {

	private Currency currency;

	private double amount;
	
	public XmlCurrencyAmount() {
		this(null, 0);
	}

	public XmlCurrencyAmount(Currency currency, double amount) {
		super();
		this.currency = currency;
		this.amount = amount;
	}

	@XmlElement
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrency() {
		return currency;
	}

	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	@XmlElement
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return currency.getAcronym() + " " + amount;
	}
}
