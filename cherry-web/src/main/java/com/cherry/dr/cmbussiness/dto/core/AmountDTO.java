package com.cherry.dr.cmbussiness.dto.core;

public class AmountDTO {
	
	/** 累积消费金额 */
	private double totalAmount;
	
	/** 累积月数 */
	private double totalMonth;

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalMonth() {
		return totalMonth;
	}

	public void setTotalMonth(double totalMonth) {
		this.totalMonth = totalMonth;
	}
	
}
