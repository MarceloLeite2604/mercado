package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalanceIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;

public class BalancePOToBalanceDataConverter implements Converter<BalancePO, BalanceData>{

	@Override
	public BalanceData convertTo(BalancePO balancePO) {
		BalanceData balanceData = new BalanceData();
		balanceData.setAmount(balancePO.getAmount());
		balanceData.setCurrency(balancePO.getBalanceIdPO().getCurrency());
		return balanceData;
	}

	@Override
	public BalancePO convertFrom(BalanceData balanceData) {
		BalancePO balancePO = new BalancePO();
		BalanceIdPO balanceIdPO = new BalanceIdPO();
		balanceIdPO.setAccountOwner(balanceData.getAccountData().getOwner());
		balanceIdPO.setCurrency(balanceData.getCurrency());
		balancePO.setBalanceIdPO(balanceIdPO);
		balancePO.setAmount(balanceData.getAmount());
		return balancePO;
	}

}
