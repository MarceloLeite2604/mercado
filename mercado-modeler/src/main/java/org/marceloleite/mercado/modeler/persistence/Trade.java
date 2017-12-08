package org.marceloleite.mercado.modeler.persistence;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {

	private int id;

	private LocalDateTime date;

	private double price;

	private double amount;

	@JsonProperty("type")
	private TradeType tradeType;

	public Trade() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}
}
