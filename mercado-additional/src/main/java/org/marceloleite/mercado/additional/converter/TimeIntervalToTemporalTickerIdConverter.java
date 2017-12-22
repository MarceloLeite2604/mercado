package org.marceloleite.mercado.additional.converter;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TemporalTickerId;

public class TimeIntervalToTemporalTickerIdConverter implements Converter<TimeInterval, TemporalTickerId> {

	@Override
	public TemporalTickerId convert(TimeInterval timeInterval) {
		TemporalTickerId temporalTickerId = new TemporalTickerId();
		temporalTickerId.setStart(timeInterval.getStart());
		temporalTickerId.setEnd(timeInterval.getEnd());
		return temporalTickerId;
	}

}
