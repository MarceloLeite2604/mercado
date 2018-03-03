package org.marceloleite.mercado.converter.entity;

import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TapiInformationPO;

public class AccountPOToAccountDataConverter implements Converter<AccountPO, AccountData> {

	@Override
	public AccountData convertTo(AccountPO accountPO) {
		AccountData accountData = new AccountData();
		accountData.setOwner(accountPO.getOwner());
		accountData.setTapiInformationData(new TapiInformationPOToTapiInformationDataConverter().convertTo(accountPO.getTapiInformationPO()));
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
		
		TapiInformationPO tapiInformationPO = new TapiInformationPOToTapiInformationDataConverter().convertFrom(accountData.getTapiInformationData());
		tapiInformationPO.setAccountPO(accountPO);
		accountPO.setTapiInformationPO(tapiInformationPO);
		
		List<BalanceData> balanceDatas = accountData.getBalanceDatas();
		List<BalancePO> balancePOs = listBalancePOToListBalanceDataConverter.convertFrom(balanceDatas);
		balancePOs.forEach(balancePO -> balancePO.setAccountPO(accountPO));
		accountPO.setBalancePOs(balancePOs);
		
		List<StrategyData> strategyDatas = accountData.getStrategyDatas();
		List<StrategyPO> strategyPOs = listStrategyPOToListStrategyDataConverter.convertFrom(strategyDatas);
		strategyPOs.forEach(strategyPO -> strategyPO.setAccount(accountPO));
		accountPO.setStrategyPOs(strategyPOs);
		
		return accountPO;
	}

}
