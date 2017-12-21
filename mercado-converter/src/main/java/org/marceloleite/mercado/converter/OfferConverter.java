package org.marceloleite.mercado.converter;

import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.Offer;

public class OfferConverter implements Converter<List<Double>, Offer> {

	private static final int UNIT_PRICE_POSITION = 0;
	private static final int QUANTITY_POSITION = 1;

	@Override
	public Offer convert(List<Double> doublesList) {
		Offer offer = new Offer();
		offer.setUnitPrice(doublesList.get(UNIT_PRICE_POSITION));
		offer.setQuantity(doublesList.get(QUANTITY_POSITION));
		return offer;
	}

}
