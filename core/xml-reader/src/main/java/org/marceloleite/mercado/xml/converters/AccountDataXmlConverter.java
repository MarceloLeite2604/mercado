package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.data.AccountData;
import org.marceloleite.mercado.data.BalanceData;
import org.marceloleite.mercado.data.BuyOrderData;
import org.marceloleite.mercado.data.DepositData;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.data.SellOrderData;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.data.TapiInformationData;
import org.marceloleite.mercado.data.WithdrawalData;
import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlDeposit;
import org.marceloleite.mercado.xml.structures.XmlSellOrder;
import org.marceloleite.mercado.xml.structures.XmlStrategy;
import org.marceloleite.mercado.xml.structures.XmlTapiInformation;
import org.marceloleite.mercado.xml.structures.XmlWithdrawals;

public class AccountDataXmlConverter implements XmlConverter<XmlAccount, AccountData> {

	@Override
	public XmlAccount convertToXml(AccountData account) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccountData convertToObject(XmlAccount xmlAccount) {
		String owner = xmlAccount.getOwner();
		String email = xmlAccount.getEmail();
		XmlBalances xmlBalances = xmlAccount.getXmlBalances();
		XmlWithdrawals xmlWithdrawals = xmlAccount.getXmlWithdrawals();
		AccountData accountData = new AccountData();

		accountData.setOwner(owner);
		accountData.setEmail(email);

		XmlTapiInformation xmlTapiInformation = xmlAccount.getXmlTapiInformation();
		TapiInformationData tapiInformationData = new TapiInformationsDataXmlConverter().convertToObject(xmlTapiInformation);
		tapiInformationData.setAccountData(accountData);
		accountData
				.setTapiInformationData(tapiInformationData);

		List<BalanceData> balanceDatas = new BalanceXmlConverter().convertToObject(xmlBalances);
		balanceDatas.forEach(balanceData -> balanceData.setAccountData(accountData));
		accountData.setBalanceDatas(balanceDatas);
		
		List<WithdrawalData> withdrawalDatas = new WithdrawalXmlConverter().convertToObject(xmlWithdrawals);
		withdrawalDatas.forEach(withdrawalData -> withdrawalData.setAccountData(accountData));
		accountData.setWithdrawalDatas(withdrawalDatas);

		List<OrderData> buyOrderDatas = createBuyOrders(xmlAccount.getXmlBuyOrders());
		withdrawalDatas.forEach(balanceData -> balanceData.setAccountData(accountData));
		accountData.setBuyOrderDatas(buyOrderDatas);

		List<OrderData> sellOrderDatas = createSellOrders(xmlAccount.getXmlSellOrders());
		sellOrderDatas.forEach(sellOrderData -> sellOrderData.setAccountData(accountData));
		accountData.setSellOrderDatas(sellOrderDatas);

		List<DepositData> depositDatas = createDeposits(xmlAccount.getXmlDeposits());
		depositDatas.forEach(depositData -> depositData.setAccountData(accountData));
		accountData.setDepositDatas(depositDatas);

		List<StrategyData> strategyDatas = createStrategies(xmlAccount.getXmlStrategies());
		strategyDatas.forEach(strategyData -> strategyData.setAccountData(accountData));
		accountData.setStrategyDatas(strategyDatas);

		return accountData;
	}

	private List<StrategyData> createStrategies(List<XmlStrategy> xmlStrategies) {
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

	private List<OrderData> createBuyOrders(List<XmlBuyOrder> xmlBuyOrders) {
		List<OrderData> buyOrdersData = new ArrayList<>();
		OrderXmlConverter orderXmlConverter = new OrderXmlConverter();
		for (XmlBuyOrder xmlBuyOrder : xmlBuyOrders) {
			BuyOrderData buyOrderData = buyOrderXmlConverter.convertToObject(xmlBuyOrder);
			buyOrdersData.add(buyOrderData);
		}
		return buyOrdersData;
	}

}
