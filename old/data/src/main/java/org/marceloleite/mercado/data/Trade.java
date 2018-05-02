package org.marceloleite.mercado.data;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.TradeType;

public class Trade {

	private Currency currency;

	private Long id;

	private MercadoBigDecimal amount;

	private ZonedDateTime date;

	private MercadoBigDecimal price;

	private TradeType tradeType;

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MercadoBigDecimal getAmount() {
		return amount;
	}

	public void setAmount(MercadoBigDecimal amount) {
		this.amount = amount;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	public MercadoBigDecimal getPrice() {
		return price;
	}

	public void setPrice(MercadoBigDecimal price) {
		this.price = price;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
}
