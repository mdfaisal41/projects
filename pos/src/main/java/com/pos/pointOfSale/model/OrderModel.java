package com.pos.pointOfSale.model;

public class OrderModel {

	private String itemId;
	private String quantity;
	private String itemPrice;
	private String subTotal;
	
	
	public OrderModel() {
	}

	public OrderModel(String itemId, String quantity, String itemPrice, String subTotal) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.itemPrice = itemPrice;
		this.subTotal = subTotal;
	}

	
	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	
}
