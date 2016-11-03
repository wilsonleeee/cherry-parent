package com.cherry.ss.prm.dto;

/**
 * 销售明细数据DTO
 * @author huzude
 *
 */
public class SaleRecordDetailDTO {
	
	// 购买产品unitcode码
	private String unitCode;
	
	// 购买产品barcode
	private String barCode;
	
	// 购买产品数量
	private int quantity;

	// 购买金额
	private double price;
	
	// 商品区分
	private String saleType;
	
	// 员工代号
	private String employeeCode;
	
	
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

	public void setPrice(float price) {
		this.price = price;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

}
