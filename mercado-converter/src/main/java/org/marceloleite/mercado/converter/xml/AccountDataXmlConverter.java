package org.marceloleite.mercado.converter.xml;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.simulator.structure.BalanceData;
import org.marceloleite.mercado.simulator.structure.BuyOrderData;
import org.marceloleite.mercado.simulator.structure.DepositData;
import org.marceloleite.mercado.simulator.structure.SellOrderData;
import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlDeposit;
import org.marceloleite.mercado.xml.structures.XmlSellOrder;
import org.marceloleite.mercado.xml.structures.XmlStrategy;

public class AccountDataXmlConverter implements XmlConverter<XmlAccount, AccountData> {

	@Override
	public XmlAccount convertToXml(AccountData account) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccountData convertToObject(XmlAccount xmlAccount) {
		String owner = xmlAccount.getOwner();
		XmlBalances xmlBalances = xmlAccount.getXmlBalances();
		BalanceData balanceData = new BalanceXmlConverter().convertToObject(xmlBalances);
		List<XmlBuyOrder> xmlBuyOrders = xmlAccount.getXmlBuyOrders();
		List<BuyOrderData> buyOrdersData = createBuyOrders(xmlBuyOrders);
		List<XmlSellOrder> xmlSellOrders = xmlAccount.getXmlSellOrders();
		List<SellOrderData> sellOrdersData = createSellOrders(xmlSellOrders);
		List<XmlDeposit> xmlDeposits = xmlAccount.getXmlDeposits();
		List<DepositData> depositsData = createDeposits(xmlDeposits);
		List<XmlStrategy> xmlStrategies = xmlAccount.getXmlStrategies();
		Map<Currency, List<String>> currenciesStrategies = createCurrenciesStrategies(xmlStrategies);

		AccountData account = new AccountData(owner, balanceData, depositsData, buyOrdersData, sellOrdersData, currenciesStrategies);
		return account;
	}

	private Map<Currency, List<String>> createCurrenciesStrategies(List<XmlStrategy> xmlStrategies) {
		Map<Currency, List<String>> currenciesStrategies = new EnumMap<Currency, List<String>>(Currency.class);
		for (XmlStrategy xmlStrategy : xmlStrategies) {
			Currency currency = xmlStrategy.getCurrency();
			currenciesStrategies.put(currency, xmlStrategy.getClassNames());
		}
		return currenciesStrategies;
	}

	private List<SellOrderData> createSellOrders(List<XmlSellOrder> xmlSellOrders) {
		List<SellOrderData> sellOrdersData = new ArrayList<>();
		SellOrderXmlConverter sellOrderXmlConverter = new SellOrderXmlConverter();
		for (XmlSellOrder xmlSellOrder : xmlSellOrders) {
			SellOrderData sellOrderData = sellOrderXmlConverter.convertToObject(xmlSellOrder);
			sellOrdersData.add(sellOrderData);
		}
		return sellOrdersData;
	}

	private List<DepositData> createDeposits(List<XmlDeposit> xmlDeposits) {
		DepositXmlConverter depositXmlConverter = new DepositXmlConverter();
		List<DepositData> deposits = new ArrayList<>();
		for (XmlDeposit xmlDeposit : xmlDeposits) {
			DepositData depositData = depositXmlConverter.convertToObject(xmlDeposit);
			deposits.add(depositData);
		}
		return deposits;
	}

	private List<BuyOrderData> createBuyOrders(List<XmlBuyOrder> xmlBuyOrders) {
		List<BuyOrderData> buyOrdersData = new ArrayList<>();
		BuyOrderXmlConverter buyOrderXmlConverter = new BuyOrderXmlConverter();
		for (XmlBuyOrder xmlBuyOrder : xmlBuyOrders) {
			BuyOrderData buyOrderData = buyOrderXmlConverter.convertToObject(xmlBuyOrder);
			buyOrdersData.add(buyOrderData);
		}
		return buyOrdersData;
	}

}
