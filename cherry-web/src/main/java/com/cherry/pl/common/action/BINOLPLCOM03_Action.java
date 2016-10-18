/*	
 * @(#)BINOLPLCOM03_Action.java     1.0 2012/01/31		
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
import com.cherry.cm.core.FileStoreDTO;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.common.form.BINOLPLCOM03_Form;
import com.cherry.pl.common.interfaces.BINOLPLCOM02_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 业务流程规则查看共通 Action
 * 
 * @author niushunjie
 * @version 1.0 2012.01.31
 */
public class BINOLPLCOM03_Action extends BaseAction implements
ModelDriven<BINOLPLCOM03_Form>{

    private static final long serialVersionUID = -9158135803204264842L;

    @Resource(name="binOLCM05_BL")
    private BINOLCM05_BL binOLCM05_BL;
    
    /** 参数FORM */
    private BINOLPLCOM03_Form form = new BINOLPLCOM03_Form();

    @Resource(name="binOLPLCOM02_BL")
    private BINOLPLCOM02_IF binOLPLCOM02_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binolcm30IF")
    private BINOLCM30_IF binolcm30IF;
    
    /**
     * 当前流程所有步骤的规则
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
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(wfName);
        List<StepDescriptor> stepList = wd.getSteps();
        List<Map<String,Object>> ruleList = new ArrayList<Map<String,Object>>();
        String ruleOnFlowCode = "";
        for (int i = 0; i < stepList.size(); i++) {
            StepDescriptor sd = stepList.get(i);
            if ("1".equals(ConvertUtil.getString(sd.getMetaAttributes().get("OS_StepShowFlag")))) {
                String stepShowText =getText(ConvertUtil.getString(sd.getMetaAttributes().get("OS_StepShowText")));
                String os_rule= ConvertUtil.getString(sd.getMetaAttributes().get("OS_Rule"));
                Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(os_rule);
                //规则类型
                String ruleType = CherryConstants.OS_RULETYPE_NO;
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
                Map<String,Object> showMap= binOLPLCOM02_BL.searchRuleMap(ruleMap, param);
                showMap.put("stepName", stepShowText);
                ruleList.add(showMap);
            }
            if(i == 0){
                String osRule = ConvertUtil.getString(sd.getMetaAttributes().get("OS_Rule"));
                Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(osRule);
                ruleOnFlowCode = ConvertUtil.getString(ruleMap.get("RuleOnFlowCode"));
            }
        }
        form.setRuleList(ruleList);
        
        Map<String,Object> wfFileContentMap = new HashMap<String,Object>();
        FileStoreDTO fsd = binolcm30IF.getFileStoreByCode(flowFileName, orgCode, brandCode);
        //配置项工作流
        wfFileContentMap.put("configWF", fsd.getFileContent());
        fsd = binolcm30IF.getFileStoreByCode(ruleOnFlowCode, orgCode, brandCode);
        //实际使用工作流
        wfFileContentMap.put("actualWF", fsd.getFileContent());
        form.setWfFileContentMap(wfFileContentMap);
        
        return SUCCESS;
    }
       
    @Override
    public BINOLPLCOM03_Form getModel() {
        return form;
    }
}
