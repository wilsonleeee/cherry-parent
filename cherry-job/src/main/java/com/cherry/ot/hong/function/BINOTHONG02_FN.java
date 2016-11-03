/*
 * @(#)BINOTHONG01_FN.java     1.0 2014/09/04
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

package com.cherry.ot.hong.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.hong.interfaces.BINOTHONG02_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 宏巍电商订单获取 FN
 * 
 * 
 * @author jijw
 * @version 1.0 2015.12.07
 */
public class BINOTHONG02_FN implements FunctionProvider {

    @Resource(name="binOTHONG02_BL")
    private BINOTHONG02_IF binOTHONG02_BL;
    
    private static Logger logger = LoggerFactory.getLogger(BINOTHONG02_FN.class.getName());
    
    @Override
    public void execute(Map paramMap, Map arg1, PropertySet ps) throws WorkflowException {
        try {
            logger.info("******************************宏巍电商(HONG02)订单获取BATCH处理开始***************************");
            paramMap.put("RunType", "AT");
            int flag = binOTHONG02_BL.tran_batchOTHONG(paramMap);
            ps.setInt("result", flag);
        } catch (Exception e) {
            // 打印错误信息
            logger.error(e.getMessage(),e);
            throw new WorkflowException();
        } finally {
            logger.info("******************************宏巍电商(HONG02)订单获取BATCH处理结束***************************");
        }
    }
}