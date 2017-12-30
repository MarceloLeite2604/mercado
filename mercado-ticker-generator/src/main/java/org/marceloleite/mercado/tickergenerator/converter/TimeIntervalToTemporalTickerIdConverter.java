package org.marceloleite.mercado.tickergenerator.converter;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;

public class TimeIntervalToTemporalTickerIdConverter implements Converter<TimeInterval, TemporalTickerIdPO> {

	@Override
	public TemporalTickerIdPO convert(TimeInterval timeInterval) {
		TemporalTickerIdPO temporalTickerId = new TemporalTickerIdPO();
		temporalTickerId.setStart(timeInterval.getStart());
		temporalTickerId.setEnd(timeInterval.getEnd());
		return temporalTickerId;
	}

}
