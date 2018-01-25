package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.xml.adapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapter.ZonedDateTimeXmlAdapter;

@XmlRootElement(name = "deposit")
@XmlType(propOrder= {"time", "currency", "amount"})
public class XmlDeposit {

	private ZonedDateTime time;

	private Currency currency;

	private Double amount;

	public XmlDeposit(ZonedDateTime time, Currency currency, Double amount) {
		super();
		this.time = time;
		this.currency = currency;
		this.amount = amount;
	}

	public XmlDeposit() {
		this(ZonedDateTimeUtils.now(), null, null);
	}

	@XmlElement
	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
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
