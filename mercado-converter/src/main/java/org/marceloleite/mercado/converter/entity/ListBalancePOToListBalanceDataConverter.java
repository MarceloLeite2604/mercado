package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.BalanceData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.BalancePO;

public class ListBalancePOToListBalanceDataConverter implements Converter<List<BalancePO>, List<BalanceData>>{

	@Override
	public List<BalanceData> convertTo(List<BalancePO> balancePOs) {
		List<BalanceData> balanceDatas = new ArrayList<>();
		BalancePOToBalanceDataConverter balancePOToBalanceDataConverter = new BalancePOToBalanceDataConverter();
		for (BalancePO balancePO : balancePOs) {
			balanceDatas.add(balancePOToBalanceDataConverter.convertTo(balancePO));
		}
		return balanceDatas;
	}

	@Override
	public List<BalancePO> convertFrom(List<BalanceData> balanceDatas) {
		List<BalancePO> balancePOs = new ArrayList<>();
		BalancePOToBalanceDataConverter balancePOToBalanceDataConverter = new BalancePOToBalanceDataConverter();
		for (BalanceData balanceData : balanceDatas) {
			balancePOs.add(balancePOToBalanceDataConverter.convertFrom(balanceData));
		}
		return balancePOs;
	}

}
