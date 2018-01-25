package org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonWithdrawal;
import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.Withdrawal;
import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.WithdrawalStatus;

public class JsonWithdrawalToWithdrawalConverter implements Converter<JsonWithdrawal, Withdrawal>{

	@Override
	public Withdrawal convertTo(JsonWithdrawal jsonWithdrawal) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAccount(jsonWithdrawal.getAccount());
		withdrawal.setAddress(jsonWithdrawal.getAddress());
		LongToZonedDateTimeConverter longToZonedDateTimeConverter = new LongToZonedDateTimeConverter();
		withdrawal.setCreated(longToZonedDateTimeConverter.convertTo(jsonWithdrawal.getCreated_timestamp()));
		withdrawal.setCurrency(Currency.getByAcronym(jsonWithdrawal.getCoin()));
		withdrawal.setDescription(jsonWithdrawal.getDescription());
		withdrawal.setFee(jsonWithdrawal.getFee());
		withdrawal.setId(jsonWithdrawal.getId());
		withdrawal.setNetQuantity(jsonWithdrawal.getNet_quantity());
		withdrawal.setQuantity(jsonWithdrawal.getQuantity());
		withdrawal.setStatus(WithdrawalStatus.getByValue(jsonWithdrawal.getStatus()));
		withdrawal.setUpdated(longToZonedDateTimeConverter.convertTo(jsonWithdrawal.getUpdated_timestamp()));
		return withdrawal;
	}

	@Override
	public JsonWithdrawal convertFrom(Withdrawal withdrawal) {
		throw new UnsupportedOperationException();
	}

}
