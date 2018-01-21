package org.marceloleite.mercado.retriever.database;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.Entity;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class TradesDatabaseUtils {

	private static TimeInterval cachedTimeIntervalAvailable;

	private static final String QUERY_MAXIMUM_DATE_RETRIEVED = "SELECT max(date) FROM " + Entity.TRADE.getName();

	private static final String QUERY_MINIMUM_DATE_RETRIEVED = "SELECT min(date) FROM " + Entity.TRADE.getName();

	private EntityManager entityManager;

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
		LocalDateTime minimumTimeRetrieved = obtainMinimumTimeRetrieved();
		LocalDateTime maximumTimeRetrieved = obtainMaximumTimeRetrieved();

		if (minimumTimeRetrieved != null && maximumTimeRetrieved != null) {
			return new TimeInterval(minimumTimeRetrieved, maximumTimeRetrieved);
		} else {
			return null;
		}
	}

	private void createEntityManager() {
		if (entityManager == null) {
			entityManager = EntityManagerController.getInstance().createEntityManager();
		}
	}

	private LocalDateTime obtainMaximumTimeRetrieved() {
		return getQueryResultAsLocalDateTime(QUERY_MAXIMUM_DATE_RETRIEVED);
	}

	private LocalDateTime obtainMinimumTimeRetrieved() {
		return getQueryResultAsLocalDateTime(QUERY_MINIMUM_DATE_RETRIEVED);
	}

	private LocalDateTime getQueryResultAsLocalDateTime(String stringQuery) {
		Query query = createQuery(stringQuery);
		Object queryResult = query.getSingleResult();
		LocalDateTime result;
		if (queryResult != null) {
			if (queryResult instanceof LocalDateTime) {
				result = (LocalDateTime) queryResult;
			} else {
				throw new RuntimeException("Cannot convert query result to \"" + LocalDateTime.class.getName()
						+ "\". Object is of type \"" + queryResult.getClass().getName() + "\".");
			}
			return result;
		} else {
			return null;
		}
	}

	private Query createQuery(String query) {
		createEntityManager();
		return entityManager.createQuery(query);
	}

}
