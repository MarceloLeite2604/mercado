package org.marceloleite.mercado.consultant.thread.properties;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;

public class BackwardConsultantPropertiesRetriever extends AbstractConsultantThreadPropertiesRetriever {

	@Override
	protected Duration retrieveTimeInterval() {
		return retrieveDurationFromProperties(ConsultantProperty.BACKWARD_TIME_INTERVAL,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TIME_INTERVAL_DURATION);
	}

	@Override
	protected Duration retrieveTradeRetrieveDuration() {
		return retrieveDurationFromProperties(ConsultantProperty.BACKWARD_TRADE_RETRIEVE_DURATION,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TRADE_RETRIEVE_DURATION);

	}

	@Override
	protected LocalDateTime retrieveEndTime() {
		return retrieveLocalDateTimeFromProperty(ConsultantProperty.BACKWARD_END_TIME,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME);
	}

	@Override
	protected LocalDateTime retrieveStartTime() {
		LocalDateTime startTime;
		startTime = retrieveLocalDateTimeFromProperty(ConsultantProperty.BACKWARD_START_TIME, null);

		if (startTime == null) {
			startTime = retrieveBackwardStartTimeFromOldestTrade();
		}

		if (startTime == null) {
			startTime = AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME;
		}
		return startTime;
	}

	private LocalDateTime retrieveBackwardStartTimeFromOldestTrade() {
		TradePO oldestTrade = getTradeDAO().retrieveOldestTrade();
		if (oldestTrade == null) {
			return null;
		} else {
			return oldestTrade.getDate();
		}
	}
}
