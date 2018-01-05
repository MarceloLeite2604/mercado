package org.marceloleite.mercado.converter.xml;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.simulator.structure.AccountData;
import org.marceloleite.mercado.simulator.structure.BalanceData;
import org.marceloleite.mercado.simulator.structure.BuyOrderData;
import org.marceloleite.mercado.simulator.structure.DepositData;
import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

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
		List<XmlDeposit> xmlDeposits = xmlAccount.getXmlDeposits();
		List<DepositData> depositsData = createDeposits(xmlDeposits);

		AccountData account = new AccountData(owner, balanceData, depositsData, buyOrdersData, null);
		return account;
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
