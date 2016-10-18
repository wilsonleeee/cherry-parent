/*
 * @(#)CounterResultCondition.java     1.0 2013/08/27
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

/**
 * 工作流柜台条件判断
 * 
 * @author niushunjie
 * @version 1.0 2013.08.27
 */
public class CounterResultCondition implements Condition{
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
    
    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        boolean flag = false;//导出，第三方审核
        String osOounterSynergyFlag = ConvertUtil.getString(args.get("CounterSynergyFlag"));//工作流里定义不需要导出的值
        Map<String,Object> mainData = (Map<String, Object>) transientVars.get("mainData");
      //niusj初次做的时候仅支持退库申请，颖通订货需要使用，临时修改，以后应该规范一下工作流中条件结点的设计和使用
        int organizationId = 0;
        try{
        	organizationId=CherryUtil.obj2int(ps.getString("BIN_OrganizationID"));}
        catch(Exception ex){
        	organizationId=0;
        }
        if(organizationId==0){        	
        	organizationId = CherryUtil.obj2int(mainData.get("BIN_OrganizationID"));
        }
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("organizationId", organizationId);
        //////////////////////////WITPOSQA-22245 2016/08/10 颖通 代理商退库申请第三方审核处理增加 By Liy Start//////////////////////////////////////
       /* Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramMap);
        if(null != counterInfo && !counterInfo.isEmpty()){
            if(!"".equals(osOounterSynergyFlag)){
                String counterSynergyFlag = ConvertUtil.getString(counterInfo.get("counterSynergyFlag"));
                //CounterSynergyFlag =0 不导出，=1 导出，值为空 导出
                if(counterSynergyFlag.equals(osOounterSynergyFlag)){
                    flag = true;
                }
            }
        }*/
        
     	Map<String,Object> organizationInfo = binOLCM19_BL.getOrganizationInfo(paramMap);  
        if(null != organizationInfo && !organizationInfo.isEmpty()){
            if(!"".equals(osOounterSynergyFlag)){
                String orgSynergyFlag = ConvertUtil.getString(organizationInfo.get("orgSynergyFlag"));
                //CounterSynergyFlag =0 不导出，=1 导出，值为空 导出
                if(orgSynergyFlag.equals(osOounterSynergyFlag)){
                    flag = true;
                }
            }
        }
        //////////////////////////WITPOSQA-22245 2016/08/10 颖通 代理商退库申请第三方审核处理增加 By Liy End//////////////////////////////////////
        return flag;
    }
}