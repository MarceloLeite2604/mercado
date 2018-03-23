package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalanceIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;

public class BalancePOToBalanceDataConverter implements Converter<BalancePO, BalanceData>{

	@Override
	public BalanceData convertTo(BalancePO balancePO) {
		BalanceData balanceData = new BalanceData();
		balanceData.setAmount(new MercadoBigDecimal(balancePO.getAmount()));
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
		balancePO.setAmount(new MercadoBigDecimal(balanceData.getAmount().toString()));
		return balancePO;
	}

}
