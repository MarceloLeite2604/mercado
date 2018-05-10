package org.marceloleite.mercado.dao.xml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class XMLDAOConfiguration {

//	@Bean
	public AccountXMLDAO accountXMLDAO() {
		return new AccountXMLDAO();
	}

//	@Bean
	public PropertyXMLDAO propertyXMLDAO() {
		return new PropertyXMLDAO();
	}

//	@Bean
	public XMLReaderWriter createXMLReaderWriter() {
		return new XMLReaderWriter();
	}
}
