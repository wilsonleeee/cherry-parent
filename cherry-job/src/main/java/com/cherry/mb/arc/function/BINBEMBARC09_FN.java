/*
 * @(#)BINBEMBTIF01_FN.java     1.0 2015/06/24
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
package com.cherry.mb.arc.function;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.arc.bl.BINBEMBARC09_BL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 计算会员完善度处理FN
 *
 * @author nanjunbo
 * @version 1.0 2017/02/09
 */
public class BINBEMBARC09_FN implements FunctionProvider{

    private static Logger logger = LoggerFactory.getLogger(BINBEMBARC09_FN.class.getName());

    /** 计算会员完善度处理BL */
    @Resource
    private BINBEMBARC09_BL binBEMBARC09_BL;

    @Override
    public void execute(Map transientVars, Map args, PropertySet ps)
            throws WorkflowException {
        try {
            logger.info("******************************计算会员完善度开始***************************");
            int flag = binBEMBARC09_BL.tran_MemSync(transientVars);
            ps.setInt("result", flag);
        } catch (Exception e) {
            // 打印错误信息
            logger.error(e.getMessage(),e);
            throw new WorkflowException();
        } finally {
            logger.info("******************************计算会员完善度结束***************************");
        }
    }

}
