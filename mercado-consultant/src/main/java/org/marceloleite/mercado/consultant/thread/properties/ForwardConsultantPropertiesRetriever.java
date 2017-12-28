package org.marceloleite.mercado.consultant.thread.properties;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;

public class ForwardConsultantPropertiesRetriever extends AbstractConsultantThreadPropertiesRetriever {

	@Override
	protected Duration retrieveTimeInterval() {
		return retrieveDurationFromProperties(ConsultantProperty.FORWARD_TIME_INTERVAL,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TIME_INTERVAL_DURATION);
	}

	@Override
	protected Duration retrieveTradeRetrieveDuration() {
		return retrieveDurationFromProperties(ConsultantProperty.FORWARD_TRADE_RETRIEVE_DURATION,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TRADE_RETRIEVE_DURATION);

	}

	@Override
	protected LocalDateTime retrieveEndTime() {
		return retrieveLocalDateTimeFromProperty(ConsultantProperty.FORWARD_END_TIME,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME);
	}

	@Override
	protected LocalDateTime retrieveStartTime() {
		LocalDateTime startTime;
		startTime = retrieveLocalDateTimeFromProperty(ConsultantProperty.FORWARD_START_TIME, null);

		if (startTime == null) {
			startTime = retrieveForwardStartTimeFromNewestTrade();
		}

		if (startTime == null) {
			startTime = AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME;
		}
		return startTime;
	}

	private LocalDateTime retrieveForwardStartTimeFromNewestTrade() {
		TradePO newestTrade = getTradeDAO().retrieveNewestTrade();
		if (newestTrade == null) {
			return null;
		} else {
			return newestTrade.getDate();
		}
	}
}
