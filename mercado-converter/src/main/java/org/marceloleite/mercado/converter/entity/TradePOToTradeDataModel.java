package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databasemodel.TradeIdPO;
import org.marceloleite.mercado.databasemodel.TradePO;

public class TradePOToTradeDataModel implements Converter<TradePO, TradeDataModel>{

	@Override
	public TradeDataModel convertTo(TradePO tradePO) {
		TradeDataModel tradeDataModel = new TradeDataModel();
		TradeIdPO tradeIdPO = tradePO.getTradeIdPO();
		tradeDataModel.setCurrency(tradeIdPO.getCurrency());
		tradeDataModel.setId(tradeIdPO.getId());
		tradeDataModel.setAmount(tradePO.getAmount());
		tradeDataModel.setDate(tradePO.getDate());
		tradeDataModel.setPrice(tradePO.getPrice());
		tradeDataModel.setTradeType(tradePO.getTradeType());
		return null;
	}

	@Override
	public TradePO convertFrom(TradeDataModel tradeDataModel) {
		TradePO tradePO = new TradePO();
		
		TradeIdPO tradeIdPO = new TradeIdPO();
		tradeIdPO.setCurrency(tradeDataModel.getCurrency());
		tradeIdPO.setId(tradeDataModel.getId());
		tradePO.setTradeIdPO(tradeIdPO);
		
		tradePO.setAmount(tradeDataModel.getAmount());
		tradePO.setDate(tradeDataModel.getDate());
		tradePO.setPrice(tradeDataModel.getPrice());
		tradePO.setTradeType(tradeDataModel.getTradeType());
		return tradePO;
	}

	
}
