package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databasemodel.TradePO;

public class ListTradePOToListTradeDataModelConverter implements Converter<List<TradePO>, List<TradeDataModel>>{

	@Override
	public List<TradeDataModel> convertTo(List<TradePO> tradePOs) {
		TradePOToTradeDataModel tradePOToTradeDataModel = new TradePOToTradeDataModel();
		List<TradeDataModel> tradeDataModels = new ArrayList<>();
		for (TradePO tradePO : tradePOs) {
			tradeDataModels.add(tradePOToTradeDataModel.convertTo(tradePO));
		}
		return tradeDataModels;
	}

	@Override
	public List<TradePO> convertFrom(List<TradeDataModel> tradeDataModels) {
		TradePOToTradeDataModel tradePOToTradeDataModel = new TradePOToTradeDataModel();
		List<TradePO> tradePOs = new ArrayList<>();
		for (TradeDataModel tradeDataModel : tradeDataModels) {
			tradePOs.add(tradePOToTradeDataModel.convertFrom(tradeDataModel));
		}
		return tradePOs;
	}

}
