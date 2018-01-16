package org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "orderbook" })
public class JsonListOrderbookResponse {

	@JsonProperty("orderbook")
	private JsonOrderbook jsonOrderbook;

	public JsonListOrderbookResponse(JsonOrderbook jsonOrderbook) {
		super();
		this.jsonOrderbook = jsonOrderbook;
	}

	public JsonListOrderbookResponse() {
		this(null);
	}

	@JsonProperty("orderbook")
	public JsonOrderbook getJsonOrderbook() {
		return jsonOrderbook;
	}

	@JsonProperty("orderbook")
	public void setJsonOrderbook(JsonOrderbook jsonOrderbook) {
		this.jsonOrderbook = jsonOrderbook;
	}
}
