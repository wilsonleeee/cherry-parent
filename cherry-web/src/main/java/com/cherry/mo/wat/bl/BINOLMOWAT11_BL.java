/*
 * @(#)BINOLMOWAT10_Action.java     1.0 2015/7/1 
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
package com.cherry.mo.wat.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mo.wat.interfaces.BINOLMOWAT11_IF;
import com.cherry.mo.wat.service.BINOLMOWAT11_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * Job失败履历查询
 * 
 * @author lzs
 * @version 1.0 2015.12.11
 */
public class BINOLMOWAT11_BL  extends SsBaseBussinessLogic implements BINOLMOWAT11_IF{
    
    @Resource(name="binOLMOWAT11_Service")
    private BINOLMOWAT11_Service binOLMOWAT11_Service;

    public int getCountJobRunFaildHistory(Map<String, Object> map) {
        return binOLMOWAT11_Service.getCountJobRunFaildHistory(map);
    }

    public List<Map<String, Object>> getJobRunFaildHistoryList(Map<String, Object> map) {
        return binOLMOWAT11_Service.getJobRunFaildHistoryList(map);
    }
    
}
