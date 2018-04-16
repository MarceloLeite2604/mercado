package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.attributeconverter.ZonedDateTimeAttributeConverter;
import org.marceloleite.mercado.model.xmladapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.model.xmladapter.DurationXmlAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "TEMPORAL_TICKERS")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "currency", "startTime", "endTime", "averagePrice", "buy", "buyOrders", "firstPrice",
		"highestPrice", "lastPrice", "lowestPrice", "orders", "previousBuy", "previousLastPrice", "previousSell",
		"sell", "sellOrders", "timeDuration", "volumeTraded" })
@XmlRootElement(name = "temporalTicker")
@XmlType(propOrder = { "currency", "startTime", "endTime", "averagePrice", "buy", "buyOrders", "firstPrice",
		"highestPrice", "lastPrice", "lowestPrice", "orders", "previousBuy", "previousLastPrice", "previousSell",
		"sell", "sellOrders", "timeDuration", "volumeTraded" })
public class TemporalTicker {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "CURRENCY", nullable = false, length = 4)
	private Currency currency;

	@Column(name = "START_TIME", nullable = false)
	private ZonedDateTime startTime;

	@Column(name = "END_TIME", nullable = false)
	private ZonedDateTime endTime;

	@Column(name = "AVERAGE_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal averagePrice;

	@Column(name = "BUY", nullable = false, precision = 20, scale = 8)
	private BigDecimal buy;

	@Column(name = "BUY_ORDERS", nullable = false)
	private Long buyOrders;

	@Column(name = "FIRST_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal firstPrice;

	@Column(name = "HIGHEST_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal highestPrice;

	@Column(name = "LAST_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal lastPrice;

	@Column(name = "LOWEST_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal lowestPrice;

	@Column(name = "ORDERS", nullable = false)
	private Long orders;

	@Column(name = "PREVIOUS_BUY", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousBuy;

	@Column(name = "PREVIOUS_LAST_PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousLastPrice;

	@Column(name = "PREVIOUS_SELL", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousSell;

	@Column(name = "SELL", nullable = false, precision = 20, scale = 8)
	private BigDecimal sell;

	@Column(name = "SELL_ORDERS", nullable = false)
	private Long sellOrders;

	@Column(name = "TIME_DURATION", nullable = false)
	private Duration timeDuration;

	@Column(name = "VOLUME_TRADED", nullable = false, precision = 20, scale = 8)
	private BigDecimal volumeTraded;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlElement
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	@XmlElement
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}

	@XmlElement
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	@XmlElement
	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	@XmlElement
	public Long getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(Long buyOrders) {
		this.buyOrders = buyOrders;
	}

	@XmlElement
	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	@XmlElement
	public BigDecimal getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(BigDecimal highestPrice) {
		this.highestPrice = highestPrice;
	}

	@XmlElement
	public BigDecimal getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	@XmlElement
	public BigDecimal getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	@XmlElement
	public Long getOrders() {
		return orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	@XmlElement
	public BigDecimal getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(BigDecimal previousBuy) {
		this.previousBuy = previousBuy;
	}

	@XmlElement
	public BigDecimal getPreviousLastPrice() {
		return previousLastPrice;
	}

	public void setPreviousLastPrice(BigDecimal previousLastPrice) {
		this.previousLastPrice = previousLastPrice;
	}

	@XmlElement
	public BigDecimal getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(BigDecimal previousSell) {
		this.previousSell = previousSell;
	}

	@XmlElement
	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	@XmlElement
	public Long getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(Long sellOrders) {
		this.sellOrders = sellOrders;
	}

	@XmlElement
	@XmlJavaTypeAdapter(DurationXmlAdapter.class)
	public Duration getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(Duration timeDuration) {
		this.timeDuration = timeDuration;
	}

	@XmlElement
	public BigDecimal getVolumeTraded() {
		return volumeTraded;
	}

	public void setVolumeTraded(BigDecimal volumeTraded) {
		this.volumeTraded = volumeTraded;
	}
}