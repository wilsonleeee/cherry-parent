/*	
 * @(#)BINOLPLCOM02_BL.java     1.0 2011/12/20		
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
package com.cherry.pl.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM26_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.common.interfaces.BINOLPLCOM02_IF;
import com.cherry.pl.common.service.BINOLPLCOM02_Service;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

/**
 * 工作流向导共通 BL
 * 
 * @author niushunjie
 * @version 1.0 2011.12.20
 */
public class BINOLPLCOM02_BL implements BINOLPLCOM02_IF{

    @Resource(name="CodeTable")
    public CodeTable code;
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM26_BL")
    private BINOLCM26_IF binOLCM26_BL;
    
    @Resource(name="binOLPLCOM02_Service")
    private BINOLPLCOM02_Service binOLPLCOM02_Service;
    
    /**
     * 取工作流程文件名
     * @param flowType
     * @return
     */
    @Override
    public String getFlowFileName(String flowType) {
        String flowFileName = "";
        if("1".equals(flowType)){
            flowFileName = "productSDConfig";
        }else if("2".equals(flowType)){
            flowFileName = "productCAConfig";
        }else if("3".equals(flowType)){
            flowFileName = "productGRConfig";
        }else if("4".equals(flowType)){
            flowFileName = "productLSConfig";
        }else if("5".equals(flowType)){
            flowFileName = "productODConfig";
        }else if("6".equals(flowType)){
            flowFileName = "productMVConfig";
        }else if("7".equals(flowType)){
            flowFileName = "productRRConfig";
        }else if("8".equals(flowType)){
            flowFileName = "promotionSDConfig";
        }else if("9".equals(flowType)){
            flowFileName = "promotionBGConfig";
        }else if("10".equals(flowType)){
            flowFileName = "proFlowConfigOD";
        }else if("11".equals(flowType)){
            flowFileName = "proFlowConfigRA";
        }else if("12".equals(flowType)){
            flowFileName = "proFlowConfigCR";
        }else if("13".equals(flowType)){
            flowFileName = "prmFlowConfigYK";
        }else if("14".equals(flowType)){
            flowFileName = "proFlowConfigAC";
        }else if("15".equals(flowType)){
            flowFileName = "prmFlowConfigGR";
        }else if("16".equals(flowType)){
            flowFileName = "proFlowConfigSL";
        }else if("17".equals(flowType)){
            flowFileName = "proFlowConfigSA";
        }
        return flowFileName;
    }

    /**
     * 返回用户ID/岗位ID/部门ID对应的名称
     * @param param
     * @return
     */
    public Map<String,Object> getShowNameMap(Map<String,Object> param){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("organizationInfoId", param.get("organizationInfoId"));
        paramMap.put("brandInfoId", param.get("brandInfoId"));
        paramMap.put("NeedValidFlag", "false");
        
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> arrayList = (List<Map<String, Object>>) param.get("arrayList");
        if(arrayList.size()>0){
            String type = ConvertUtil.getString(param.get("type"));
            paramMap.put("type", type);
            paramMap.put("IDArray", arrayList.toArray());
            resultList = binOLCM26_BL.getCodeByType(paramMap);
        }
        Map<String,Object> resultMap = new Hashtable<String,Object>();
        for(int i=0;i<resultList.size();i++){
            resultMap.put(ConvertUtil.getString(resultList.get(i).get("code")), ConvertUtil.getString(resultList.get(i).get("name")));
        }
        return resultMap;
    }
    
