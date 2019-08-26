package com.pos.inventory.service;

import java.util.List;

import com.pos.inventory.model.Inventory;

public interface InventoryService {

	public List<Inventory> getProductList(Inventory inventory) throws Exception;
	
	public Inventory getProductInfo(Inventory inventory) throws Exception;
	
	public Inventory saveIngredients(Inventory inventory);
	public Inventory saveStoreProduct(Inventory inventory);
	public List<Inventory> getInventoryList(Inventory inventory);
	public List<Inventory> getInventoryListSupplier(Inventory inventory);
	public Inventory getPriceSum(Inventory inventory);
	public List<Inventory> getProductView(Inventory inventory) throws Exception;
	public Inventory saveWastage(Inventory inventory);
	public List<Inventory> getWastageView(Inventory inventory) throws Exception;
	public Inventory getWastageInfo(Inventory inventory);
}