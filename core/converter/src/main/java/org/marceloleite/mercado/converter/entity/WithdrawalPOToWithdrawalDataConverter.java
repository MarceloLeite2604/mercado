package org.marceloleite.mercado.converter.entity;

import java.math.BigDecimal;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.WithdrawalData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.WithdrawalIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.WithdrawalPO;

public class WithdrawalPOToWithdrawalDataConverter implements Converter<WithdrawalPO, WithdrawalData>{

	@Override
	public WithdrawalData convertTo(WithdrawalPO withdrawalPO) {
		WithdrawalData withdrawalData = new WithdrawalData();
		withdrawalData.setAmount(withdrawalPO.getAmount().doubleValue());
		withdrawalData.setCurrency(withdrawalPO.getId().getCurrency());
		return withdrawalData;
	}

	@Override
	public WithdrawalPO convertFrom(WithdrawalData withdrawalData) {
		WithdrawalPO withdrawalPO = new WithdrawalPO();
		WithdrawalIdPO withdrawalIdPO = new WithdrawalIdPO();
		withdrawalIdPO.setAccoOwner(withdrawalData.getAccountData().getOwner());
		withdrawalIdPO.setCurrency(withdrawalData.getCurrency());
		withdrawalPO.setId(withdrawalIdPO);
		withdrawalPO.setAmount(new BigDecimal(withdrawalData.getAmount()));
		return withdrawalPO;
	}

}