    /**
     * 查询在界面上显示的规则
     * @param ruleMap
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> searchRuleMap(Map<String, Object> ruleMap,
            Map<String, Object> param) {
        String organizationInfoId = ConvertUtil.getString(param.get("organizationInfoId"));
        String brandInfoId = ConvertUtil.getString(param.get("brandInfoId"));
        String ruleType = ConvertUtil.getString(param.get("ruleType"));
        String os_navigation_user = ConvertUtil.getString(param.get("os.navigation.user"));
        String os_navigation_depart = ConvertUtil.getString(param.get("os.navigation.depart"));
        String os_navigation_pos = ConvertUtil.getString(param.get("os.navigation.pos"));
        String os_navigation_departType = ConvertUtil.getString(param.get("os.navigation.departType"));
        //管辖
        String os_navigation_PrivilegeFollow = ConvertUtil.getString(param.get("os.navigation.PrivilegeFollow"));
        //关注
        String os_navigation_PrivilegeLike = ConvertUtil.getString(param.get("os.navigation.PrivilegeLike"));
        //管辖和关注
        String os_navigation_PrivilegeALL = ConvertUtil.getString(param.get("os.navigation.PrivilegeALL"));
        
        //当前规则
        List<Map<String,Object>> ruleContext = (List<Map<String,Object>>) ruleMap.get("RuleContext");
        
        List<String> userIDList = new ArrayList<String>();
        List<String> positionCategoryIDList = new ArrayList<String>();
        List<String> organizationIDList = new ArrayList<String>();
        List<String> departTypeIDList = new ArrayList<String>();
        
        //解析RuleContext获得需要查询名称的ID list
        for(int i=0;i<ruleContext.size();i++){
            if(CherryConstants.OS_RULETYPE_EASY.equals(ruleType) || CherryConstants.OS_RULETYPE_CHERRYSHOW.equals(ruleType)){
                String actorType = ConvertUtil.getString(ruleContext.get(i).get("ActorType"));
                String actorValue = ConvertUtil.getString(ruleContext.get(i).get("ActorValue"));
                if("U".equals(actorType)){
                    userIDList.add(actorValue);
                }else if("D".equals(actorType)){
                    organizationIDList.add(actorValue);
                }else if("P".equals(actorType)){
                    positionCategoryIDList.add(actorValue);
                }
            }else if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
                String roleTypeCreater = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeCreater"));
                String roleValueCreater = ConvertUtil.getString(ruleContext.get(i).get("RoleValueCreater"));
                if ("U".equals(roleTypeCreater)) {
                    userIDList.add(roleValueCreater);
                } else if ("D".equals(roleTypeCreater)) {
                    organizationIDList.add(roleValueCreater);
                } else if ("P".equals(roleTypeCreater)) {
                    positionCategoryIDList.add(roleValueCreater);
                } else if("DT".equals(roleTypeCreater)){
                    departTypeIDList.add(roleValueCreater);
                }
                
                String roleTypeAuditor = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeAuditor"));
                String roleValueAuditor = ConvertUtil.getString(ruleContext.get(i).get("RoleValueAuditor"));
                if ("U".equals(roleTypeAuditor)) {
                    userIDList.add(roleValueAuditor);
                } else if ("D".equals(roleTypeAuditor)) {
                    organizationIDList.add(roleValueAuditor);
                } else if ("P".equals(roleTypeAuditor)) {
                    positionCategoryIDList.add(roleValueAuditor);
                }
            }else if(CherryConstants.OS_RULETYPE_INSTEAD.equals(ruleType)){
            	// 代收模式
                String roleTypeReceiver = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeReceiver"));
                String roleValueReceiver = ConvertUtil.getString(ruleContext.get(i).get("RoleValueReceiver"));
                if ("U".equals(roleTypeReceiver)) {
                    userIDList.add(roleValueReceiver);
                } else if ("D".equals(roleTypeReceiver)) {
                    organizationIDList.add(roleValueReceiver);
                } else if ("P".equals(roleTypeReceiver)) {
                    positionCategoryIDList.add(roleValueReceiver);
                } else if("DT".equals(roleTypeReceiver)){
                    departTypeIDList.add(roleValueReceiver);
                }
                
                String roleTypeConfirmation = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeConfirmation"));
                String roleValueConfirmation = ConvertUtil.getString(ruleContext.get(i).get("RoleValueConfirmation"));
                if ("U".equals(roleTypeConfirmation)) {
                    userIDList.add(roleValueConfirmation);
                } else if ("D".equals(roleTypeConfirmation)) {
                    organizationIDList.add(roleValueConfirmation);
                } else if ("P".equals(roleTypeConfirmation)) {
                    positionCategoryIDList.add(roleValueConfirmation);
                }
            }
        }
        
        //按type分别查询，取得ID与名称的Map
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("organizationInfoId", organizationInfoId);
        paramMap.put("brandInfoId", brandInfoId);
        paramMap.put("type", "1");
        paramMap.put("arrayList", userIDList);
        Map<String,Object> userNameMap = getShowNameMap(paramMap);
        paramMap.put("type", "2");
        paramMap.put("arrayList", positionCategoryIDList);
        Map<String,Object> posNameMap = getShowNameMap(paramMap);
        paramMap.put("type", "3");
        paramMap.put("arrayList", organizationIDList);
        Map<String,Object> orgNameMap = getShowNameMap(paramMap);
        
        if(CherryConstants.OS_RULETYPE_EASY.equals(ruleType) || CherryConstants.OS_RULETYPE_CHERRYSHOW.equals(ruleType)){
            //简单模式
            //设置显示值
            for(int i=0;i<ruleContext.size();i++){
                String actorType = ConvertUtil.getString(ruleContext.get(i).get("ActorType"));
                String actorValue = ConvertUtil.getString(ruleContext.get(i).get("ActorValue"));
                
                String actorTypeText = "";
                String actorValueText = "";
                if("U".equals(actorType)){
                    actorTypeText = os_navigation_user;
                    actorValueText = ConvertUtil.getString(userNameMap.get(actorValue));
                }else if("D".equals(actorType)){
                    actorTypeText = os_navigation_depart;
                    actorValueText = ConvertUtil.getString(orgNameMap.get(actorValue));
                }else if("P".equals(actorType)){
                    actorTypeText = os_navigation_pos;
                    actorValueText = ConvertUtil.getString(posNameMap.get(actorValue));
                }
                ruleContext.get(i).put("ActorTypeText",actorTypeText);
                ruleContext.get(i).put("ActorValueText", actorValueText);
            }
        }else if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
            //3 复杂模式(审核)
            //设置显示值
            for(int i=0;i<ruleContext.size();i++){
                String roleTypeCreater = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeCreater"));
                String roleTypeAuditor = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeAuditor"));
                String roleValueCreater = ConvertUtil.getString(ruleContext.get(i).get("RoleValueCreater"));
                String roleValueAuditor = ConvertUtil.getString(ruleContext.get(i).get("RoleValueAuditor"));
                String rolePrivilegeFlag = ConvertUtil.getString(ruleContext.get(i).get("RolePrivilegeFlag"));
                
                String roleTypeCreaterText = "";
                String roleValueCreaterText = "";
                if("U".equals(roleTypeCreater)){
                    roleTypeCreaterText = os_navigation_user;
                    roleValueCreaterText = ConvertUtil.getString(userNameMap.get(roleValueCreater));
                }else if("D".equals(roleTypeCreater)){
                    roleTypeCreaterText = os_navigation_depart;
                    roleValueCreaterText = ConvertUtil.getString(orgNameMap.get(roleValueCreater));
                }else if("P".equals(roleTypeCreater)){
                    roleTypeCreaterText = os_navigation_pos;
                    roleValueCreaterText = ConvertUtil.getString(posNameMap.get(roleValueCreater));
                }else if("DT".equals(roleTypeCreater)){
                    roleTypeCreaterText = os_navigation_departType;
                    roleValueCreaterText = ConvertUtil.getString(code.getVal("1000", roleValueCreater));
                }
                ruleContext.get(i).put("RoleTypeCreaterText",roleTypeCreaterText);
                ruleContext.get(i).put("RoleValueCreaterText", roleValueCreaterText);
                
                String roleTypeAuditorText = "";
                String roleValueAuditorText = "";
                if("U".equals(roleTypeAuditor)){
                    roleTypeAuditorText = os_navigation_user;
                    roleValueAuditorText = ConvertUtil.getString(userNameMap.get(roleValueAuditor));
                }else if("D".equals(roleTypeAuditor)){
                    roleTypeAuditorText = os_navigation_depart;
                    roleValueAuditorText = ConvertUtil.getString(orgNameMap.get(roleValueAuditor));
                }else if("P".equals(roleTypeAuditor)){
                    roleTypeAuditorText = os_navigation_pos;
                    roleValueAuditorText = ConvertUtil.getString(posNameMap.get(roleValueAuditor));
                    
                    //权限范围
                    String rolePrivilegeFlagText = "";
                    if(CherryConstants.OS_PRIVILEGEFLAG_FOLLOW.equals(rolePrivilegeFlag) || "".equals(rolePrivilegeFlag)){
                        //管辖
                        rolePrivilegeFlagText = os_navigation_PrivilegeFollow;
                        //无权限范围字段的老数据认为是管辖
                        ruleContext.get(i).put("RolePrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
                    }else if(CherryConstants.OS_PRIVILEGEFLAG_LIKE.equals(rolePrivilegeFlag)){
                        //关注
                        rolePrivilegeFlagText = os_navigation_PrivilegeLike;
                    }else if(CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(rolePrivilegeFlag)){
                        //管辖和关注
                        rolePrivilegeFlagText = os_navigation_PrivilegeALL;
                    }
                    ruleContext.get(i).put("RolePrivilegeFlagText", rolePrivilegeFlagText);
                }
                ruleContext.get(i).put("RoleTypeAuditorText", roleTypeAuditorText);
                ruleContext.get(i).put("RoleValueAuditorText", roleValueAuditorText);
            }
        }else if(CherryConstants.OS_RULETYPE_INSTEAD.equals(ruleType)){
            //5 代收模式(接收)
            // 设置显示值
            for(int i=0;i<ruleContext.size();i++){
                String roleTypeReceiver = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeReceiver"));
                String roleTypeConfirmation = ConvertUtil.getString(ruleContext.get(i).get("RoleTypeConfirmation"));
                String roleValueReceiver = ConvertUtil.getString(ruleContext.get(i).get("RoleValueReceiver"));
                String roleValueConfirmation = ConvertUtil.getString(ruleContext.get(i).get("RoleValueConfirmation"));
                String rolePrivilegeRecFlag = ConvertUtil.getString(ruleContext.get(i).get("RolePrivilegeRecFlag"));
                
                String roleTypeReceiverText = "";
                String roleValueReceiverText = "";
                if("U".equals(roleTypeReceiver)){
                	roleTypeReceiverText = os_navigation_user;
                	roleValueReceiverText = ConvertUtil.getString(userNameMap.get(roleValueReceiver));
                }else if("D".equals(roleTypeReceiver)){
                	roleTypeReceiverText = os_navigation_depart;
                	roleValueReceiverText = ConvertUtil.getString(orgNameMap.get(roleValueReceiver));
                }else if("P".equals(roleTypeReceiver)){
                	roleTypeReceiverText = os_navigation_pos;
                	roleValueReceiverText = ConvertUtil.getString(posNameMap.get(roleValueReceiver));
                }else if("DT".equals(roleTypeReceiver)){
                	roleTypeReceiverText = os_navigation_departType;
                	roleValueReceiverText = ConvertUtil.getString(code.getVal("1000", roleValueReceiver));
                }
                ruleContext.get(i).put("RoleTypeReceiverText",roleTypeReceiverText);
                ruleContext.get(i).put("RoleValueReceiverText", roleValueReceiverText);
                
                String roleTypeConfirmationText = "";
                String roleValueConfirmationText = "";
                if("U".equals(roleTypeConfirmation)){
                	roleTypeConfirmationText = os_navigation_user;
                	roleValueConfirmationText = ConvertUtil.getString(userNameMap.get(roleValueConfirmation));
                }else if("D".equals(roleTypeConfirmation)){
                	roleTypeConfirmationText = os_navigation_depart;
                	roleValueConfirmationText = ConvertUtil.getString(orgNameMap.get(roleValueConfirmation));
                }else if("P".equals(roleTypeConfirmation)){
                	roleTypeConfirmationText = os_navigation_pos;
                	roleValueConfirmationText = ConvertUtil.getString(posNameMap.get(roleValueConfirmation));
                    
                    //权限范围
                    String rolePrivilegeRecFlagText = "";
                    if(CherryConstants.OS_PRIVILEGEFLAG_FOLLOW.equals(rolePrivilegeRecFlag) || "".equals(rolePrivilegeRecFlag)){
                        //管辖
                    	rolePrivilegeRecFlagText = os_navigation_PrivilegeFollow;
                        //无权限范围字段的老数据认为是管辖
                        ruleContext.get(i).put("RolePrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
                    }else if(CherryConstants.OS_PRIVILEGEFLAG_LIKE.equals(rolePrivilegeRecFlag)){
                        //关注
                    	rolePrivilegeRecFlagText = os_navigation_PrivilegeLike;
                    }else if(CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(rolePrivilegeRecFlag)){
                        //管辖和关注
                    	rolePrivilegeRecFlagText = os_navigation_PrivilegeALL;
                    }
                    ruleContext.get(i).put("RolePrivilegeRecFlagText", rolePrivilegeRecFlagText);
                }
                ruleContext.get(i).put("RoleTypeConfirmationText", roleTypeConfirmationText);
                ruleContext.get(i).put("RoleValueConfirmationText", roleValueConfirmationText);
            }
        }
        return ruleMap;
    }

    /**
     * 设置按钮区
     * @param param
     */
    @Override
    public List<Map<String, Object>> getButtonList(Map<String, Object> param) {
        long workFlowId = Long.parseLong(ConvertUtil.getString(param.get("workFlowId")));
        String flowType = ConvertUtil.getString(param.get("flowType"));
        String flowFileName = ConvertUtil.getString(param.get("wfName"));//getFlowFileName(flowType);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(flowFileName);
        
        List<Map<String,Object>> buttonList = new ArrayList<Map<String,Object>>();
        
        int[] actionArr = workflow.getAvailableActions(workFlowId, null);
        ActionDescriptor[] actionDsArr = null;
        if (actionArr != null && actionArr.length > 0) {
            actionDsArr = new ActionDescriptor[actionArr.length];
            for (int i = 0; i < actionArr.length; i++) {
                actionDsArr[i] = wd.getAction(actionArr[i]);

                Map<String, Object> button = new HashMap<String, Object>();
                String buttonClass = ConvertUtil.getString(actionDsArr[i].getMetaAttributes().get("OS_ButtonClass"));
                String buttonName = ConvertUtil.getString(actionDsArr[i].getMetaAttributes().get("OS_ButtonNameCode"));
                button.put("buttonClass", buttonClass);
                button.put("buttonName", buttonName);
                int actionId = actionDsArr[i].getId();

                if ("icon-movel".equals(buttonClass)) {
                    // 上一步
                    button.put("buttonOnclick", "binOLPLCOM02.doNext("+ actionId + ", 'next');return false;");
                } else if ("icon-save".equals(buttonClass)) {
                    // 保存
                    button.put("buttonOnclick", "binOLPLCOM02.doSave("+ actionId + ");return false;");
                    if("os.navigation.SaveAll".equals(buttonName)){
                        button.put("buttonOnclick", "binOLPLCOM02.doSaveAll("+ actionId + ");return false;");
                    }
                } else if ("icon-mover".equals(buttonClass)) {
                    // 下一步
                    button.put("buttonOnclick", "binOLPLCOM02.doNext("+ actionId + ", 'back');return false;");
                }
                buttonList.add(button);
            }
        }
        return buttonList;
    }

    /**
     * 根据工作流名称取workflowid
     * @param param
     * @return
     */
    @Override
    public long getWorkFlowID(Map<String, Object> param) {
        long workflowId = 0;
        List<Map<String,Object>> list = binOLPLCOM02_Service.getWorkFlowID(param);
        if(null != list && list.size()>0){
            workflowId = Long.parseLong(ConvertUtil.getString(list.get(0).get("ID")));
        }
        return workflowId;
    }

}
