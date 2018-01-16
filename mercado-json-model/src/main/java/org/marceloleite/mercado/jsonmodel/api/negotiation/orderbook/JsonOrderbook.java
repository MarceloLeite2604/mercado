package org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook;

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
@JsonPropertyOrder({ "bids", "asks", "latest_order_id" })
public class JsonOrderbook {

	@JsonProperty("bids")
	private List<JsonOrderbookRegister> bids = null;

	@JsonProperty("asks")
	private List<JsonOrderbookRegister> asks = null;

	@JsonProperty("latest_order_id")
	private Long latestOrderId;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("bids")
	public List<JsonOrderbookRegister> getBids() {
		return bids;
	}

	@JsonProperty("bids")
	public void setBids(List<JsonOrderbookRegister> bids) {
		this.bids = bids;
	}

	@JsonProperty("asks")
	public List<JsonOrderbookRegister> getAsks() {
		return asks;
	}

	@JsonProperty("asks")
	public void setAsks(List<JsonOrderbookRegister> asks) {
		this.asks = asks;
	}

	@JsonProperty("latest_order_id")
	public Long getLatestOrderId() {
		return latestOrderId;
	}

	@JsonProperty("latest_order_id")
	public void setLatestOrderId(Long latestOrderId) {
		this.latestOrderId = latestOrderId;
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
