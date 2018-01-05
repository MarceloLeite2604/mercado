package org.marceloleite.mercado.jsonmodel;

import java.util.HashMap;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "date", "price", "amount", "tid", "type" })
public class JsonTrade {

	@JsonProperty("date")
	private Long date;

	@JsonProperty("price")
	private Double price;

	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("tid")
	private Long tid;

	@JsonProperty("type")
	private String type;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonIgnore
	private Currency currency;

	@JsonProperty("date")
	public Long getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(Long date) {
		this.date = date;
	}

	@JsonProperty("price")
	public Double getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(Double price) {
		this.price = price;
	}

	@JsonProperty("amount")
	public Double getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JsonProperty("tid")
	public Long getTid() {
		return tid;
	}

	@JsonProperty("tid")
	public void setTid(Long tid) {
		this.tid = tid;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonIgnore
	public Currency getCurrency() {
		return currency;
	}

	@JsonIgnore
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
