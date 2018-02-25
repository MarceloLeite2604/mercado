package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.TemporalTicker;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerPO;

public class ListTemporalTickerPOToListTemporalTickerConverter implements Converter<List<TemporalTickerPO>, List<TemporalTicker>>{

	@Override
	public List<TemporalTicker> convertTo(List<TemporalTickerPO> temporalTickerPOs) {
		TemporalTickerPOToTemporalTickerConverter temporalTickerPOToTemporalTickerConverter = new TemporalTickerPOToTemporalTickerConverter();
		List<TemporalTicker> temporalTickers = new ArrayList<>();
		for (TemporalTickerPO temporalTickerPO : temporalTickerPOs) {
			temporalTickers.add(temporalTickerPOToTemporalTickerConverter.convertTo(temporalTickerPO));
		}
		return temporalTickers;
	}

	@Override
	public List<TemporalTickerPO> convertFrom(List<TemporalTicker> temporalTickers) {
		TemporalTickerPOToTemporalTickerConverter temporalTickerPOToTemporalTickerConverter = new TemporalTickerPOToTemporalTickerConverter();
		List<TemporalTickerPO> temporalTickerPOs = new ArrayList<>();
		for (TemporalTicker temporalTicker : temporalTickers) {
			temporalTickerPOs.add(temporalTickerPOToTemporalTickerConverter.convertFrom(temporalTicker));
		}
		return temporalTickerPOs;
	}

}
