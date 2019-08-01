package com.pos.pointOfSale.service;

import java.util.List;

import com.pos.pointOfSale.model.PointOfSale;

public interface PointOfSaleService {
	public List<PointOfSale> getItemList(PointOfSale pointOfSale) throws Exception;
	public PointOfSale saveItem(PointOfSale pointOfSale);
	public PointOfSale saveItemConfig(PointOfSale pointOfSale);
	public List<PointOfSale> getItemConfigList(PointOfSale pointOfSale);
	public PointOfSale getOrderInfo(PointOfSale pointOfSale);
	public PointOfSale saveOrder(PointOfSale pointOfSale);
	public PointOfSale getOrderPrice(PointOfSale pointOfSale);
	public PointOfSale saveDiscount(PointOfSale pointOfSale);
	public List<PointOfSale> getDiscountList(PointOfSale pointOfSale);
	public PointOfSale getDiscountInfo(PointOfSale pointOfSale);
	public List<PointOfSale> getOrderInfoList(PointOfSale pointOfSale) throws Exception;
	public List<PointOfSale> getOrderList(PointOfSale pointOfSale);
	public List<PointOfSale> getPendingOrderList(PointOfSale pointOfSale);
	public List<PointOfSale> getPendingOrderInfoList(PointOfSale pointOfSale) throws Exception;
	public PointOfSale cancelOrder(PointOfSale pointOfSale);
	public List<PointOfSale> getOrderEditList(PointOfSale pointOfSale) throws Exception;
	public PointOfSale getOrderTotalAmount(String encOrderId);
	public PointOfSale saveOrderFinalize(PointOfSale pointOfSale);
}
