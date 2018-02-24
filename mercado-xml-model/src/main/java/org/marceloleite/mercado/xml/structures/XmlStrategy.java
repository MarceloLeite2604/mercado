package org.marceloleite.mercado.xml.structures;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;

@XmlRootElement(name = "strategy")
@XmlType(propOrder= {"currency", "classNames"})
public class XmlStrategy {
	
	private Currency currency;
	
	private List<String> classNames;
	
	public XmlStrategy(Currency currency, List<String> classNames) {
		super();
		this.currency = currency;
		this.classNames = classNames;
	}

	public XmlStrategy() {
		this(null, null);
	}

	@XmlElement(name="currency")
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlElementWrapper(name="classes")
	@XmlElement(name="class")
	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}	
}
