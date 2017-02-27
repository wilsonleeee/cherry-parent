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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流柜台条件判断
 *
 * @author niushunjie
 * @version 1.0 2013.08.27
 */
public class CounterResultCondition implements Condition {

    private static Logger logger = LoggerFactory.getLogger(CounterResultCondition.class.getName());

    @Resource(name = "binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;

    @Override
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        boolean flag = false;//导出，第三方审核
        String osOounterSynergyFlag = ConvertUtil.getString(args.get("CounterSynergyFlag"));//工作流里定义不需要导出的值
        Map<String, Object> mainData = (Map<String, Object>) transientVars.get("mainData");
        //niusj初次做的时候仅支持退库申请，颖通订货需要使用，临时修改，以后应该规范一下工作流中条件结点的设计和使用
        int organizationId = 0;
        try {
            organizationId = CherryUtil.obj2int(ps.getString("BIN_OrganizationID"));
        } catch (Exception ex) {
            organizationId = 0;
        }
        if (organizationId == 0) {
            organizationId = CherryUtil.obj2int(mainData.get("BIN_OrganizationID"));
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
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

        Map<String, Object> organizationInfo = binOLCM19_BL.getOrganizationInfo(paramMap);
        if (null != organizationInfo && !organizationInfo.isEmpty()) {
            if (!"".equals(osOounterSynergyFlag)) {
                String orgSynergyFlag = ConvertUtil.getString(organizationInfo.get("orgSynergyFlag"));
                //CounterSynergyFlag =0 不导出，=1 导出，值为空 导出
                //上面注释中的逻辑已经调整：看工作流中配置的值是否和柜台属性中对应的值一致，一致返回true，否则返回false
                if (orgSynergyFlag.equals(osOounterSynergyFlag)) {
                    //TODO:调查浓妆淡抹退库申请流程问题
                    String OS_BillID = "";
                    String OS_BillType = "";

                    try {
                        OS_BillID = ps.getString("OS_BillID");
                        OS_BillType = ps.getString("OS_BillType");
                    } catch (Exception ex) {
                        logger.error("工作流条件判断发生异常：", ex);
                    }
                    logger.error("工作流判定不需要导出到第三方审核：OS_BillType=" + OS_BillType + ",OS_BillID=" + OS_BillID + ",对应组织ID=" + organizationId);
                    flag = true;
                }
            }
        }
        //////////////////////////WITPOSQA-22245 2016/08/10 颖通 代理商退库申请第三方审核处理增加 By Liy End//////////////////////////////////////
        return flag;
    }
}