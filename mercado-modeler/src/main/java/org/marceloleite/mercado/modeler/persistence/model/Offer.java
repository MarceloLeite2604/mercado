package org.marceloleite.mercado.modeler.persistence.model;

public class Offer {
	
	private double unitPrice;
	
	private double quantity;
	
	public Offer() {
		super();
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
