/*
 * @(#)CounterResultCondition.java     1.0 2015-11-4
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * @ClassName: CloudPosCondition 
 * @Description: TODO(工作流云POS条件判断) 
 * @author menghao
 * @version v1.0.0 2015-11-4 
 *
 */
public class CloudPosCondition implements Condition{
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        boolean flag = false;
        //工作流里定义是否支持云POS的特殊流程
        String osCloudPosFlag = ConvertUtil.getString(args.get("CloudPosFlag"));
        Map<String,Object> mainData = (Map<String, Object>) transientVars.get("mainData");
        //目前只支持云POS的盘点申请功能
        String organizationID = "";
        try{
        	organizationID=ConvertUtil.getString(ps.getString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART));
        }catch(Exception ex){
        	organizationID="";
        }
        if("".equals(organizationID)){
        	Map<String, Object> stocktakeReqMainData = (Map<String, Object>)mainData.get("stocktakeReqMainData");
        	organizationID = ConvertUtil.getString(stocktakeReqMainData.get("BIN_OrganizationID"));
        }
        
        // 在云POS或者其他部门类型申请的单据
        if(!binOLSTCM07_BL.checkOrganizationType(organizationID)){
            if("1".equals(osCloudPosFlag)){
                flag = true;
            }
        }
        return flag;
    }
}