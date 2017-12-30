package org.marceloleite.mercado.consultant.thread.property;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;

public class ForwardConsultantPropertiesRetriever extends AbstractConsultantThreadPropertiesRetriever {

	@Override
	protected Duration retrieveTimeInterval() {
		return retrieveDurationProperty(ConsultantProperty.FORWARD_TIME_INTERVAL,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TIME_INTERVAL_DURATION);
	}

	@Override
	protected Duration retrieveTradeRetrieveDuration() {
		return retrieveDurationProperty(ConsultantProperty.FORWARD_TRADE_RETRIEVE_DURATION,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_TRADE_RETRIEVE_DURATION);

	}

	@Override
	protected LocalDateTime retrieveEndTime() {
		return retrieveLocalDateTimeProperty(ConsultantProperty.FORWARD_END_TIME,
				AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME);
	}

	@Override
	protected LocalDateTime retrieveStartTime() {
		LocalDateTime startTime;
		startTime = retrieveLocalDateTimeProperty(ConsultantProperty.FORWARD_START_TIME, null);

		if (startTime == null) {
			startTime = retrieveForwardStartTimeFromNewestTrade();
		}

		if (startTime == null) {
			startTime = AbstractConsultantThreadPropertiesRetriever.DEFAULT_START_TIME;
		}
		return startTime;
	}
	
	@Override
	protected boolean retrieveDatabaseValuesIgnored() {
		return retrieveBooleanProperty(ConsultantProperty.FORWARD_IGNORE_VALUES_ON_DATABASE, false);
	}
	
	@Override
	protected Duration retrieveTradesSiteRetrieverStepDuration() {
		return retrieveDurationProperty(ConsultantProperty.FORWARD_TRADES_SITE_RETRIEVER_STEP_DURATION, null);
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
