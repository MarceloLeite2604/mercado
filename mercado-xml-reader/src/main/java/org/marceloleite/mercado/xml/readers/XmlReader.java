package org.marceloleite.mercado.xml.readers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalanceEntryList;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlCurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

public class XmlReader {

	// private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;

	private static final Class<?>[] XML_CLASSES = { XmlBuyOrder.class, XmlAccount.class, XmlBalanceEntryList.class,
			XmlBalances.class, XmlDeposit.class, XmlCurrencyAmount.class };

	private static final String XML_DIRECTORY_PATH = "src/main/resources/xml/";
	private static final String ACCOUNTS_DIRECTORY_NAME = "accounts/";

	private static final String XML_FILE_EXTENSION = "XML";

	public XmlReader() {
		try {
			this.jaxbContext = createJaxbContext();
			this.unmarshaller = createUnmarshaller();
		} catch (JAXBException exception) {
			throw new RuntimeException(exception);
		}
	}

	private JAXBContext createJaxbContext() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(XML_CLASSES);
		return jaxbContext;
	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller;
	}

	public List<XmlAccount> readAccounts() {
		List<File> xmlAccountFiles = getXmlAccountFiles();
		List<XmlAccount> xmlAccounts = new ArrayList<>();
		for (File xmlAccountFile : xmlAccountFiles) {
			xmlAccounts.add(readAccount(xmlAccountFile));
		}
		return xmlAccounts;
	}

	private List<File> getXmlAccountFiles() {
		File accountsDirectory = new File(XML_DIRECTORY_PATH + ACCOUNTS_DIRECTORY_NAME);
		FilenameFilter xmlFileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith(XML_FILE_EXTENSION.toUpperCase());
			}
		};
		File[] xmlFiles = accountsDirectory.listFiles(xmlFileNameFilter);
		return Arrays.asList(xmlFiles);
	}

	private XmlAccount readAccount(File xmlAccountFile) {
		Object unmarshalledObject;
		try {
			unmarshalledObject = unmarshaller.unmarshal(xmlAccountFile);
		} catch (JAXBException exception) {
			throw new RuntimeException(exception);
		}
		if (!(unmarshalledObject instanceof XmlAccount)) {
			throw new RuntimeException(
					"The file \"" + xmlAccountFile.getAbsolutePath() + "\" does not contain an Account XML.");
		}
		return (XmlAccount)unmarshalledObject;
	}
}
