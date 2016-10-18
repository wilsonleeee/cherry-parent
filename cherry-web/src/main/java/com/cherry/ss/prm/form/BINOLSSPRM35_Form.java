/*		
 * @(#)BINOLSSPRM35_Form.java     1.0 2010/11/18		
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

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 出入库记录查询form
 * 
 * @author lipc
 * @version 1.0 2010.11.18
 *
 */
public class BINOLSSPRM35_Form extends BINOLCM13_Form{

	/** 单据号 */
	private String tradeNo;
	
	/** 单据号 */
	private String relevantNo;

	/** 开始日 */
	private String startDate;
	
	/** 结束日 */
	private String endDate;
	
	/** 业务类型 */
	private String tradeType;
	
	/** 审核状态 */
	private String verifiedFlag;
	
	/**促销产品厂商ID*/
    private String prmVendorId;
    
    /**促销产品名称*/
    private String nameTotal;
    
    /** 字符编码 **/
	private String charset;
	
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getStartDate() {
		return startDate;
	}
	public String getRelevantNo() {
		return relevantNo;
	}

	public void setRelevantNo(String relevantNo) {
		this.relevantNo = relevantNo;
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

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getPrmVendorId() {
		return prmVendorId;
	}

	public void setPrmVendorId(String prmVendorId) {
		this.prmVendorId = prmVendorId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getNameTotal() {
		return nameTotal;
	}

	public void setNameTotal(String nameTotal) {
		this.nameTotal = nameTotal;
	}
}
