
/*  
 * @(#)BINOLSTJCS05_Form.java    1.0 2011-9-5     
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
package com.cherry.st.jcs.form;

import com.cherry.cm.form.DataTable_BaseForm;

public class BINOLSTJCS05_Form extends DataTable_BaseForm {

	//仓库/区域名称
	private String name;
	
	//仓库/区域code
	private String code;
	
	//所属品牌
	private String brandInfoId;
	
	//业务类型
	private String businessType;
	
	//实体仓库ID
	private String DeportId;
	
	//出库ID区分
	private String OutFlag;
	
	//仓库或者区域String
	private String deportOrRegionStr;
	
	//是否同时进行逆向业务
	private String flag;
	
	//仓库业务关系ID组String
	private String deportBusinessStr;
	
	//仓库业务关系ID
	private String depotBusinessId;
	
	/**按部门组织架构配置*/
	private String configByDepOrg;
	
	public String getDepotBusinessId() {
		return depotBusinessId;
	}

	public void setDepotBusinessId(String depotBusinessId) {
		this.depotBusinessId = depotBusinessId;
	}

	public String getDeportBusinessStr() {
		return deportBusinessStr;
	}

	public void setDeportBusinessStr(String deportBusinessStr) {
		this.deportBusinessStr = deportBusinessStr;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDeportId() {
		return DeportId;
	}

	public void setDeportId(String deportId) {
		DeportId = deportId;
	}

	public String getOutFlag() {
		return OutFlag;
	}

	public void setOutFlag(String outFlag) {
		OutFlag = outFlag;
	}

	public String getDeportOrRegionStr() {
		return deportOrRegionStr;
	}

	public void setDeportOrRegionStr(String deportOrRegionStr) {
		this.deportOrRegionStr = deportOrRegionStr;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

    public String getConfigByDepOrg() {
        return configByDepOrg;
    }

    public void setConfigByDepOrg(String configByDepOrg) {
        this.configByDepOrg = configByDepOrg;
    }
}
