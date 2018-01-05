package org.marceloleite.mercado.xml.reader;

import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.spi.XmlReader;

import org.marceloleite.mercado.converter.xml.AccountXmlConverter;
import org.marceloleite.mercado.simulator.structure.Account;
import org.marceloleite.mercado.xml.structures.XmlAccount;

public class AccountsReader {

	public List<Account> readAccounts() {
		XmlReader xmlReader = new XmlReader();
		List<XmlAccount> xmlAccounts = xmlReader.readAccounts();
		List<Account> accounts = new ArrayList<>();
		AccountXmlConverter accountXmlConverter = new AccountXmlConverter();
		for (XmlAccount xmlAccount : xmlAccounts) {
			Account account = accountXmlConverter.convertToObject(xmlAccount);
			accounts.add(account);
		}
		return accounts;
	}
}
