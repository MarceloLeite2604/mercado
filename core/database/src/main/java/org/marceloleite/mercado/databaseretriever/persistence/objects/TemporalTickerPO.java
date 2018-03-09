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
	private double averagePrice;

	private double buy;

	@Column(name="BUY_ORDERS")
	private BigDecimal buyOrders;

	@Column(name="FIRST_PRICE")
	private double firstPrice;

	@Column(name="HIGHEST_PRICE")
	private double highestPrice;

	@Column(name="LAST_PRICE")
	private double lastPrice;

	@Column(name="LOWEST_PRICE")
	private double lowestPrice;

	private BigDecimal orders;

	@Column(name="PREVIOUS_BUY")
	private double previousBuy;

	@Column(name="PREVIOUS_LAST_PRICE")
	private double previousLastPrice;

	@Column(name="PREVIOUS_SELL")
	private double previousSell;

	private double sell;

	@Column(name="SELL_ORDERS")
	private BigDecimal sellOrders;

	@Column(name="TIME_DURATION")
	private BigDecimal timeDuration;

	@Column(name="VOLUME_TRADED")
	private double volumeTraded;

	public TemporalTickerPO() {
	}

	public TemporalTickerIdPO getId() {
		return this.id;
	}

	public void setId(TemporalTickerIdPO id) {
		this.id = id;
	}

	public double getAveragePrice() {
		return this.averagePrice;
	}

	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}

	public double getBuy() {
		return this.buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public BigDecimal getBuyOrders() {
		return this.buyOrders;
	}

	public void setBuyOrders(BigDecimal buyOrders) {
		this.buyOrders = buyOrders;
	}

	public double getFirstPrice() {
		return this.firstPrice;
	}

	public void setFirstPrice(double firstPrice) {
		this.firstPrice = firstPrice;
	}

	public double getHighestPrice() {
		return this.highestPrice;
	}

	public void setHighestPrice(double highestPrice) {
		this.highestPrice = highestPrice;
	}

	public double getLastPrice() {
		return this.lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getLowestPrice() {
		return this.lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public BigDecimal getOrders() {
		return this.orders;
	}

	public void setOrders(BigDecimal orders) {
		this.orders = orders;
	}

	public double getPreviousBuy() {
		return this.previousBuy;
	}

	public void setPreviousBuy(double previousBuy) {
		this.previousBuy = previousBuy;
	}

	public double getPreviousLastPrice() {
		return this.previousLastPrice;
	}

	public void setPreviousLastPrice(double previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	public double getPreviousSell() {
		return this.previousSell;
	}

	public void setPreviousSell(double previousSell) {
		this.previousSell = previousSell;
	}

	public double getSell() {
		return this.sell;
	}

	public void setSell(double sell) {
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

	public double getVolumeTraded() {
		return this.volumeTraded;
	}

	public void setVolumeTraded(double volumeTraded) {
		this.volumeTraded = volumeTraded;
	}

	@Override
	public Class<?> getEntityClass() {
		return TemporalTickerPO.class;
	}

}