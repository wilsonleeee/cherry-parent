
/*  
 * @(#)StIndexForm.java    1.0 2011-11-22     
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

package com.cherry.st.common.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class StIndexForm extends DataTable_BaseForm {

	//实体仓库ID
	private String depotId;
	
	//入出库区分
	private String inOutFlag;
	
	//业务类型
	private String businessType;
	
	//查询字符串
	private String inputString;
	
	//品牌ID
	private String brandInfoId;
	
	//部门ID
	private String departId;
	
	//选择类型
	private String checkType;
	
	//是否支持终端盘点
	private String witposStacking;

	public String getDepotId() {
		return depotId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public String getInOutFlag() {
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getInputString() {
		return inputString;
	}

	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

	public String getWitposStacking() {
		return witposStacking;
	}

	public void setWitposStacking(String witposStacking) {
		this.witposStacking = witposStacking;
	}
	
}
