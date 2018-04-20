package org.marceloleite.mercado.comparator;

import java.util.Comparator;

import org.marceloleite.mercado.model.Trade;

public class TradeComparatorByIdDesc implements Comparator<Trade> {
	
	private static TradeComparatorByIdDesc instance;
	
	private TradeComparatorByIdDesc() {
	}

	@Override
	public int compare(Trade firstTrade, Trade secondTrade) {
		return (secondTrade.getId().compareTo(firstTrade.getId()));
	}
	
	public static TradeComparatorByIdDesc getInstance() {
		if (instance == null) {
			instance = new TradeComparatorByIdDesc();
		}
		return instance;
	}

}
