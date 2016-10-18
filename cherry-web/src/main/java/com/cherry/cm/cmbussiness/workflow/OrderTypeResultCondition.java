/*
 * @(#)OrderTypeResultCondition.java     1.0 2013/09/09
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

package com.cherry.cm.cmbussiness.workflow;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

/**
 * 工作流订单类型条件判断
 * 
 * @author niushunjie
 * @version 1.0 2013.09.09
 */
public class OrderTypeResultCondition implements Condition{
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    /**
     * 判断订单的订单类型是否是工作流里定义的
     */
    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        boolean flag = false;
        String orderType = ConvertUtil.getString(args.get("OrderType"));
        int billID = ps.getInt("BIN_ProductOrderID");
        Map<String,Object> orderMainData = binOLSTCM02_BL.getProductOrderMainData(billID,null);
        if(ConvertUtil.getString(orderMainData.get("OrderType")).equals(orderType)){
            flag= true;
        }
        return flag;
    }
}