package org.marceloleite.mercado.converter.entity;

import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.data.WithdrawalData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TapiInformationPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.WithdrawalPO;

public class AccountPOToAccountDataConverter implements Converter<AccountPO, AccountData> {

	@Override
	public AccountData convertTo(AccountPO accountPO) {
		AccountData accountData = new AccountData();
		accountData.setOwner(accountPO.getOwner());
		accountData.setTapiInformationData(new TapiInformationPOToTapiInformationDataConverter().convertTo(accountPO.getTapiInformationPO()));
		accountData.setEmail(accountPO.getEmail());
		
		List<BalancePO> balancePOs = accountPO.getBalancePOs();
		List<BalanceData> balanceDatas = new ListBalancePOToListBalanceDataConverter().convertTo(balancePOs);
		accountData.setBalanceDatas(balanceDatas);
		
		List<WithdrawalPO> withdrawalPOs = accountPO.getWithdrawalPOs();
		List<WithdrawalData> withdrawalDatas = new ListWithdrawalPOToListWithdrawalDataConverter().convertTo(withdrawalPOs);
		accountData.setWithdrawalDatas(withdrawalDatas);
		
		List<StrategyPO> strategyPOs = accountPO.getStrategyPOs();
		List<StrategyData> strategyDatas = new ListStrategyPOToListStrategyDataConverter().convertTo(strategyPOs);
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
		
		accountPO.setEmail(accountData.getEmail());
		
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
