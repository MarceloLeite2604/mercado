package org.marceloleite.mercado.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "date", "price", "amount", "tid", "type" })
public class Trade {

	@JsonProperty("date")
	private Integer date;
	
	@JsonProperty("price")
	private Double price;
	
	@JsonProperty("amount")
	private Double amount;
	
	@JsonProperty("tid")
	private Integer tid;
	
	@JsonProperty("type")
	private String type;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("date")
	public Integer getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(Integer date) {
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
	public Integer getTid() {
		return tid;
	}

	@JsonProperty("tid")
	public void setTid(Integer tid) {
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

}
