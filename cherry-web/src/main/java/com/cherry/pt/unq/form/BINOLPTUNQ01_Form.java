/*  
 * @(#)BINOLPTUNQ01_Form.java    1.0 2016-05-26     
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

package com.cherry.pt.unq.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 
 * 唯一码生成Form
 * 
 * @author zw
 * @version 1.0 2016.05.26
 */
public class BINOLPTUNQ01_Form extends DataTable_BaseForm implements Serializable{

	private static final long serialVersionUID = -1452099391238604136L;

	/** 产品唯一码ID **/
	private String prtUniqueCodeID;
	
	/** 生成时间 **/
	private String generateDate; 
	
	/** 生成方式 **/
	private String generateType; 
	
	/** 生成数量 **/
	private String generateCountVal; 
	
	/** 箱数 **/
	private String boxCount; 
	
	/** 每箱规格(数量) **/
	private String spec;
	
	/** 是否需要箱码 **/
	private String needBoxCode; 
	
	/** 默认激活状态 **/
	private String defaultActivationStatus;
	
	/** 产品ID **/
	private String productID;
	
	/** 导出EXCEl次数 **/
	private String exportExcelCount;
	
	/** 导出PDF次数 **/
	private String exportPdfCount;
	
	/** 产品唯一码明细ID **/
	private String prtUniqueCodeDetailID;
	
	/** 产品厂商ID **/
	private String productVendorID;
	
	/** 产品厂商ID **/
	private String prtVendorId;
	
	/** 积分唯一码 **/
	private String pointUniqueCode;
	
	/** 箱码 **/
	private String boxCode;
	
	/** 关联唯一码 **/
	private String relUniqueCode;
	
	/** 激活状态 **/
	private String activationStatus;
	
	/** 使用状态 **/
	private String useStatus;
	
	/** 唯一码生成List */
	private List<Map<String, Object>> unqGenerateList;

	/** 生成日期开始日期 */
	private String fromDate ;
	
	/** 生成日期结束日期 */
	private String  toDate ;
	
	/** 产品批次号  */
	private String prtUniqueCodeBatchNo;
		
	/** 产品批次描述  */
	private String prtUniqueCodeBatchDescribe;


	public String getGenerateDate() {
		return generateDate;
	}

	public void setGenerateDate(String generateDate) {
		this.generateDate = generateDate;
	}

	public String getGenerateType() {
		return generateType;
	}

	public void setGenerateType(String generateType) {
		this.generateType = generateType;
	}

	public String getGenerateCountVal() {
		return generateCountVal;
	}

	public void setGenerateCountVal(String generateCountVal) {
		this.generateCountVal = generateCountVal;
	}

	public String getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(String boxCount) {
		this.boxCount = boxCount;
	}
	
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getNeedBoxCode() {
		return needBoxCode;
	}

	public void setNeedBoxCode(String needBoxCode) {
		this.needBoxCode = needBoxCode;
	}

	public String getDefaultActivationStatus() {
		return defaultActivationStatus;
	}

	public void setDefaultActivationStatus(String defaultActivationStatus) {
		this.defaultActivationStatus = defaultActivationStatus;
	}


	public String getExportExcelCount() {
		return exportExcelCount;
	}

	public void setExportExcelCount(String exportExcelCount) {
		this.exportExcelCount = exportExcelCount;
	}

	public String getExportPdfCount() {
		return exportPdfCount;
	}

	public void setExportPdfCount(String exportPdfCount) {
		this.exportPdfCount = exportPdfCount;
	}


	public String getPrtUniqueCodeID() {
		return prtUniqueCodeID;
	}

	public void setPrtUniqueCodeID(String prtUniqueCodeID) {
		this.prtUniqueCodeID = prtUniqueCodeID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getPrtUniqueCodeDetailID() {
		return prtUniqueCodeDetailID;
	}

	public void setPrtUniqueCodeDetailID(String prtUniqueCodeDetailID) {
		this.prtUniqueCodeDetailID = prtUniqueCodeDetailID;
	}

	public String getProductVendorID() {
		return productVendorID;
	}

	public void setProductVendorID(String productVendorID) {
		this.productVendorID = productVendorID;
	}
	
	public String getPrtVendorId() {
		return prtVendorId;
	}

	public void setPrtVendorId(String prtVendorId) {
		this.prtVendorId = prtVendorId;
	}

	public String getPointUniqueCode() {
		return pointUniqueCode;
	}
	
	public String getBoxCode() {
		return boxCode;
	}

	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}

	public void setPointUniqueCode(String pointUniqueCode) {
		this.pointUniqueCode = pointUniqueCode;
	}

	public String getRelUniqueCode() {
		return relUniqueCode;
	}

	public void setRelUniqueCode(String relUniqueCode) {
		this.relUniqueCode = relUniqueCode;
	}

	public String getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	
	public List<Map<String, Object>> getUnqGenerateList() {
		return unqGenerateList;
	}

	public void setUnqGenerateList(List<Map<String, Object>> unqGenerateList) {
		this.unqGenerateList = unqGenerateList;
	}
	

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getPrtUniqueCodeBatchNo() {
		return prtUniqueCodeBatchNo;
	}

	public void setPrtUniqueCodeBatchNo(String prtUniqueCodeBatchNo) {
		this.prtUniqueCodeBatchNo = prtUniqueCodeBatchNo;
	}

	public String getPrtUniqueCodeBatchDescribe() {
		return prtUniqueCodeBatchDescribe;
	}

	public void setPrtUniqueCodeBatchDescribe(String prtUniqueCodeBatchDescribe) {
		this.prtUniqueCodeBatchDescribe = prtUniqueCodeBatchDescribe;
	}

}
