package org.marceloleite.mercado.retriever.database;

import java.time.ZonedDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.objects.Entity;

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
		ZonedDateTime minimumTimeRetrieved = obtainMinimumTimeRetrieved();
		ZonedDateTime maximumTimeRetrieved = obtainMaximumTimeRetrieved();

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

	private ZonedDateTime obtainMaximumTimeRetrieved() {
		return getQueryResultAsZonedDateTime(QUERY_MAXIMUM_DATE_RETRIEVED);
	}

	private ZonedDateTime obtainMinimumTimeRetrieved() {
		return getQueryResultAsZonedDateTime(QUERY_MINIMUM_DATE_RETRIEVED);
	}

	private ZonedDateTime getQueryResultAsZonedDateTime(String stringQuery) {
		Query query = createQuery(stringQuery);
		Object queryResult = query.getSingleResult();
		ZonedDateTime result;
		if (queryResult != null) {
			if (queryResult instanceof ZonedDateTime) {
				result = (ZonedDateTime) queryResult;
			} else {
				throw new RuntimeException("Cannot convert query result to \"" + ZonedDateTime.class.getName()
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
