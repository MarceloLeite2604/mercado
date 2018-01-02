package org.marceloleite.mercado.xml.structures;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.adapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapter.LocalDateTimeXmlAdapter;

@XmlRootElement(name = "deposit")
@XmlType(propOrder= {"time", "currency", "amount"})
public class XmlDeposit {

	private LocalDateTime time;

	private Currency currency;

	private Double amount;

	public XmlDeposit(LocalDateTime time, Currency currency, Double amount) {
		super();
		this.time = time;
		this.currency = currency;
		this.amount = amount;
	}

	public XmlDeposit() {
		this(LocalDateTime.now(), null, null);
	}

	@XmlElement
	@XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	@XmlElement
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlElement
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
