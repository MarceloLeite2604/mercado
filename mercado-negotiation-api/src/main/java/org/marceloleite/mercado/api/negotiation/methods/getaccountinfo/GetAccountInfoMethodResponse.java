package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo.JsonAccountInfoToAccountInfoConverter;
import org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo.JsonBalanceDeserializer;
import org.marceloleite.mercado.converter.json.api.negotiation.getaccountinfo.JsonWithdrawalLimitsDeserializer;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonAccountInfo;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonBalance;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonWithdrawalLimits;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.AccountInfo;

public class GetAccountInfoMethodResponse extends AbstractTapiResponse<JsonAccountInfo, AccountInfo> {

	private AccountInfo accountInfo;

	public GetAccountInfoMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse);
		this.accountInfo = getFormattedResponseData();
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	@Override
	protected Class<?> getJsonResponseDataClass() {
		return JsonAccountInfo.class;
	}

	@Override
	protected Converter<JsonAccountInfo, AccountInfo> getConverter() {
		return new JsonAccountInfoToAccountInfoConverter();
	}

	@Override
	protected JsonAccountInfo getJsonResponseData() {
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter(JsonAccountInfo.class);
		objectToJsonConverter.addDeserializer(JsonBalance.class,
				new JsonBalanceDeserializer());
		objectToJsonConverter.addDeserializer(JsonWithdrawalLimits.class,
				new JsonWithdrawalLimitsDeserializer());
		JsonAccountInfo jsonGetAccountInfo = objectToJsonConverter.convertFromToObject(getResponseData(),
				JsonAccountInfo.class);
		return jsonGetAccountInfo;
	}

}
