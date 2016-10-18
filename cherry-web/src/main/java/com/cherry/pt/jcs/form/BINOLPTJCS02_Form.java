/*	
 * @(#)BINOLPTJCS02_Form.java     1.0 2012-7-26 		
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
package com.cherry.pt.jcs.form;

import java.util.List;
import java.util.Map;

/**
 * 
 * 	产品扩展属性Form
 * 
 * @author jijw
 * @version 1.0 2012-7-26
 */
public class BINOLPTJCS02_Form {

	/** 扩展属性ID */
	private String extendPropertyID;

	/** 组织ID */
	private String organizationInfoID;

	/** 品牌ID */
	private String brandInfoID;
	
	/** 扩展对象（物理表） */
	private String extendedTable;
	
	/** 组ID */
	private String groupID;
	
	/** 组名称  */
	private String groupName;
	
	/** 组名称(英文)  */
	private String groupNameForeign;
	
	/** 扩展属性键值 */
	private String propertyKey;
	
	/** 扩展属性中文名称 */
	private String propertyName;
	
	/** 扩展属性英文名称 */
	private String propertyNameForeign;

	/** 扩展属性画面控件类型 */
	private String viewType;
	
	/** 有效区分 */
	private String validFlag;
	
	/** 更新次数*/
	private String modifyCount ;
	
	/** 更新时间*/
	private String updateTime;

	/** 产品扩展属性list*/
	private List<Map<String,Object>> extPropertyList;
	
	/************************                  产品扩展属性预设值         start                                ****************************/
	
	/** 产品扩展属性预设值 ID */
	private String extendDefValueID;
	
	/** 产品扩展属性预设值 codeKey(选项值) */
	private String codeKey;
	
	/** 产品扩展属性预设值 value(选项名称（中文）) */
	private String value1;
	
	/** 产品扩展属性预设值 value(选项名称（英文）) */
	private String value2;
	
	/** 产品扩展属性预设值 value(选项名称（其他语言）) */
	private String value3;
	
	/** 产品扩展属性预设值 OrderNumber(排序) */
	private String orderNumber;
	
	/** 产品扩展属性预设值List(排序) */
	private List<Map<String,Object>> extDefValueList;
	
	/** 要编辑的codeKey **/
	private String [] codeKeyArr;
	
	/** 要编辑的value1 **/
	private String [] value1Arr;
	
	/** 要编辑的value2 **/
	private String [] value2Arr;
	
	/** 要编辑的value3 **/
	private String [] value3Arr;
	
	/** 要编辑的orderNumber **/
	private String [] orderNumberArr;
	
	/************************                  产品扩展属性预设值         end                                ****************************/

	public String getExtendPropertyID() {
		return extendPropertyID;
	}

	public void setExtendPropertyID(String extendPropertyID) {
		this.extendPropertyID = extendPropertyID;
	}

	public String getOrganizationInfoID() {
		return organizationInfoID;
	}

	public void setOrganizationInfoID(String organizationInfoID) {
		this.organizationInfoID = organizationInfoID;
	}

	public String getBrandInfoID() {
		return brandInfoID;
	}

	public void setBrandInfoID(String brandInfoID) {
		this.brandInfoID = brandInfoID;
	}

	public String getExtendedTable() {
		return extendedTable;
	}

	public void setExtendedTable(String extendedTable) {
		this.extendedTable = extendedTable;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<Map<String, Object>> getExtPropertyList() {
		return extPropertyList;
	}

	public void setExtPropertyList(List<Map<String, Object>> extPropertyList) {
		this.extPropertyList = extPropertyList;
	}
	
	public String getExtendDefValueID() {
		return extendDefValueID;
	}

	public void setExtendDefValueID(String extendDefValueID) {
		this.extendDefValueID = extendDefValueID;
	}
	
	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}
	
	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<Map<String, Object>> getExtDefValueList() {
		return extDefValueList;
	}

	public void setExtDefValueList(List<Map<String, Object>> extDefValueList) {
		this.extDefValueList = extDefValueList;
	}
	
	public String[] getCodeKeyArr() {
		return codeKeyArr;
	}

	public void setCodeKeyArr(String[] codeKeyArr) {
		this.codeKeyArr = codeKeyArr;
	}

	public String[] getValue1Arr() {
		return value1Arr;
	}

	public void setValue1Arr(String[] value1Arr) {
		this.value1Arr = value1Arr;
	}

	public String[] getValue2Arr() {
		return value2Arr;
	}

	public void setValue2Arr(String[] value2Arr) {
		this.value2Arr = value2Arr;
	}

	public String[] getValue3Arr() {
		return value3Arr;
	}

	public void setValue3Arr(String[] value3Arr) {
		this.value3Arr = value3Arr;
	}

	public String[] getOrderNumberArr() {
		return orderNumberArr;
	}

	public void setOrderNumberArr(String[] orderNumberArr) {
		this.orderNumberArr = orderNumberArr;
	}
	
	public String getGroupNameForeign() {
		return groupNameForeign;
	}

	public void setGroupNameForeign(String groupNameForeign) {
		this.groupNameForeign = groupNameForeign;
	}

	public String getPropertyNameForeign() {
		return propertyNameForeign;
	}

	public void setPropertyNameForeign(String propertyNameForeign) {
		this.propertyNameForeign = propertyNameForeign;
	}
	

}
