package org.marceloleite.mercado.api.negotiation.converters;

import org.marceloleite.mercado.api.negotiation.methods.getwithdrawal.GetWithdrawalResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal.JsonWithdrawalToWithdrawalConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;

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
