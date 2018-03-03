package org.marceloleite.mercado.xml.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.xml.converters.AccountDataXmlConverter;
import org.marceloleite.mercado.xml.structures.XmlAccount;

public class AccountsXmlReader {

	private String xmlDirectoryPath;

	public AccountsXmlReader(String xmlDirectoryPath) {
		super();
		this.xmlDirectoryPath = xmlDirectoryPath;
	}

	public AccountsXmlReader() {
		this(null);
	}

	public List<AccountData> readAccounts() {
		
		XmlReader xmlReader;
		if (xmlDirectoryPath != null) {
			File xmlDirectoryFile = new File(xmlDirectoryPath);
			if ( !xmlDirectoryFile.exists() ) {
				throw new IllegalArgumentException("The directory \""+xmlDirectoryPath+"\" does not exist.");
			}
			
			if (!xmlDirectoryFile.isDirectory()) {
				throw new IllegalArgumentException("The file \""+xmlDirectoryPath+"\" is not a directory.");
			}
			
			xmlReader = new XmlReader(xmlDirectoryPath);
		} else {
			xmlReader = new XmlReader();
		}
		
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
