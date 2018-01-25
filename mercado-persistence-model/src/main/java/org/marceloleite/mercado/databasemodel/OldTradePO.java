package org.marceloleite.mercado.databasemodel;

import java.time.LocalDateTime;


public class OldTradePO implements PersistenceObject<TradeIdPO> {

	private TradeIdPO tradeIdPO;

	private LocalDateTime date;

	private double price;

	private double amount;

	private TradeType tradeType;

	public OldTradePO() {
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

	@Override
	public Class<?> getEntityClass() {
		return TradePO.class;
	}
}
