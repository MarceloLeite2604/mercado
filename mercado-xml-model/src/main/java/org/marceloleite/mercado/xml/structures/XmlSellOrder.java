package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.adapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapter.ZonedDateTimeXmlAdapter;

@XmlRootElement(name = "sellOrder")
public class XmlSellOrder {

	private ZonedDateTime time;

	private Currency currencyToSell;

	private Double amountToSell;

	private Currency currencyToReceive;

	private Double amountToReceive;

	public XmlSellOrder() {
		this(null, null, null, null, null);
	}

	private XmlSellOrder(ZonedDateTime time, Currency currencyToSell, Double amountToSell, Currency currencyToReceive,
			Double amountToReceive) {
		super();
		this.time = time;
		this.currencyToSell = currencyToSell;
		this.amountToSell = amountToSell;
		this.currencyToReceive = currencyToReceive;
		this.amountToReceive = amountToReceive;
	}

	public XmlSellOrder(ZonedDateTime time, Currency currencyToSell, Double amountToSell, Currency currencyToReceive) {
		this(time, currencyToSell, amountToSell, currencyToReceive, null);
	}

	public XmlSellOrder(ZonedDateTime time, Currency currencyToSell, Currency currencyToReceive, Double amountToReceive) {
		this(time, currencyToSell, null, currencyToReceive, amountToReceive);
	}

	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrencyToSell() {
		return currencyToSell;
	}

	public void setCurrencyToSell(Currency currencyToSell) {
		this.currencyToSell = currencyToSell;
	}

	@XmlElement(required = false)
	public Double getAmountToSell() {
		return amountToSell;
	}

	public void setAmountToSell(Double amountToSell) {
		this.amountToSell = amountToSell;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrencyToReceive() {
		return currencyToReceive;
	}

	public void setCurrencyToReceive(Currency currencyToReceive) {
		this.currencyToReceive = currencyToReceive;
	}

	@XmlElement(required = false)
	public Double getAmountToReceive() {
		return amountToReceive;
	}

	public void setAmountToReceive(Double amountToReceive) {
		this.amountToReceive = amountToReceive;
	}
}
