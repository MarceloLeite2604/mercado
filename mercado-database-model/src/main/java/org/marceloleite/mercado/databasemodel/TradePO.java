package org.marceloleite.mercado.databasemodel;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.marceloleite.mercado.commons.Currency;

@Entity(name = "Trades")
public class TradePO implements PersistenceObject<Long> {

	@Id
	private Long id;

	private Currency currency;

	private LocalDateTime date;

	private double price;

	private double amount;

	@Enumerated(EnumType.STRING)
	private TradeType tradeType;

	public TradePO() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public Class<?> getEntityClass() {
		return TradePO.class;
	}
}
