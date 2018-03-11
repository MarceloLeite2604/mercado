package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.marceloleite.mercado.commons.TradeType;

/**
 * The persistent class for the TRADES database table.
 * 
 */
@Entity
@Table(name="TRADES")
@NamedQuery(name="TradePO.findAll", query="SELECT t FROM TradePO t")
public class TradePO implements Serializable, PersistenceObject<TradeIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TradeIdPO id;

	@Column(name="AMOUNT")
	private double amount;

	@Column(name="PRICE")
	private double price;

	@Column(name="TRADE_DATE")
	private ZonedDateTime tradeDate;

	@Column(name="TRADE_TYPE")
	private TradeType tradeType;

	public TradePO() {
	}

	public TradeIdPO getId() {
		return this.id;
	}

	public void setId(TradeIdPO id) {
		this.id = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ZonedDateTime getTradeDate() {
		return this.tradeDate;
	}

	public void setTradeDate(ZonedDateTime tradeDate) {
		this.tradeDate = tradeDate;
	}

	public TradeType getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	@Override
	public Class<?> getEntityClass() {
		return TradePO.class;
	}

}