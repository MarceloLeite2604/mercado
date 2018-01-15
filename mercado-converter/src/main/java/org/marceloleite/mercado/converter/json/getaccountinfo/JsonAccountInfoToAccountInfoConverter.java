package org.marceloleite.mercado.converter.json.getaccountinfo;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.accountinfo.JsonAccountInfo;
import org.marceloleite.mercado.negotiationapi.model.getaccountinfo.AccountInfo;

public class JsonAccountInfoToAccountInfoConverter implements Converter<JsonAccountInfo, AccountInfo>{

	@Override
	public AccountInfo convertTo(JsonAccountInfo jsonAccountInfo) {
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setBalance(new JsonBalanceToBalanceConverter().convertTo(jsonAccountInfo.getJsonBalance()));
		accountInfo.setWithdrawalLimits(new JsonWithdrawalLimitsToWithdrawalLimitsConverter().convertTo(jsonAccountInfo.getJsonWithdrawlLimits()));
		return accountInfo;
	}

	@Override
	public JsonAccountInfo convertFrom(AccountInfo accountInfo) {
		throw new UnsupportedOperationException();
	}

}
