package org.marceloleite.mercado.converter.xml;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.AccountData;
import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.base.model.data.BuyOrderData;
import org.marceloleite.mercado.base.model.data.DepositData;
import org.marceloleite.mercado.base.model.data.SellOrderData;
import org.marceloleite.mercado.base.model.data.StrategyData;
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
		List<BalanceData> balanceDatas = new BalanceXmlConverter().convertToObject(xmlBalances);
		List<XmlBuyOrder> xmlBuyOrders = xmlAccount.getXmlBuyOrders();
		List<BuyOrderData> buyOrdersData = createBuyOrders(xmlBuyOrders);
		List<XmlSellOrder> xmlSellOrders = xmlAccount.getXmlSellOrders();
		List<SellOrderData> sellOrdersData = createSellOrders(xmlSellOrders);
		List<XmlDeposit> xmlDeposits = xmlAccount.getXmlDeposits();
		List<DepositData> depositsData = createDeposits(xmlDeposits);
		List<XmlStrategy> xmlStrategies = xmlAccount.getXmlStrategies();
		List<StrategyData> strategyDatas = createCurrenciesStrategies(xmlStrategies);

		AccountData account = new AccountData(owner, balanceDatas, depositsData, buyOrdersData, sellOrdersData, strategyDatas);
		return account;
	}

	private List<StrategyData> createCurrenciesStrategies(List<XmlStrategy> xmlStrategies) {
		StrategyDataXmlConverter strategyDataXmlConverter = new StrategyDataXmlConverter();
		List<StrategyData> strategyDatas = new ArrayList<>();
		for (XmlStrategy xmlStrategy : xmlStrategies) {
			StrategyData strategyData = strategyDataXmlConverter.convertToObject(xmlStrategy);
			strategyDatas.add(strategyData);
		}
		return strategyDatas;
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
