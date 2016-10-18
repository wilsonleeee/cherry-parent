/*  
 * @(#)BINOLMOCIO07_Form.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;

public class BINOLMOCIO07_Form extends BINOLCM13_Form{
	/** ID */
	private String saleTargetID;
	
	/** 销售单位类型 */
	private String type;
	
	/** 参数 */
	private String parameter;
	
	/** 目标月*/
	private String targetMonth;
	
	/** 目标日 **/
	private String targetDay;
	
	/** 销售金额指标 */
	private String targetMoney;
	
	/** 销售数量指标 */
	private String targetQuantity;
	
	/** 完成金额  */
	private String completeMoney;
	
	/** 完成数量  */
	private String completeQuantity;
	
	/** 品牌*/
	private String brandInfoId;
	
	private String validFlag;
	
	private String count;
	
	private String[] parameterArr;
	
	private String[] nameArr;
	
	private String[] differentArr;
	
	private String editTargetDate;
	
	private String editTargetMoney;
	
	private String editTargetQuantity;
	
	private String addBrandInfoId;
	
	private String addtype;
	
	private String targetDateType;
	
	/**编辑销售目标的品牌ID*/
	private String editBrandInfoId;
	
	private String addTargetMonth;
	
	private String addTargetDay;
	
	private String addTargetMoney;
	
	private String addTargetQuantity;
	
	private String addpop;
	
	/** 活动Code */
	private String campaignCode;
	
	/** 活动名称 */
	private String campaignName;
	
	//要获取的树节点标志
	private String treeNodesFlag;
	
	/**导入销售目标所属品牌*/
	private String importBrandInfoId;
	
	/**处理结果信息显示*/
	private String message;
	
	/** 销售目标类型 */
	private String targetType;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImportBrandInfoId() {
		return importBrandInfoId;
	}

	public void setImportBrandInfoId(String importBrandInfoId) {
		this.importBrandInfoId = importBrandInfoId;
	}
	
	public String getEditTargetDate() {
		return editTargetDate;
	}

	public void setEditTargetDate(String editTargetDate) {
		this.editTargetDate = editTargetDate;
	}

	public String getEditTargetMoney() {
		return editTargetMoney;
	}

	public void setEditTargetMoney(String editTargetMoney) {
		this.editTargetMoney = editTargetMoney;
	}

	public String getEditTargetQuantity() {
		return editTargetQuantity;
	}

	public void setEditTargetQuantity(String editTargetQuantity) {
		this.editTargetQuantity = editTargetQuantity;
	}

	public String getCompleteMoney() {
		return completeMoney;
	}

	public void setCompleteMoney(String completeMoney) {
		this.completeMoney = completeMoney;
	}

	public String getCompleteQuantity() {
		return completeQuantity;
	}

	public void setCompleteQuantity(String completeQuantity) {
		this.completeQuantity = completeQuantity;
	}

	private List<Map<String, Object>> saleTargetList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getTargetMoney() {
		return targetMoney;
	}

	public void setTargetMoney(String targetMoney) {
		this.targetMoney = targetMoney;
	}

	public String getTargetQuantity() {
		return targetQuantity;
	}

	public void setTargetQuantity(String targetQuantity) {
		this.targetQuantity = targetQuantity;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setSaleTargetList(List<Map<String, Object>> saleTargetList) {
		this.saleTargetList = saleTargetList;
	}

	public List<Map<String, Object>> getSaleTargetList() {
		return saleTargetList;
	}

	public void setSaleTargetID(String saleTargetID) {
		this.saleTargetID = saleTargetID;
	}

	public String getSaleTargetID() {
		return saleTargetID;
	}

	public void setParameterArr(String[] parameterArr) {
		this.parameterArr = parameterArr;
	}

	public String[] getParameterArr() {
		return parameterArr;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCount() {
		return count;
	}

	public void setNameArr(String[] nameArr) {
		this.nameArr = nameArr;
	}

	public String[] getNameArr() {
		return nameArr;
	}

	public void setDifferentArr(String[] differentArr) {
		this.differentArr = differentArr;
	}

	public String[] getDifferentArr() {
		return differentArr;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setAddBrandInfoId(String addBrandInfoId) {
		this.addBrandInfoId = addBrandInfoId;
	}

	public String getAddBrandInfoId() {
		return addBrandInfoId;
	}

	public void setTreeNodesFlag(String treeNodesFlag) {
		this.treeNodesFlag = treeNodesFlag;
	}

	public String getTreeNodesFlag() {
		return treeNodesFlag;
	}

	public void setAddtype(String addtype) {
		this.addtype = addtype;
	}

	public String getAddtype() {
		return addtype;
	}

	public String getEditBrandInfoId() {
		return editBrandInfoId;
	}

	public void setEditBrandInfoId(String editBrandInfoId) {
		this.editBrandInfoId = editBrandInfoId;
	}

	public void setAddTargetMoney(String addTargetMoney) {
		this.addTargetMoney = addTargetMoney;
	}

	public String getAddTargetMoney() {
		return addTargetMoney;
	}

	public void setAddTargetQuantity(String addTargetQuantity) {
		this.addTargetQuantity = addTargetQuantity;
	}

	public String getAddTargetQuantity() {
		return addTargetQuantity;
	}

	public void setAddpop(String addpop) {
		this.addpop = addpop;
	}

	public String getAddpop() {
		return addpop;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getTargetDay() {
		return targetDay;
	}

	public void setTargetDay(String targetDay) {
		this.targetDay = targetDay;
	}

	public String getTargetMonth() {
		return targetMonth;
	}

	public void setTargetMonth(String targetMonth) {
		this.targetMonth = targetMonth;
	}

	public String getAddTargetMonth() {
		return addTargetMonth;
	}

	public void setAddTargetMonth(String addTargetMonth) {
		this.addTargetMonth = addTargetMonth;
	}

	public String getAddTargetDay() {
		return addTargetDay;
	}

	public void setAddTargetDay(String addTargetDay) {
		this.addTargetDay = addTargetDay;
	}

	public String getTargetDateType() {
		return targetDateType;
	}

	public void setTargetDateType(String targetDateType) {
		this.targetDateType = targetDateType;
	}

}