package org.marceloleite.mercado.databasemodel;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity(name = "TRADES")
public class TradePO implements PersistenceObject<TradeIdPO> {

	@EmbeddedId
	private TradeIdPO tradeIdPO;

	@Column(name="TRADE_DATE", nullable = false)
	private ZonedDateTime date;

	@Column(name="PRICE", nullable=false)
	private double price;

	@Column(name="AMOUNT", nullable=false)
	private double amount;

	@Column(name="TRADE_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TradeType tradeType;

	public TradePO() {
	}

	@Override
	public TradeIdPO getId() {
		return tradeIdPO;
	}
	
	public TradeIdPO getTradeIdPO() {
		return this.tradeIdPO;
	}

	public void setTradeIdPO(TradeIdPO tradeIdPO) {
		this.tradeIdPO = tradeIdPO;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
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

	@Override
	public Class<?> getEntityClass() {
		return TradePO.class;
	}
}
