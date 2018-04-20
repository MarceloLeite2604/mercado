package org.marceloleite.mercado.comparator;

import java.util.Comparator;

import org.marceloleite.mercado.model.Trade;

public class TradeComparatorById implements Comparator<Trade> {
	
	private static TradeComparatorById instance;
	
	private TradeComparatorById() {
	}

	@Override
	public int compare(Trade firstTrade, Trade secondTrade) {
		return (firstTrade.getId().compareTo(secondTrade.getId()));
	}
	
	public static TradeComparatorById getInstance() {
		if (instance == null) {
			instance = new TradeComparatorById();
		}
		return instance;
	}

}
