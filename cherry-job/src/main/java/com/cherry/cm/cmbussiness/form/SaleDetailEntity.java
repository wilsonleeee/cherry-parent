package com.cherry.cm.cmbussiness.form;

public class SaleDetailEntity 
{
	private String barcode;//商品条码
	private String unitcode;//厂商编码
	private int quantity;//数量
	private double price;//价格
	private double ori_price;//原价
	private String type;//产品类型
	private String maincode;//主活动码
	private String mainname;//主活动名称
	private int ItemTag;//业务的每个商品流水号
	private double discount;//单品折扣
	private int productid;//产品id
	private String proname;//产品名称
	private int mainitem_tag;//一个主活动内部的分组号
	private String activitycode;//活动code
	private int new_flag;//是否是促销引擎新增的记录，如果0不是新增的记录，如果是1是新增的记录
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaincode() {
		return maincode;
	}

	public void setMaincode(String maincode) {
		this.maincode = maincode;
	}

	public String getMainname() {
		return mainname;
	}

	public void setMainname(String mainname) {
		this.mainname = mainname;
	}

	public int getItemTag() {
		return ItemTag;
	}

	public void setItemTag(int itemTag) {
		ItemTag = itemTag;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public int getMainitem_tag() {
		return mainitem_tag;
	}

	public void setMainitem_tag(int mainitem_tag) {
		this.mainitem_tag = mainitem_tag;
	}

	public String getActivitycode() {
		return activitycode;
	}

	public void setActivitycode(String activitycode) {
		this.activitycode = activitycode;
	}

	public int getNew_flag() {
		return new_flag;
	}

	public void setNew_flag(int new_flag) {
		this.new_flag = new_flag;
	}
	
}
