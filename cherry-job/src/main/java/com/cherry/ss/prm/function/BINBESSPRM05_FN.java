/*	
 * @(#)BINBESSPRM05_FN.java     1.0 2011/08/09	
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
package com.cherry.ss.prm.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.prm.bl.BINBESSPRM05_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 促销产品同步Function
 * 
 * 
 * @author niushunjie
 * @version 1.0 2010.08.09
 */
public class BINBESSPRM05_FN implements FunctionProvider{

    @Resource
    private BINBESSPRM05_BL binBESSPRM05_BL;
    
    private static Logger logger = LoggerFactory.getLogger(BINBESSPRM05_FN.class.getName());
    
    @SuppressWarnings("unchecked")
    @Override
    public void execute(Map promotionProduct, Map arg1, PropertySet ps)
            throws WorkflowException {
        try {
            logger.info("******************************促销产品同步BATCH处理开始***************************");
            int flag = binBESSPRM05_BL.tran_sync(promotionProduct);
            ps.setInt("result", flag);
        } catch (Exception e) {
            // 打印错误信息
            logger.error(e.getMessage(),e);
            throw new WorkflowException();
        } finally {
            logger.info("******************************促销产品同步BATCH处理结束***************************");
        }
    }

}
