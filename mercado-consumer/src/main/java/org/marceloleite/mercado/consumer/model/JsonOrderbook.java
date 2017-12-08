package org.marceloleite.mercado.consumer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "asks", "bids" })
public class JsonOrderbook {

	@JsonProperty("asks")
	private List<List<Double>> asks = null;
	
	@JsonProperty("bids")
	private List<List<Double>> bids = null;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("asks")
	public List<List<Double>> getAsks() {
		return asks;
	}

	@JsonProperty("asks")
	public void setAsks(List<List<Double>> asks) {
		this.asks = asks;
	}

	@JsonProperty("bids")
	public List<List<Double>> getBids() {
		return bids;
	}

	@JsonProperty("bids")
	public void setBids(List<List<Double>> bids) {
		this.bids = bids;
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