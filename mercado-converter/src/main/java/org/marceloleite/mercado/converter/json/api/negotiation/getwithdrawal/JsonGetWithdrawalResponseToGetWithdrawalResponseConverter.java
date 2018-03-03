package org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;
import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.GetWithdrawalResponse;

public class JsonGetWithdrawalResponseToGetWithdrawalResponseConverter
		implements Converter<JsonGetWithdrawalResponse, GetWithdrawalResponse> {

	@Override
	public GetWithdrawalResponse convertTo(JsonGetWithdrawalResponse jsonGetWithdrawalResponse) {
		GetWithdrawalResponse getWithdrawalResponse = new GetWithdrawalResponse();
		getWithdrawalResponse.setWithdrawal(new JsonWithdrawalToWithdrawalConverter().convertTo(jsonGetWithdrawalResponse.getJsonWithdrawal()));
		return getWithdrawalResponse;
	}

	@Override
	public JsonGetWithdrawalResponse convertFrom(GetWithdrawalResponse getWithdrawalResponse) {
		throw new UnsupportedOperationException();
	}

}
