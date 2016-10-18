/*  
 * @(#)TransactionlogDTO.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.ps.act.dto;

import java.util.Date;

public class TransactionlogDTO {
	private String brand;
	private String machineCode;
	private String barCode;
	private String uniCode;
	private String yxdType;
	private String quantity;
	private int price;
	private String type;
	private Date txdDate;
	private String txdTime;
	private String baCode;
	private String counterCode;
	private int priceSum;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getUniCode() {
		return uniCode;
	}

	public void setUniCode(String uniCode) {
		this.uniCode = uniCode;
	}

	public String getYxdType() {
		return yxdType;
	}

	public void setYxdType(String yxdType) {
		this.yxdType = yxdType;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTxdDate() {
		return txdDate;
	}

	public void setTxdDate(Date txdDate) {
		this.txdDate = txdDate;
	}

	public String getTxdTime() {
		return txdTime;
	}

	public void setTxdTime(String txdTime) {
		this.txdTime = txdTime;
	}

	public String getBaCode() {
		return baCode;
	}

	public void setBaCode(String baCode) {
		this.baCode = baCode;
	}

	public String getCounterCode() {
		return counterCode;
	}

	public void setCounterCode(String counterCode) {
		this.counterCode = counterCode;
	}

	public int getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(int priceSum) {
		this.priceSum = priceSum;
	}
	
}
