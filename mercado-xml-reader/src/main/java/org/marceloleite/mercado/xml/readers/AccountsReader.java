package org.marceloleite.mercado.xml.readers;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.AccountData;
import org.marceloleite.mercado.xml.converters.AccountDataXmlConverter;
import org.marceloleite.mercado.xml.structures.XmlAccount;

public class AccountsReader {

	public List<AccountData> readAccounts() {
		XmlReader xmlReader = new XmlReader();
		List<XmlAccount> xmlAccounts = xmlReader.readAccounts();
		List<AccountData> accountsData = new ArrayList<>();
		AccountDataXmlConverter accountDataXmlConverter = new AccountDataXmlConverter();
		for (XmlAccount xmlAccount : xmlAccounts) {
			AccountData accountData = accountDataXmlConverter.convertToObject(xmlAccount);
			accountsData.add(accountData);
		}
		return accountsData;
	}
}
