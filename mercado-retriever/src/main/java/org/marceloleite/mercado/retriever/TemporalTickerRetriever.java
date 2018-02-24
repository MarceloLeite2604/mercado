package org.marceloleite.mercado.retriever;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.datamodel.ListToMapTradeDataModelConverter;
import org.marceloleite.mercado.converter.entity.ListTemporalTickerPOToListTemporalTickerDataModelConverter;
import org.marceloleite.mercado.converter.entity.TemporalTickerPOToTemporalTickerDataModelConverter;
import org.marceloleite.mercado.database.data.structure.TemporalTickerDataModel;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databasemodel.TemporalTickerIdPO;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TemporalTickerDAO;

public class TemporalTickerRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = false;

	private static final int FIRST_CALL = 0;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TemporalTickerRetriever.class);

	private TemporalTickerDAO temporalTickerDAO;

	public TemporalTickerRetriever() {
		super();
		this.temporalTickerDAO = new TemporalTickerDAO();
	}

	public TemporalTickerDataModel retrieve(Currency currency, TimeInterval timeInterval,
			boolean ignoreValueOnDatabsase) {
		return retrieve(currency, timeInterval, ignoreValueOnDatabsase, FIRST_CALL);
	}

	private TemporalTickerDataModel retrieve(Currency currency, TimeInterval timeInterval,
			boolean ignoreValueOnDatabsase, int calls) {

		TemporalTickerPO temporalTickerPO = null;
		if (!ignoreValueOnDatabsase) {

			TemporalTickerIdPO temporalTickerIdPO = new TemporalTickerIdPO();
			temporalTickerIdPO.setCurrency(currency);
			temporalTickerIdPO.setStart(timeInterval.getStart());
			temporalTickerIdPO.setEnd(timeInterval.getEnd());
			TemporalTickerPO temporalTickerPOForEnquirement = new TemporalTickerPO();
			temporalTickerPOForEnquirement.setTemporalTickerIdPO(temporalTickerIdPO);
			temporalTickerPO = temporalTickerDAO.findById(temporalTickerPOForEnquirement);

		}

		if (temporalTickerPO == null) {
			TradesRetriever tradesRetriever = new TradesRetriever();

			List<TradeDataModel> trades = tradesRetriever.retrieve(currency, timeInterval.getStart(),
					timeInterval.getEnd(), IGNORE_DATABASE_VALUES);
			Map<Long, TradeDataModel> tradesMap = new ListToMapTradeDataModelConverter().convertTo(trades);
			TemporalTickerDataModel temporalTickerDataModel = new TemporalTickerCreator().create(currency, timeInterval,
					tradesMap);
			if (temporalTickerDataModel != null) {
				temporalTickerPO = new TemporalTickerPOToTemporalTickerDataModelConverter()
						.convertFrom(temporalTickerDataModel);
				temporalTickerDAO.merge(temporalTickerPO);
			}
		}

		return new TemporalTickerPOToTemporalTickerDataModelConverter().convertTo(temporalTickerPO);
	}

	public List<TemporalTickerDataModel> bulkRetrieve(Currency currency,
			TimeDivisionController timeDivisionController) {
		List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency, timeDivisionController);
		ListTemporalTickerPOToListTemporalTickerDataModelConverter listTemporalTickerPOToListTemporalTickerDataModelConverter = new ListTemporalTickerPOToListTemporalTickerDataModelConverter();
		List<TemporalTickerDataModel> temporalTickerDataModels = listTemporalTickerPOToListTemporalTickerDataModelConverter.convertTo(temporalTickerPOs);
		temporalTickerDataModels = retrieveTemporalTickersNotFoundOnBulk(temporalTickerDataModels, currency, timeDivisionController);
		
		return sortTemporalTickers(temporalTickerDataModels);
	}

	public TreeMap<TimeInterval, Map<Currency, TemporalTickerDataModel>> bulkRetrieve(
			TimeDivisionController timeDivisionController) {
		ListTemporalTickerPOToListTemporalTickerDataModelConverter listTemporalTickerPOToListTemporalTickerDataModelConverter = new ListTemporalTickerPOToListTemporalTickerDataModelConverter();
		Map<Currency, List<TemporalTickerDataModel>> temporalTickerDataModelsByCurrency = new EnumMap<>(Currency.class);
		for (Currency currency : Currency.values()) {
			if (currency.isDigital()) {
				List<TemporalTickerPO> temporalTickerPOs = temporalTickerDAO.bulkRetrieve(currency,
						timeDivisionController);
				List<TemporalTickerDataModel> temporalTickerDataModels = new ListTemporalTickerPOToListTemporalTickerDataModelConverter()
						.convertTo(temporalTickerPOs);
				temporalTickerDataModels = retrieveTemporalTickersNotFoundOnBulk(temporalTickerDataModels, currency,
						timeDivisionController);

				temporalTickerDataModelsByCurrency.put(currency,
						listTemporalTickerPOToListTemporalTickerDataModelConverter.convertTo(temporalTickerPOs));
			}
		}

		return elaborateTemporalTickerPOsByTimeInterval(temporalTickerDataModelsByCurrency);
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTickerDataModel>> elaborateTemporalTickerPOsByTimeInterval(
			Map<Currency, List<TemporalTickerDataModel>> temporalTickerPOsByCurrency) {
		TreeMap<TimeInterval, Map<Currency, TemporalTickerDataModel>> result = new TreeMap<>();
		for (Currency currency : temporalTickerPOsByCurrency.keySet()) {
			List<TemporalTickerDataModel> temporalTickerDataModels = temporalTickerPOsByCurrency.get(currency);
			for (TemporalTickerDataModel temporalTickerDataModel : temporalTickerDataModels) {
				TimeInterval timeInterval = new TimeInterval(temporalTickerDataModel.getStart(),
						temporalTickerDataModel.getEnd());
				Map<Currency, TemporalTickerDataModel> temporalTickerDataModelByCurrency = result
						.getOrDefault(timeInterval, new EnumMap<>(Currency.class));
				temporalTickerDataModelByCurrency.put(temporalTickerDataModel.getCurrency(), temporalTickerDataModel);
				result.put(timeInterval, temporalTickerDataModelByCurrency);
			}
		}
		return result;
	}

	private List<TemporalTickerDataModel> retrieveTemporalTickersNotFoundOnBulk(List<TemporalTickerDataModel> temporalTickerDataModels,
			Currency currency, TimeDivisionController timeDivisionController) {
		/*List<TemporalTickerIdPO> temporalTickerIdPOsFromBulk = temporalTickerPOs.stream().map(TemporalTickerPO::getId)
				.collect(Collectors.toList());*/
		TemporalTickerDataModel temporalTickerDataModelToCheck;
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			temporalTickerDataModelToCheck = new TemporalTickerDataModel();
			temporalTickerDataModelToCheck.setCurrency(currency);
			temporalTickerDataModelToCheck.setStart(timeInterval.getStart());
			temporalTickerDataModelToCheck.setEnd(timeInterval.getEnd());
			if (!temporalTickerDataModels.contains(temporalTickerDataModelToCheck)) {
				TemporalTickerDataModel temporalTickerDataModelRetrieved = retrieve(currency, timeInterval, IGNORE_DATABASE_VALUES);
				temporalTickerDataModels.add(temporalTickerDataModelRetrieved);
			}
		}
		return temporalTickerDataModels;
	}

	private List<TemporalTickerDataModel> sortTemporalTickers(List<TemporalTickerDataModel> temporalTickerPOs) {
		Collections.sort(temporalTickerPOs, new Comparator<TemporalTickerDataModel>() {

			public int compare(TemporalTickerDataModel firstObject, TemporalTickerDataModel secondObject) {
				if (firstObject.getStart().isBefore(secondObject.getStart())) {
					return -1;
				} else if (firstObject.getStart().isAfter(secondObject.getStart())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		return temporalTickerPOs;
	}

}
