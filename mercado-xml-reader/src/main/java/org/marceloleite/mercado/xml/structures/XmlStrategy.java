package org.marceloleite.mercado.xml.structures;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;

@XmlRootElement(name = "strategy")
public class XmlStrategy {
	
	private Currency currency;
	
	private List<XmlClass> xmlClasses;
	
	public XmlStrategy(Currency currency, List<XmlClass> xmlClasses) {
		super();
		this.currency = currency;
		this.xmlClasses = xmlClasses;
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
	public List<XmlClass> getXmlClasses() {
		return xmlClasses;
	}

	public void setXmlClasses(List<XmlClass> xmlClasses) {
		this.xmlClasses = xmlClasses;
	}	
}
