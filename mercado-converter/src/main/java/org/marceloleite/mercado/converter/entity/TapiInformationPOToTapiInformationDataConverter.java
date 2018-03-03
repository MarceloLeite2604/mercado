package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.TapiInformationData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TapiInformationIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TapiInformationPO;

public class TapiInformationPOToTapiInformationDataConverter implements Converter<TapiInformationPO, TapiInformationData>{

	@Override
	public TapiInformationData convertTo(TapiInformationPO tapiInformationPO) {
		TapiInformationData tapiInformationData = new TapiInformationData();
		tapiInformationData.setId(tapiInformationPO.getId().getId());
		tapiInformationData.setSecret(tapiInformationPO.getSecret());
		return tapiInformationData;
	}

	@Override
	public TapiInformationPO convertFrom(TapiInformationData tapiInformationData) {
		TapiInformationPO tapiInformationPO = new TapiInformationPO();
		TapiInformationIdPO tapiInformationIdPO = new TapiInformationIdPO();
		tapiInformationIdPO.setAccoOwner(tapiInformationData.getAccountData().getOwner());
		tapiInformationIdPO.setId(tapiInformationData.getId());
		tapiInformationPO.setId(tapiInformationIdPO);
		tapiInformationPO.setSecret(tapiInformationData.getSecret());
		return tapiInformationPO;
	}

}
