package org.marceloleite.mercado.converter.entity;

import java.math.BigDecimal;

import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalanceIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;

public class BalancePOToBalanceDataConverter implements Converter<BalancePO, BalanceData>{

	@Override
	public BalanceData convertTo(BalancePO balancePO) {
		BalanceData balanceData = new BalanceData();
		balanceData.setAmount(balancePO.getAmount().doubleValue());
		balanceData.setCurrency(balancePO.getId().getCurrency());
		return balanceData;
	}

	@Override
	public BalancePO convertFrom(BalanceData balanceData) {
		BalancePO balancePO = new BalancePO();
		BalanceIdPO balanceIdPO = new BalanceIdPO();
		balanceIdPO.setAccoOwner(balanceData.getAccountData().getOwner());
		balanceIdPO.setCurrency(balanceData.getCurrency());
		balancePO.setId(balanceIdPO);
		balancePO.setAmount(new BigDecimal(balanceData.getAmount()));
		return balancePO;
	}

}
