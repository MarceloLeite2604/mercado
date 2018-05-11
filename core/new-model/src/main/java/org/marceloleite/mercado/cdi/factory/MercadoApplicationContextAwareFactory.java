package org.marceloleite.mercado.cdi.factory;

import javax.inject.Inject;

import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
class MercadoApplicationContextAwareFactory {
	
	@SuppressWarnings("unused")
	@Inject
	private MercadoApplicationContextAware mercadoApplicationContextAware;
}
