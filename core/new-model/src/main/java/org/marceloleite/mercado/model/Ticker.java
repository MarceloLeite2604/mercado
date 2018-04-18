package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
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
import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeFromEpochDeserializer;
import org.marceloleite.mercado.commons.json.serializer.ZonedDateTimeFromEpochSerializer;
import org.marceloleite.mercado.model.xmladapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.model.xmladapter.ZonedDateTimeXmlAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "TICKERS")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "currency", "tickerTime", "buy", "high", "last", "low", "sell", "volume" })
@XmlRootElement(name = "ticker")
@XmlType(propOrder = { "currency", "tickerTime", "buy", "high", "last", "low", "sell", "volume" })
public class Ticker {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "CURRENCY", length = 4)
	private Currency currency;

	@Column(name = "TICKER_TIME", nullable = false)
	@JsonProperty("date")
	@JsonSerialize(using = ZonedDateTimeFromEpochSerializer.class)
	@JsonDeserialize(using = ZonedDateTimeFromEpochDeserializer.class)
	private ZonedDateTime tickerTime;

	@Column(name = "BUY", nullable = false, precision = 20, scale = 8)
	private BigDecimal buy;

	@Column(name = "HIGH", nullable = false, precision = 20, scale = 8)
	private BigDecimal high;

	@Column(name = "LAST", nullable = false, precision = 20, scale = 8)
	private BigDecimal last;

	@Column(name = "LOW", nullable = false, precision = 20, scale = 8)
	private BigDecimal low;

	@Column(name = "SELL", nullable = false, precision = 20, scale = 8)
	private BigDecimal sell;

	@Column(name = "VOLUME", nullable = false, precision = 20, scale = 8)
	@JsonProperty("vol")
	private BigDecimal volume;

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
	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getTickerTime() {
		return tickerTime;
	}

	public void setTickerTime(ZonedDateTime tickerTime) {
		this.tickerTime = tickerTime;
	}

	@XmlElement
	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	@XmlElement
	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	@XmlElement
	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	@XmlElement
	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	@XmlElement
	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}

	@XmlElement
	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}