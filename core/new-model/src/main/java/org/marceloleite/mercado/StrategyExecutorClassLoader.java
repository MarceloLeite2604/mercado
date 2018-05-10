package org.marceloleite.mercado;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.strategy.StrategyExecutor;
import org.springframework.stereotype.Component;

@Component
public class StrategyExecutorClassLoader {

	@SuppressWarnings("unchecked")
	public StrategyExecutor load(Strategy strategy) {

		if (strategy == null) {
			throw new IllegalArgumentException("Strategy argument cannot be null.");
		}

		if (strategy.getClassName() == null) {
			throw new IllegalArgumentException("Strategy executor class name cannot be null on \"" + strategy + "\".");
		}

		try {
			Class<? extends StrategyExecutor> strategyClass = (Class<? extends StrategyExecutor>) Class
					.forName(strategy.getClassName()
							.trim());
			Constructor<? extends StrategyExecutor> constructor = strategyClass.getConstructor(Strategy.class);

			return constructor.newInstance(strategy);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeException("Error while loading strategy class.", exception);
		}
	}
}