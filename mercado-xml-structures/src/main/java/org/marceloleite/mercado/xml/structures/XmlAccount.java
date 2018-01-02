package org.marceloleite.mercado.xml.structures;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.xml.adapter.XmlBalancesXmlAdapter;

@XmlRootElement(name="account")
public class XmlAccount {

	private String owner;

	private XmlBalances xmlBalances;

	private List<XmlDeposit> xmlDeposits;
	
	private List<XmlBuyOrder> xmlBuyOrders;

	public XmlAccount() {
		this("");
	}

	public XmlAccount(String owner) {
		super();
		this.owner = owner;
		this.xmlBalances = new XmlBalances();
		this.xmlDeposits = new ArrayList<>();
		this.xmlBuyOrders = new ArrayList<>();
	}

	@XmlElement
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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
}