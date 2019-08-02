package com.pos.pointOfSale.model;

import java.util.List;


public class PointOfSale {

	private String itemId;
	private String encItemId;
	private String itemCode;
	private String itemName;
	private String itemPrice;
	private String updateBy;
	private String updateDate;
	private String productId;
	private String quantity;
	private String unitId;
	private String productName;
	private String unitName;
	private String encProductId;
	private String orderPrice;
	private String orderId;
	private String encOrderId;
	private String totalPrice;
	private String discountAmount;
	private String message;
	private String mCode;
	private String referenceById;
	private String referenceByName;
	private String discountId;
	private String encDiscountId;
	private String fromDate;
	private String toDate;
	private List<OrderModel> orderModelList;
	
	private String tableNo;
	private String subTotal;
	
	private String itemOrderId;
	private String encItemOrderId;
	private String orderTotalAmount;
	
	private String receivedAmount;
	private String changeAmount;
	private String cardPayAmount;
	private String cashPayAmount;
	private String netPayableAmount;
	
	private String completedYn;
	private String finalizedYn;
	
	private String employeeId;
	private String employeeName;
	private String orderNo;
	private String orderDate;
	
	
	
	public String getCompletedYn() {
		return completedYn;
	}
	public void setCompletedYn(String completedYn) {
		this.completedYn = completedYn;
	}
	public String getFinalizedYn() {
		return finalizedYn;
	}
	public void setFinalizedYn(String finalizedYn) {
		this.finalizedYn = finalizedYn;
	}
	public String getNetPayableAmount() {
		return netPayableAmount;
	}
	public void setNetPayableAmount(String netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}
	public String getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public String getChangeAmount() {
		return changeAmount;
	}
	public void setChangeAmount(String changeAmount) {
		this.changeAmount = changeAmount;
	}
	public String getCardPayAmount() {
		return cardPayAmount;
	}
	public void setCardPayAmount(String cardPayAmount) {
		this.cardPayAmount = cardPayAmount;
	}
	public String getCashPayAmount() {
		return cashPayAmount;
	}
	public void setCashPayAmount(String cashPayAmount) {
		this.cashPayAmount = cashPayAmount;
	}
	public String getOrderTotalAmount() {
		return orderTotalAmount;
	}
	public void setOrderTotalAmount(String orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}
	public String getItemOrderId() {
		return itemOrderId;
	}
	public void setItemOrderId(String itemOrderId) {
		this.itemOrderId = itemOrderId;
	}
	public String getEncItemOrderId() {
		return encItemOrderId;
	}
	public void setEncItemOrderId(String encItemOrderId) {
		this.encItemOrderId = encItemOrderId;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getReferenceByName() {
		return referenceByName;
	}
	public void setReferenceByName(String referenceByName) {
		this.referenceByName = referenceByName;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDiscountId() {
		return discountId;
	}
	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}
	public String getEncDiscountId() {
		return encDiscountId;
	}
	public void setEncDiscountId(String encDiscountId) {
		this.encDiscountId = encDiscountId;
	}

	public String getReferenceById() {
		return referenceById;
	}
	public void setReferenceById(String referenceById) {
		this.referenceById = referenceById;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEncOrderId() {
		return encOrderId;
	}
	public void setEncOrderId(String encOrderId) {
		this.encOrderId = encOrderId;
	}
	public List<OrderModel> getOrderModelList() {
		return orderModelList;
	}
	public void setOrderModelList(List<OrderModel> orderModelList) {
		this.orderModelList = orderModelList;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getEncProductId() {
		return encProductId;
	}
	public void setEncProductId(String encProductId) {
		this.encProductId = encProductId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getmCode() {
		return mCode;
	}
	public void setmCode(String mCode) {
		this.mCode = mCode;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getEncItemId() {
		return encItemId;
	}
	public void setEncItemId(String encItemId) {
		this.encItemId = encItemId;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getTableNo() {
		return tableNo;
	}
	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	
}
