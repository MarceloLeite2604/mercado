package org.marceloleite.mercado.base.model.old;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.base.model.Trade;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TradeType;

public class OldTrade {

	private Currency currency;

	private Long id;

	private Double amount;

	private ZonedDateTime date;

	private Double price;

	private TradeType tradeType;

	public OldTrade(Currency currency, Long id, Double amount, ZonedDateTime date, Double price, TradeType tradeType) {
		super();
		this.currency = currency;
		this.id = id;
		this.amount = amount;
		this.date = date;
		this.price = price;
		this.tradeType = tradeType;
	}

	public OldTrade(Trade trade) {
		this(trade.getCurrency(), trade.getId(), trade.getAmount(), trade.getDate(),
				trade.getPrice(), trade.getTradeType());
	}

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
}
