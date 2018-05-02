package org.marceloleite.mercado.jsonmodel.api.data;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "high", "low", "vol", "last", "buy", "sell", "date" })
public class JsonTickerValues {

	@JsonProperty("high")
	private double high;
	
	@JsonProperty("low")
	private double low;
	
	@JsonProperty("vol")
	private double vol;
	
	@JsonProperty("last")
	private double last;
	
	@JsonProperty("buy")
	private double buy;
	
	@JsonProperty("sell")
	private double sell;
	
	@JsonProperty("date")
	private long date;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("high")
	public Double getHigh() {
		return high;
	}

	@JsonProperty("high")
	public void setHigh(Double high) {
		this.high = high;
	}

	@JsonProperty("low")
	public double getLow() {
		return low;
	}

	@JsonProperty("low")
	public void setLow(double low) {
		this.low = low;
	}

	@JsonProperty("vol")
	public double getVol() {
		return vol;
	}

	@JsonProperty("vol")
	public void setVol(double vol) {
		this.vol = vol;
	}

	@JsonProperty("last")
	public double getLast() {
		return last;
	}

	@JsonProperty("last")
	public void setLast(double last) {
		this.last = last;
	}

	@JsonProperty("buy")
	public double getBuy() {
		return buy;
	}

	@JsonProperty("buy")
	public void setBuy(double buy) {
		this.buy = buy;
	}

	@JsonProperty("sell")
	public double getSell() {
		return sell;
	}

	@JsonProperty("sell")
	public void setSell(double sell) {
		this.sell = sell;
	}

	@JsonProperty("date")
	public Long getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(Long date) {
		this.date = date;
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