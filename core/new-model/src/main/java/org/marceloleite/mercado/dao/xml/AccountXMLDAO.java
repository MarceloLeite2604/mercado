package org.marceloleite.mercado.dao.xml;

import java.io.File;
import java.util.List;

import javax.inject.Named;

import org.marceloleite.mercado.dao.interfaces.AccountDAO;
import org.marceloleite.mercado.model.Account;
import org.springframework.util.StringUtils;

@Named("AccountXMLDAO")
public class AccountXMLDAO extends BaseXMLDAO<Account> implements AccountDAO {

	private static final String ENTITY_DIRECTORY = "accounts";

	private static final String FILE_NAME_PREFIX = "account";

	private static final String FILE_NAME_TEMPLATE = FILE_NAME_PREFIX + "_%s";

	private static final String FILE_NAME_REGEX = FILE_NAME_PREFIX + "_.+";

	@Override
	public Account findByOwner(String owner) {
		if (StringUtils.isEmpty(owner)) {
			throw new RuntimeException("Account owner cannot be null or empty.");
		}
		List<File> XMLFiles = findXMLFileByRegex(FILE_NAME_REGEX);
		for (File XMLFile : XMLFiles) {
			Account account = readXMLFile(XMLFile);
			if (owner.equals(account.getOwner())) {
				return account;
			}
		}
		return null;
	}

	@Override
	public <S extends Account> S save(S account) {
		return writeXMLFile(account);
	}

	@Override
	protected String createFileName(Account account) {
		return String.format(FILE_NAME_TEMPLATE, account.getOwner());
	}

	@Override
	protected String getEntityDirectory() {
		return ENTITY_DIRECTORY;
	}
}
