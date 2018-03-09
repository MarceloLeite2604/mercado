package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal.JsonWithdrawalToWithdrawalConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonWithdrawCoinResult;
import org.marceloleite.mercado.negotiationapi.model.withdrawcoin.WithdrawCoinResult;

public class JsonWithdrawCoinResultToWithdrawCoinResultConverter
		implements Converter<JsonWithdrawCoinResult, WithdrawCoinResult> {

	@Override
	public WithdrawCoinResult convertTo(JsonWithdrawCoinResult jsonWithdrawCoinResult) {
		WithdrawCoinResult withdrawCoinResult = new WithdrawCoinResult();
		withdrawCoinResult.setWithdrawal(
				new JsonWithdrawalToWithdrawalConverter().convertTo(jsonWithdrawCoinResult.getJsonWithdrawal()));
		return null;
	}

	@Override
	public JsonWithdrawCoinResult convertFrom(WithdrawCoinResult withdrawCoinResult) {
		throw new UnsupportedOperationException();
	}

}
