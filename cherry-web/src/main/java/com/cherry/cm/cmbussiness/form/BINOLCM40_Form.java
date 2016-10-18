/*	
 * @(#)BINOLCM40_Form.java     1.0 2013/12/25		
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
package com.cherry.cm.cmbussiness.form;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * Batch工作流步骤查看 共通Form
 * 
 * @author niushunjie
 * @version 1.0 2013/12/25
 */
public class BINOLCM40_Form extends DataTable_BaseForm {
	
    /** batch执行结果 **/
    private String batchResult;
    
    /** 当前步骤json格式 **/
    private String currentStepsJson;
    
    /** 步骤json格式 **/
    private String stepsJson;

    public String getBatchResult() {
        return batchResult;
    }

    public void setBatchResult(String batchResult) {
        this.batchResult = batchResult;
    }

    public String getCurrentStepsJson() {
        return currentStepsJson;
    }

    public void setCurrentStepsJson(String currentStepsJson) {
        this.currentStepsJson = currentStepsJson;
    }

    public String getStepsJson() {
        return stepsJson;
    }

    public void setStepsJson(String stepsJson) {
        this.stepsJson = stepsJson;
    }
}