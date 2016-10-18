/*
 * @(#)BINOLSSPRM34_Form.java     1.0 2010/11/24
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

/**
 * 
 * 发货单编辑Form
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.24
 */
public class BINOLSSPRM34_Form {
	
	/** 促销产品收发货ID */
	private String deliverId;
	
	/** 收发货单号 */
	private String deliverRecNo;
	
	/** 更新日时 */
	private String deliverUpdateTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 发货部门ID */
	private String deliverDepId;
	
	/** 发货仓库ID */
	private String inventoryId;
	
	/** 收货部门ID */
	private String receiveDepId;
	
	/** 明细信息 */
	private String detailInfo;
	
	/** 发货理由 */
	private String reason;
	
	/** 发货区分 */
	private String deliverKbn;
	
	/** 保存区分 */
	private String saveKbn;

	public String getDeliverId() {
		return deliverId;
	}
	
	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}
	
	public String getDeliverRecNo() {
		return deliverRecNo;
	}

	public void setDeliverRecNo(String deliverRecNo) {
		this.deliverRecNo = deliverRecNo;
	}

	public String getDeliverUpdateTime() {
		return deliverUpdateTime;
	}

	public void setDeliverUpdateTime(String deliverUpdateTime) {
		this.deliverUpdateTime = deliverUpdateTime;
	}
	
	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}
	
	public String getDeliverDepId() {
		return deliverDepId;
	}

	public void setDeliverDepId(String deliverDepId) {
		this.deliverDepId = deliverDepId;
	}
	
	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	public String getReceiveDepId() {
		return receiveDepId;
	}

	public void setReceiveDepId(String receiveDepId) {
		this.receiveDepId = receiveDepId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDeliverKbn() {
		return deliverKbn;
	}

	public void setDeliverKbn(String deliverKbn) {
		this.deliverKbn = deliverKbn;
	}

	public String getSaveKbn() {
		return saveKbn;
	}

	public void setSaveKbn(String saveKbn) {
		this.saveKbn = saveKbn;
	}
}
