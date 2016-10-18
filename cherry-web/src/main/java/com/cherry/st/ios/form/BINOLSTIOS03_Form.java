/*  
 * @(#)BINOLSTIOS03_Form.java     1.0 2011/05/31      
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
package com.cherry.st.ios.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTIOS03_Form extends DataTable_BaseForm{
	/** 操作日期 */
	private String operateDate;
	
	/**厂商编码*/
	private String unitCode;

	/**产品条码*/
	private String barCode;
	
	/**备注*/
	private String comments;
	
	/**实体仓库List*/
	private List<Map<String, Object>> depotList;
	
	/**实体仓库ID*/
	private String depotInfoId;
	
	/**逻辑仓库ID*/
	private String logicInventoryInfoId;
	
	/**逻辑仓库List*/
	private List<Map<String, Object>>logicDepotsList;
	
	/**产品厂商ID组*/
	private String[] productVendorIdArr;
	/**部门*/
	private String departInit;	
	/**部门ID*/
	private String inOrganizationId;

  private int organizationId;
	/**价格*/
	private String[] priceArr;
	
	/**产品数量组*/
	private String[] quantityArr;
	
	/**备注组*/	
	private String[] commentsArr;
	
	/**品牌id*/
	private String brandInfoId;
	
	public String[] getProductVendorIdArr() {
		return productVendorIdArr;
	}

	public void setProductVendorIdArr(String[] productVendorIdArr) {
		this.productVendorIdArr = productVendorIdArr;
	}

	public String[] getQuantityArr() {
		return quantityArr;
	}

	public void setQuantityArr(String[] quantityArr) {
		this.quantityArr = quantityArr;
	}

	public String[] getCommentsArr() {
		return commentsArr;
	}

	public void setCommentsArr(String[] commentsArr) {
		this.commentsArr = commentsArr;
	}

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

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setDepotList(List<Map<String, Object>> depotList) {
		this.depotList = depotList;
	}

	public List<Map<String, Object>> getDepotList() {
		return depotList;
	}

	public void setDepotInfoId(String depotInfoId) {
		this.depotInfoId = depotInfoId;
	}

	public String getDepotInfoId() {
		return depotInfoId;
	}

	public void setLogicDepotsList(List<Map<String, Object>> logicDepotsList) {
		this.logicDepotsList = logicDepotsList;
	}

	public List<Map<String, Object>> getLogicDepotsList() {
		return logicDepotsList;
	}

	public void setLogicInventoryInfoId(String logicInventoryInfoId) {
		this.logicInventoryInfoId = logicInventoryInfoId;
	}

	public String getLogicInventoryInfoId() {
		return logicInventoryInfoId;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public void setPriceArr(String[] priceArr) {
		this.priceArr = priceArr;
	}

	public String[] getPriceArr() {
		return priceArr;
	}
	public String getDepartInit() {
		return departInit;
	}

	public void setDepartInit(String departInit) {
		this.departInit = departInit;
	}
	
    public String getInOrganizationId() {
			return inOrganizationId;
	}

	public void setInOrganizationId(String inOrganizationId) {
			this.inOrganizationId = inOrganizationId;
	}

	public int getOrganizationId() {
			return organizationId;
	}

	public void setOrganizationId(int organizationId) {
			this.organizationId = organizationId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

		
}
