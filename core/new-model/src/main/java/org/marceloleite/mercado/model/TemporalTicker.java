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
@JsonPropertyOrder({ "currency", "start", "end", "duration", "first", "last", "previousLast", "highest", "lowest",
		"average", "lastBuy", "previousLastBuy", "lastSell", "previousLastSell", "orders",
		"buyOrders", "sellOrders", "volumeTraded" })
@XmlRootElement(name = "temporalTicker")
@XmlType(propOrder = { "currency", "start", "end", "duration", "first", "last", "previousLast", "highest", "lowest",
		"average", "lastBuy", "previousLastBuy", "lastSell", "previousLastSell", "orders",
		"buyOrders", "sellOrders", "volumeTraded" })
public class TemporalTicker {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "CURRENCY", nullable = false, length = 4)
	private Currency currency;

	@Column(name = "START", nullable = false)
	private ZonedDateTime start;

	@Column(name = "END", nullable = false)
	private ZonedDateTime end;

	@Column(name = "DURATION", nullable = false)
	private Duration duration;

	@Column(name = "FIRST", nullable = false, precision = 20, scale = 8)
	private BigDecimal first;

	@Column(name = "LAST", nullable = false, precision = 20, scale = 8)
	private BigDecimal last;

	@Column(name = "PREVIOUS_LAST", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousLast;

	@Column(name = "HIGHEST", nullable = false, precision = 20, scale = 8)
	private BigDecimal highest;

	@Column(name = "LOWEST", nullable = false, precision = 20, scale = 8)
	private BigDecimal lowest;

	@Column(name = "AVERAGE", nullable = false, precision = 20, scale = 8)
	private BigDecimal average;

	@Column(name = "BUY", nullable = false, precision = 20, scale = 8)
	private BigDecimal buy;

	@Column(name = "PREVIOUS_BUY", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousBuy;

	@Column(name = "SELL", nullable = false, precision = 20, scale = 8)
	private BigDecimal sell;

	@Column(name = "PREVIOUS_SELL", nullable = false, precision = 20, scale = 8)
	private BigDecimal previousSell;

	@Column(name = "ORDERS", nullable = false)
	private Long orders;

	@Column(name = "BUY_ORDERS", nullable = false)
	private Long buyOrders;

	@Column(name = "SELL_ORDERS", nullable = false)
	private Long sellOrders;

	@Column(name = "VOLUME_TRADED", nullable = false, precision = 20, scale = 8)
	private BigDecimal volumeTraded;

	public TemporalTicker() {
		super();
	}
	
	private TemporalTicker(Builder builder) {
		super();
		this.currency = builder.currency;
		this.start = builder.start;
		this.end = builder.end;
		this.duration = builder.duration;
		this.first = builder.first;
		this.last = builder.last;
		this.previousLast = builder.previousLast;
		this.highest = builder.highest;
		this.lowest = builder.lowest;
		this.average = builder.average;
		this.buy = builder.buy;
		this.previousBuy = builder.previousBuy;
		this.sell = builder.sell;
		this.previousSell = builder.previousSell;
		this.orders = builder.orders;
		this.buyOrders = builder.buyOrders;
		this.sellOrders = builder.sellOrders;
		this.volumeTraded = builder.volumeTraded;
	}

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
	public ZonedDateTime getStart() {
		return start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
	}

	@XmlElement
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	public ZonedDateTime getEnd() {
		return end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
	}

	@XmlElement
	@XmlJavaTypeAdapter(DurationXmlAdapter.class)
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@XmlElement
	public BigDecimal getFirst() {
		return first;
	}

	public void setFirst(BigDecimal first) {
		this.first = first;
	}

	@XmlElement
	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	@XmlElement
	public BigDecimal getPreviousLast() {
		return previousLast;
	}

	public void setPreviousLast(BigDecimal previousLast) {
		this.previousLast = previousLast;
	}

	@XmlElement
	public BigDecimal getHighest() {
		return highest;
	}

	public void setHighest(BigDecimal highest) {
		this.highest = highest;
	}

	@XmlElement
	public BigDecimal getLowest() {
		return lowest;
	}

	public void setLowest(BigDecimal lowest) {
		this.lowest = lowest;
	}

	@XmlElement
	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

	@XmlElement
	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	@XmlElement
	public BigDecimal getPreviousBuy() {
		return previousBuy;
	}

	public void setPreviousBuy(BigDecimal previousBuy) {
		this.previousBuy = previousBuy;
	}

	@XmlElement
	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	@XmlElement
	public BigDecimal getPreviousSell() {
		return previousSell;
	}

	public void setPreviousSell(BigDecimal previousSell) {
		this.previousSell = previousSell;
	}

	@XmlElement
	public Long getOrders() {
		return orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	@XmlElement
	public Long getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(Long buyOrders) {
		this.buyOrders = buyOrders;
	}

	@XmlElement
	public Long getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(Long sellOrders) {
		this.sellOrders = sellOrders;
	}

	@XmlElement
	public BigDecimal getVolumeTraded() {
		return volumeTraded;
	}

	public void setVolumeTraded(BigDecimal volumeTraded) {
		this.volumeTraded = volumeTraded;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Currency currency;
		private ZonedDateTime start;
		private ZonedDateTime end;
		private Duration duration;
		private BigDecimal first;
		private BigDecimal last;
		private BigDecimal previousLast;
		private BigDecimal highest;
		private BigDecimal lowest;
		private BigDecimal average;
		private BigDecimal buy;
		private BigDecimal previousBuy;
		private BigDecimal sell;
		private BigDecimal previousSell;
		private Long orders;
		private Long buyOrders;
		private Long sellOrders;
		private BigDecimal volumeTraded;

		private Builder() {
		}
		
		public Builder currency(Currency currency) {
			this.currency = currency;
			return this;
		}
		
		public Builder start(ZonedDateTime start) {
			this.start = start;
			return this;
		}
		
		public Builder end(ZonedDateTime end) {
			this.end = end;
			return this;
		}
		
		public Builder duration(Duration duration) {
			this.duration = duration;
			return this;
		}
		
		public Builder first(BigDecimal first) {
			this.first = first;
			return this;
		}
		
		public Builder last(BigDecimal last) {
			this.last = last;
			return this;
		}
		
		public Builder previousLast(BigDecimal previousLast) {
			this.previousLast = previousLast;
			return this;
		}
		
		public Builder highest(BigDecimal highest) {
			this.highest = highest;
			return this;
		}
		
		public Builder lowest(BigDecimal lowest) {
			this.lowest = lowest;
			return this;
		}
		
		public Builder average(BigDecimal average) {
			this.average = average;
			return this;
		}
		
		public Builder buy(BigDecimal buy) {
			this.buy = buy;
			return this;
		}
		
		public Builder previousBuy(BigDecimal previousBuy) {
			this.previousBuy = previousBuy;
			return this;
		}
		
		public Builder sell(BigDecimal sell) {
			this.sell = sell;
			return this;
		}
		
		public Builder previousSell(BigDecimal previousSell) {
			this.previousSell = previousSell;
			return this;
		}
		
		public Builder orders(Long orders) {
			this.orders = orders;
			return this;
		}
		
		public Builder buyOrders(Long buyOrders) {
			this.buyOrders = buyOrders;
			return this;
		}
		
		public Builder sellOrders(Long sellOrders) {
			this.sellOrders = sellOrders;
			return this;
		}
		
		public Builder volumeTraded(BigDecimal volumeTraded) {
			this.volumeTraded = volumeTraded;
			return this;
		}
		
		public TemporalTicker build() {
			return new TemporalTicker(this);
		}
	}
}