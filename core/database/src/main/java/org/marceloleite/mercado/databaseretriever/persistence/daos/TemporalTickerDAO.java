package org.marceloleite.mercado.databaseretriever.persistence.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databaseretriever.persistence.converters.CurrencyAttributeConverter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.Entity;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TemporalTickerPO;

public class TemporalTickerDAO extends AbstractDAO<TemporalTickerPO> {

	private static final String CURRENCY_PARAMETER = "currency";

	private static final String DURATION_PARAMETER = "duration";

	private static final String START_PARAMETER = "start";

	private static final String END_PARAMETER = "end";

	private static final String BULK_RETRIEVE = "SELECT * FROM " + Entity.TEMPORAL_TICKER.getName()
			+ " WHERE currency = :" + CURRENCY_PARAMETER + " AND time_duration = :" + DURATION_PARAMETER
			+ " AND start_time >= :" + START_PARAMETER + " AND start_time < :" + END_PARAMETER;

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TemporalTickerPO.class;
	}

	@Override
	public TemporalTickerPO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof TemporalTickerPO)) {
			new ClassCastExceptionThrower(TemporalTickerPO.class, persistenceObject);
		}

		return (TemporalTickerPO) persistenceObject;
	}

	public List<TemporalTickerPO> bulkRetrieve(Currency currency, TimeDivisionController timeDivisionController) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(CURRENCY_PARAMETER, new CurrencyAttributeConverter().convertToDatabaseColumn(currency));
		parameters.put(DURATION_PARAMETER, timeDivisionController.getDivisionDuration().getSeconds());
		parameters.put(START_PARAMETER, timeDivisionController.getStart());
		parameters.put(END_PARAMETER, timeDivisionController.getEnd());
		List<? extends PersistenceObject<?>> queryResult = executeQuery(BULK_RETRIEVE, parameters);
		List<TemporalTickerPO> temporalTickerPOs = castToTradeList(queryResult);
		return temporalTickerPOs;
	}

	public List<TemporalTickerPO> castToTradeList(List<? extends PersistenceObject<?>> objects) {
		List<TemporalTickerPO> trades = new ArrayList<>();

		for (Object object : objects) {
			trades.add(castToTemporalTickerPO(object));
		}

		return trades;
	}

	public TemporalTickerPO castToTemporalTickerPO(Object object) {

		if (object == null) {
			return (TemporalTickerPO) null;
		}

		if (!(object instanceof TemporalTickerPO)) {
			new ClassCastExceptionThrower(getPOClass(), object).throwException();
		}
		return (TemporalTickerPO) object;
	}
}
