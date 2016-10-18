/*	
 * @(#)BINOLPLCOM02_IF.java     1.0 2011/12/20		
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
package com.cherry.pl.common.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 工作流向导共通 IF
 * 
 * @author niushunjie
 * @version 1.0 2011.12.20
 */
public interface BINOLPLCOM02_IF {
    /**
     * 取工作流程文件名
     * @param flowType
     * @return
     */
    public String getFlowFileName(String flowType);
    
    /**
     * 查询在界面上显示的规则
     * @param ruleMap
     * @param param
     * @return
     */
    public Map<String,Object> searchRuleMap(Map<String,Object> ruleMap,Map<String,Object> param);
    
    /**
     * 设置按钮区
     * @param param
     */
    public List<Map<String,Object>> getButtonList(Map<String,Object> param);
    
    /**
     * 根据工作流名称取workflowid
     * @param param
     * @return
     */
    public long getWorkFlowID(Map<String,Object> param);
}
