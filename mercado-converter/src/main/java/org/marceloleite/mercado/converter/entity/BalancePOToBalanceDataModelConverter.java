package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.BalanceDataModel;
import org.marceloleite.mercado.databasemodel.BalanceIdPO;
import org.marceloleite.mercado.databasemodel.BalancePO;

public class BalancePOToBalanceDataModelConverter implements Converter<BalancePO, BalanceDataModel>{

	@Override
	public BalanceDataModel convertTo(BalancePO balancePO) {
		BalanceDataModel balanceDataModel = new BalanceDataModel();
		balanceDataModel.setCurrency(balancePO.getBalanceIdPO().getCurrency());
		balanceDataModel.setAmount(balancePO.getAmount());
		return balanceDataModel;
	}

	@Override
	public BalancePO convertFrom(BalanceDataModel balanceDataModel) {
		BalancePO balancePO = new BalancePO();
		BalanceIdPO balanceIdPO = new BalanceIdPO();
		balanceIdPO.setAccountOwner(balanceDataModel.getAccountDataModel().getOwner());
		balanceIdPO.setCurrency(balanceDataModel.getCurrency());
		balancePO.setBalanceIdPO(balanceIdPO);
		balancePO.setAmount(balanceDataModel.getAmount());
		return balancePO;
	}

}
