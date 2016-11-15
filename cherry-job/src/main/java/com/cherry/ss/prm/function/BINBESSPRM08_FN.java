/*	
 * @(#)BINBESSPRM07_FN.java     1.0 2014/09/03	
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

import com.cherry.ss.prm.bl.BINBESSPRM08_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 家化优惠券推送
 */
public class BINBESSPRM08_FN implements FunctionProvider {

    @Resource
    private BINBESSPRM08_BL binbessprm08BL;

    private static Logger logger = LoggerFactory.getLogger(BINBESSPRM08_FN.class.getName());


    /**
     * 家化优惠券推送，分三步
     * @param transientVars
     * @param args
     * @param ps
     * @throws WorkflowException
     */
    public void execute(Map transientVars, Map args, PropertySet ps)
            throws WorkflowException {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 组织信息ID
            map.put("organizationInfoId", transientVars.get("organizationInfoId"));
            // 品牌信息ID
            map.put("brandInfoId", transientVars.get("brandInfoId"));
            // 品牌Code
            map.put("brandCode", transientVars.get("brandCode"));
            logger.info("******************************优惠券汇总信息推送处理开始***************************");
            binbessprm08BL.sendGenerate(map);
            logger.info("******************************优惠券汇总信息推送处理结束***************************");
            logger.info("******************************优惠券发放清单推送处理开始***************************");
            binbessprm08BL.tran_sendCoupon(map);
            logger.info("******************************优惠券发放清单推送处理结束***************************");
            logger.info("******************************优惠券状态更新推送处理开始***************************");
            binbessprm08BL.tran_sendUpdateCoupon(map);
            logger.info("******************************优惠券状态更新推送处理结束***************************");
            ps.setInt("result", 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new WorkflowException();
        }
    }

}
