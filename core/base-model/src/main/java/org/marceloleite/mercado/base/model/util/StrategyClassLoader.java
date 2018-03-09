package org.marceloleite.mercado.base.model.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.marceloleite.mercado.base.model.Strategy;
import org.marceloleite.mercado.commons.Currency;

public class StrategyClassLoader {

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
