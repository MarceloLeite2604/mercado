package org.marceloleite.mercado.cdi;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Named("MercadoApplicationContextAware")
public final class MercadoApplicationContextAware implements ApplicationContextAware {

	private static final Logger LOGGER = LogManager.getLogger(MercadoApplicationContextAware.class);

	private static ApplicationContext APPLICATION_CONTEXT;

	private MercadoApplicationContextAware() {
		LOGGER.debug("Initializing MercadoApplicationContextAware. ");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		APPLICATION_CONTEXT = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass) {
		return APPLICATION_CONTEXT.getBean(beanClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> beanClass, String beanName) {

		if (beanClass == null) {
			throw new IllegalArgumentException("Bean class cannot be null.");
		}

		if (StringUtils.isEmpty(beanName)) {
			throw new IllegalArgumentException("Bean name cannot be null or empty.");
		}

		return (T) APPLICATION_CONTEXT.getBeansOfType(beanClass)
				.entrySet()
				.stream()
				.filter(entry -> entry.getKey()
						.equals(beanName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException(
						"Could not find a bean of class \"" + beanClass.getName() + "\" named \"" + beanName + "\".")).getValue();
	}
}
