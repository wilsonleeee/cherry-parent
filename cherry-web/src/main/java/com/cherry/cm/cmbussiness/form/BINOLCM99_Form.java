/*  
 * @(#)BINOLCM99_Form.java    1.0.0 2011/10/20    
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
package com.cherry.cm.cmbussiness.form;

public class BINOLCM99_Form {
	
	/** 导出数据格式类型 */
	private String exportType;

	/** 导出文件名 */
	private String exportName;

	/** 导出报表页面ID */
	private String pageId;
	
	/** 导出报表模式：0：一览模式，1：详细模式 */
	private int reportMode;
	
	/** 数据源查询条件参数 */
	private String params;
	
	private int isView;
	
	/** 单据ID */
	private String[] billId;
	
	/** 单据号 */
	private String[] billNo;
	
	/** 唯一码批次号 */
	private String prtUniqueCodeID;
	

	/**目前只用于区分销售单为正常单还是签收单(1:表示签收单，0或者空：表示正常单)*/
	private String receiptFlag;
	
	/** webPos销售单的打印分为直接打印与补打小票两种模式 */
	private String printType;
	
	/**打印吊牌模式类型 1为批量打印 2为全部打印*/
	private String printTagType;
	
	/**商品品牌*/
	private String originalBrand;
	
	public String getExportType() {
		return exportType;
	}

	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public int getReportMode() {
		return reportMode;
	}

	public void setReportMode(int reportMode) {
		this.reportMode = reportMode;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public int getIsView() {
		return isView;
	}

	public void setIsView(int isView) {
		this.isView = isView;
	}

	public String[] getBillId() {
		return billId;
	}

	public void setBillId(String[] billId) {
		this.billId = billId;
	}

	public String[] getBillNo() {
		return billNo;
	}

	public void setBillNo(String[] billNo) {
		this.billNo = billNo;
	}
	
	public String getPrtUniqueCodeID() {
		return prtUniqueCodeID;
	}

	public void setPrtUniqueCodeID(String prtUniqueCodeID) {
		this.prtUniqueCodeID = prtUniqueCodeID;
	}	

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public String getReceiptFlag() {
		return receiptFlag;
	}

	public void setReceiptFlag(String receiptFlag) {
		this.receiptFlag = receiptFlag;
	}

	public String getPrintTagType() {
		return printTagType;
	}

	public void setPrintTagType(String printTagType) {
		this.printTagType = printTagType;
	}

	public String getOriginalBrand() {
		return originalBrand;
	}

	public void setOriginalBrand(String originalBrand) {
		this.originalBrand = originalBrand;
	}
	
	
}
