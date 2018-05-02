package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapters.ZonedDateTimeXmlAdapter;

@XmlRootElement(name="buyOrder")
public class XmlBuyOrder {

	private ZonedDateTime time;

	private Currency currencyToBuy;

	private MercadoBigDecimal amountToBuy;

	private Currency currencyToPay;

	private MercadoBigDecimal amountToPay;

	public XmlBuyOrder() {
		this(null, null, null, null, null);
	}

	private XmlBuyOrder(ZonedDateTime time, Currency currencyToBuy, MercadoBigDecimal amountToBuy, Currency currencyToPay,
			MercadoBigDecimal amountToPay) {
		super();
		this.time = time;
		this.currencyToBuy = currencyToBuy;
		this.amountToBuy = amountToBuy;
		this.currencyToPay = currencyToPay;
		this.amountToPay = amountToPay;
	}

	public XmlBuyOrder(ZonedDateTime time, Currency currencyToBuy, MercadoBigDecimal amountToBuy, Currency currencyToPay) {
		this(time, currencyToBuy, amountToBuy, currencyToPay, null);
	}

	public XmlBuyOrder(ZonedDateTime time, Currency currencyToBuy, Currency currencyToPay, MercadoBigDecimal amountToPay) {
		this(time, currencyToBuy, null, currencyToPay, amountToPay);
	}

	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrencyToBuy() {
		return currencyToBuy;
	}

	public void setCurrencyToBuy(Currency currencyToBuy) {
		this.currencyToBuy = currencyToBuy;
	}

	@XmlElement(required = false)
	public MercadoBigDecimal getAmountToBuy() {
		return amountToBuy;
	}

	public void setAmountToBuy(MercadoBigDecimal amountToBuy) {
		this.amountToBuy = amountToBuy;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrencyToPay() {
		return currencyToPay;
	}

	public void setCurrencyToPay(Currency currencyToPay) {}

	@XmlElement(required = false)
	public MercadoBigDecimal getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(MercadoBigDecimal amountToPay) {
		this.amountToPay = amountToPay;
	}
}
