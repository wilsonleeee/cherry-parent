/*
 * @(#)BINOLSTJCS01_Form.java     1.0 2011/08/25
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

/**
 * 
 * 逻辑仓库与业务关联
 * @author LuoHong
 * @version 1.0 2011.08.25
 * 
 **/
package com.cherry.st.jcs.form;

import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTJCS09_Form extends DataTable_BaseForm {
	/** 逻辑仓库关系Id */
	private int logicDepotId;
	/** 业务所属 **/
	private String logicType;
	/** 业务类型代码 */
	private String businessType;
	/** 产品类型 */
	private String productType;
	/** 逻辑仓库 */
	private String logicInvId;
	/** 出入库区分 */
	private String inOutFlag;
	/** 优先级 */
	private String configOrder;
	/** 备注 */
	private String comments;
	/** 有效区分 */
	private String validFlag;
	/** 所属品牌 */
	private String brandInfoId;
	/** 逻辑仓库关系List */
	private List<Map<String, Object>> logicDepotList;
	/** 弹出框标题 */
	private String messageTitle;
	/** 弹出框内容 */
	private String messageBody;
	/** 逻辑仓库名称 */
	private String inventoryName;
	/** 逻辑仓库代码 */
	private String logicInventoryCode;
	/** 子类型 */
	private String subType;
	/** 终端类型 */
	private String type;
	/** 编辑区分    1编辑*/
	private String editFlag;
	/** 被编辑的SubType*/
	private String editedSubType;
	/** 被编辑的ProductType*/
	private String editedProductType;
	
	private String logicDepotIdArr;
	
	/** 更新时间*/
	private String updateTime;
	
	/** 更新次数*/
	private String modifyCount ;

	public String getLogicDepotIdArr() {
		return logicDepotIdArr;
	}

	public void setLogicDepotIdArr(String logicDepotIdArr) {
		this.logicDepotIdArr = logicDepotIdArr;
	}
	
	public String getLogicType() {
		return logicType;
	}

	public void setLogicType(String logicType) {
		this.logicType = logicType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getInOutFlag() {
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}

	public String getConfigOrder() {
		return configOrder;
	}

	public void setConfigOrder(String configOrder) {
		this.configOrder = configOrder;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getLogicInvId() {
		return logicInvId;
	}

	public void setLogicInvId(String logicInvId) {
		this.logicInvId = logicInvId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public List<Map<String, Object>> getLogicDepotList() {
		return logicDepotList;
	}

	public void setLogicDepotList(List<Map<String, Object>> logicDepotList) {
		this.logicDepotList = logicDepotList;
	}

	public int getLogicDepotId() {
		return logicDepotId;
	}

	public void setLogicDepotId(int logicDepotId) {
		this.logicDepotId = logicDepotId;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getLogicInventoryCode() {
		return logicInventoryCode;
	}

	public void setLogicInventoryCode(String logicInventoryCode) {
		this.logicInventoryCode = logicInventoryCode;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}

	public String getEditedSubType() {
		return editedSubType;
	}

	public void setEditedSubType(String editedSubType) {
		this.editedSubType = editedSubType;
	}

	public String getEditedProductType() {
		return editedProductType;
	}

	public void setEditedProductType(String editedProductType) {
		this.editedProductType = editedProductType;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

}
