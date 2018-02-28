package org.marceloleite.mercado.simulator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.strategies.Strategy;

public class StrategyLoader {

	@SuppressWarnings("unchecked")
	public Strategy load(String className, Currency currency) {

		try {
			Class<? extends Strategy> strategyClass = (Class<? extends Strategy>) Class.forName(className);
			Constructor<? extends Strategy> constructor = strategyClass.getConstructor(Currency.class);

			return constructor.newInstance(currency);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeException("Error while loading strategy class.", exception);
		}
	}
}
