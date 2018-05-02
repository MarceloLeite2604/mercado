package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapters.ZonedDateTimeXmlAdapter;

@XmlRootElement(name = "sellOrder")
public class XmlSellOrder {

	private ZonedDateTime time;

	private Currency currencyToSell;

	private MercadoBigDecimal amountToSell;

	private Currency currencyToReceive;

	private MercadoBigDecimal amountToReceive;

	public XmlSellOrder() {
		this(null, null, null, null, null);
	}

	private XmlSellOrder(ZonedDateTime time, Currency currencyToSell, MercadoBigDecimal amountToSell, Currency currencyToReceive,
			MercadoBigDecimal amountToReceive) {
		super();
		this.time = time;
		this.currencyToSell = currencyToSell;
		this.amountToSell = amountToSell;
		this.currencyToReceive = currencyToReceive;
		this.amountToReceive = amountToReceive;
	}

	public XmlSellOrder(ZonedDateTime time, Currency currencyToSell, MercadoBigDecimal amountToSell, Currency currencyToReceive) {
		this(time, currencyToSell, amountToSell, currencyToReceive, null);
	}

	public XmlSellOrder(ZonedDateTime time, Currency currencyToSell, Currency currencyToReceive, MercadoBigDecimal amountToReceive) {
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
	public MercadoBigDecimal getAmountToSell() {
		return amountToSell;
	}

	public void setAmountToSell(MercadoBigDecimal amountToSell) {
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
	public MercadoBigDecimal getAmountToReceive() {
		return amountToReceive;
	}

	public void setAmountToReceive(MercadoBigDecimal amountToReceive) {
		this.amountToReceive = amountToReceive;
	}
}
