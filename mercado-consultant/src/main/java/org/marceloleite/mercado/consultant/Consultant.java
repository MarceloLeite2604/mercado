package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.consultant.configuration.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardPropertiesReader;

public class Consultant {
	
	private static final Duration DEFAULT_TRADE_RETRIEVE_DURATION = Duration.ofSeconds(10);

	StandardPropertiesReader standardPropertiesReader;

	private LocalDateTime lastTimeRetrieved;

	private Duration tradeRetrieveDuration;

	private TradeDAO tradeDAO;

	public Consultant() {
		this.tradeDAO = new TradeDAO();
		this.standardPropertiesReader = new StandardPropertiesReader();
	}

	public void startConsulting() {
		retrieveConfiguration();
		retrieveLastTimeRetrieved();
		retrieveConsultingDuration();
		System.out.println(tradeRetrieveDuration);
	}

	private void retrieveConsultingDuration() {
		Property consultingPeriodProperty = standardPropertiesReader.getProperty(ConsultantProperty.CONSULTING_PERIOD);
		System.out.println(new ObjectToJsonConverter().convert(consultingPeriodProperty));
		if (null != consultingPeriodProperty.getValue()) {
			tradeRetrieveDuration = new StringToDurationConverter().convert(consultingPeriodProperty.getValue());	
		} else {
			tradeRetrieveDuration = DEFAULT_TRADE_RETRIEVE_DURATION;
		}
		
	}

	private void retrieveLastTimeRetrieved() {
		TradePO newestTrade = tradeDAO.retrieveNewestTrade();
		if (null != newestTrade) {
			lastTimeRetrieved = newestTrade.getDate();
		} else {
			lastTimeRetrieved = LocalDateTime.of(2017, 01, 01, 0, 0);
		}
	}

	private void retrieveConfiguration() {
		standardPropertiesReader.readConfiguration();
	}

}
