package org.marceloleite.mercado.converter.xml;

import java.util.List;

import org.marceloleite.mercado.simulator.structure.Account;
import org.marceloleite.mercado.simulator.structure.Balance;
import org.marceloleite.mercado.simulator.structure.BuyOrder;
import org.marceloleite.mercado.simulator.structure.Deposit;
import org.marceloleite.mercado.simulator.structure.TemporalController;
import org.marceloleite.mercado.simulator.structure.TemporalToken;
import org.marceloleite.mercado.xml.structures.XmlAccount;
import org.marceloleite.mercado.xml.structures.XmlBalances;
import org.marceloleite.mercado.xml.structures.XmlBuyOrder;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

public class AccountXmlConverter implements XmlConverter<XmlAccount, Account> {

	@Override
	public XmlAccount convertToXml(Account account) {

		return null;
	}

	@Override
	public Account convertToObject(XmlAccount xmlAccount) {
		String owner = xmlAccount.getOwner();
		XmlBalances xmlBalances = xmlAccount.getXmlBalances();
		Balance balance = new BalanceXmlConverter().convertToObject(xmlBalances);
		List<XmlBuyOrder> xmlBuyOrders = xmlAccount.getXmlBuyOrders();
		TemporalController<BuyOrder> buyOrders = createBuyOrders(xmlBuyOrders);
		List<XmlDeposit> xmlDeposits = xmlAccount.getXmlDeposits();
		TemporalController<Deposit> deposits = createDeposits(xmlDeposits);
		
		Account account = new Account(owner, balance, deposits, buyOrders);
		return account;
	}

	private TemporalController<Deposit> createDeposits(List<XmlDeposit> xmlDeposits) {
		DepositXmlConverter depositXmlConverter = new DepositXmlConverter();
		TemporalController<Deposit> deposits = new TemporalController<>();
		for (XmlDeposit xmlDeposit : xmlDeposits) {
			Deposit deposit = depositXmlConverter.convertToObject(xmlDeposit);
			TemporalToken<Deposit> temporalToken = new TemporalToken<>(deposit.getTime(), deposit);
			deposits.add(temporalToken);
		}
		return deposits;
	}

	private TemporalController<BuyOrder> createBuyOrders(List<XmlBuyOrder> xmlBuyOrders) {
		TemporalController<BuyOrder> buyOrders = new TemporalController<>();
		BuyOrderXmlConverter buyOrderXmlConverter = new BuyOrderXmlConverter();
		for (XmlBuyOrder xmlBuyOrder : xmlBuyOrders) {
			BuyOrder buyOrder = buyOrderXmlConverter.convertToObject(xmlBuyOrder);
			TemporalToken<BuyOrder> temporalToken = new TemporalToken<>(buyOrder.getTime(), buyOrder);
			buyOrders.add(temporalToken);
		}
		return buyOrders;
	}

}
