package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.json.deserializer.TradeTypeDeserializer;
import org.marceloleite.mercado.commons.json.deserializer.ZonedDateTimeFromEpochDeserializer;
import org.marceloleite.mercado.commons.json.serializer.TradeTypeSerializer;
import org.marceloleite.mercado.commons.json.serializer.ZonedDateTimeFromEpochSerializer;
import org.marceloleite.mercado.model.xmladapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.model.xmladapter.TradeTypeXmlAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "TRADES")
@JsonPropertyOrder({ "id", "currency", "amount", "price", "time", "type" })
@XmlRootElement(name = "trade")
@XmlType(propOrder = { "id", "currency", "amount", "price", "time", "type" })
public class Trade {

	@Id
	@Column(name = "ID")
	@JsonProperty("tid")
	private Long id;

	@Column(name = "CURRENCY", nullable = false, length = 4)
	private Currency currency;

	@Column(name = "AMOUNT", nullable = false, precision = 20, scale = 8)
	private BigDecimal amount;

	@Column(name = "PRICE", nullable = false, precision = 20, scale = 8)
	private BigDecimal price;

	@Column(name = "TIME", nullable = false)
	@JsonProperty("date")
	@JsonSerialize(using = ZonedDateTimeFromEpochSerializer.class)
	@JsonDeserialize(using = ZonedDateTimeFromEpochDeserializer.class)
	private ZonedDateTime time;

	@Column(name = "TYPE", nullable = false, length = 8)
	@JsonSerialize(using = TradeTypeSerializer.class)
	@JsonDeserialize(using = TradeTypeDeserializer.class)
	private TradeType type;

	@XmlElement
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
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@XmlElement
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	@XmlElement
	@XmlJavaTypeAdapter(TradeTypeXmlAdapter.class)
	public TradeType getType() {
		return type;
	}

	public void setType(TradeType type) {
		this.type = type;
	}

}