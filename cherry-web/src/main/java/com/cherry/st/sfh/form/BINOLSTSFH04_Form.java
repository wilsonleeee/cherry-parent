/*  
 * @(#)BINOLSTSFH04_Form.java     1.0 2011/09/14      
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
package com.cherry.st.sfh.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * 产品发货单一览Form
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public class BINOLSTSFH04_Form extends BINOLCM13_Form{
	
	/** 发货单号 */
	private String deliverNo;

	/**关联单号*/
	private String relevanceNo;
	
	/** 导入批次 */
	private String importBatch;
	
    /** 发货部门 */
    private String outOrgan;
    
    /** 收货部门 */
    private String inOrgan;
    
    /**开始日期*/
    private String startDate;
    
    /**结束日期*/
    private String endDate;
    
    /**审核状态*/
    private String verifiedFlag;
    
    /**处理状态*/
    private String tradeStatus;

    /**产品厂商ID*/
    private String prtVendorId;
    
    /**产品名称*/
    private String productName;
    
    /** 单据ID */
	private String[] billId;
    
    /**产品发货单List*/
    @SuppressWarnings("unchecked")
    private List productDeliverList;
  
    /**收货部门Id*/
    private String inOrganizationId;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**部门联动条 查询 发货部门/收货部门 标志*/
    private String departInOutFlag;
    
    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

    public String getOutOrgan() {
        return outOrgan;
    }

    public void setOutOrgan(String outOrgan) {
        this.outOrgan = outOrgan;
    }

    public String getInOrgan() {
        return inOrgan;
    }

    public void setInOrgan(String inOrgan) {
        this.inOrgan = inOrgan;
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

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getPrtVendorId() {
        return prtVendorId;
    }

    public void setPrtVendorId(String prtVendorId) {
        this.prtVendorId = prtVendorId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SuppressWarnings("unchecked")
    public List getProductDeliverList() {
        return productDeliverList;
    }

    @SuppressWarnings("unchecked")
    public void setProductDeliverList(List productDeliverList) {
        this.productDeliverList = productDeliverList;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }
	
	public String getRelevanceNo() {
		return relevanceNo;
	}

	public void setRelevanceNo(String relevanceNo) {
		this.relevanceNo = relevanceNo;
	}
	
	public String getInOrganizationId() {
			return inOrganizationId;
		}
	
	public void setInOrganizationId(String inOrganizationId) {
			this.inOrganizationId = inOrganizationId;
		}

	public String getImportBatch() {
		return importBatch;
	}

	public void setImportBatch(String importBatch) {
		this.importBatch = importBatch;
	}

	public String[] getBillId() {
		return billId;
	}

	public void setBillId(String[] billId) {
		this.billId = billId;
	}

    public String getDepartInOutFlag() {
        return departInOutFlag;
    }

    public void setDepartInOutFlag(String departInOutFlag) {
        this.departInOutFlag = departInOutFlag;
    }

}
