package org.marceloleite.mercado.retriever.database;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databaseretriever.persistence.daos.TradeDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class TradesDatabaseUtils {

	private static TimeInterval cachedTimeIntervalAvailable;

	public TimeInterval retrieveTimeIntervalAvailable(boolean retrieveFromCache) {
		if (retrieveFromCache) {
			if (cachedTimeIntervalAvailable == null) {
				cachedTimeIntervalAvailable = elaborateTimeIntervalAvailable();
			}
			return cachedTimeIntervalAvailable;
		} else {

			return elaborateTimeIntervalAvailable();
		}
	}

	private TimeInterval elaborateTimeIntervalAvailable() {
		TradeDAO tradeDAO = new TradeDAO();
		TradePO oldestTrade = tradeDAO.retrieveOldestTrade();
		TradePO newestTrade = tradeDAO.retrieveNewestTrade();

		if (oldestTrade != null && newestTrade != null) {
			TimeInterval timeInterval = new TimeInterval(oldestTrade.getTradeDate(), newestTrade.getTradeDate());
			return timeInterval;
		} else {
			return null;
		}
	}
}
