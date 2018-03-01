package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.base.model.data.AccountData;
import org.marceloleite.mercado.converter.entity.AccountPOToAccountDataConverter;
import org.marceloleite.mercado.databaseretriever.persistence.daos.AccountDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.AccountPO;

public class AccountRetriever {

	private AccountDAO accountDAO;
	
	public AccountRetriever() {
		this.accountDAO = new AccountDAO();
	}
	
	public AccountData retrieve(String owner) {
		AccountPO accountPOforEquirement = new AccountPO();
		accountPOforEquirement.setOwner(owner);
		AccountPO accountPO = accountDAO.findById(accountPOforEquirement);
		
		AccountData accountData = null;
		if ( accountPO != null ) {
			accountData = new AccountPOToAccountDataConverter().convertTo(accountPO);
		}
		
		return accountData; 
	}
	
	public void save(AccountData accountData) {
		AccountPO accountPO = new AccountPOToAccountDataConverter().convertFrom(accountData);
		accountDAO.merge(accountPO);
	}
}
