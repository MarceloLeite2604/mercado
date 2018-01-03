package org.marceloleite.mercado.converter.xml;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.CurrencyAmount;
import org.marceloleite.mercado.simulator.structure.Deposit;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

public class DepositXmlConverter implements XmlConverter<XmlDeposit, Deposit> {

	@Override
	public XmlDeposit convertToXml(Deposit deposit) {
		LocalDateTime time = deposit.getTime();
		Currency currency = deposit.getCurrencyAmount().getCurrency();
		double amount = deposit.getCurrencyAmount().getAmount();
		return new XmlDeposit(time, currency, amount);
	}

	@Override
	public Deposit convertToObject(XmlDeposit xmlDeposit) {
		Double amount = xmlDeposit.getAmount();
		Currency currency = xmlDeposit.getCurrency();
		LocalDateTime time = xmlDeposit.getTime();
		CurrencyAmount currencyAmount = new CurrencyAmount(currency, amount);
		return new Deposit(time, currencyAmount);
	}

}
