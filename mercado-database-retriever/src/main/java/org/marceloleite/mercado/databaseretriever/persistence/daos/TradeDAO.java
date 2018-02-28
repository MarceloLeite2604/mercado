package org.marceloleite.mercado.databaseretriever.persistence.daos;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.ClassCastExceptionThrower;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.converters.CurrencyAttributeConverter;
import org.marceloleite.mercado.databaseretriever.persistence.converters.TradeTypeAttributeConverter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.Entity;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PersistenceObject;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class TradeDAO extends AbstractDAO<TradePO> {

	private static final String START_PARAMETER = "start";

	private static final String END_PARAMETER = "end";

	private static final String CURRENCY_PARAMETER = "currency";

	private static final String TRADE_TYPE_PARAMETER = "tradeType";

	private static final String DATE_PARAMETER = "date";

	private static final String BETWEEN_TIME_INTERVAL_QUERY = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE currency = :" + CURRENCY_PARAMETER + " AND trade_date >= :" + START_PARAMETER + " AND trade_date < :"
			+ END_PARAMETER;

	private static final String NEWEST_TRADE_RETRIEVED = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE date = ( SELECT max(trade_date) FROM " + Entity.TRADE.getName() + ")";

	private static final String OLDEST_TRADE_RETRIEVED = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE date = ( SELECT min(trade_date) FROM " + Entity.TRADE.getName() + ")";

	private static final String PREVIOUS_TRADE = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE trade_id = (SELECT max(trade_id) FROM " + Entity.TRADE.getName()
			+ "  WHERE trade_date = ( SELECT max(trade_date) FROM " + Entity.TRADE.getName() + " WHERE trade_type = :"
			+ TRADE_TYPE_PARAMETER + " AND currency = :" + CURRENCY_PARAMETER + " AND trade_date <= :" + DATE_PARAMETER
			+ ") ) AND currency = :" + CURRENCY_PARAMETER;

	private static final String NEXT_TRADE = "SELECT * FROM " + Entity.TRADE.getName()
			+ " WHERE trade_id = (SELECT min(trade_id) FROM " + Entity.TRADE.getName()
			+ "  WHERE trade_date = ( SELECT min(trade_date) FROM " + Entity.TRADE.getName() + " WHERE trade_type = :"
			+ TRADE_TYPE_PARAMETER + " AND currency = :" + CURRENCY_PARAMETER + " AND trade_date >= :" + DATE_PARAMETER
			+ ") ) AND currency = :" + CURRENCY_PARAMETER;

	public List<TradePO> retrieve(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		createEntityManager();

		CurrencyAttributeConverter currencyAttributeConverter = new CurrencyAttributeConverter();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(START_PARAMETER, start);
		parameters.put(END_PARAMETER, end);
		parameters.put(CURRENCY_PARAMETER, currencyAttributeConverter.convertToDatabaseColumn(currency));
		List<? extends PersistenceObject<?>> queryResult = executeQuery(BETWEEN_TIME_INTERVAL_QUERY, parameters);
		return castToTradeList(queryResult);
	}

	public List<TradePO> castToTradeList(List<? extends PersistenceObject<?>> objects) {
		List<TradePO> trades = new ArrayList<>();

		for (Object object : objects) {
			trades.add(castToTradePO(object));
		}

		return trades;
	}

	public TradePO castToTradePO(Object object) {

		if (object == null) {
			return (TradePO) null;
		}

		if (!(object instanceof TradePO)) {
			new ClassCastExceptionThrower(getPOClass(), object).throwException();
		}
		return (TradePO) object;
	}

	public TradePO retrieveNewestTrade() {
		PersistenceObject<?> queryResult = executeQueryForSingleResult(NEWEST_TRADE_RETRIEVED, null);
		return castToTradePO(queryResult);
	}

	public TradePO retrieveOldestTrade() {
		PersistenceObject<?> queryResult = executeQueryForSingleResult(OLDEST_TRADE_RETRIEVED, null);
		return castToTradePO(queryResult);
	}

	@Override
	public Class<? extends PersistenceObject<?>> getPOClass() {
		return TradePO.class;
	}

	@Override
	public TradePO castPersistenceObject(PersistenceObject<?> persistenceObject) {
		if (!(persistenceObject instanceof TradePO)) {
			new ClassCastExceptionThrower(TradePO.class, persistenceObject);
		}

		return (TradePO) persistenceObject;
	}

	public TradePO retrievePreviousTrade(Currency currency, TradeType tradeType, ZonedDateTime date) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TRADE_TYPE_PARAMETER, new TradeTypeAttributeConverter().convertToDatabaseColumn(tradeType));
		parameters.put(CURRENCY_PARAMETER, new CurrencyAttributeConverter().convertToDatabaseColumn(currency));
		parameters.put(DATE_PARAMETER, date);
		PersistenceObject<?> queryResult = executeQueryForSingleResult(PREVIOUS_TRADE, parameters);
		return castToTradePO(queryResult);
	}

	public TradePO retrieveNextTrade(Currency currency, TradeType tradeType, ZonedDateTime date) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TRADE_TYPE_PARAMETER, new TradeTypeAttributeConverter().convertToDatabaseColumn(tradeType));
		parameters.put(CURRENCY_PARAMETER, new CurrencyAttributeConverter().convertToDatabaseColumn(currency));
		parameters.put(DATE_PARAMETER, date);
		PersistenceObject<?> queryResult = executeQueryForSingleResult(NEXT_TRADE, parameters);
		return castToTradePO(queryResult);
	}
}
