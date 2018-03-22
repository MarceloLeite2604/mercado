package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the TEMPORAL_TICKERS database table.
 * 
 */
@Entity
@Table(name="TEMPORAL_TICKERS")
@NamedQuery(name="TemporalTickerPO.findAll", query="SELECT t FROM TemporalTickerPO t")
public class TemporalTickerPO implements Serializable, PersistenceObject<TemporalTickerIdPO> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TemporalTickerIdPO id;

	@Column(name="AVERAGE_PRICE")
	private BigDecimal averagePrice;

	@Column(name="BUY")
	private BigDecimal buy;

	@Column(name="BUY_ORDERS")
	private BigDecimal buyOrders;

	@Column(name="FIRST_PRICE")
	private BigDecimal firstPrice;

	@Column(name="HIGHEST_PRICE")
	private BigDecimal highestPrice;

	@Column(name="LAST_PRICE")
	private BigDecimal lastPrice;

	@Column(name="LOWEST_PRICE")
	private BigDecimal lowestPrice;

	@Column(name="ORDERS")
	private BigDecimal orders;

	@Column(name="PREVIOUS_BUY")
	private BigDecimal previousBuy;

	@Column(name="PREVIOUS_LAST_PRICE")
	private BigDecimal previousLastPrice;

	@Column(name="PREVIOUS_SELL")
	private BigDecimal previousSell;

	@Column(name="SELL")
	private BigDecimal sell;

	@Column(name="SELL_ORDERS")
	private BigDecimal sellOrders;

	@Column(name="TIME_DURATION")
	private BigDecimal timeDuration;

	@Column(name="VOLUME_TRADED")
	private BigDecimal volumeTraded;

	public TemporalTickerPO() {
	}

	public TemporalTickerIdPO getId() {
		return this.id;
	}

	public void setId(TemporalTickerIdPO id) {
		this.id = id;
	}

	public BigDecimal getAveragePrice() {
		return this.averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public BigDecimal getBuy() {
		return this.buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	public BigDecimal getBuyOrders() {
		return this.buyOrders;
	}

	public void setBuyOrders(BigDecimal buyOrders) {
		this.buyOrders = buyOrders;
	}

	public BigDecimal getFirstPrice() {
		return this.firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public BigDecimal getHighestPrice() {
		return this.highestPrice;
	}

	public void setHighestPrice(BigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}

	public BigDecimal getLastPrice() {
		return this.lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	public BigDecimal getLowestPrice() {
		return this.lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public BigDecimal getOrders() {
		return this.orders;
	}

	public void setOrders(BigDecimal orders) {
		this.orders = orders;
	}

	public BigDecimal getPreviousBuy() {
		return this.previousBuy;
	}

	public void setPreviousBuy(BigDecimal previousBuy) {
		this.previousBuy = previousBuy;
	}

	public BigDecimal getPreviousLastPrice() {
		return this.previousLastPrice;
	}

	public void setPreviousLastPrice(BigDecimal previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	public BigDecimal getPreviousSell() {
		return this.previousSell;
	}

	public void setPreviousSell(BigDecimal previousSell) {
		this.previousSell = previousSell;
	}

	public BigDecimal getSell() {
		return this.sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	public BigDecimal getSellOrders() {
		return this.sellOrders;
	}

	public void setSellOrders(BigDecimal sellOrders) {
		this.sellOrders = sellOrders;
	}

	public BigDecimal getTimeDuration() {
		return this.timeDuration;
	}

	public void setTimeDuration(BigDecimal timeDuration) {
		this.timeDuration = timeDuration;
	}

	public BigDecimal getVolumeTraded() {
		return this.volumeTraded;
	}

	public void setVolumeTraded(BigDecimal volumeTraded) {
		this.volumeTraded = volumeTraded;
	}

	@Override
	public Class<?> getEntityClass() {
		return TemporalTickerPO.class;
	}

}