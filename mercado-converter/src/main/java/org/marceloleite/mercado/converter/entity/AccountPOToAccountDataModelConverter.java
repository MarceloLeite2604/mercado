package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.AccountDataModel;
import org.marceloleite.mercado.databasemodel.AccountPO;

public class AccountPOToAccountDataModelConverter implements Converter<AccountPO, AccountDataModel> {

	@Override
	public AccountDataModel convertTo(AccountPO accountPO) {
		AccountDataModel accountDataModel = new AccountDataModel();
		accountDataModel.setOwner(accountPO.getOwner());
		return accountDataModel;
	}

	@Override
	public AccountPO convertFrom(AccountDataModel accountDataModel) {
		AccountPO accountPO = new AccountPO();
		accountPO.setOwner(accountDataModel.getOwner());
		return accountPO;
	}

}
