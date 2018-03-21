package org.marceloleite.mercado.api.negotiation.converters;

import org.marceloleite.mercado.api.negotiation.methods.withdrawcoin.WithdrawCoinResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal.JsonWithdrawalToWithdrawalConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonWithdrawCoinResponse;

public class JsonWithdrawCoinResponseToWithdrawCoinResponseConverter
		implements Converter<JsonWithdrawCoinResponse, WithdrawCoinResponse> {

	@Override
	public WithdrawCoinResponse convertTo(JsonWithdrawCoinResponse jsonWithdrawCoinResponse) {
		WithdrawCoinResponse withdrawCoinResponse = new WithdrawCoinResponse();
		withdrawCoinResponse.setWithdrawal(
				new JsonWithdrawalToWithdrawalConverter().convertTo(jsonWithdrawCoinResponse.getJsonWithdrawal()));
		return null;
	}

	@Override
	public JsonWithdrawCoinResponse convertFrom(WithdrawCoinResponse withdrawCoinResult) {
		throw new UnsupportedOperationException();
	}

}