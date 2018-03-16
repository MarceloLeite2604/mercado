package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.WithdrawalData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.WithdrawalPO;

public class ListWithdrawalPOToListWithdrawalDataConverter implements Converter<List<WithdrawalPO>, List<WithdrawalData>> {

	@Override
	public List<WithdrawalData> convertTo(List<WithdrawalPO> withdrawalPOs) {
		List<WithdrawalData> withdrawalDatas = new ArrayList<>();
		if (withdrawalPOs != null && !withdrawalPOs.isEmpty()) {
			WithdrawalPOToWithdrawalDataConverter withdrawalPOToWithdrawalDataConverter = new WithdrawalPOToWithdrawalDataConverter();
			for (WithdrawalPO withdrawalPO : withdrawalPOs) {
				withdrawalDatas.add(withdrawalPOToWithdrawalDataConverter.convertTo(withdrawalPO));
			}
		}
		return withdrawalDatas;
	}

	@Override
	public List<WithdrawalPO> convertFrom(List<WithdrawalData> withdrawalDatas) {
		List<WithdrawalPO> withdrawalPOs = new ArrayList<>();
		if (withdrawalDatas != null && !withdrawalDatas.isEmpty()) {
			WithdrawalPOToWithdrawalDataConverter withdrawalPOToWithdrawalDataConverter = new WithdrawalPOToWithdrawalDataConverter();
			for (WithdrawalData withdrawalData : withdrawalDatas) {
				withdrawalPOs.add(withdrawalPOToWithdrawalDataConverter.convertFrom(withdrawalData));
			}
		}
		return withdrawalPOs;
	}

}
