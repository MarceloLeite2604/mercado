package org.marceloleite.mercado.xml.structures;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapters.ZonedDateTimeXmlAdapter;

@XmlRootElement(name = "deposit")
@XmlType(propOrder= {"time", "currency", "amount"})
public class XmlDeposit {

	private ZonedDateTime time;

	private Currency currency;

	private BigDecimal amount;

	public XmlDeposit(ZonedDateTime time, Currency currency, BigDecimal amount) {
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
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
