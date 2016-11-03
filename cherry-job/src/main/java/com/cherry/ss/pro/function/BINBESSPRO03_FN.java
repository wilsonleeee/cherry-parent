/*
 * @(#)BINBESSPRO03_FN.java     1.0 2011/09/01
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

package com.cherry.ss.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.pro.bl.BINBESSPRO03_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品库存同步FN
 * 
 * 
 * @author niushunjie
 * @version 1.0 2010.12.09
 */
public class BINBESSPRO03_FN implements FunctionProvider {

    @Resource
    private BINBESSPRO03_BL binBESSPRO03_BL;
    
    private static Logger logger = LoggerFactory.getLogger(BINBESSPRO03_FN.class.getName());
    
    @Override
    public void execute(Map product, Map arg1, PropertySet ps)
            throws WorkflowException {
        try {
            logger.info("******************************产品同步BATCH处理开始***************************");
            int flag = binBESSPRO03_BL.tran_sync(product);
            ps.setInt("result", flag);
        } catch (Exception e) {
            // 打印错误信息
            logger.error(e.getMessage(),e);
            throw new WorkflowException();
        } finally {
            logger.info("******************************产品同步BATCH处理结束***************************");
        }
    }

}
