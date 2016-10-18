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
import com.cherry.mo.wat.interfaces.BINOLMOWAT10_IF;
import com.cherry.mo.wat.service.BINOLMOWAT10_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

/**
 * 
 * Job运行履历查询
 * 
 * @author ZCF
 * @version 1.0 2015.06.26
 */
public class BINOLMOWAT10_BL  extends SsBaseBussinessLogic implements BINOLMOWAT10_IF{
    
    @Resource(name="binOLMOWAT10_Service")
    private BINOLMOWAT10_Service binOLMOWAT10_Service;

    public int getCountJobRun(Map<String, Object> map) {
        return binOLMOWAT10_Service.getCountJobRun(map);
    }

    public List<Map<String, Object>> getJobRunList(Map<String, Object> map) {
        return binOLMOWAT10_Service.getJobRunList(map);
    }
    
}
