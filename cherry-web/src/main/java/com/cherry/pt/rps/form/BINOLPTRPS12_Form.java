/*		
 * @(#)BINOLPTRPS12_Form.java     1.0 2011/03/16		
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
package com.cherry.pt.rps.form;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;


/**
 * 产品详细库存form
 * 
 * @author lipc
 * @version 1.0 2011.03.16
 */
public class BINOLPTRPS12_Form extends BINOLCM13_Form{
	
	/** 产品厂商ID */
	private String prtVendorId;
	
	/** 产品ID */
	private String productId;
	
	private String validFlag;
	
	/** 开始日期 */
	private String startDate;
	
	/** 截止日期 */
	private String endDate;
	
	/** 日期1 */
	private String date1;
	
	/** 日期2 */
	private String date2;
	
	/** 月度库存结算日 */
	private String cutOfDate;
	
	/** 计算加减方式: */
	private String flag;
	
	/** 计算前进方式 */
	private String flagB;
	
	/** 实体仓库ID**/
	private int	inventoryInfoID;
	
	/** 逻辑仓库ID**/
	private int logicInventoryInfoID;
	
	/**入库出库**/
	private int stockType;
	
	/**业务类型**/
	private String tradeType;
	
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public int getInventoryInfoID() {
		return inventoryInfoID;
	}

	public void setInventoryInfoID(int inventoryInfoID) {
		this.inventoryInfoID = inventoryInfoID;
	}

	public int getLogicInventoryInfoID() {
		return logicInventoryInfoID;
	}

	public void setLogicInventoryInfoID(int logicInventoryInfoID) {
		this.logicInventoryInfoID = logicInventoryInfoID;
	}

	public int getStockType() {
		return stockType;
	}

	public void setStockType(int stockType) {
		this.stockType = stockType;
	}

	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getCutOfDate() {
		return cutOfDate;
	}

	public void setCutOfDate(String cutOfDate) {
		this.cutOfDate = cutOfDate;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlagB() {
		return flagB;
	}

	public void setFlagB(String flagB) {
		this.flagB = flagB;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
}
