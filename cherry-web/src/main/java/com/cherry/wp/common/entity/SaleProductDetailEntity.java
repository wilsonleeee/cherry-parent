package com.cherry.wp.common.entity;

public class SaleProductDetailEntity {

	/**规则code*/
	private String maincode;
	/**商品条码*/
	private String barcode;
	/**厂商编码*/
	private String unicode;
	/**数量*/
	private int quantity;
	/**优惠后的价格*/
	private double price;
	/**原价*/
	private double ori_price;
	/**产品名称*/
	private String proname;
	
	public SaleProductDetailEntity(){
		super();
	}
	
	public SaleProductDetailEntity(String maincode, String barcode,
			String unicode, int quantity, double price, double ori_price,
			String proname) {
		super();
		this.maincode = maincode;
		this.barcode = barcode;
		this.unicode = unicode;
		this.quantity = quantity;
		this.price = price;
		this.ori_price = ori_price;
		this.proname = proname;
	}
	public String getMaincode() {
		return maincode;
	}
	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getUnicode() {
		return unicode;
	}
	public void setUnicode(String unicode) {
		this.unicode = unicode;
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
	public double getOri_price() {
		return ori_price;
	}
	public void setOri_price(double ori_price) {
		this.ori_price = ori_price;
	}
	public String getProname() {
		return proname;
	}
	public void setProname(String proname) {
		this.proname = proname;
	}
	
	
}
