/*
 * @(#)BINOLWSMNG07_Form.java     1.0 2015-10-29 
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
package com.cherry.wp.ws.mng.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

/**
 * 
 * @ClassName: BINOLWSMNG07_Form 
 * @Description: TODO(盘点申请Form) 
 * @author menghao
 * @version v1.0.0 2015-10-29 
 *
 */
public class BINOLWSMNG07_Form extends BINOLCM13_Form{
    /** 品牌ID */
    private String brandInfoId;
    
    /**业务日期*/
    private String bussinessDate;
    
    /**申请日期*/
    private String applyDate;
    
    /** 员工ID */
    private String employeeId;
    
    /**员工姓名*/
    private String employeeName;
    
    /** 部门ID */
    private String organizationId;
    
    /** 盘点仓库ID */
    private String depotId;

    /** 逻辑仓库ID */
    private String logicinventId;

    /** 产品名称 */
    private String nameTotal;
    
    /**产品厂商ID*/
    private String prtVendorId;

    /** 开始日 */
    private String startDate;
    
    /** 结束日 */
    private String endDate;

    /** 盘点申请单号 */
    private String stockTakingNo;

    /** 审核状态 */
    private String verifiedFlag;
    
    /**业务类型*/
    private String tradeType;
    
    /** 汇总信息 */
    private Map<String, Object> sumInfo;
    
    /**盘点申请List*/
    private List<Map<String,Object>> proStocktakeRequestList;
    
    /** 盘点理由*/
    private String reason;
    
    /** 批次号*/
    private String[] batchNoArr;
    
    /** 账面数量*/
    private String[] bookCountArr;
    
    /**实盘数量*/
    private String[] checkQuantityArr;
    
    /** 盘差*/
    private String[] gainCountArr;
    
    /** 盘点数量*/
    private String[] quantityArr;
    
    /** 厂商编码*/
    private String[] unitCodeArr;
    
    /** 条码*/
    private String[] barCodeArr;
    
    /** 厂商ID*/
    private String[] productVendorIDArr;
    
    /** 备注 */
    private String[] commentsArr;
    
    /**价格*/
    private String[] priceArr;
    
    /**盘点处理方式*/
    private String[] htArr;
    
    /** 是否盲盘*/
    private String blindFlag;
    
    /**是否批次盘点*/
    private String isBatchStockTaking;
    
    /**是否支持终端判断*/
    private String witposStaking;
    
    /** 单据工作流ID */
    private String workFlowID;
    
    /** 盘点单ID */
    private String proStocktakeRequestID;
    
    /** 已经暂存的盘点申请单据号*/
    private String stockTakingNoIF;
    
    /** 操作类型 */
    private String operateType;
    
    /** 盘点单List */
    private List<Map<String, Object>> takingList;
    
    /** 盘点单信息 */
    private Map<String, Object> takingInfo;
    
    /** 盘点申请单明细List */
    private List<Map<String, Object>> takingReqDetailList;
    
    /**实际做业务的员工*/
    private String tradeEmployeeID;
    
    /**BAList*/
    private List<Map<String,Object>> counterBAList;
    
    /**柜台号*/
    private String counterCode;
    
    /** （配置项）产品盘点使用价格 */
    private String sysConfigUsePrice;
    
    /**全盘=all，自由盘点为空*/
    private String addType;
    
    /**审核状态Code值List*/
    private List<Map<String,Object>> verifiedFlagsCAList;
    
	public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLogicinventId() {
        return logicinventId;
    }

    public void setLogicinventId(String logicinventId) {
        this.logicinventId = logicinventId;
    }

    public String getNameTotal() {
        return nameTotal;
    }

    public void setNameTotal(String nameTotal) {
        this.nameTotal = nameTotal;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getStockTakingNo() {
        return stockTakingNo;
    }

    public void setStockTakingNo(String stockTakingNo) {
        this.stockTakingNo = stockTakingNo;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }

    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
    }

    public List<Map<String, Object>> getProStocktakeRequestList() {
        return proStocktakeRequestList;
    }

