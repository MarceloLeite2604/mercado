package org.marceloleite.mercado.xml.reader;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.converter.xml.AccountDataXmlConverter;
import org.marceloleite.mercado.simulator.data.AccountData;
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
