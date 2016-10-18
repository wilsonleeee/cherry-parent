/*	
 * @(#)BINOLPLCOM02_Action.java     1.0 2011/12/20		
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
package com.cherry.pl.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM30_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.common.form.BINOLPLCOM02_Form;
import com.cherry.pl.common.interfaces.BINOLPLCOM02_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 工作流向导共通 Action
 * 
 * @author niushunjie
 * @version 1.0 2011.12.20
 */
public class BINOLPLCOM02_Action extends BaseAction implements
ModelDriven<BINOLPLCOM02_Form>{

    private static final long serialVersionUID = 8673988749245348158L;

    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
       
    @Resource(name="binolcm30IF")
    private BINOLCM30_IF binolcm30IF;
    
    /** 参数FORM */
    private BINOLPLCOM02_Form form = new BINOLPLCOM02_Form();

    @Resource(name="binOLPLCOM02_BL")
    private BINOLPLCOM02_IF binOLPLCOM02_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    
    /**
     * 编辑画面第一次弹出时执行的操作
     * @return
     * @throws Exception
     */
    public String init() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String flowType = form.getFlowType();
        //工作流向导流程名称
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String brandInfoId = form.getBrandInfoId();
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));
        
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        if(session.containsKey(wfName)){
            session.remove(wfName);
        }        
        return editInit();
    }

    /**
     * 编辑工作流配置
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
    public String editInit() throws Exception {
	    // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        List<Map<String,Object>> topSteps = new ArrayList<Map<String,Object>>();
        String flowType = form.getFlowType();
        //工作流向导流程名称
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String brandInfoId = form.getBrandInfoId();
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));
        
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(wfName);
        List<StepDescriptor> list = wd.getSteps();
        //配置时的工作流ID，读取数据库
        long workFlowId = 0;      
        if(workFlowId == 0){
            Map<String,Object> paramWorkFlowID = new HashMap<String,Object>();
            paramWorkFlowID.put("NAME", wfName);
            workFlowId = binOLPLCOM02_BL.getWorkFlowID(paramWorkFlowID);
            //数据库无该流程配置，新建一个实例
            if(workFlowId == 0){
                workFlowId = workflow.initialize(wfName, 1, null);
            }
        }
        //设置工作流ID
        form.setWorkFlowId(ConvertUtil.getString(workFlowId)); 
        
        List currentSteps = workflow.getCurrentSteps(workFlowId);
        SimpleStep ss = (SimpleStep) currentSteps.get(0);
        String onStep = ConvertUtil.getString(wd.getStep(ss.getStepId()).getMetaAttributes().get("OS_StepShowOrder"));
        String rule = "";
        //读取sesssion中的临时步骤规则
        if(null != session.get(wfName)){
            Map<String,Object> sessionMap = (Map<String,Object>) session.get(wfName);
            if(null != sessionMap.get(onStep)){
                rule = ConvertUtil.getString( sessionMap.get(onStep));
            }
        }

        // 设置导航条当前步骤Order
        form.setOnStep(onStep);
        //向导当前步骤ID
        form.setStepId(ss.getStepId());
        
        if(null == rule || "".equals(rule)){
            rule = ConvertUtil.getString(wd.getStep(ss.getStepId()).getMetaAttributes().get("OS_Rule"));
        }
       
        String ruleType = CherryConstants.OS_RULETYPE_NO;
        String ruleOnFlowCode = "";
        if(null != rule && !"".equals(rule)){
            Map ruleMap = (Map) JSONUtil.deserialize(rule);
            
            //业务工作流文件名
            ruleOnFlowCode = ConvertUtil.getString(ruleMap.get("RuleOnFlowCode"));
            
            //规则类型
            ruleType = ConvertUtil.getString(ruleMap.get("RuleType"));
            
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
            param.put("brandInfoId", brandInfoId);
            param.put("ruleType", ruleType);
            param.put("os.navigation.user", getText("os.navigation.user"));
            param.put("os.navigation.depart", getText("os.navigation.depart"));
            param.put("os.navigation.pos", getText("os.navigation.pos"));
            param.put("os.navigation.departType", getText("os.navigation.departType"));
            param.put("os.navigation.PrivilegeFollow", getText("os.navigation.PrivilegeFollow"));
            param.put("os.navigation.PrivilegeLike", getText("os.navigation.PrivilegeLike"));
            param.put("os.navigation.PrivilegeALL", getText("os.navigation.PrivilegeALL"));

            form.setRuleMap(binOLPLCOM02_BL.searchRuleMap(ruleMap, param));
            
            if(null != ruleMap.get("RuleOnAction")){
                List ruleOnActionList = (List) ruleMap.get("RuleOnAction");
                int[] actionArr = new int[ruleOnActionList.size()];
                for(int i=0;i<ruleOnActionList.size();i++){
                    actionArr[i] = Integer.parseInt(ConvertUtil.getString(ruleOnActionList.get(i)));
                }
                form.setRuleOnAction(actionArr);
            }
            
            if(null != ruleMap.get("RuleOnStep")){
                List ruleOnStepList = (List) ruleMap.get("RuleOnStep");
                int[] stepArr = new int[ruleOnStepList.size()];
                for(int i=0;i<ruleOnStepList.size();i++){
                    stepArr[i] = Integer.parseInt(ConvertUtil.getString(ruleOnStepList.get(i)));
                }
                form.setRuleOnStep(stepArr);
            }
            
        }else{
            form.setRuleMap(null);
            form.setRuleOnAction(null);
            form.setRuleOnStep(null);
        }
        form.setRuleType(ruleType);
        form.setRuleOnFlowCode(ruleOnFlowCode);
        
        Map<String,Object> paramBtn = new HashMap<String,Object>();
        paramBtn.put("workFlowId", workFlowId);
        paramBtn.put("flowType", flowType);
        paramBtn.put("wfName", wfName);
        // 设置按钮区
        form.setButtonList(binOLPLCOM02_BL.getButtonList(paramBtn));

        for (int i = 0; i < list.size(); i++) {
            StepDescriptor sd = list.get(i);
            if ("1".equals(sd.getMetaAttributes().get("OS_StepShowFlag"))) {
                Map<String, Object> step = new HashMap<String, Object>();
                String stepShowText =getText(ConvertUtil.getString(sd.getMetaAttributes().get("OS_StepShowText")));
                step.put("stepName", stepShowText);
                topSteps.add(step);
            }
        }
        // 设置TOP导航条
        form.setTopStepsList(topSteps);
        
        return SUCCESS;
    }

    /**
     * 点击上下一步按钮时
     * @return
     * @throws Exception
     */
    public String preNextStep() throws Exception {
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String flowType = form.getFlowType();
        //工作流向导流程名称
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        
        String brandInfoId = form.getBrandInfoId();
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));        
        
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(wfName);
        
        long workFlowId = Long.parseLong(form.getWorkFlowId()); 
        int curActionId = CherryUtil.obj2int(form.getActionId());
        if (curActionId != 0) {
            
            List currentSteps = workflow.getCurrentSteps(workFlowId);
            SimpleStep ss = (SimpleStep) currentSteps.get(0);
       
            String rule = ConvertUtil.getString(wd.getStep(ss.getStepId()).getMetaAttributes().get("OS_Rule"));
            String thirdPartyFlag = form.getHideThirdParty();
            String canEditFlag = form.getHideCanEditFlag();
            String preferredFlag = form.getHidePreferredFlag();
            String autoAuditFlag = form.getHideAutoAuditFlag();
            
            if(null != rule && !"".equals(rule)){
                Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(rule);
                String ruleParams = form.getRuleParams();
                ruleMap.put("RuleContext", JSONUtil.deserialize(ruleParams));
                ruleMap.put("ThirdPartyFlag", thirdPartyFlag);
                if(null != canEditFlag && !"".equals(canEditFlag)){
                    ruleMap.put("CanEditFlag", canEditFlag);
                }
                if(null != preferredFlag && !"".equals(preferredFlag)){
                    ruleMap.put("PreferredFlag", preferredFlag);
                }
                if(null != autoAuditFlag && !"".equals(autoAuditFlag)){
                    ruleMap.put("AutoAuditFlag", autoAuditFlag);
                }
                ruleMap.put("stepId", ss.getStepId());
                
                Map<String,Object> stepMap = (Map<String, Object>) session.get(wfName);
                if(null == stepMap){
                    stepMap = new HashMap<String,Object>();
                }
                String jsonRule = JSONUtil.serialize(ruleMap);
                stepMap.put(form.getOnStep(), jsonRule);
                session.put(wfName, stepMap);
            }
            
            workflow.doAction_single(workFlowId, curActionId, null);           
        }        
        return editInit();
    }
       
    @Override
    public BINOLPLCOM02_Form getModel() {
        return form;
    }
    
    /**
     * 保存规则
     * @return
     * @throws Exception 
     */
    public String saveRule() throws Exception{
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String thirdPartyFlag = form.getThirdPartyFlag();
        String canEditFlag = form.getCanEditFlag();
        String preferredFlag = form.getPreferredFlag();
        String autoAuditFlag = form.getAutoAuditFlag();
        String flowType = form.getFlowType();
        //工作流向导流程名称
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        String brandInfoId = form.getBrandInfoId();
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));  
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        String ruleType = form.getRuleType();
        
        long workFlowId = Long.parseLong(form.getWorkFlowId());
        List stepList = workflow.getCurrentSteps(workFlowId);
        SimpleStep step = (SimpleStep)stepList.get(0);

        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(wfName);
        List currentSteps = workflow.getCurrentSteps(workFlowId);
        SimpleStep ss = (SimpleStep) currentSteps.get(0);
   
        String rule = ConvertUtil.getString(wd.getStep(ss.getStepId()).getMetaAttributes().get("OS_Rule"));
        Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(rule);
        ruleMap.put("ThirdPartyFlag", thirdPartyFlag);
        if(null != canEditFlag && !"".equals(canEditFlag)){
            ruleMap.put("CanEditFlag", canEditFlag);
        }
        if(null != preferredFlag && !"".equals(preferredFlag)){
            ruleMap.put("PreferredFlag", preferredFlag);
        }
        if(null != autoAuditFlag && !"".equals(autoAuditFlag)){
            ruleMap.put("AutoAuditFlag", autoAuditFlag);
        }
        
        String[] roleTypeActorArr = form.getRoleTypeActor();
        String[] roleValueActorArr = form.getRoleValueActor();
        
        String[] roleTypeCreaterArr = form.getRoleTypeCreater();
        String[] roleValueCreaterArr = form.getRoleValueCreater();
        
        String[] roleTypeAuditorArr = form.getRoleTypeAuditor();
        String[] roleValueAuditorArr = form.getRoleValueAuditor();
        
        String[] rolePrivilegeFlagArr = form.getRolePrivilegeFlag();
        
        String[] roleTypeReceiverArr = form.getRoleTypeReceiver();
        String[] roleValueReceiverArr = form.getRoleValueReceiver();
        
        String[] roleTypeConfirmationArr = form.getRoleTypeConfirmation();
        String[] roleValueConfirmationArr = form.getRoleValueConfirmation();
        
        String[] rolePrivilegeRecFlagArr = form.getRolePrivilegeRecFlag();

        List<Map<String,Object>> ruleContext = new ArrayList<Map<String,Object>>();
        if(CherryConstants.OS_RULETYPE_EASY.equals(ruleType) || CherryConstants.OS_RULETYPE_CHERRYSHOW.equals(ruleType)){
            if(null != roleTypeActorArr){
                for(int i=0;i<roleTypeActorArr.length;i++){
                    Map<String,Object> ruleContextMap = new HashMap<String,Object>();
                    ruleContextMap.put("SortNo", i+1);
                    ruleContextMap.put("ActorType", roleTypeActorArr[i]);
                    ruleContextMap.put("ActorValue", roleValueActorArr[i]);
                    ruleContext.add(ruleContextMap);
                }
            }
        }else if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
            if(null != roleTypeCreaterArr){
                for(int i=0;i<roleTypeCreaterArr.length;i++){
                    Map<String,Object> ruleContextMap = new HashMap<String,Object>();
                    ruleContextMap.put("SortNo", i+1);
                    ruleContextMap.put("RoleTypeCreater", roleTypeCreaterArr[i]);
                    ruleContextMap.put("RoleValueCreater", roleValueCreaterArr[i]);
                    ruleContextMap.put("RoleTypeAuditor", roleTypeAuditorArr[i]);
                    ruleContextMap.put("RoleValueAuditor", roleValueAuditorArr[i]);
                    if("P".equals(roleTypeAuditorArr[i])){
                        ruleContextMap.put("RolePrivilegeFlag", rolePrivilegeFlagArr[i]);
                    }
                    ruleContext.add(ruleContextMap);
                }
            }
        }else if(CherryConstants.OS_RULETYPE_INSTEAD.equals(ruleType)){
            if(null != roleTypeReceiverArr){
                for(int i=0;i<roleTypeReceiverArr.length;i++){
                    Map<String,Object> ruleContextMap = new HashMap<String,Object>();
                    ruleContextMap.put("SortNo", i+1);
                    ruleContextMap.put("RoleTypeReceiver", roleTypeReceiverArr[i]);
                    ruleContextMap.put("RoleValueReceiver", roleValueReceiverArr[i]);
                    ruleContextMap.put("RoleTypeConfirmation", roleTypeConfirmationArr[i]);
                    ruleContextMap.put("RoleValueConfirmation", roleValueConfirmationArr[i]);
                    if("P".equals(roleTypeConfirmationArr[i])){
                        ruleContextMap.put("RolePrivilegeRecFlag", rolePrivilegeRecFlagArr[i]);
                    }
                    ruleContext.add(ruleContextMap);
                }
            }
        }
        ruleMap.put("RuleContext", ruleContext);
        String ruleJSONString = JSONUtil.serialize(ruleMap);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orgCode", orgCode);
        map.put("brandCode", brandCode);
        map.put("FileName", flowFileName);
        map.put("RuleJSONString", ruleJSONString);
        map.put("RuleOnStep", new int[]{step.getStepId()});
        
        //更新向导工作流文件
        int cnt = binolcm30IF.updateWorkFlowFile(map); 

        if(cnt == 1){
            if(ruleMap.containsKey("RuleOnAction")){
                if(null != ruleMap.get("RuleOnAction")){
                    List ruleOnActionList = (List) ruleMap.get("RuleOnAction");
                    if(ruleOnActionList.size()>0){
                        int[] ruleOnActionArr = new int[ruleOnActionList.size()];
                        for(int i=0;i<ruleOnActionList.size();i++){
                            ruleOnActionArr[i] = Integer.parseInt(ConvertUtil.getString(ruleOnActionList.get(i)));
                        }
                        map.put("RuleOnAction", ruleOnActionArr);
                    }
                }
            }
            
            map.put("FileName", ConvertUtil.getString(ruleMap.get("RuleOnFlowCode")));
            if(ruleMap.containsKey("RuleOnStep")){
                if(null != ruleMap.get("RuleOnStep")){
                    List ruleOnStepList = (List) ruleMap.get("RuleOnStep");
                    if(ruleOnStepList.size()>0){
                        int[] ruleOnStepArr = new int[ruleOnStepList.size()];
                        for(int i=0;i<ruleOnStepList.size();i++){
                            ruleOnStepArr[i] = Integer.parseInt(ConvertUtil.getString(ruleOnStepList.get(i)));
                        }
                        map.put("RuleOnStep", ruleOnStepArr);
                    }
                }
            }
            //更新业务工作流文件
            cnt = binolcm30IF.updateWorkFlowFile(map);
            if(cnt == 1){
                this.addActionMessage(getText("ICM00001"));
            }else{
                this.addActionError(getText("ECM00005"));
            }
        }else{
            this.addActionError(getText("ECM00005"));
        }
        
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
    
    /**
     * 保存所有规则
     * @return
     * @throws Exception 
     */
    public String saveAllRule() throws Exception{
        int cnt =0;
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        
        String brandInfoId = form.getBrandInfoId();
        String flowType = form.getFlowType();
        long workFlowId = Long.parseLong(form.getWorkFlowId());
        //List stepList = workflow.getCurrentSteps(workFlowId);
        //SimpleStep step = (SimpleStep)stepList.get(0);
        
        //工作流向导流程名称
        String flowFileName = binOLPLCOM02_BL.getFlowFileName(flowType);
        String orgCode = userInfo.getOrganizationInfoCode();
        String brandCode = binOLCM05_BL.getBrandCode(CherryUtil.obj2int(brandInfoId));
        String wfName = ConvertUtil.getWfName(orgCode, brandCode, flowFileName);
        
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(wfName);
        
        //最后一步写入session
        List currentSteps = workflow.getCurrentSteps(workFlowId);
        SimpleStep ss = (SimpleStep) currentSteps.get(0);
   
        String lastRule = ConvertUtil.getString(wd.getStep(ss.getStepId()).getMetaAttributes().get("OS_Rule"));
        String lastThirdPartyFlag = form.getHideThirdParty();
        String lastCanEditFlag = form.getHideCanEditFlag();
        String lastPreferredFlag = form.getHidePreferredFlag();
        String lastAutoAuditFlag = form.getHideAutoAuditFlag();
        
        if(null != lastRule && !"".equals(lastRule)){
            Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(lastRule);
            String ruleParams = form.getRuleParams();
            ruleMap.put("RuleContext", JSONUtil.deserialize(ruleParams));
            ruleMap.put("ThirdPartyFlag", lastThirdPartyFlag);
            if(null != lastCanEditFlag && !"".equals(lastCanEditFlag)){
                ruleMap.put("CanEditFlag", lastCanEditFlag);
            }
            if(null != lastPreferredFlag && !"".equals(lastPreferredFlag)){
                ruleMap.put("PreferredFlag", lastPreferredFlag);
            }
            if(null != lastAutoAuditFlag && !"".equals(lastAutoAuditFlag)){
                ruleMap.put("AutoAuditFlag", lastAutoAuditFlag);
            }
            ruleMap.put("stepId", ss.getStepId());
            
            Map<String,Object> stepMap = (Map<String, Object>) session.get(wfName);
            if(null == stepMap){
                stepMap = new HashMap<String,Object>();
            }
            String jsonRule = JSONUtil.serialize(ruleMap);
            stepMap.put(form.getOnStep(), jsonRule);
            session.put(wfName, stepMap);
        }
        
        List<StepDescriptor> list = wd.getSteps();
        Map<String,Object> sessionMap = (Map<String,Object>) session.get(wfName);
        for(int i=0;i<list.size();i++){
            StepDescriptor sd = list.get(i);
            if ("1".equals(sd.getMetaAttributes().get("OS_StepShowFlag"))) {
                //从session中取步骤，取不到则取工作流向导文件里对应的OS_Rule。
                Map<String,Object> ruleMap = new HashMap<String,Object>();
                String strRule = ConvertUtil.getString(sessionMap.get(sd.getMetaAttributes().get("OS_StepShowOrder")));
                if(!"".equals(strRule)){
                    ruleMap.putAll((Map<String,Object>)JSONUtil.deserialize(strRule));
                }else{
                    strRule = ConvertUtil.getString(sd.getMetaAttributes().get("OS_Rule"));
                    if(!"".equals(strRule)){
                        ruleMap.putAll((Map<String,Object>)JSONUtil.deserialize(strRule));
                        ruleMap.put("stepId", sd.getId());
                    }
                }
                if(null != ruleMap && !ruleMap.isEmpty()){
                    String thirdPartyFlag = ConvertUtil.getString(ruleMap.get("ThirdPartyFlag"));
                    String canEditFlag = ConvertUtil.getString(ruleMap.get("CanEditFlag"));
                    String preferredFlag = ConvertUtil.getString(ruleMap.get("PreferredFlag"));
                    String autoAuditFlag = ConvertUtil.getString(ruleMap.get("AutoAuditFlag"));
                    String ruleType = ConvertUtil.getString(ruleMap.get("RuleType"));
                    String ruleOnFlowCode = ConvertUtil.getString(ruleMap.get("RuleOnFlowCode"));
                    int stepId = Integer.parseInt(ConvertUtil.getString(ruleMap.get("stepId"))); 
                    
                    int[] ruleOnActionArr = null;
                    if(null != ruleMap.get("RuleOnAction")){
                        List ruleOnActionList = (List) ruleMap.get("RuleOnAction");
                        ruleOnActionArr = new int[ruleOnActionList.size()];
                        for(int j=0;j<ruleOnActionList.size();j++){
                            ruleOnActionArr[j] = Integer.parseInt(ConvertUtil.getString(ruleOnActionList.get(j)));
                        }
                    }
                    
                    int[] ruleOnStepArr = null;
                    if(null != ruleMap.get("RuleOnStep")){
                        List ruleOnStepList = (List) ruleMap.get("RuleOnStep");
                        ruleOnStepArr = new int[ruleOnStepList.size()];
                        for(int j=0;j<ruleOnStepList.size();j++){
                            ruleOnStepArr[j] = Integer.parseInt(ConvertUtil.getString(ruleOnStepList.get(j)));
                        }
                    }
                    
                    List<Map<String,Object>> ruleContext = (List<Map<String,Object>>) ruleMap.get("RuleContext");
                    
                    Map<String,Object> rule = new HashMap<String,Object>();
                    rule.put("ThirdPartyFlag", thirdPartyFlag);
                    if(!"".equals(canEditFlag)){
                        rule.put("CanEditFlag", canEditFlag);
                    }
                    if(!"".equals(preferredFlag)){
                        rule.put("PreferredFlag", preferredFlag);
                    }
                    if(!"".equals(autoAuditFlag)){
                        rule.put("AutoAuditFlag", autoAuditFlag);
                    }
                    if(null != ruleOnStepArr && ruleOnStepArr.length>0){
                        rule.put("RuleOnStep", ruleOnStepArr);
                    }
                    if(null != ruleOnActionArr && ruleOnActionArr.length>0){
                        rule.put("RuleOnAction", ruleOnActionArr);
                    }
                    rule.put("RuleType", ruleType);
                    rule.put("RuleOnFlowCode", ruleOnFlowCode);
                    rule.put("RuleContext", ruleContext);
                    
                    String ruleJSONString = JSONUtil.serialize(rule);
                    
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("orgCode", orgCode);
                    map.put("brandCode", brandCode);
                    map.put("FileName", flowFileName);
                    map.put("RuleJSONString", ruleJSONString);
                    map.put("RuleOnStep", new int[]{stepId});
                    
                    //更新向导工作流文件
                    cnt = binolcm30IF.updateWorkFlowFile(map);
                    if(cnt == 1){
                        if(ruleMap.containsKey("RuleOnAction")){
                            if(null != ruleMap.get("RuleOnAction")){
                                List ruleOnActionList = (List) ruleMap.get("RuleOnAction");
                                if(ruleOnActionList.size()>0){
                                    int[] curRuleOnActionArr = new int[ruleOnActionList.size()];
                                    for(int j=0;j<ruleOnActionList.size();j++){
                                        curRuleOnActionArr[j] = Integer.parseInt(ConvertUtil.getString(ruleOnActionList.get(j)));
                                    }
                                    map.put("RuleOnAction", curRuleOnActionArr);
                                }
                            }
                        }
                        
                        map.put("FileName", ruleOnFlowCode);
                        if(ruleMap.containsKey("RuleOnStep")){
                            if(null != ruleMap.get("RuleOnStep")){
                                List ruleOnStepList = (List) ruleMap.get("RuleOnStep");
                                if(ruleOnStepList.size()>0){
                                    int[] curRuleOnStepArr = new int[ruleOnStepList.size()];
                                    for(int j=0;j<ruleOnStepList.size();j++){
                                        curRuleOnStepArr[j] = Integer.parseInt(ConvertUtil.getString(ruleOnStepList.get(j)));
                                    }
                                    map.put("RuleOnStep", curRuleOnStepArr);
                                }
                            }
                        }
                        
                        //更新业务工作流文件
                        cnt = binolcm30IF.updateWorkFlowFile(map);
                    }else{
                        break;
                    }
                }
            }
        }
        if(cnt == 1){
            this.addActionMessage(getText("ICM00001"));
            session.remove(wfName);
        }else{
            this.addActionError(getText("ECM00005"));
        }
        return CherryConstants.GLOBAL_ACCTION_RESULT;
    }
}
