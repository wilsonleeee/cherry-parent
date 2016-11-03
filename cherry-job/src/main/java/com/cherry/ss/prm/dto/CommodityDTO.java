package com.cherry.ss.prm.dto;

/**
 * 商品信息DTO(可以是促销品或者产品)
 * @author huzude
 *
 */
public class CommodityDTO {
	// 商品unitCode
	private String unitCode;
	
    // 商品barCode
	private String barCode;
	
	// 商品个数
	private int quantity;
	
	// 商品价格
	private double price;
	
	// 商品价格
	private double priceOther;
	
	// 商品个数(理论值)
	private int quantityOther;
	
	// 促销活动ID
	private String activeID;
	

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantityOther() {
		return quantityOther;
	}

	public void setQuantityOther(int quantityOther) {
		this.quantityOther = quantityOther;
	}

	public String getActiveID() {
		return activeID;
	}

	public void setActiveID(String activeID) {
		this.activeID = activeID;
	}

	public double getPriceOther() {
		return priceOther;
	}

	public void setPriceOther(double priceOther) {
		this.priceOther = priceOther;
	}
	
	
}
