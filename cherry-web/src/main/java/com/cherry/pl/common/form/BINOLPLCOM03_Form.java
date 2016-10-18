/*	
 * @(#)BINOLPLCOM03_Form.java     1.0 2012/01/31		
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
package com.cherry.pl.common.form;

import java.util.List;
import java.util.Map;

/**
 * 业务流程规则查看共通 Form
 * 
 * @author niushunjie
 * @version 1.0 2012.01.31
 */
public class BINOLPLCOM03_Form {   
    /** 品牌信息ID */
    private String brandInfoId;
    
    /**工作流文件名称*/
    private String flowType;
    
    /**工作流文件*/
    private Map<String,Object> wfFileContentMap;

    /**所有步骤规则*/
    private List<Map<String,Object>> ruleList;

    public String getBrandInfoId() {
        return brandInfoId;
    }

    public void setBrandInfoId(String brandInfoId) {
        this.brandInfoId = brandInfoId;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public List<Map<String, Object>> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Map<String, Object>> ruleList) {
        this.ruleList = ruleList;
    }

    public Map<String, Object> getWfFileContentMap() {
        return wfFileContentMap;
    }

    public void setWfFileContentMap(Map<String, Object> wfFileContentMap) {
        this.wfFileContentMap = wfFileContentMap;
    }
}
