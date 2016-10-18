/*  
 * @(#)BINOLMOMAN04_Form.java     1.0 2011/05/31      
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
package com.cherry.mo.man.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLMOMAN04_Form extends DataTable_BaseForm {

	private String machineType = "";

	private String LeftRootNodes = "";
	
	private String RightAboveRootNodes = "";
	
	private String RightUndersideRootNodes = "";
	
	private String regionType = "";
	
	private String updateStatus = "";
	
	private String id = "";
	
	private String checkNodesArray ="";
	
	private String brandInfoId = "";
	
	private Boolean checked = false;
	
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getCheckNodesArray() {
		return checkNodesArray;
	}

	public void setCheckNodesArray(String checkNodesArray) {
		this.checkNodesArray = checkNodesArray;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String getLeftRootNodes() {
		return LeftRootNodes;
	}

	public void setLeftRootNodes(String leftRootNodes) {
		LeftRootNodes = leftRootNodes;
	}

	public String getRightAboveRootNodes() {
		return RightAboveRootNodes;
	}

	public void setRightAboveRootNodes(String rightAboveRootNodes) {
		RightAboveRootNodes = rightAboveRootNodes;
	}

	public String getRightUndersideRootNodes() {
		return RightUndersideRootNodes;
	}

	public void setRightUndersideRootNodes(String rightUndersideRootNodes) {
		RightUndersideRootNodes = rightUndersideRootNodes;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	
}
