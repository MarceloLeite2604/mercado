package org.marceloleite.mercado.converter.xml;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.CurrencyAmountData;
import org.marceloleite.mercado.simulator.structure.DepositData;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

public class DepositXmlConverter implements XmlConverter<XmlDeposit, DepositData> {

	@Override
	public XmlDeposit convertToXml(DepositData deposit) {
		LocalDateTime time = deposit.getTime();
		Currency currency = deposit.getCurrencyAmount().getCurrency();
		double amount = deposit.getCurrencyAmount().getAmount();
		return new XmlDeposit(time, currency, amount);
	}

	@Override
	public DepositData convertToObject(XmlDeposit xmlDeposit) {
		Double amount = xmlDeposit.getAmount();
		Currency currency = xmlDeposit.getCurrency();
		LocalDateTime time = xmlDeposit.getTime();
		CurrencyAmountData currencyAmount = new CurrencyAmountData(currency, amount);
		return new DepositData(time, currencyAmount);
	}

}
