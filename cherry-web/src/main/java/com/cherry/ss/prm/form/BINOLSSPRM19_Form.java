/*		
 * @(#)BINOLSSPRM19_Form.java     1.0 2010/12/10		
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

import java.util.List;
import java.util.Map;

/**
 * 调拨画面的form
 * 
 * @author dingyc
 * 
 */
public class BINOLSSPRM19_Form {

	/** 调拨日期 */
	private String deliverDate;

	// out
	/** 岗位ID List */
	private List<Map<String, String>> positionIdList;
	/** 调入部门List */
	private List<Map<String, String>> inOrganizationList;
	/** 调入仓库 List */
	private List<Map<String, Object>> inDepotList;
	/** 调入逻辑仓库 List */
	private List<Map<String, Object>> logicInventoryList;
	/** 调出部门List */
	private List<Map<String, Object>> outOrganizationList;

	// in
	/** 岗位ID */
	private String positionId;

	/** 调入部门 */
	private String inOrganizationId;

	/** 调入仓库 */
	private String inDepotId;

	/** 调入逻辑仓库 */
	private String inLoginDepotId;

	/** 调拨原因 */
	private String reasonAll;

	/** 调出部门 */
	private String[] outOrganizationIDArr;

	/** 产品厂商ID */
	private String[] promotionProductVendorIDArr;

	/** 促销品单价 */
	private String[] priceUnitArr;

	/** 数量 基本单位 */
	private String[] quantityuArr;

	/** 单条总价 */
	private String[] priceTotalArr;

	/** 发货理由 */
	private String[] reasonArr;

	private String departInit;

	private int organizationId;

	private String outOrganizationId;

	/**
	 * @return the deliverDate
	 */
	public String getDeliverDate() {
		return deliverDate;
	}

	/**
	 * @param deliverDate
	 *            the deliverDate to set
	 */
	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

	/**
	 * @return the positionIdList
	 */
	public List<Map<String, String>> getPositionIdList() {
		return positionIdList;
	}

	/**
	 * @param positionIdList
	 *            the positionIdList to set
	 */
	public void setPositionIdList(List<Map<String, String>> positionIdList) {
		this.positionIdList = positionIdList;
	}

	/**
	 * @return the inOrganizationList
	 */
	public List<Map<String, String>> getInOrganizationList() {
		return inOrganizationList;
	}

	/**
	 * @param inOrganizationList
	 *            the inOrganizationList to set
	 */
	public void setInOrganizationList(
			List<Map<String, String>> inOrganizationList) {
		this.inOrganizationList = inOrganizationList;
	}

	/**
	 * @return the inDepotList
	 */
	public List<Map<String, Object>> getInDepotList() {
		return inDepotList;
	}

	/**
	 * @param inDepotList
	 *            the inDepotList to set
	 */
	public void setInDepotList(List<Map<String, Object>> inDepotList) {
		this.inDepotList = inDepotList;
	}

	/**
	 * @return the logicInventoryList
	 */
	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	/**
	 * @param logicInventoryList
	 *            the logicInventoryList to set
	 */
	public void setLogicInventoryList(
			List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}

	/**
	 * @return the outOrganizationList
	 */
	public List<Map<String, Object>> getOutOrganizationList() {
		return outOrganizationList;
	}

	/**
	 * @param outOrganizationList
	 *            the outOrganizationList to set
	 */
	public void setOutOrganizationList(
			List<Map<String, Object>> outOrganizationList) {
		this.outOrganizationList = outOrganizationList;
	}

	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId
	 *            the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the inOrganizationId
	 */
	public String getInOrganizationId() {
		return inOrganizationId;
	}

	/**
	 * @param inOrganizationId
	 *            the inOrganizationId to set
	 */
	public void setInOrganizationId(String inOrganizationId) {
		this.inOrganizationId = inOrganizationId;
	}

	/**
	 * @return the inDepotId
	 */
	public String getInDepotId() {
		return inDepotId;
	}

	/**
	 * @param inDepotId
	 *            the inDepotId to set
	 */
	public void setInDepotId(String inDepotId) {
		this.inDepotId = inDepotId;
	}

	/**
	 * @return the inLoginDepotId
	 */
	public String getInLoginDepotId() {
		return inLoginDepotId;
	}

	/**
	 * @param inLoginDepotId
	 *            the inLoginDepotId to set
	 */
	public void setInLoginDepotId(String inLoginDepotId) {
		this.inLoginDepotId = inLoginDepotId;
	}

	/**
	 * @return the reasonAll
	 */
	public String getReasonAll() {
		return reasonAll;
	}

	/**
	 * @param reasonAll
	 *            the reasonAll to set
	 */
	public void setReasonAll(String reasonAll) {
		this.reasonAll = reasonAll;
	}

	/**
	 * @return the outOrganizationIDArr
	 */
	public String[] getOutOrganizationIDArr() {
		return outOrganizationIDArr;
	}

	/**
	 * @param outOrganizationIDArr
	 *            the outOrganizationIDArr to set
	 */
	public void setOutOrganizationIDArr(String[] outOrganizationIDArr) {
		this.outOrganizationIDArr = outOrganizationIDArr;
	}

	/**
	 * @return the promotionProductVendorIDArr
	 */
	public String[] getPromotionProductVendorIDArr() {
		return promotionProductVendorIDArr;
	}

	/**
	 * @param promotionProductVendorIDArr
	 *            the promotionProductVendorIDArr to set
	 */
	public void setPromotionProductVendorIDArr(
			String[] promotionProductVendorIDArr) {
		this.promotionProductVendorIDArr = promotionProductVendorIDArr;
	}

	/**
	 * @return the priceUnitArr
	 */
	public String[] getPriceUnitArr() {
		return priceUnitArr;
	}

	/**
	 * @param priceUnitArr
	 *            the priceUnitArr to set
	 */
	public void setPriceUnitArr(String[] priceUnitArr) {
		this.priceUnitArr = priceUnitArr;
	}

	/**
	 * @return the quantityuArr
	 */
	public String[] getQuantityuArr() {
		return quantityuArr;
	}

	/**
	 * @param quantityuArr
	 *            the quantityuArr to set
	 */
	public void setQuantityuArr(String[] quantityuArr) {
		this.quantityuArr = quantityuArr;
	}

	/**
	 * @return the priceTotalArr
	 */
	public String[] getPriceTotalArr() {
		return priceTotalArr;
	}

	/**
	 * @param priceTotalArr
	 *            the priceTotalArr to set
	 */
	public void setPriceTotalArr(String[] priceTotalArr) {
		this.priceTotalArr = priceTotalArr;
	}

	/**
	 * @return the reasonArr
	 */
	public String[] getReasonArr() {
		return reasonArr;
	}

	/**
	 * @param reasonArr
	 *            the reasonArr to set
	 */
	public void setReasonArr(String[] reasonArr) {
		this.reasonArr = reasonArr;
	}

	public String getDepartInit() {
		return departInit;
	}

	public void setDepartInit(String departInit) {
		this.departInit = departInit;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getOutOrganizationId() {
		return outOrganizationId;
	}

	public void setOutOrganizationId(String outOrganizationId) {
		this.outOrganizationId = outOrganizationId;
	}

}
