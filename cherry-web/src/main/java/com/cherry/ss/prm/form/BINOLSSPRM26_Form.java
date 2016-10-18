/*
 * @(#)BINOLSSPRM26_Form.java     1.0 2010/11/05
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
package com.cherry.ss.prm.form;

/**
 * 
 * 盘点单明细Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.05
 */
public class BINOLSSPRM26_Form {
	
	/** 促销产品盘点ID */
	private String stockTakingId;

	/** 盈亏 */
	private String profitKbn;

	public String getStockTakingId() {
		return stockTakingId;
	}

	public void setStockTakingId(String stockTakingId) {
		this.stockTakingId = stockTakingId;
	}

	public String getProfitKbn() {
		return profitKbn;
	}

	public void setProfitKbn(String profitKbn) {
		this.profitKbn = profitKbn;
	}
}
