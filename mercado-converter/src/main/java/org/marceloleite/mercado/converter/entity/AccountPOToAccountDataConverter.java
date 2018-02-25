package org.marceloleite.mercado.converter.entity;

import java.util.List;

import org.marceloleite.mercado.base.model.data.AccountData;
import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.AccountPO;
import org.marceloleite.mercado.databasemodel.BalancePO;
import org.marceloleite.mercado.databasemodel.StrategyPO;

public class AccountPOToAccountDataConverter implements Converter<AccountPO, AccountData> {

	@Override
	public AccountData convertTo(AccountPO accountPO) {
		AccountData accountData = new AccountData();
		accountData.setOwner(accountPO.getOwner());
		ListBalancePOToListBalanceDataConverter listBalancePOToListBalanceDataConverter = new ListBalancePOToListBalanceDataConverter();
		ListStrategyPOToListStrategyDataConverter listStrategyPOToListStrategyDataConverter = new ListStrategyPOToListStrategyDataConverter();
		List<BalancePO> balancePOs = accountPO.getBalancePOs();
		List<BalanceData> balanceDatas = listBalancePOToListBalanceDataConverter.convertTo(balancePOs);
		accountData.setBalanceDatas(balanceDatas);
		List<StrategyPO> strategyPOs = accountPO.getStrategyPOs();
		List<StrategyData> strategyDatas = listStrategyPOToListStrategyDataConverter.convertTo(strategyPOs);
		accountData.setStrategyDatas(strategyDatas);;
		return accountData;
	}

	@Override
	public AccountPO convertFrom(AccountData accountData) {
		ListBalancePOToListBalanceDataConverter listBalancePOToListBalanceDataConverter = new ListBalancePOToListBalanceDataConverter();
		ListStrategyPOToListStrategyDataConverter listStrategyPOToListStrategyDataConverter = new ListStrategyPOToListStrategyDataConverter();
		AccountPO accountPO = new AccountPO();
		accountPO.setOwner(accountData.getOwner());
		List<BalanceData> balanceDatas = accountData.getBalanceDatas();
		List<BalancePO> balancePOs = listBalancePOToListBalanceDataConverter.convertFrom(balanceDatas);
		accountPO.setBalancePOs(balancePOs);
		List<StrategyData> strategyDatas = accountData.getStrategyDatas();
		List<StrategyPO> strategyPOs = listStrategyPOToListStrategyDataConverter.convertFrom(strategyDatas);
		accountPO.setStrategyPOs(strategyPOs);
		return accountPO;
	}

}
