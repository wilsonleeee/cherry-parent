/*  
 * @(#)BINOLCM26_IF.java    1.0 2011-11-09     
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
package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM26_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM26_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 工作流修改审核人BL
 * 
 * @author niushunjie
 * @version 1.0.0 2011.12.09
 */
public class BINOLCM26_BL implements BINOLCM26_IF{
    @Resource(name="CodeTable")
    public CodeTable code;
    
    @Resource(name="binOLCM19_BL")
    public BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLCM26_Service")
    public BINOLCM26_Service binOLCM26_Service;
    
    @Resource(name="workflow")
    private Workflow workflow;

    @Override
    public List<Map<String, Object>> getCodeByType(Map<String, Object> map) {
        // 身份类型
        String type = (String)map.get("type");
        // 身份类型为用户的场合
        if("1".equals(type)) {
            // 查询用户List
            return binOLCM26_Service.getUserInfoList(map);
        } 
        // 身份类型为岗位的场合
        else if("2".equals(type)) {
            // 查询岗位List
            return binOLCM26_Service.getPosInfoList(map);
        }
        // 身份类型为部门的场合
        else if("3".equals(type)) {
            // 查询部门List
            return binOLCM26_Service.getOrgInfoList(map);
        }
        // 身份类型为部门类型的场合
        else if("4".equals(type)){
            //从Code值表查询
            List<Map<String,Object>> codeList = code.getCodes("1000");
            if(null != codeList && codeList.size()>0){
                for(int i=0;i<codeList.size();i++){
                    Map<String,Object> codeMap = codeList.get(i);
                    codeMap.put("code", codeMap.get("CodeKey"));
                    codeMap.put("name", codeMap.get("Value"));
                }
            }else{
                codeList = new ArrayList<Map<String,Object>>();
            }
            return codeList;
        }
        return null;
    }
    
    /**
     * 更新审核人
     * @param map
     * @return
     */
    public int tran_updateAuditor(Map<String, Object> map){
        long workFlowId = Long.valueOf(ConvertUtil.getString(map.get("workFlowId")));
        String auditorId = ConvertUtil.getString(map.get("auditorID"));
        String stringValue = "";
        String type = ConvertUtil.getString(map.get("auditorType"));
        String ruleType = ConvertUtil.getString(map.get("ruleType"));
        String privilegeFlag = ConvertUtil.getString(map.get("privilegeFlag"));
        //审核者身份类型
        if("1".equals(type)){
            stringValue = CherryConstants.OS_ACTOR_TYPE_USER + auditorId;
        }else if("2".equals(type)){
            stringValue = CherryConstants.OS_ACTOR_TYPE_POSITION + auditorId;
        }else if("3".equals(type)){
            stringValue = CherryConstants.OS_ACTOR_TYPE_DEPART + auditorId;
        }
        try{
            PropertySet propertyset = workflow.getPropertySet(workFlowId);
            //取当前操作
            String currentOperate = propertyset.getString("OS_Current_Operate");
            //调用工作流API设置新参与者
            propertyset.setString(CherryConstants.OS_ACTOR+currentOperate, stringValue);

            stringValue = stringValue.replaceAll(CherryConstants.OS_ACTOR_TYPE_USER,"U");
            stringValue = stringValue.replaceAll(CherryConstants.OS_ACTOR_TYPE_POSITION,"P");
            stringValue = stringValue.replaceAll(CherryConstants.OS_ACTOR_TYPE_DEPART,"D");
            stringValue += ",";
            
            Map<String,Object> paramData = new HashMap<String,Object>();
            paramData.put("WorkFlowID", workFlowId);
            paramData.put("CurrentOperate", currentOperate);
            paramData.put("CurrentParticipant", stringValue);
            if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType) && "2".equals(type)){
//                IBatisPropertySet ips = (IBatisPropertySet) workflow.getPropertySet(workFlowId);
//                Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
//                String employeeID = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE));
//                //不再把岗位ID替换为用户ID直接用岗位ID
//                stringValue = binOLCM19_BL.changePToU(false,true,employeeID, auditorId, privilegeFlag);
//                stringValue = stringValue.replaceAll(CherryConstants.OS_ACTOR_TYPE_USER,"U");
//                stringValue = stringValue.replaceAll(CherryConstants.OS_ACTOR_TYPE_POSITION,"P");

                //把限制转成JSON格式
                Map<String,Object> privilegeFlagMap = new HashMap<String,Object>();
                privilegeFlagMap.put("1", privilegeFlag);
                privilegeFlag = JSONUtil.serialize(privilegeFlagMap);
                propertyset.setString(CherryConstants.SESSION_PRIVILEGE_FLAG+currentOperate, privilegeFlag);
                
                int privilegeFlagIndex = stringValue.indexOf(CherryConstants.SESSION_PRIVILEGE_FLAG);
                if(privilegeFlagIndex > -1){
                    stringValue = stringValue.substring(0,privilegeFlagIndex);
                }
                stringValue = binOLCM19_BL.processingCommaString(stringValue);
                paramData.put("CurrentParticipant", stringValue);
                
                //复杂模式+审核者是岗位
                paramData.put("ParticipantLimit", privilegeFlag);
            }else{
                propertyset.remove(CherryConstants.SESSION_PRIVILEGE_FLAG+currentOperate);
                paramData.put("ParticipantLimit", null);
            }
            UserInfo userInfo = (UserInfo)map.get("UserInfo");
            paramData.putAll(binOLCM19_BL.setInventoryUserTask(paramData));
            paramData.put("CreatedBy", userInfo.getBIN_UserID());
            paramData.put("CreatePGM", "BINOLCM26");
            paramData.put("UpdatedBy", userInfo.getBIN_UserID());
            paramData.put("UpdatePGM", "BINOLCM26");
            int cnt = binOLCM19_BL.updateInventoryUserTask(paramData);
            if(cnt == 0){
                binOLCM19_BL.insertInventoryUserTask(paramData);
            }
            
            return 1;
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public Map<String,Object> getCurrentOperateInfo(Map<String, Object> map) throws JSONException {
        long workFlowId = Long.valueOf(ConvertUtil.getString(map.get("workFlowId")));
        PropertySet propertyset = workflow.getPropertySet(workFlowId);
        String currentOperate = propertyset.getString("OS_Current_Operate");
        
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(workFlowId));
        List<SimpleStep> stepList = workflow.getCurrentSteps(workFlowId);
        SimpleStep ss = (SimpleStep) stepList.get(0);
        Map<String,Object> metaAttr = wd.getStep(ss.getStepId()).getMetaAttributes();
        String osRule = ConvertUtil.getString(metaAttr.get("OS_Rule"));
        Map<String,Object> ruleMap = new HashMap();
        if(!"".equals(osRule)){
            ruleMap = (Map<String,Object>) JSONUtil.deserialize(osRule);
        }
        Map<String,Object> currentOperateInfo = new HashMap<String,Object>();
        currentOperateInfo.put("currentOperate", currentOperate);
        currentOperateInfo.put("ruleType", ConvertUtil.getString(ruleMap.get("RuleType")));
        return currentOperateInfo;
    }
}
