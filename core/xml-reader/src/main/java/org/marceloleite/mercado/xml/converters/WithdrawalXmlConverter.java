package org.marceloleite.mercado.xml.converters;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.data.WithdrawalData;
import org.marceloleite.mercado.xml.structures.XmlWithdrawals;

public class WithdrawalXmlConverter implements XmlConverter<XmlWithdrawals, List<WithdrawalData>> {

	@Override
	public XmlWithdrawals convertToXml(List<WithdrawalData> withdrawalDatas) {
		XmlWithdrawals xmlWithdrawals = new XmlWithdrawals();
		for (WithdrawalData withdrawalData : withdrawalDatas) {
			xmlWithdrawals.put(withdrawalData.getCurrency(), withdrawalData.getAmount());
		}
		return xmlWithdrawals;
	}

	@Override
	public List<WithdrawalData> convertToObject(XmlWithdrawals xlmWithdrawals) {
		List<WithdrawalData> withdrawalDatas = new ArrayList<>();
		if (xlmWithdrawals != null) {
			for (Currency currency : Currency.values()) {
				Double amount = xlmWithdrawals.getOrDefault(currency, 0.0);
				WithdrawalData withdrawalData = new WithdrawalData();
				withdrawalData.setCurrency(currency);
				withdrawalData.setAmount(amount);
				withdrawalDatas.add(withdrawalData);
			}
		}
		return withdrawalDatas;
	}
}
