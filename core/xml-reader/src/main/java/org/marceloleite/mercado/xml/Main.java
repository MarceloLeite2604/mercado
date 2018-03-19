package org.marceloleite.mercado.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.marceloleite.mercado.xml.adapters.XmlBalancesXmlAdapter;
import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalanceEntryList;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlClass;
import org.marceloleite.mercado.xml.structures.XmlCurrencyAmount;
import org.marceloleite.mercado.xml.structures.XmlDeposit;
import org.marceloleite.mercado.xml.structures.XmlOrder;
import org.marceloleite.mercado.xml.structures.XmlParameter;
import org.marceloleite.mercado.xml.structures.XmlStrategy;

public class Main {

	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;
	private static JAXBContext jaxbContext;

	private static final String XML_DIRECTORY_PATH = "src/main/resources/xml/";

	private static XmlCurrencyAmount xmlCurrencyAmount;
	private static XmlDeposit xmlDeposit;
	private static XmlBalances xmlBalances;
	private static XmlAccount xmlAccount;
	private static XmlOrder xmlOrder;
	private static XmlStrategy xmlStrategy;
	private static XmlClass xmlClass;
	private static XmlParameter xmlParameter;

	public static void main(String[] args) throws Exception {
		jaxbContext = createJaxbContext();
		marshaller = createMarshaller();
		unmarshaller = createUnmarshaller();
		createObjects();

		marshallXmlCurrencyAmount();
		marshallXmlDeposit();
		marshallXmlBalances();
		marshallXmlBuyOrder();
		marshallXmlAccount();

		unmarshallXmlCurrencyAmount();
		unmarshallXmlDeposit();
		unmarshallXmlBalances();
		unmarshallXmlBuyOrder();
		unmarshallXmlAccount();
	}

	private static JAXBContext createJaxbContext() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlBuyOrder.class, XmlAccount.class,
				XmlBalanceEntryList.class, XmlBalances.class, XmlDeposit.class, XmlCurrencyAmount.class,
				XmlClass.class, XmlStrategy.class, XmlParameter.class);
		return jaxbContext;
	}

	private static Marshaller createMarshaller() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		return marshaller;
	}

	private static Unmarshaller createUnmarshaller() throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return unmarshaller;
	}

	private static void createObjects() {
		xmlCurrencyAmount = new XmlCurrencyAmount(Currency.BITCOIN, 0.123);
		xmlDeposit = new XmlDeposit(ZonedDateTimeUtils.now(), Currency.BITCOIN, 0.2236);
		xmlBalances = new XmlBalances();
		xmlBalances.put(Currency.BITCOIN, 0.123);
		xmlOrder = new XmlOrder(Currency.REAL, Currency.BITCOIN, 1.0, 20000.0);
		xmlAccount = new XmlAccount();
		xmlAccount.setOwner("Marcelo");
		xmlAccount.setXmlDeposits(Arrays.asList(xmlDeposit));
		xmlAccount.setXmlBalances(xmlBalances);
		xmlAccount.setXmlBuyOrders(Arrays.asList(xmlOrder));
		
		List<XmlParameter> xmlParameters = new ArrayList<>();
		xmlParameter = new XmlParameter();
		xmlParameter.setName("growthPercentageThreshold");
		xmlParameter.setValue("0.0496");
		xmlParameters.add(xmlParameter);
		
		xmlParameter = new XmlParameter();
		xmlParameter.setName("shrinkPercentageThreshold");
		xmlParameter.setValue("-0.05");
		xmlParameters.add(xmlParameter);
		
		List<XmlClass> xmlClasses = new ArrayList<>();
		xmlClass = new XmlClass();
		xmlClass.setName("org.marceloleite.mercado.simulator.strategies.third.ThirdStrategy");
		xmlClass.setXmlParameters(xmlParameters);
		xmlClasses.add(xmlClass);
		
		
		List<XmlStrategy> xmlStrategies = new ArrayList<>();
		xmlStrategy = new XmlStrategy();
		xmlStrategy.setCurrency(Currency.BITCOIN);
		xmlStrategy.setXmlClasses(xmlClasses);
		xmlStrategies.add(xmlStrategy);
		
		xmlAccount.setXmlStrategies(xmlStrategies);
	}

	private static void marshallXmlBalances() throws Exception {
		marshaller.marshal(new XmlBalancesXmlAdapter().marshal(xmlBalances), createFileForClassXml(XmlBalances.class));
	}

	private static void marshallXmlCurrencyAmount() throws JAXBException {
		marshaller.marshal(xmlCurrencyAmount, createFileForClassXml(XmlCurrencyAmount.class));
	}

	private static void marshallXmlDeposit() throws JAXBException {
		marshaller.marshal(xmlDeposit, createFileForClassXml(XmlDeposit.class));
	}

	private static void marshallXmlBuyOrder() throws JAXBException {
		marshaller.marshal(xmlOrder, createFileForClassXml(XmlBuyOrder.class));
	}

	private static void marshallXmlAccount() throws JAXBException {
		marshaller.marshal(xmlAccount, createFileForClassXml(XmlAccount.class));
	}

	private static void unmarshallXmlCurrencyAmount() throws JAXBException {
		xmlCurrencyAmount = (XmlCurrencyAmount) unmarshaller.unmarshal(createFileForClassXml(XmlCurrencyAmount.class));
	}

	private static void unmarshallXmlDeposit() throws JAXBException {
		xmlDeposit = (XmlDeposit) unmarshaller.unmarshal(createFileForClassXml(XmlDeposit.class));
	}

	private static void unmarshallXmlBalances() throws Exception {
		XmlBalanceEntryList xmlBalanceEntryList = (XmlBalanceEntryList) unmarshaller
				.unmarshal(createFileForClassXml(XmlBalances.class));
		xmlBalances = new XmlBalancesXmlAdapter().unmarshal(xmlBalanceEntryList);
	}

	private static void unmarshallXmlBuyOrder() throws JAXBException {
		xmlOrder = (XmlOrder) unmarshaller.unmarshal(createFileForClassXml(XmlOrder.class));
	}

	private static void unmarshallXmlAccount() throws JAXBException {
		xmlAccount = (XmlAccount) unmarshaller.unmarshal(createFileForClassXml(XmlAccount.class));
	}

	private static final File createFileForClassXml(Class<?> clazz) {
		String className = clazz.getSimpleName();
		return new File(XML_DIRECTORY_PATH + className + ".xml");
	}
}
