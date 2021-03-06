package org.marceloleite.mercado.retriever.converter;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerIdPO;

public class TimeIntervalToTemporalTickerIdConverter implements Converter<TimeInterval, TemporalTickerIdPO> {

	@Override
	public TemporalTickerIdPO convertTo(TimeInterval timeInterval) {
		TemporalTickerIdPO temporalTickerId = new TemporalTickerIdPO();
		temporalTickerId.setStartTime(timeInterval.getStart());
		temporalTickerId.setEndTime(timeInterval.getEnd());
		return temporalTickerId;
	}

	@Override
	public TimeInterval convertFrom(TemporalTickerIdPO object) {
		throw new UnsupportedOperationException();
	}

}
