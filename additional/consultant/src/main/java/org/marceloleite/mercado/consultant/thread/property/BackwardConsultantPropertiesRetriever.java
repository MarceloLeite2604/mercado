package org.marceloleite.mercado.consultant.thread.property;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class BackwardConsultantPropertiesRetriever extends AbstractConsultantThreadPropertiesRetriever {

	@Override
	protected Duration retrieveTimeInterval() {
		return retrieveDurationProperty(ConsultantProperty.BACKWARD_TIME_INTERVAL,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TIME_INTERVAL_DURATION);
	}

	@Override
	protected Duration retrieveTradeRetrieveDuration() {
		return retrieveDurationProperty(ConsultantProperty.BACKWARD_TRADE_RETRIEVE_DURATION,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TRADE_RETRIEVE_DURATION);

	}

	@Override
	protected ZonedDateTime retrieveEndTime() {
		return retrieveZonedDateTimeProperty(ConsultantProperty.BACKWARD_END_TIME,
				AbstractConsultantThreadPropertiesRetriever.OLDEST_SIMULATION_TIME);
	}

	@Override
	protected ZonedDateTime retrieveStartTime() {
		ZonedDateTime startTime;
		startTime = retrieveZonedDateTimeProperty(ConsultantProperty.BACKWARD_START_TIME, null);

		if (startTime == null) {
			startTime = retrieveBackwardStartTimeFromOldestTrade();
		}

		if (startTime == null) {
			startTime = AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME;
		}
		return startTime;
	}

	@Override
	protected boolean retrieveDatabaseValuesIgnored() {
		return retrieveBooleanProperty(ConsultantProperty.BACKWARD_IGNORE_VALUES_ON_DATABASE, false);
	}

	@Override
	protected Duration retrieveTradesSiteRetrieverStepDuration() {
		return retrieveDurationProperty(ConsultantProperty.BACKWARD_TRADES_SITE_RETRIEVER_STEP_DURATION, null);
	}

	private ZonedDateTime retrieveBackwardStartTimeFromOldestTrade() {
		TradePO oldestTrade = getTradeDAO().retrieveOldestTrade();
		if (oldestTrade == null) {
			return null;
		} else {
			return oldestTrade.getTradeDate();
		}
	}
}
