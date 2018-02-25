package org.marceloleite.mercado.converter.xml;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.base.model.data.CurrencyAmountData;
import org.marceloleite.mercado.base.model.data.DepositData;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.structures.XmlDeposit;

public class DepositXmlConverter implements XmlConverter<XmlDeposit, DepositData> {

	@Override
	public XmlDeposit convertToXml(DepositData deposit) {
		ZonedDateTime time = deposit.getTime();
		Currency currency = deposit.getCurrencyAmount().getCurrency();
		double amount = deposit.getCurrencyAmount().getAmount();
		return new XmlDeposit(time, currency, amount);
	}

	@Override
	public DepositData convertToObject(XmlDeposit xmlDeposit) {
		Double amount = xmlDeposit.getAmount();
		Currency currency = xmlDeposit.getCurrency();
		ZonedDateTime time = xmlDeposit.getTime();
		CurrencyAmountData currencyAmount = new CurrencyAmountData(currency, amount);
		return new DepositData(time, currencyAmount);
	}

}
