/*
 * @(#)BINOLPLSCF15_Form.java     1.0 2011/12/19
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

package com.cherry.pl.scf.form;

import java.util.List;
import java.util.Map;

/**
 * 业务流程配置Form
 * 
 * @author niushunjie
 * @version 1.0 2011.12.19
 */
public class BINOLPLSCF15_Form {
	
    /**品牌ID*/
    private String brandInfoId;
    
    /**品牌List*/
    private List<Map<String,Object>> brandInfoList;
    
    /**工作流程信息List*/
    private List<Map<String,Object>> workflowInfoList;

    public List<Map<String, Object>> getWorkflowInfoList() {
        return workflowInfoList;
    }

    public void setWorkflowInfoList(List<Map<String, Object>> workflowInfoList) {
        this.workflowInfoList = workflowInfoList;
    }

    public List<Map<String, Object>> getBrandInfoList() {
        return brandInfoList;
    }

    public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

}