    public void setProStocktakeRequestList(List<Map<String, Object>> proStocktakeRequestList) {
        this.proStocktakeRequestList = proStocktakeRequestList;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String[] getBatchNoArr() {
        return batchNoArr;
    }

    public void setBatchNoArr(String[] batchNoArr) {
        this.batchNoArr = batchNoArr;
    }

    public String[] getBookCountArr() {
        return bookCountArr;
    }

    public void setBookCountArr(String[] bookCountArr) {
        this.bookCountArr = bookCountArr;
    }

    public String[] getGainCountArr() {
        return gainCountArr;
    }

    public void setGainCountArr(String[] gainCountArr) {
        this.gainCountArr = gainCountArr;
    }

    public String[] getQuantityArr() {
        return quantityArr;
    }

    public void setQuantityArr(String[] quantityArr) {
        this.quantityArr = quantityArr;
    }

    public String[] getUnitCodeArr() {
        return unitCodeArr;
    }

    public void setUnitCodeArr(String[] unitCodeArr) {
        this.unitCodeArr = unitCodeArr;
    }

    public String[] getBarCodeArr() {
        return barCodeArr;
    }

    public void setBarCodeArr(String[] barCodeArr) {
        this.barCodeArr = barCodeArr;
    }

    public String[] getProductVendorIDArr() {
        return productVendorIDArr;
    }

    public void setProductVendorIDArr(String[] productVendorIDArr) {
        this.productVendorIDArr = productVendorIDArr;
    }

    public String[] getPriceArr() {
        return priceArr;
    }

    public void setPriceArr(String[] priceArr) {
        this.priceArr = priceArr;
    }

    public String getBlindFlag() {
        return blindFlag;
    }

    public void setBlindFlag(String blindFlag) {
        this.blindFlag = blindFlag;
    }

    public String getIsBatchStockTaking() {
        return isBatchStockTaking;
    }

    public void setIsBatchStockTaking(String isBatchStockTaking) {
        this.isBatchStockTaking = isBatchStockTaking;
    }

    public String getWitposStaking() {
        return witposStaking;
    }

    public void setWitposStaking(String witposStaking) {
        this.witposStaking = witposStaking;
    }

    public String getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(String workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getBussinessDate() {
        return bussinessDate;
    }

    public void setBussinessDate(String bussinessDate) {
        this.bussinessDate = bussinessDate;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String[] getCheckQuantityArr() {
        return checkQuantityArr;
    }

    public void setCheckQuantityArr(String[] checkQuantityArr) {
        this.checkQuantityArr = checkQuantityArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public List<Map<String, Object>> getTakingList() {
        return takingList;
    }

    public void setTakingList(List<Map<String, Object>> takingList) {
        this.takingList = takingList;
    }

    public Map<String, Object> getTakingInfo() {
        return takingInfo;
    }

    public void setTakingInfo(Map<String, Object> takingInfo) {
        this.takingInfo = takingInfo;
    }

    public List<Map<String, Object>> getCounterBAList() {
        return counterBAList;
    }

    public void setCounterBAList(List<Map<String, Object>> counterBAList) {
        this.counterBAList = counterBAList;
    }

    public String getTradeEmployeeID() {
        return tradeEmployeeID;
    }

    public void setTradeEmployeeID(String tradeEmployeeID) {
        this.tradeEmployeeID = tradeEmployeeID;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getSysConfigUsePrice() {
        return sysConfigUsePrice;
    }

    public void setSysConfigUsePrice(String sysConfigUsePrice) {
        this.sysConfigUsePrice = sysConfigUsePrice;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    public String[] getHtArr() {
        return htArr;
    }

    public void setHtArr(String[] htArr) {
        this.htArr = htArr;
    }

    public List<Map<String, Object>> getVerifiedFlagsCAList() {
        return verifiedFlagsCAList;
    }

    public void setVerifiedFlagsCAList(List<Map<String, Object>> verifiedFlagsCAList) {
        this.verifiedFlagsCAList = verifiedFlagsCAList;
    }

	public String getProStocktakeRequestID() {
		return proStocktakeRequestID;
	}

	public void setProStocktakeRequestID(String proStocktakeRequestID) {
		this.proStocktakeRequestID = proStocktakeRequestID;
	}

	public List<Map<String, Object>> getTakingReqDetailList() {
		return takingReqDetailList;
	}

	public void setTakingReqDetailList(List<Map<String, Object>> takingReqDetailList) {
		this.takingReqDetailList = takingReqDetailList;
	}

	public String getStockTakingNoIF() {
		return stockTakingNoIF;
	}

	public void setStockTakingNoIF(String stockTakingNoIF) {
		this.stockTakingNoIF = stockTakingNoIF;
	}
}