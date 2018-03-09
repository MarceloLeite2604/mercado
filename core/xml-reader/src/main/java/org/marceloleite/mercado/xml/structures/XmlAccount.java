package org.marceloleite.mercado.xml.structures;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.xml.adapters.XmlBalancesXmlAdapter;

@XmlRootElement(name="account")
public class XmlAccount {

	private String owner;
	
	private String email;
	
	private XmlTapiInformation xmlTapiInformation;

	private XmlBalances xmlBalances;

	private List<XmlDeposit> xmlDeposits;
	
	private List<XmlBuyOrder> xmlBuyOrders;
	
	private List<XmlSellOrder> xmlSellOrders;
	
	private List<XmlStrategy> xmlStrategies;

	public XmlAccount() {
		this("");
	}

	public XmlAccount(String owner) {
		super();
		this.owner = owner;
		this.xmlBalances = new XmlBalances();
		this.xmlDeposits = new ArrayList<>();
		this.xmlBuyOrders = new ArrayList<>();
		this.xmlSellOrders = new ArrayList<>();
		this.xmlStrategies = new ArrayList<>();
	}

	@XmlElement
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name="tapiInformations")
	public XmlTapiInformation getXmlTapiInformation() {
		return xmlTapiInformation;
	}

	public void setXmlTapiInformation(XmlTapiInformation xmlTapiInformation) {
		this.xmlTapiInformation = xmlTapiInformation;
	}

	@XmlElement(name="balances")
	@XmlJavaTypeAdapter(XmlBalancesXmlAdapter.class)
	public XmlBalances getXmlBalances() {
		return xmlBalances;
	}

	public void setXmlBalances(XmlBalances xmlBalances) {
		this.xmlBalances = xmlBalances;
	}

	@XmlElementWrapper(name="deposits")
	@XmlElement(name="deposit")
	public List<XmlDeposit> getXmlDeposits() {
		return xmlDeposits;
	}

	public void setXmlDeposits(List<XmlDeposit> xmlDeposits) {
		this.xmlDeposits = xmlDeposits;
	}
	
	@XmlElementWrapper(name="buyOrders")
	@XmlElement(name="buyOrder")
	public List<XmlBuyOrder> getXmlBuyOrders() {
		return xmlBuyOrders;
	}

	public void setXmlBuyOrders(List<XmlBuyOrder> xmlBuyOrders) {
		this.xmlBuyOrders = xmlBuyOrders;
	}
	
	@XmlElementWrapper(name="sellOrders")
	@XmlElement(name="sellOrder")
	public List<XmlSellOrder> getXmlSellOrders() {
		return this.xmlSellOrders;
	}
	
	public void setXmlSellOrders(List<XmlSellOrder> xmlSellOrders) {
		this.xmlSellOrders = xmlSellOrders;
	}

	public List<XmlStrategy> getXmlStrategies() {
		return xmlStrategies;
	}

	@XmlElementWrapper(name="strategies")
	@XmlElement(name="strategy")
	public void setXmlStrategies(List<XmlStrategy> xmlStrategies) {
		this.xmlStrategies = xmlStrategies;
	}
}