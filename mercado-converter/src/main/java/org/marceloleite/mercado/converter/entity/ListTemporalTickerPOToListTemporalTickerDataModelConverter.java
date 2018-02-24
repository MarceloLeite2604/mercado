package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class ListTemporalTickerPOToListTemporalTickerDataModelConverter implements Converter<List<TemporalTickerPO>, List<TemporalTickerDataModel>>{

	@Override
	public List<TemporalTickerDataModel> convertTo(List<TemporalTickerPO> temporalTickerPOs) {
		TemporalTickerPOToTemporalTickerDataModelConverter temporalTickerPOToTemporalTickerDataModelConverter = new TemporalTickerPOToTemporalTickerDataModelConverter();
		List<TemporalTickerDataModel> temporalTickerDataModels = new ArrayList<>();
		for (TemporalTickerPO temporalTickerPO : temporalTickerPOs) {
			temporalTickerDataModels.add(temporalTickerPOToTemporalTickerDataModelConverter.convertTo(temporalTickerPO));
		}
		return temporalTickerDataModels;
	}

	@Override
	public List<TemporalTickerPO> convertFrom(List<TemporalTickerDataModel> temporalTickerDataModels) {
		TemporalTickerPOToTemporalTickerDataModelConverter temporalTickerPOToTemporalTickerDataModelConverter = new TemporalTickerPOToTemporalTickerDataModelConverter();
		List<TemporalTickerPO> temporalTickerPOs = new ArrayList<>();
		for (TemporalTickerDataModel temporalTickerDataModel : temporalTickerDataModels) {
			temporalTickerPOs.add(temporalTickerPOToTemporalTickerDataModelConverter.convertFrom(temporalTickerDataModel));
		}
		return temporalTickerPOs;
	}

}
