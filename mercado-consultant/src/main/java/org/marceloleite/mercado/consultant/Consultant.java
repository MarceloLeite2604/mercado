package org.marceloleite.mercado.consultant;

import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.properties.StandardPropertiesReader;

public class Consultant {

	StandardPropertiesReader standardPropertiesReader;

	public void startConsulting() {
		TradeDAO tradeDAO = new TradeDAO();
		TradePO oldestTrade = tradeDAO.retrieveOldestTrade();
		TradePO newestTrade = tradeDAO.retrieveNewestTrade();
		
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		System.out.println(localDateTimeToStringConverter.convert(oldestTrade.getDate()));
		System.out.println(localDateTimeToStringConverter.convert(newestTrade.getDate()));
	}

	private void retrieveConfiguration() {
		standardPropertiesReader.readConfiguration();
	}

}
