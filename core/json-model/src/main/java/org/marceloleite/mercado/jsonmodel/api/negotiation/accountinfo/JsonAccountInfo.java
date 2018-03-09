package org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonAccountInfo {

	@JsonProperty("balance")
	private JsonBalance jsonBalance;

	@JsonProperty("withdrawal_limits")
	private JsonWithdrawalLimits jsonWithdrawlLimits;

	public JsonAccountInfo(JsonBalance jsonBalance, JsonWithdrawalLimits jsonWithdrawlLimits) {
		super();
		this.jsonBalance = jsonBalance;
		this.jsonWithdrawlLimits = jsonWithdrawlLimits;
	}

	public JsonAccountInfo() {
		this(null, null);
	}

	@JsonProperty("balance")
	public JsonBalance getJsonBalance() {
		return jsonBalance;
	}

	@JsonProperty("balance")
	public void setJsonBalance(JsonBalance jsonBalance) {
		this.jsonBalance = jsonBalance;
	}

	@JsonProperty("withdrawal_limits")
	public JsonWithdrawalLimits getJsonWithdrawlLimits() {
		return jsonWithdrawlLimits;
	}

	@JsonProperty("withdrawal_limits")
	public void setJsonWithdrawlLimits(JsonWithdrawalLimits jsonWithdrawlLimits) {
		this.jsonWithdrawlLimits = jsonWithdrawlLimits;
	}
}
