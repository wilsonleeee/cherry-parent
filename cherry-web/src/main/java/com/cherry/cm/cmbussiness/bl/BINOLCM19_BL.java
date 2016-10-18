/*		
 * @(#)BINOLCM19_BL.java     1.0 2011/08/30           	
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

import com.cherry.cm.cmbeans.CherryTaskInstance;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM19_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.bl.BINOLSSCM09_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 共通业务类  用于OSworkFlow相关的操作
 * @author dingyongchang
 */
public class BINOLCM19_BL implements BINOLCM19_IF{

	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binOLCM01_BL;
	
	@Resource(name="binOLCM19_Service")
    private BINOLCM19_Service binolcm19_Service;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binolsscm04_bl;
	
	@Resource(name="binOLSSCM03_BL")
	private BINOLSSCM03_BL binolsscm03_bl;
	
	@Resource(name="binOLSSCM08_BL")
	private BINOLSSCM08_IF binOLSSCM08_BL;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLSTCM04_BL")
	private BINOLSTCM04_IF binOLSTCM04_BL;
	
	@Resource(name="binOLSTCM05_BL")
	private BINOLSTCM05_IF binOLSTCM05_BL;
	
	@Resource(name="binOLSTCM06_BL")
	private BINOLSTCM06_IF binOLSTCM06_BL;
	
	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_IF binOLSTCM08_BL;
	
	@Resource(name="binOLSTCM09_BL")
	private BINOLSTCM09_IF binOLSTCM09_BL;
	
	@Resource(name="binOLSTCM13_BL")
	private BINOLSTCM13_IF binOLSTCM13_BL;
	
	@Resource(name="binOLSTCM14_BL")
	private BINOLSTCM14_IF binOLSTCM14_BL;
	
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_BL binOLSSCM09_BL;
    
    @Resource(name="binOLSTCM19_BL")
    private BINOLSTCM19_IF binOLSTCM19_BL;
    
	@Resource(name="binOLSTCM21_BL")
	private BINOLSTCM21_IF binOLSTCM21_BL;
	
	/* (non-Javadoc)
	 * @see com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF#getCurrActionByOSID(long)
	 * 根据工作流实例ID，取得当前能操作的步骤
	 * 在MQ中调用
	 */
	@Override	
	public ActionDescriptor[] getCurrActionByOSID(long osID) {
		
		int[] actionArr = workflow.getAvailableActions(osID, null);
		WorkflowDescriptor wd =workflow.getWorkflowDescriptor(workflow.getWorkflowName(osID));
		ActionDescriptor[] actionDsArr =null;
		if(actionArr!=null&&actionArr.length>0){
			actionDsArr = new ActionDescriptor[actionArr.length];
			for(int i =0;i<actionArr.length;i++){
			ActionDescriptor ad = wd.getAction(actionArr[i]);
			actionDsArr[i]=ad;
			}
		}
		
		return actionDsArr;
	}
	
	/**
	 * 根据工作流实例ID，取得当前能操作的步骤
	 * 打开单据明细时取得当前可执行的action时调用此方法
	 */
	@Override
    public ActionDescriptor[] getCurrActionByOSID(long osID, UserInfo userInfo){
//	    PropertySet ps = workflow.getPropertySet(osID);
	    IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(osID);
	    Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
	    String OS_BillID = ConvertUtil.getString(propertyMap.get("OS_BillID"));
	    String OS_BillCreator = ConvertUtil.getString(propertyMap.get("OS_BillCreator"));
	    String OS_BillType = ConvertUtil.getString(propertyMap.get("OS_BillType"));
	    String OS_Current_Operate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
	    String OS_ProType = ConvertUtil.getString(propertyMap.get("OS_ProType"));
	    String OS_BillCreator_Depart = ConvertUtil.getString(propertyMap.get("OS_BillCreator_Depart"));
	    
	    CherryTaskInstance cherryTaskInstance = new CherryTaskInstance();
	    cherryTaskInstance.setBillID(OS_BillID);
	    cherryTaskInstance.setBillCreator(OS_BillCreator);
	    cherryTaskInstance.setBillType(OS_BillType);
	    cherryTaskInstance.setCurrentOperate(OS_Current_Operate);
	    cherryTaskInstance.setProType(OS_ProType);
	    cherryTaskInstance.setBillCreator_Depart(OS_BillCreator_Depart);
	    
	    Map<String,Object> paramData = new HashMap<String,Object>();
        return getCurrActionByOSID(osID,userInfo,cherryTaskInstance,paramData);
    }
	
	/**
	 * 根据工作流实例ID，取得用户能操作的步骤
	 * 被方法getCurrActionByOSID(long osID, UserInfo userInfo)调用
	 * @param osID
	 * @param userInfo
	 * @param cherryTaskInstance
	 * @return
	 */
	public ActionDescriptor[] getCurrActionByOSID(long osID, UserInfo userInfo,CherryTaskInstance cherryTaskInstance,Map<String,Object> paramData){
		ActionDescriptor[] actionDsArr = null;

//		PropertySet ps = workflow.getPropertySet(osID);
		IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(osID);
	    Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
		
		// 当前操作
		String operate = cherryTaskInstance.getCurrentOperate();

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("WorkFlowID", osID);
		List<Map<String,Object>> inventoryUserTask = getInventoryUserTaskByOSID(param);
	    // 当前操作的参与者
        String participant = "";
        //执行者限制
        String participantLimit = "";
        if(null != inventoryUserTask && inventoryUserTask.size()>0){
            participant = ConvertUtil.getString(inventoryUserTask.get(0).get("CurrentParticipant"))+",";
            participantLimit = ConvertUtil.getString(inventoryUserTask.get(0).get("ParticipantLimit"));
        }

		String userID = CherryConstants.OS_ACTOR_TYPE_U + userInfo.getBIN_UserID();
		String positionID = CherryConstants.OS_ACTOR_TYPE_P + userInfo.getBIN_PositionCategoryID();
		String departID = CherryConstants.OS_ACTOR_TYPE_D + userInfo.getBIN_OrganizationID();
		
		
		if (participant != null && !"".equals(participant)) {			
			// Inventory.BIN_InventoryUserTask表中设定的参与者
			// 如果当前用户 出现在当前操作的参与者中，即返回当前的actions
			boolean flag = false;
			if (participant.indexOf(userID+",") > -1 || participant.indexOf(departID+",") > -1) {
				flag = true;
			} else if (participant.indexOf(positionID+",") > -1) {
                try {
                    WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osID));
                    List<SimpleStep> stepList = workflow.getCurrentSteps(osID);
                    SimpleStep ss = (SimpleStep) stepList.get(0);
                    Map<String,Object> metaAttr = wd.getStep(ss.getStepId()).getMetaAttributes();
                    String osRule = ConvertUtil.getString(metaAttr.get("OS_Rule"));
                    Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(osRule);
                    //复杂模式
                    if(CherryConstants.OS_RULETYPE_HARD.equals(ConvertUtil.getString(ruleMap.get("RuleType")))){
                        // 当前操作的权限标志
                        String privilegeFlag = "";
                        String createType = "";
                        //取出positionID在CurrentParticipant中的位置，然后查出在ParticipantLimit中的值是多少。
                        String[] participantArr = participant.split(",");
                        Map<String,Object> participantMap = (Map<String, Object>) JSONUtil.deserialize(participantLimit);
                        for(int i=0;i<participantArr.length;i++){
                            if(positionID.equals(participantArr[i])){
                                privilegeFlag = ConvertUtil.getString(participantMap.get(String.valueOf(i+1)));
                                //区分发起者类型是否是部门类型
                                createType = ConvertUtil.getString(participantMap.get(CherryConstants.OS_ROLETYPE_CREATER+String.valueOf(i+1)));
                                break;
                            }
                        }
                        // 如果按照用户和部门不能匹配到，但是按照岗位能匹配到，则还要验证用户是否关注或管辖了实例创建者
                        String os_billcreator = cherryTaskInstance.getBillCreator();
                        String os_BilCreatorDepart = cherryTaskInstance.getBillCreator_Depart();
                        if(createType.equals("DT")||createType.equals("D")){
                            flag = hasChildDepart(ConvertUtil.getString(userInfo.getBIN_UserID()), os_BilCreatorDepart, privilegeFlag,paramData);
                        }else{
                            flag = hasChildUser(ConvertUtil.getString(userInfo.getBIN_UserID()), os_billcreator, privilegeFlag,paramData);
                        }
                    }else{
                        //简单模式
                        flag = true;
                    }
                } catch (JSONException e) {
                    flag = false;
                }
			}
			if(flag){
				int[] actionArr = workflow.getAvailableActions(osID, null);
				WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osID));
				if (actionArr != null && actionArr.length > 0) {
					actionDsArr = new ActionDescriptor[actionArr.length];
					for (int i = 0; i < actionArr.length; i++) {
						ActionDescriptor ad = wd.getAction(actionArr[i]);
						actionDsArr[i] = ad;
					}
				}
			}
		} else {
//			// 如果PS表中没有指定过参与者，则匹配action下的<meta name="OS_Rule">元素中的规则
//			List<ActionDescriptor> list = new ArrayList<ActionDescriptor>();
//			int[] actionArr = workflow.getAvailableActions(osID, null);
//			WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osID));
//			if (actionArr != null && actionArr.length > 0) {
//				Map metaMap;
//				//Map<String,String> tmpMap = new HashMap<String,String>();;
//
//                String OS_BillCreator_User ="";
//                String OS_BillCreator_Position ="";
//                String OS_BillCreator_Depart ="";
//                try{
//                    OS_BillCreator_User = ConvertUtil.getString(propertyMap.get("OS_BillCreator_User"));
//                    OS_BillCreator_Position = ConvertUtil.getString(propertyMap.get("OS_BillCreator_Position"));
//                    OS_BillCreator_Depart = ConvertUtil.getString(propertyMap.get("OS_BillCreator_Depart"));
//                }catch(Exception ex){
//                    OS_BillCreator_User ="";
//                    OS_BillCreator_Position ="";
//                    OS_BillCreator_Depart ="";
//                }
//				cherryTaskInstance.setBillCreator_User(OS_BillCreator_User);
//				cherryTaskInstance.setBillCreator_Position(OS_BillCreator_Position);
//				cherryTaskInstance.setBillCreator_Depart(OS_BillCreator_Depart);
//				
//				for (int i = 0; i < actionArr.length; i++) {
//					ActionDescriptor ad = wd.getAction(actionArr[i]);
//					metaMap = ad.getMetaAttributes();
//					// 排除action标签下meta元素对任务参与者做出的限制
//					String rule;
//					if (metaMap == null) {
//						rule = "";
//					} else {
//						rule = String.valueOf(metaMap.get("OS_Rule"));
//					}
//					try {
//					    //排除OS_DefaultAction
//					    if(!metaMap.containsKey("OS_DefaultAction")){
//	                        if (canDoAction(userInfo, ps, rule,cherryTaskInstance,paramData)) {
//	                            // actionDsArr[i] = ad;
//	                            list.add(ad);
//	                        }
//					    }
//					} catch (Exception e) {
//						// TODO:
//					}
//
//				}
//			}
//
//			actionDsArr = new ActionDescriptor[list.size()];
//			for (int i = 0; i < list.size(); i++) {
//				actionDsArr[i] = list.get(i);
//			}
		}
		return actionDsArr;
	}
	
	/* (non-Javadoc)
	 * @see com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF#getCurrentActionByOSID(long, com.cherry.cm.cmbeans.UserInfo)
	 * 根据工作流实例ID，取得当前的业务类型 这是存储在ps中的字段
	 */
	@Override
	public String getCurrentOperation(long osID) {
//		PropertySet ps = workflow.getPropertySet(osID);
		IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(osID);
		Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
		return ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_CURRENT_OPERATE));
	}

	/* (non-Javadoc)
	 * @see com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF#getUserTasks(java.util.Map)
	 * 返回任务的主要部分信息
	 * 在首页上显示任务时调用
	 */
	@Override
	public List<CherryTaskInstance> getUserTasks(Map<String, Object> pramMap) {
        List<CherryTaskInstance> retList = new ArrayList<CherryTaskInstance>();

        String organizationInfoID = String.valueOf(pramMap.get("BIN_OrganizationInfoID"));
        String brandInfoID  = String.valueOf(pramMap.get("BIN_BrandInfoID"));
        String userID = String.valueOf(pramMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
        String positionID = String.valueOf(pramMap.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
        String organizationID = String.valueOf(pramMap.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put(CherryConstants.OS_ACTOR_TYPE_USER, CherryConstants.OS_ACTOR_TYPE_U+userID);
        param.put(CherryConstants.OS_ACTOR_TYPE_POSITION, CherryConstants.OS_ACTOR_TYPE_P+positionID);
        param.put(CherryConstants.OS_ACTOR_TYPE_DEPART, CherryConstants.OS_ACTOR_TYPE_D+organizationID);
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        List<Map<String,Object>> taskList = getInventoryUserTask(param);
		
        if (taskList == null || taskList.size() < 1) {
            return retList;
        }

        UserInfo userinfo = new UserInfo();
        userinfo.setBIN_UserID(Integer.parseInt(userID));
        userinfo.setBIN_PositionCategoryID(Integer.parseInt(positionID));
        userinfo.setBIN_OrganizationID(Integer.parseInt(organizationID));
        userinfo.setLanguage(String.valueOf(pramMap.get("language")));

        CherryTaskInstance ret;
        Map<String,Object> paramData = new HashMap<String,Object>();
        for (int i = 0; i < taskList.size(); i++) {
            Map<String, Object> tmp = taskList.get(i);
            long osid = Long.parseLong(String.valueOf(tmp.get("WorkFlowID")));

            ret = new CherryTaskInstance();
            // 实例ID
            ret.setEntryID(String.valueOf(tmp.get("WorkFlowID")));
            // 业务类型
            String billType = getBillTypeByTableName(String.valueOf(tmp.get("RelevanceTableName")));
            ret.setBillType(billType);
            // 当前单据ID（一个流程中可能会产生多个业务单据，但是当前单据ID只有1个）
            ret.setBillID(String.valueOf(tmp.get("BillID")));
            // 当前操作编码
            ret.setCurrentOperate(String.valueOf(tmp.get("CurrentOperate")));
            //关联表为后台销售表时首页任务名称不显示产品/促销品
            if(!tmp.get("RelevanceTableName").equals("Sale.BIN_BackstageSale")){
                // 产品类型
                ret.setProType(String.valueOf(tmp.get("ProType")));
            }else{
                ret.setProType("");
            }
            //语言
            ret.setLanguage(String.valueOf(pramMap.get("language")));
            // 单据创建者
            ret.setBillCreator(String.valueOf(tmp.get("BillCreator")));
            //员工编号名称
            ret.setEmployeeCodeName(String.valueOf(tmp.get("EmployeeCodeName")));
            //单据号
            ret.setBillCode(String.valueOf(tmp.get("BillNo")));
            //总数量
            ret.setTotalQuantity(ConvertUtil.getString(tmp.get("TotalQuantity")));
            //总金额
            ret.setTotalAmount(ConvertUtil.getString(tmp.get("TotalAmount")));
            //发起方部门编号名称
            ret.setDepartCodeNameFrom(ConvertUtil.getString(tmp.get("DepartCodeNameFrom")));
            //接受方部门编号名称
            ret.setDepartCodeNameTo(ConvertUtil.getString(tmp.get("DepartCodeNameTo")));
            
            //复杂模式下，当前步骤的参与者有岗位时，判断审核者是否（管辖/关注/管辖+关注 根据ParticipantLimit）发起者，不符合的去掉不在首页上显示。
            // 代收模式下，当前步骤的参与者有岗位时，判断确认者是否（管辖/关注/管辖+关注，依据是：ParticipantLimit字段中包含‘RTR’，接收者身份类型）接收者，不符合的去掉在首页上的显示。
            //简单模式下，即使参与者有岗位，ParticipantLimit没有值
            String currentParticipant = ConvertUtil.getString(tmp.get("CurrentParticipant"))+",";
            String participantLimit = ConvertUtil.getString(tmp.get("ParticipantLimit"));
            //增加到首页上显示标志
            boolean addFlag = true;
            if(currentParticipant.indexOf(CherryConstants.OS_ACTOR_TYPE_P)>-1 && !"".equals(participantLimit)){
            	if(participantLimit.indexOf(CherryConstants.OS_ROLETYPE_RECEIVER)<=-1){
            		addFlag = false;
                    //复杂模式   参与者是岗位
                    String[] currentParticipantArr = currentParticipant.split(",");
                    Map<String,Object> participantLimitMap = new HashMap<String,Object>();
                    try {
                        participantLimitMap = (Map<String, Object>) JSONUtil.deserialize(participantLimit);
                    } catch (JSONException e) {
                    }
                    String employeeID = ret.getBillCreator();
                    IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(ret.getEntryID()));
                    for(int j=0;j<currentParticipantArr.length;j++){
                        if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_U+userID)){
                            addFlag = true;
                            break;
                        }else if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_D+organizationID)){
                            addFlag = true;
                            break;
                        }else if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_P+positionID)){
                            String curPrivilegeFlag = ConvertUtil.getString(participantLimitMap.get(String.valueOf(j+1)));
                            String creatorType = ConvertUtil.getString(participantLimitMap.get(CherryConstants.OS_ROLETYPE_CREATER+String.valueOf(j+1)));
                            if(creatorType.equals("DT")||creatorType.equals("D")){
                                //发起者的部门
                                String departID = ps.getString("OS_BillCreator_Depart");
                                if(hasChildDepart(userID,departID,curPrivilegeFlag,paramData)){
                                    addFlag = true;
                                    break;
                                }
                            }else{
                                if(hasChildUser(userID,employeeID,curPrivilegeFlag,paramData)){
                                    addFlag = true;
                                    break;
                                }
                            }
                        }
                    }
            	} else {
            		addFlag = false;
                    //代收模式   参与者是岗位
                    String[] currentParticipantArr = currentParticipant.split(",");
                    Map<String,Object> participantLimitMap = new HashMap<String,Object>();
                    try {
                        participantLimitMap = (Map<String, Object>) JSONUtil.deserialize(participantLimit);
                    } catch (JSONException e) {
                    }
                    IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(Long.parseLong(ret.getEntryID()));
                    for(int j=0;j<currentParticipantArr.length;j++){
                        if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_U+userID)){
                            addFlag = true;
                            break;
                        }else if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_D+organizationID)){
                            addFlag = true;
                            break;
                        }else if(currentParticipantArr[j].equals(CherryConstants.OS_ACTOR_TYPE_P+positionID)){
                            String curPrivilegeFlag = ConvertUtil.getString(participantLimitMap.get(String.valueOf(j+1)));
                        	// 接收者的部门
                            String departID = ps.getString("OS_BillReceiver_Depart");
                            if(hasChildDepart(userID,departID,curPrivilegeFlag,paramData)){
                                addFlag = true;
                                break;
                            }
                        }
                    }
            	}
            }
            if(addFlag){
                retList.add(ret); 
            }
        }
        return retList;
	}

	/**
	 * 判断当前登录者是否满足简单规则(RuleType=2)
	 * @param actorU
	 * @param actorP
	 * @param actorD
	 * @param actorType
	 * @param actorValue
	 * @return
	 */
	private boolean isMachingRuleTypeOne(String actorU,String actorP,String actorD,String actorType,String actorValue){
		if("U".equals(actorType)){
			return actorU.equals(actorValue);
		}else if("P".equals(actorType)){
			return actorP.equals(actorValue);
		}else if("D".equals(actorType)){
			return actorD.equals(actorValue);
		}
		return false;
	}
	
	/**
	 * 判断actorUserID所表示的员工是否管辖或关注了creatEmployeeID所表示的员工
	 * @param actorUserID
	 * @param creatEmployeeID
	 * @param privilegeFlag
	 * @param paramData
	 * @return
	 */
	private boolean hasChildUser(String actorUserID,String creatEmployeeID,String privilegeFlag,Map<String,Object> paramData){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BIN_UserID1", actorUserID);
		map.put("BIN_EmployeeID", creatEmployeeID);
        if("".equals(privilegeFlag)){
            //privilegeFlag没有设置时，权限范围为管辖
            map.put("PrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
        }else if(!CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)){
            map.put("PrivilegeFlag", privilegeFlag);
        }
        List<Map<String,Object>> employeePrivilegeList = new ArrayList<Map<String,Object>>();
        if(null == paramData.get("childUserList")){
            List<Map<String,Object>> list = binolcm19_Service.getChildUserListByEmployeeID(map);
            paramData.put("childUserList", list);
        }
        employeePrivilegeList = (List)paramData.get("childUserList");
        privilegeFlag = ConvertUtil.getString(map.get("PrivilegeFlag"));
        if(null != employeePrivilegeList && !employeePrivilegeList.isEmpty()){
            for(int i=0;i<employeePrivilegeList.size();i++){
                String curSubEmployeeID = ConvertUtil.getString(employeePrivilegeList.get(i).get("BIN_SubEmployeeID"));
                String currivilegeFlag = ConvertUtil.getString(employeePrivilegeList.get(i).get("PrivilegeFlag"));
                if("".equals(privilegeFlag) && creatEmployeeID.equals(curSubEmployeeID)){
                    return true;
                }else if(creatEmployeeID.equals(curSubEmployeeID) && privilegeFlag.equals(currivilegeFlag)){
                    return true;
                }
            } 
        }
		return false;
	}

	/**
	 * 判断CherryShow按钮是否显示
	 */
	@Override
	public int macthCherryShowRule(UserInfo userinfo, String menuID) {
		List<Map<String, Object>> list = binolcm19_Service.getMenuTarget(menuID);
		if(list==null || list.size()==0){
			return 0;
		}
		String fs = String.valueOf(list.get(0).get("MENU_INDICATION_TARGET"));
		if(null==fs||"".equals(fs)||"null".equals(fs)){
			return 0;
		}
		try {
			Map fsMap = (Map) JSONUtil.deserialize(fs);
			if(!fsMap.containsKey("FileCode")||!fsMap.containsKey("StepID")){
				return 0;
			}
			String fileCode = String.valueOf(fsMap.get("FileCode"));
			String stepID = String.valueOf(fsMap.get("StepID"));
			String workFileName =ConvertUtil.getWfName(userinfo.getOrganizationInfoCode(), userinfo.getBrandCode(), fileCode);
			WorkflowDescriptor wd =workflow.getWorkflowDescriptor(workFileName);
			StepDescriptor sd = wd.getStep(Integer.parseInt(stepID));
			String rule = sd.getMetaAttributes().get("OS_Rule").toString();
			Map ruleMap = (Map) JSONUtil.deserialize(rule);
			
			String thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
			if("1".equals(thirdPartyFlag)){
				//如果是第三方完成，则强制设为不符合规则
				return 1;
			}
			
			List ruleList =  (List)ruleMap.get("RuleContext");
			//没有配置规则，显示按钮
			if(null == ruleList || ruleList.size()==0){
			    return 0;
			}
			String actorType;
			String actorValue;
			String actorU = String.valueOf(userinfo.getBIN_UserID());
			String actorP = String.valueOf(userinfo.getBIN_PositionCategoryID());
			String actorD = String.valueOf(userinfo.getBIN_OrganizationID());
			for(int i=0;i<ruleList.size();i++){
				Map tmpMap = (Map)ruleList.get(i);
				actorType = String.valueOf(tmpMap.get("ActorType"));
				actorValue = String.valueOf(tmpMap.get("ActorValue"));
				if(isMachingRuleTypeOne(actorU,actorP,actorD,actorType,actorValue)){
					return 2;
				}				
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}		
	}
	
    /**
     * 查询employeeID对应的员工有没有岗位为positionID的上司
     * @param employeeID
     * @param positionID
     * @param privilegeFlag
     * @return 上司List
     */
    private List<Map<String,Object>> findBossByPosition(String employeeID,String positionID,String privilegeFlag){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("BIN_EmployeeID", employeeID);
        map.put("BIN_PositionCategoryID", positionID);
        if("".equals(privilegeFlag)){
            //privilegeFlag没有设置时，权限范围为管辖
            map.put("PrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
        }else if(!CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)){
            map.put("PrivilegeFlag", privilegeFlag);
        }
        List<Map<String,Object>> bossList = binolcm19_Service.getBossByEmployeeID(map);
        return bossList;
    }
	
	/**
	 * 返回审核者
	 * @param userID
	 * @param positionID
	 * @param departID
	 * @param employeeID
	 * @param praMap
	 * @return 任务参与者类型 +ID，无审核者返回""
	 */
    private String findAuditor(String userID,String positionID ,String departID,String employeeID,String departTypeID,Map<String,Object> praMap){
        String creatType = String.valueOf(praMap.get("RoleTypeCreater"));
        String creatValue =String.valueOf(praMap.get("RoleValueCreater"));
        String auditType = String.valueOf(praMap.get("RoleTypeAuditor"));
        String auditValue = String.valueOf(praMap.get("RoleValueAuditor"));
        String privilegeFlag = ConvertUtil.getString(praMap.get("RolePrivilegeFlag"));
//        //当发起者类型=审核者类型，发起者=审核者，不需要审核
//        if(creatType.equals(auditType)){
//            if(creatValue.equals(auditValue)){
//                return "";
//            }
//        }
//        //按审核者类型判断，当对应类型的发起者=审核者，不需要审核
//        if("U".equals(auditType)){
//            if(userID.equals(auditValue)){
//                return "";
//            }
//        }else if("P".equals(auditType)){
//            if(positionID.equals(auditValue)){
//                return "";
//            }
//        }else if("D".equals(auditType)){
//            if(departID.equals(auditValue)){
//                return "";
//            }
//        }
        //简单的判定，包括了业务规则
        if("U".equals(creatType)){//如果规则设定：   发起者为用户
            if(userID.equals(creatValue)||"ALL".equals(creatValue)){
                if("U".equals(auditType)){
                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核
                    if(!userID.equals(auditValue)){
                        return CherryConstants.OS_ACTOR_TYPE_USER + auditValue;
                    }else{
                        return "";
                    }
                }else if("P".equals(auditType)){
                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
                    return changePToU(false,true,employeeID,auditValue,privilegeFlag);
                }else if("D".equals(auditType)){
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    if(!departID.equals(auditValue)){
//                        return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    }else{
//                        return "";
//                    }
                }
            }
        }else if("P".equals(creatType)||"ALL".equals(creatValue)){//如果规则设定：   发起者为岗位
            if(positionID.equals(creatValue)){
                if("U".equals(auditType)){
                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核
                    if(!userID.equals(auditValue)){
                        return CherryConstants.OS_ACTOR_TYPE_USER + auditValue;
                    }else{
                        return "";
                    }
                }else if("P".equals(auditType)){
                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
                    return changePToU(false,true,employeeID,auditValue,privilegeFlag);
                }else if("D".equals(auditType)){
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    if(!departID.equals(auditValue)){
//                        return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    }else{
//                        return "";
//                    }
                }
            }
        }else if("D".equals(creatType)||"ALL".equals(creatValue)){//如果规则设定：  发起者为部门
            //如果创建者的部门是 规则中的发起部门
            if(departID.equals(creatValue)){
                if("U".equals(auditType)){
                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核
                    if(!userID.equals(auditValue)){
                        return CherryConstants.OS_ACTOR_TYPE_USER + auditValue;
                    }else{
                        return "";
                    }
                }else if("P".equals(auditType)){
                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
                    return changePToU(false,true,employeeID,auditValue,privilegeFlag);
                }else if("D".equals(auditType)){
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    if(!departID.equals(auditValue)){
//                        return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
//                    }else{
//                        return "";
//                    }
                }
            }
        } else if ("DT".equals(creatType) || "ALL".equals(creatValue)) {// 如果规则设定：发起者为部门类型
            if (departTypeID.equals(creatValue)) {
                if ("U".equals(auditType)) {
                    // 如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核
                    if (!userID.equals(auditValue)) {
                        return CherryConstants.OS_ACTOR_TYPE_USER + auditValue;
                    } else {
                        return "";
                    }
                } else if ("P".equals(auditType)) {
                    // 如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
                    // return changePToU(false, true, employeeID, auditValue, privilegeFlag);
                    praMap.put("RoleTypeCreater", creatType);
                    return getStrBossByOrganizationID(true, employeeID, departID, auditValue, privilegeFlag);
                } else if ("D".equals(auditType)) {
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + auditValue;
                }
            }
        }
        return "";
    }
    
    /**
	 * 返回确认者
	 * @param departID 接收部门ID
	 * @param praMap 配置的规则
	 * @return 任务参与者类型 +ID，无确认者返回""（此时认为确认者为接收部门的员工）
	 */
    private String findConfirmation(String departID,String departTypeID,Map<String,Object> praMap){
        String receiveType = String.valueOf(praMap.get("RoleTypeReceiver"));
        String receiveValue =String.valueOf(praMap.get("RoleValueReceiver"));
        String confirmType = String.valueOf(praMap.get("RoleTypeConfirmation"));
        String confirmValue = String.valueOf(praMap.get("RoleValueConfirmation"));
        String privilegeFlag = ConvertUtil.getString(praMap.get("RolePrivilegeRecFlag"));
        //简单的判定，包括了业务规则
//        if("U".equals(receiveType)){//如果规则设定：   接收者为用户
//            if(userID.equals(receiveValue)||"ALL".equals(receiveValue)){
//                if("U".equals(confirmType)){
//                    //如果确认者的类型为用户，且确认者为接收者，那么使用默认规则,否则采用配置的确认者
//                    if(!userID.equals(confirmValue)){
//                        return CherryConstants.OS_ACTOR_TYPE_USER + confirmValue;
//                    }else{
//                        return "";
//                    }
//                }else if("P".equals(confirmType)){
//                    //如果确认者的类型为岗位，则查看接收者是否有该岗位的上司，如果有则采用此类型的确认者，否则使用默认规则
//                    return changePToU(false,true,employeeID,confirmValue,privilegeFlag);
//                }else if("D".equals(confirmType)){
//                	// 确认者的类型为部门，则该部门下的所有员工都可进行确认
//                    return CherryConstants.OS_ACTOR_TYPE_DEPART + confirmValue;
//                }
//            }
//        }else if("P".equals(receiveType)||"ALL".equals(receiveValue)){//如果规则设定：   接收者为岗位
//            if(positionID.equals(receiveValue)){
//                if("U".equals(confirmType)){
//                    //如果确认者的类型为用户，且确认者为接收者，那么使用默认规则,否则采用此类型的确认者
//                    if(!userID.equals(confirmValue)){
//                        return CherryConstants.OS_ACTOR_TYPE_USER + confirmValue;
//                    }else{
//                        return "";
//                    }
//                }else if("P".equals(confirmType)){
//                    //如果确认者的类型为岗位，则查看接收者是否有该岗位的上司，如果有则采用此类型的确认者，否则使用默认规则
//                    return changePToU(false,true,employeeID,confirmValue,privilegeFlag);
//                }else if("D".equals(confirmType)){
//                	// 确认者的类型为部门，则该部门下的所有员工都可进行确认
//                    return CherryConstants.OS_ACTOR_TYPE_DEPART + confirmValue;
//                }
//            }
//        }else 
        if("D".equals(receiveType)||"ALL".equals(receiveValue)){//如果规则设定：  接收者为部门
            //如果接收者的部门是 规则中的接收者部门
            if(departID.equals(receiveValue)){
                if("U".equals(confirmType)){
                    //如果确认者的类型为用户，那么采用此类型的确认者,否则使用默认规则
                    return CherryConstants.OS_ACTOR_TYPE_USER + confirmValue;
                }else if("P".equals(confirmType)){
                    //如果确认者的类型为岗位，则查看 接收者部门是否有该岗位的上司，如果有则采用此类型的确认者,否则使用默认规则
                    return getStrBossByOrganizationID(true,"",departID,confirmValue,privilegeFlag);
                }else if("D".equals(confirmType)){
                	// 确认者的类型为部门，则该部门下的所有员工都可进行确认
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + confirmValue;
                }
            }
        } else if ("DT".equals(receiveType) || "ALL".equals(receiveValue)) {// 如果规则设定：接收者为部门类型
            if (departTypeID.equals(receiveValue)) {
                if ("U".equals(confirmType)) {
                    // 如果确认者的类型为用户，那么采用此类型的确认者,否则使用默认规则
                    return CherryConstants.OS_ACTOR_TYPE_USER + confirmValue;
                } else if ("P".equals(confirmType)) {
                    // 如果确认者的类型为岗位，则查看 接收者是否有该岗位的上司，如果有则采用此类型的确认者,否则使用默认规则
                    praMap.put("RoleTypeReceiver", receiveType);
                    return getStrBossByOrganizationID(true, "", departID, confirmValue, privilegeFlag);
                } else if ("D".equals(confirmType)) {
                	// 确认者的类型为部门，则该部门下的所有员工都可进行确认
                    return CherryConstants.OS_ACTOR_TYPE_DEPART + confirmValue;
                }
            }
        }
        return "";
    }
	
    /**
     * 查找符合规则的审核者
     */
    @Override
    public String findMatchingAuditor(Map<String, String> map) throws Exception {
        String auditor = "";
        try {
            String userID = map.get(CherryConstants.OS_ACTOR_TYPE_USER);
            String positionID = map.get(CherryConstants.OS_ACTOR_TYPE_POSITION);
            String departID = map.get(CherryConstants.OS_ACTOR_TYPE_DEPART);
            String ruleStr = map.get(CherryConstants.OS_META_Rule);
            String employeeID = map.get(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE);

            if (ruleStr == null || "".equals(ruleStr)) {
                // 没有配置规则，无需审核
                return "";
            }
            Map ruleMap = (Map) JSONUtil.deserialize(ruleStr);
            String thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
            if("1".equals(thirdPartyFlag)){
                //如果是第三方完成，则强制设为需要审核
                return "ThirdParty";
            }
            
            List<Map<String,Object>> ruleList =  (List<Map<String,Object>>)ruleMap.get("RuleContext");
            
            //根据部门ID查出部门类型ID
            Map<String,Object> departInfo = binOLCM01_BL.getDepartmentInfoByID(departID, null);
            String departTypeID = "";
            if(null != departInfo){
                departTypeID = ConvertUtil.getString(departInfo.get("Type"));
            }
            //是否判断优先级（true判断，false不判断）,若无该字段，则按判断优先级处理。
            String preferredFlag = ConvertUtil.getString(ruleMap.get("PreferredFlag"));
            Map<Object,Object> privilegeFlagMap = new HashMap<Object,Object>();
            //审核者字符串按逗号分隔的当前位置（用于岗位）
            int auditIndex = 1;
            for(int i=0;i<ruleList.size();i++){
                Map<String,Object> tmpMap = (Map<String,Object>)ruleList.get(i);
                String auditorPrivilege = findAuditor(userID,positionID,departID,employeeID,departTypeID,tmpMap);
                
                String privilegeFlag = "";
                String curAuditor = "";
                int privilegeFlagIndex = auditorPrivilege.indexOf(CherryConstants.SESSION_PRIVILEGE_FLAG);
                if(privilegeFlagIndex > -1){
                    //审核者为岗位时的权限标志
                    privilegeFlag = auditorPrivilege.substring(privilegeFlagIndex+CherryConstants.SESSION_PRIVILEGE_FLAG.length());
                    curAuditor = auditorPrivilege.substring(0, privilegeFlagIndex-1);
                    if(!"".equals(curAuditor)){
                        privilegeFlagMap.put(auditIndex, privilegeFlag);
                        //发起者身份类型不为空写入任务表
                        if(!ConvertUtil.getString(tmpMap.get("RoleTypeCreater")).equals("")){
                            privilegeFlagMap.put(CherryConstants.OS_ROLETYPE_CREATER+auditIndex,tmpMap.get("RoleTypeCreater"));
                        }
                    }
                }else{
                    curAuditor = auditorPrivilege;
                }
                
                //判断优先级时，审核者有值时跳出循环，不判断优先级时，查出所有有值的审核者，用逗号分隔。
                if(!"".equals(curAuditor)){
                    if("false".equals(preferredFlag)){
                        auditor += curAuditor+",";
                    }else{
                        auditor = curAuditor+",";
                        break;
                    }
                    auditIndex ++;
                }
            }
            //auditor = processingCommaString(auditor);
            if(null != privilegeFlagMap && !privilegeFlagMap.isEmpty()){
                //privilegeFlag JSON格式 {"1":"1","2":"0","4":"A"}
                auditor += CherryConstants.SESSION_PRIVILEGE_FLAG+JSONUtil.serialize(privilegeFlagMap);
            }
        } catch (Exception e) {
            throw new Exception("ECM00048");
        }

        return auditor;
    }
    
    /**
     * 查找条例规则的确认者
     * @param organizationID:接收部门ID
     * @param OS_Rule:配置的规则
     */
    @Override
    public String findMatchingConfirmation(Map<String, String> map) throws Exception {
        String confirmation = "";
        try {
//            String userID = map.get(CherryConstants.OS_ACTOR_TYPE_USER);
//            String positionID = map.get(CherryConstants.OS_ACTOR_TYPE_POSITION);
            String departID = map.get(CherryConstants.OS_ACTOR_TYPE_DEPART);
            String ruleStr = map.get(CherryConstants.OS_META_Rule);
//            String employeeID = map.get(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE);

            if (ruleStr == null || "".equals(ruleStr)) {
                // 没有配置规则，默认确认者为接收部门的人员
                return "";
            }
            Map ruleMap = (Map) JSONUtil.deserialize(ruleStr);
            // 接收确认流程不支持第三方确认
//            String thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
//            if("1".equals(thirdPartyFlag)){
//                //如果是第三方完成，则强制设为需要审核
//                return "ThirdParty";
//            }
            
            List<Map<String,Object>> ruleList =  (List<Map<String,Object>>)ruleMap.get("RuleContext");
            
            //根据部门ID查出部门类型ID
            Map<String,Object> departInfo = binOLCM01_BL.getDepartmentInfoByID(departID, null);
            String departTypeID = "";
            if(null != departInfo){
                departTypeID = ConvertUtil.getString(departInfo.get("Type"));
            }
            //是否判断优先级（true判断，false不判断）,若无该字段，则按判断优先级处理。
            String preferredFlag = ConvertUtil.getString(ruleMap.get("PreferredFlag"));
            Map<Object,Object> privilegeFlagMap = new HashMap<Object,Object>();
            //确认者字符串按逗号分隔的当前位置（用于岗位）
            int confirmIndex = 1;
            for(int i=0;i<ruleList.size();i++){
                Map<String,Object> tmpMap = (Map<String,Object>)ruleList.get(i);
                String confirmationPrivilege = findConfirmation(departID,departTypeID,tmpMap);
                
                String privilegeFlag = "";
                String curConfirmation = "";
                int privilegeFlagIndex = confirmationPrivilege.indexOf(CherryConstants.SESSION_PRIVILEGE_FLAG);
                if(privilegeFlagIndex > -1){
                    //确认者为岗位时的权限标志
                    privilegeFlag = confirmationPrivilege.substring(privilegeFlagIndex+CherryConstants.SESSION_PRIVILEGE_FLAG.length());
                    curConfirmation = confirmationPrivilege.substring(0, privilegeFlagIndex-1);
                    if(!"".equals(curConfirmation)){
                        privilegeFlagMap.put(confirmIndex, privilegeFlag);
                        //接收者身份类型不为空写入任务表
                        if(!ConvertUtil.getString(tmpMap.get("RoleTypeReceiver")).equals("")){
                            privilegeFlagMap.put(CherryConstants.OS_ROLETYPE_RECEIVER+confirmIndex,tmpMap.get("RoleTypeReceiver"));
                        }
                    }
                }else{
                	curConfirmation = confirmationPrivilege;
                }
                
                //判断优先级时，确认者有值时跳出循环，不判断优先级时，查出所有有值的确认者，用逗号分隔。
                if(!"".equals(curConfirmation)){
                    if("false".equals(preferredFlag)){
                    	confirmation += curConfirmation+",";
                    }else{
                    	confirmation = curConfirmation+",";
                        break;
                    }
                    confirmIndex ++;
                }
            }
            //auditor = processingCommaString(auditor);
            if(null != privilegeFlagMap && !privilegeFlagMap.isEmpty()){
                //privilegeFlag JSON格式 {"1":"1","2":"0","4":"A"}
            	confirmation += CherryConstants.SESSION_PRIVILEGE_FLAG+JSONUtil.serialize(privilegeFlagMap);
            }
        } catch (Exception e) {
            throw new Exception("ECM00048");
        }

        return confirmation;
    }
    
    /**
     * 插入【库存业务用户任务表】
     */
    @Override
    public int insertInventoryUserTask(Map<String, Object> map){
        return binolcm19_Service.insertInventoryUserTask(map);
    }
    
    /**
     * 修改【库存业务用户任务表】
     */
    @Override
    public int updateInventoryUserTask(Map<String, Object> map){
        return binolcm19_Service.updateInventoryUserTask(map);
    }
    
    /**
     * 删除【库存业务用户任务表】
     */
    @Override
    public int deleteInventoryUserTask(Map<String, Object> map){
        return binolcm19_Service.deleteInventoryUserTask(map);
    }
    
    /**
     * 查询【库存业务用户任务表】取出登录用户需处理的任务
     */
    @Override
    public List<Map<String,Object>> getInventoryUserTask(Map<String, Object> map){
        return binolcm19_Service.getInventoryUserTask(map);
    }
    
    /**
     * 查询【库存业务用户任务表】根据工作流ID
     */
    @Override
    public List<Map<String,Object>> getInventoryUserTaskByOSID(Map<String, Object> map){
        return binolcm19_Service.getInventoryUserTaskByOSID(map);
    }
    
    /**
     * 设置【库存业务用户任务表】所需跟业务单据有关的值
     */
    @Override
    public Map<String,Object> setInventoryUserTask(Map<String,Object> param){
        String workFlowID = ConvertUtil.getString(param.get("WorkFlowID"));
        long osID = Long.parseLong(workFlowID);
        IBatisPropertySet ps = (IBatisPropertySet) workflow.getPropertySet(osID);
        Map<String,Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
        
        String billType = ConvertUtil.getString(propertyMap.get("OS_BillType"));
        String current_Operate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
        String proType = ConvertUtil.getString(propertyMap.get("OS_ProType"));
        int billCreator = CherryUtil.obj2int(propertyMap.get("OS_BillCreator"));
        
        String relevanceTableName = "";
        int billID = 0;
        String billNo = "";
        int organizationIDF = 0;
        int organizationIDT = 0;
        String totalQuantity = "";
        String totalAmount = "";
        Map<String,Object> mainBillData = new HashMap<String,Object>();
        if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
            if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
                //发货
                relevanceTableName = "Inventory.BIN_PromotionDeliver";
                billID = ps.getInt("BIN_PromotionDeliverID");
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_PromotionDeliverID", billID);
                mainBillData = binolsscm04_bl.getPromotionDeliverMain(paramMap);
                billNo = ConvertUtil.getString(mainBillData.get("DeliverReceiveNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDReceive"));
            }else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
                //调拨
                relevanceTableName = "Inventory.BIN_PromotionAllocation";
                billID = ps.getInt("BIN_PromotionAllocationID");
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_PromotionAllocationID", billID);
                mainBillData = binolsscm03_bl.getPromotionAllocationMain(paramMap);
                billNo = ConvertUtil.getString(mainBillData.get("AllocationNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDAccept"));
            }else if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
                //移库
                relevanceTableName = "Inventory.BIN_PromotionShift";
                billID = ps.getInt("BIN_PromotionShiftID");
                mainBillData = binOLSSCM08_BL.getPrmShiftMainData(billID);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
                //入库
                relevanceTableName = "Inventory.BIN_PrmInDepot";
                billID = ps.getInt("BIN_PrmInDepotID");
                mainBillData = binOLSSCM09_BL.getPrmInDepotMainData(billID,null);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
//                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }
        }else if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
            if(CherryConstants.OS_BILLTYPE_MV.equals(billType)){
                //移库
                relevanceTableName = "Inventory.BIN_ProductShift";
                billID = ps.getInt("BIN_ProductShiftID");
                mainBillData = binOLSTCM04_BL.getProductShiftMainData(billID);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_LS.equals(billType)){
                //报损
                relevanceTableName = "Inventory.BIN_OutboundFree";
                billID = ps.getInt("BIN_OutboundFreeID");
                mainBillData = binOLSTCM05_BL.getOutboundFreeMainData(billID,null);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_GR.equals(billType)){
                //入库
                relevanceTableName = "Inventory.BIN_ProductInDepot";
                billID = ps.getInt("BIN_ProductInDepotID");
                mainBillData = binOLSTCM08_BL.getProductInDepotMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_OD.equals(billType)){
                //订货
                relevanceTableName = "Inventory.BIN_ProductOrder";
                billID = ps.getInt("BIN_ProductOrderID");
                mainBillData = binOLSTCM02_BL.getProductOrderMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("OrderNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDAccept"));
            }else if(CherryConstants.OS_BILLTYPE_CA.equals(billType)){
                //盘点
                relevanceTableName = "Inventory.BIN_ProductStockTaking";
                billID = ps.getInt("BIN_ProductStockTakingID");
                mainBillData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("StockTakingNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_SD.equals(billType)){
                //发货
                relevanceTableName = "Inventory.BIN_ProductDeliver";
                billID = ps.getInt("BIN_ProductDeliverID");
                mainBillData = binOLSTCM03_BL.getProductDeliverMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("DeliverNoIF"));
                if(current_Operate.equals(CherryConstants.OPERATE_OD_AUDIT) || current_Operate.equals(CherryConstants.OPERATE_OD_AUDIT_SEC)){
                    //订货业务在一审、二审时，PS表中的BillType是SD发货，所以发起方部门是发货单的接收部门，接受方部门是发货部门
                    organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDReceive"));
                    organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                }else{
                    organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                    organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDReceive"));
                }
            }else if(CherryConstants.OS_BILLTYPE_RR.equals(billType)){
                //退库
                relevanceTableName = "Inventory.BIN_ProductReturn";
                billID = ps.getInt("BIN_ProductReturnID");
                mainBillData = binOLSTCM09_BL.getProductReturnMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("ReturnNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDReceive"));
            }else if(CherryConstants.OS_BILLTYPE_RA.equals(billType)){
                //退库申请
                relevanceTableName = "Inventory.BIN_ProReturnRequest";
                billID = ps.getInt("BIN_ProReturnRequestID");
                mainBillData = binOLSTCM13_BL.getProReturnRequestMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("BillNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDReceive"));
            }else if(CherryConstants.OS_BILLTYPE_CR.equals(billType)){
                //盘点申请
                relevanceTableName = "Inventory.BIN_ProStocktakeRequest";
                billID = ps.getInt("BIN_ProStocktakeRequestID");
                mainBillData = binOLSTCM14_BL.getProStocktakeRequestMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("StockTakingNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }else if(CherryConstants.OS_BILLTYPE_BG.equals(billType)){
                //调拨
                relevanceTableName = "Inventory.BIN_ProductAllocation";
                billID = ps.getInt("BIN_ProductAllocationID");
                mainBillData = binOLSTCM16_BL.getProductAllocationMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("AllocationNoIF"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDIn"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationIDOut"));
            }else if(CherryConstants.OS_BILLTYPE_NS.equals(billType)){
                //后台销售
                relevanceTableName = "Sale.BIN_BackstageSale";
                billID = ps.getInt("BIN_BackstageSaleID");
                mainBillData = binOLSTCM19_BL.getBackstageSaleMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("BillCode"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
                organizationIDT = CherryUtil.obj2int(mainBillData.get("CustomerID"));
            }else if(CherryConstants.OS_BILLTYPE_SA.equals(billType)){
                //退货申请
                relevanceTableName = "Sale.BIN_SaleReturnRequest";
                billID = ps.getInt("BIN_SaleReturnRequestID");
                mainBillData = binOLSTCM21_BL.getSaleReturnRequestMainData(billID, null);
                billNo = ConvertUtil.getString(mainBillData.get("BillCode"));
                organizationIDF = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationID"));
            }
        }
        int organizationInfoID = CherryUtil.obj2int(mainBillData.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(mainBillData.get("BIN_BrandInfoID"));
        totalQuantity = ConvertUtil.getString(mainBillData.get("TotalQuantity"));
        totalAmount = ConvertUtil.getString(mainBillData.get("TotalAmount"));
        if(CherryConstants.OS_BILLTYPE_NS.equals(billType)){
            //销售表无TotalAmount字段，取PayAmount
            totalAmount = ConvertUtil.getString(mainBillData.get("PayAmount"));
        }
        
        Map<String,Object> inventoryUserTaskMap = new HashMap<String,Object>();
        inventoryUserTaskMap.put("BIN_OrganizationInfoID", organizationInfoID);
        inventoryUserTaskMap.put("BIN_BrandInfoID", brandInfoID);
        inventoryUserTaskMap.put("WorkFlowID", workFlowID);
        inventoryUserTaskMap.put("RelevanceTableName", relevanceTableName);
        inventoryUserTaskMap.put("BillID", billID);
        inventoryUserTaskMap.put("BillNo", billNo);
        inventoryUserTaskMap.put("BillCreator", billCreator);
        inventoryUserTaskMap.put("BIN_OrganizationIDF", organizationIDF);
        if(organizationIDT != 0){
            inventoryUserTaskMap.put("BIN_OrganizationIDT", organizationIDT); 
        }
        inventoryUserTaskMap.put("TotalQuantity", totalQuantity);
        inventoryUserTaskMap.put("TotalAmount", totalAmount);
        inventoryUserTaskMap.put("ProType", proType);
        inventoryUserTaskMap.put("CurrentOperate", current_Operate);
        inventoryUserTaskMap.put("CreatedBy", param.get(CherryConstants.OS_ACTOR_TYPE_USER));
        inventoryUserTaskMap.put("CreatePGM", param.get("CurrentUnit"));
        inventoryUserTaskMap.put("UpdatedBy", param.get(CherryConstants.OS_ACTOR_TYPE_USER));
        inventoryUserTaskMap.put("UpdatePGM", param.get("CurrentUnit"));
        
        return inventoryUserTaskMap;
    }
    
    /**
     * 根据changeFlag把岗位ID是否转成用户ID
     * @param changeFlag false 不转了，直接用岗位ID
     * @param appendPrivilegeFlag
     * @param employeeID
     * @param positionID
     * @param privilegeFlag
     * @return
     */
    @Override
    public String changePToU(boolean changeFlag,boolean appendPrivilegeFlag,String employeeID,String positionID,String privilegeFlag){
        List<Map<String,Object>> bossList = findBossByPosition(employeeID,positionID,privilegeFlag);
        StringBuffer sb = new StringBuffer();
        if(null != bossList && bossList.size()>0){
            if(changeFlag){
                for(int i=0;i<bossList.size();i++){
                    if(null != bossList.get(i).get("BIN_UserID")){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_USER)
                            .append(bossList.get(i).get("BIN_UserID"))
                            .append(",");
                    }
                }
            }else{
                sb.append(CherryConstants.OS_ACTOR_TYPE_POSITION)
                    .append(positionID)
                    .append(",");
            }
            if(appendPrivilegeFlag){
                sb.append(CherryConstants.SESSION_PRIVILEGE_FLAG)
                    .append(privilegeFlag);
            }
        }
        return sb.toString();
    }
    
    /**
     * 查询部门权限表organizationID对应的员工有没有岗位为positionID的上司
     * @param employeeID
     * @param positionID
     * @param privilegeFlag
     * @return 上司List
     */
    private List<Map<String,Object>> findBossByOrganizationID(String organizationID,String positionID,String privilegeFlag){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("BIN_OrganizationID", organizationID);
        map.put("BIN_PositionCategoryID", positionID);
        if("".equals(privilegeFlag)){
            //privilegeFlag没有设置时，权限范围为管辖
            map.put("PrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
        }else if(!CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)){
            map.put("PrivilegeFlag", privilegeFlag);
        }
        List<Map<String,Object>> bossList = binolcm19_Service.getBossByOrganizationID(map);
        return bossList;
    }
    
    /**
     * 取得以逗号分隔的岗位ID字符串
     * @param appendPrivilegeFlag
     * @param createdEmployeeID
     * @param departID
     * @param positionID
     * @param privilegeFlag
     * @return
     */
    public String getStrBossByOrganizationID(boolean appendPrivilegeFlag,String createdEmployeeID,String departID,String positionID,String privilegeFlag){
        List<Map<String,Object>> bossList = findBossByOrganizationID(departID,positionID,privilegeFlag);
        StringBuffer sb = new StringBuffer();
        if(null != bossList && bossList.size()>0){
            sb.append(CherryConstants.OS_ACTOR_TYPE_POSITION)
                .append(positionID)
                .append(",");
            if(appendPrivilegeFlag){
                sb.append(CherryConstants.SESSION_PRIVILEGE_FLAG)
                    .append(privilegeFlag);
            }
        }
        return sb.toString();
    }
    
    /**
     * 判断actorUserID所表示的员工是否管辖或关注了creatDepartID所表示的部门
     * @param actorUserID
     * @param creatDepartID
     * @param privilegeFlag
     * @param paramData
     * @return
     */
    private boolean hasChildDepart(String actorUserID,String creatDepartID,String privilegeFlag,Map<String,Object> paramData){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("BIN_UserID1", actorUserID);
        if("".equals(privilegeFlag)){
            //privilegeFlag没有设置时，权限范围为管辖
            map.put("PrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
        }else if(!CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)){
            map.put("PrivilegeFlag", privilegeFlag);
        }
        List<Map<String,Object>> departPrivilegeList = new ArrayList<Map<String,Object>>();
        if(null == paramData.get("childDepartList")){
            List<Map<String,Object>> list = binolcm19_Service.getChildDepartListByUserID(map);
            paramData.put("childDepartList", list);
        }
        departPrivilegeList = (List)paramData.get("childDepartList");
        privilegeFlag = ConvertUtil.getString(map.get("PrivilegeFlag"));
        if(null != departPrivilegeList && !departPrivilegeList.isEmpty()){
            for(int i=0;i<departPrivilegeList.size();i++){
                String curOrganizationID = ConvertUtil.getString(departPrivilegeList.get(i).get("BIN_OrganizationID"));
                String currivilegeFlag = ConvertUtil.getString(departPrivilegeList.get(i).get("PrivilegeFlag"));
                if("".equals(privilegeFlag) && creatDepartID.equals(curOrganizationID)){
                    return true;
                }else if(creatDepartID.equals(curOrganizationID) && privilegeFlag.equals(currivilegeFlag)){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 去掉重复的、空的以逗号分隔的字符串数组 (最后转成U1,U2,P1,D1,)
     * @param participant
     * @return
     */
    @Override
    public String processingCommaString(String participant){
        String str = "";
        String[] participantArr = participant.split(",");
        //排除重复，空值
        List<String> participantList = new ArrayList<String>();
        for(int i=0;i<participantArr.length;i++){
            if(!participantList.contains(participantArr[i]) && !"".equals(participantArr[i])) {
                participantList.add(participantArr[i]);
            }
        }
        for(int i=0;i<participantList.size();i++){
            str += participantList.get(i)+",";
        }
        return str;
    }
    
    /**
     * 根据表名取业务类型
     * @param tableName
     * @return
     */
    private String getBillTypeByTableName(String tableName){
        String billType = "";
        if(tableName.equals("Inventory.BIN_PromotionDeliver")){
            billType = CherryConstants.OS_BILLTYPE_SD;
        }else if(tableName.equals("Inventory.BIN_PromotionAllocation")){
            billType = CherryConstants.OS_BILLTYPE_BG;
        }else if(tableName.equals("Inventory.BIN_PromotionShift")){
            billType = CherryConstants.OS_BILLTYPE_MV;
        }else if(tableName.equals("Inventory.BIN_ProductShift")){
            billType = CherryConstants.OS_BILLTYPE_MV;
        }else if(tableName.equals("Inventory.BIN_OutboundFree")){
            billType = CherryConstants.OS_BILLTYPE_LS;
        }else if(tableName.equals("Inventory.BIN_ProductInDepot")){
            billType = CherryConstants.OS_BILLTYPE_GR;
        }else if(tableName.equals("Inventory.BIN_ProductOrder")){
            billType = CherryConstants.OS_BILLTYPE_OD;
        }else if(tableName.equals("Inventory.BIN_ProductStockTaking")){
            billType = CherryConstants.OS_BILLTYPE_CA;
        }else if(tableName.equals("Inventory.BIN_ProductDeliver")){
            billType = CherryConstants.OS_BILLTYPE_SD;
        }else if(tableName.equals("Inventory.BIN_ProductReturn")){
            billType = CherryConstants.OS_BILLTYPE_RR;
        }else if(tableName.equals("Inventory.BIN_ProReturnRequest")){
            billType = CherryConstants.OS_BILLTYPE_RA;
        }else if(tableName.equals("Inventory.BIN_ProStocktakeRequest")){
            billType = CherryConstants.OS_BILLTYPE_CR;
        }else if(tableName.equals("Inventory.BIN_ProductAllocation")){
            billType = CherryConstants.OS_BILLTYPE_BG;
        }else if(tableName.equals("Inventory.BIN_PrmInDepot")){
            billType = CherryConstants.OS_BILLTYPE_GR;
        }else if(tableName.equals("Sale.BIN_BackstageSale")){
            billType = CherryConstants.OS_BILLTYPE_NS;
        }else if(tableName.equals("Sale.BIN_SaleReturnRequest")){
            billType = CherryConstants.OS_BILLTYPE_SA;
        }
        return billType;
    }
    
    /**
     * 取得柜台详细信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getCounterInfo(Map<String, Object> map) {
        // 取得柜台详细信息
        Map<String, Object> counterInfo = binolcm19_Service.getCounterInfo(map);
        return counterInfo;
    }
    

    /**
     * 取得部门详细信息
     * 
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> getOrganizationInfo(Map<String, Object> map) {
        // 取得部门详细信息
        Map<String, Object> counterInfo = binolcm19_Service.getOrganizationInfo(map);
        return counterInfo;
    }
    
    /**
     * 把岗位ID、部门ID转成具体的人
     * @param param
     * @return List<String>
     */
    @Override
    public List<String> getPersonList(Map<String,Object> param){
        String createdEmployeeID = ConvertUtil.getString(param.get("OS_BillCreator"));
        String createdDepartID = ConvertUtil.getString(param.get("OS_BillCreator_Depart"));
        // 接收部门ID
        String receiveDepartID = ConvertUtil.getString(param.get("OS_BillReceiver_Depart"));
        List<String> personList = new ArrayList<String>();
        List<Map<String,Object>> participantList = (List<Map<String, Object>>) param.get("participantList");
        if(null == participantList){
            return personList;
        }
        List<Map<String,Object>> bossList = new ArrayList<Map<String,Object>>();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        for(int i=0;i<participantList.size();i++){
            Map<String,Object> participantMap = participantList.get(i);
            String positionID = ConvertUtil.getString(participantMap.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
            String departID = ConvertUtil.getString(participantMap.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
            if(!"".equals(positionID)){
                String privilegeFlag = ConvertUtil.getString(participantMap.get("privilegeFlag"));
                String privilegeRecFlag = ConvertUtil.getString(participantMap.get("privilegeRecFlag"));
                if("".equals(privilegeFlag)){
                    //简单模式
                    paramMap.put("BIN_PositionCategoryID", positionID);
                    bossList = binolcm19_Service.getEmployeeInPositionCategoryList(paramMap);
                }else{
                	if("".equals(privilegeRecFlag)) {
	                    //复杂模式
	                    String roleTypeCreater = ConvertUtil.getString(participantMap.get("RoleTypeCreater"));
	                    if(roleTypeCreater.equals("DT")){
	                        bossList = findBossByOrganizationID(createdDepartID,positionID,privilegeFlag);
	                    }else{
	                        bossList = findBossByPosition(createdEmployeeID,positionID,privilegeFlag);
	                    }
                	} else {
                		// 代收模式
	                    bossList = findBossByOrganizationID(receiveDepartID,positionID,privilegeFlag);
                	}
                }
            }else if(!"".equals(departID)){
                Map<String,Object> departParam = new HashMap<String,Object>();
                departParam.put("BIN_OrganizationID", departID);
                bossList = binolcm19_Service.getEmployeeInDepartList(departParam);
            }
            if(null != bossList && bossList.size()>0){
                for(int j=0;j<bossList.size();j++){
                    String employeeCodeName = ConvertUtil.getString(bossList.get(j).get("EmployeeCodeName"));
                    if(!personList.contains(employeeCodeName)){
                        personList.add(employeeCodeName);
                    }
                }
            }
        }
        return personList;
    }
    
//    @Override
//    @Deprecated
//    public CherryTaskInstance getTaskInfo(CherryTaskInstance taskinstance) {
//        
//      if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(taskinstance.getProType())){
//          //如果是促销品
//          Map<String, Object> pramMap =  new HashMap<String, Object>();
//          pramMap.put("language", taskinstance.getLanguage());
//          if(CherryConstants.OS_BILLTYPE_SD.equals(taskinstance.getBillType())){
//              //如果是发货业务
//              pramMap.put("BIN_PromotionDeliverID", taskinstance.getBillID());
//              Map<String, Object> ret = binolsscm04_bl.getPromotionDeliverMain(pramMap);
//              if(ret!=null){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeCodeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("DeliverReceiveNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartReceiveCodeName")));
//              }
//          }else if(CherryConstants.OS_BILLTYPE_BG.equals(taskinstance.getBillType())){
//              //如果是调拨业务
//              pramMap.put("BIN_PromotionAllocationID", taskinstance.getBillID());
//              Map<String, Object> ret = binolsscm03_bl.getPromotionAllocationMain(pramMap);
//              if(ret!=null){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeCodeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("AllocationNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartReceiveCodeName")));
//              }
//          }else if(CherryConstants.OS_BILLTYPE_MV.equals(taskinstance.getBillType())){
//              //如果是移库业务
//                Map<String, Object> ret = binOLSSCM08_BL.getPrmShiftMainData(CherryUtil.obj2int(taskinstance.getBillID()),taskinstance.getLanguage());
//                if(ret!=null){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeCodeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("BillNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//                }
//          }
//      }else if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(taskinstance.getProType())){
//          //如果是产品
//          if(CherryConstants.OS_BILLTYPE_OD.equals(taskinstance.getBillType())){
//              //如果是订货业务
//              //pramMap.put("BIN_ProductOrderID", taskinstance.getBillID());
//              Map<String, Object> ret = binOLSTCM02_BL.getProductOrderMainData(CherryUtil.string2int(taskinstance.getBillID()),taskinstance.getLanguage());
//              if(null != ret){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("OrderNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//              }
//          }else if(CherryConstants.OS_BILLTYPE_SD.equals(taskinstance.getBillType())){
//              //如果是发货业务
//              Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(CherryUtil.string2int(taskinstance.getBillID()),taskinstance.getLanguage());
//              if(null != ret){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("DeliverNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//              }
//          }else if(CherryConstants.OS_BILLTYPE_LS.equals(taskinstance.getBillType())){
//              //如果是报损业务
//              Map<String, Object> ret = binOLSTCM05_BL.getOutboundFreeMainData(CherryUtil.string2int(taskinstance.getBillID()),taskinstance.getLanguage());
//              if(null != ret){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("BillNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//              }
//          }else if(CherryConstants.OS_BILLTYPE_CA.equals(taskinstance.getBillType())){
//                //如果是盘点业务
//                Map<String, Object> ret = binOLSTCM06_BL.getStockTakingMainData(CherryUtil.string2int(taskinstance.getBillID()),taskinstance.getLanguage());
//                if(null != ret){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("StockTakingNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//                }
//            }else if(CherryConstants.OS_BILLTYPE_GR.equals(taskinstance.getBillType())){
//              //如果是入库业务
//              Map<String, Object> ret = binOLSTCM08_BL.getProductInDepotMainData(CherryUtil.string2int(taskinstance.getBillID()), taskinstance.getLanguage());
//              if(null != ret){
//                   taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                     taskinstance.setBillCode(ConvertUtil.getString(ret.get("BillNoIF")));
//                     taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//              }
//            }else if(CherryConstants.OS_BILLTYPE_MV.equals(taskinstance.getBillType())){
//              //如果是移库业务
//              Map<String, Object> ret = binOLSTCM04_BL.getProductShiftMainData(CherryUtil.string2int(taskinstance.getBillID()));
//              if(null != ret){
//                  taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                  taskinstance.setBillCode(ConvertUtil.getString(ret.get("BillNoIF")));
//                  taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//              }
//            }else if(CherryConstants.OS_BILLTYPE_RR.equals(taskinstance.getBillType())){
//                //如果是退库业务
//                Map<String, Object> ret = binOLSTCM09_BL.getProductReturnMainData(CherryUtil.string2int(taskinstance.getBillID()), taskinstance.getLanguage());
//                if(null != ret){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("ReturnNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//                }
//            }else if(CherryConstants.OS_BILLTYPE_RA.equals(taskinstance.getBillType())){
//                //如果是退库申请业务
//                Map<String, Object> ret = binOLSTCM13_BL.getProReturnRequestMainData(CherryUtil.string2int(taskinstance.getBillID()), taskinstance.getLanguage());
//                if(null != ret){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("BillNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//                }
//            }else if(CherryConstants.OS_BILLTYPE_CR.equals(taskinstance.getBillType())){
//                //如果是盘点申请业务
//                Map<String, Object> ret = binOLSTCM14_BL.getProStocktakeRequestMainData(CherryUtil.string2int(taskinstance.getBillID()), taskinstance.getLanguage());
//                if(null != ret){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("StockTakingNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeName")));
//                }
//            }else if(CherryConstants.OS_BILLTYPE_BG.equals(taskinstance.getBillType())){
//                //如果是调拨业务
//                Map<String, Object> ret = binOLSTCM16_BL.getProductAllocationMainData(CherryUtil.string2int(taskinstance.getBillID()), taskinstance.getLanguage());
//                if(null != ret){
//                    taskinstance.setEmployeeCodeName(ConvertUtil.getString(ret.get("EmployeeName")));
//                    taskinstance.setBillCode(ConvertUtil.getString(ret.get("AllocationNoIF")));
//                    taskinstance.setDepartCodeName(ConvertUtil.getString(ret.get("DepartCodeNameIn")));
//                }
//            }
//      }
//        return taskinstance;
//    }
    
//    @Override
//    public Object getPropertySetValue(long osID, String key, String type) {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
//    /**
//     * 匹配审核规则，通过返回true（即需要审核），不通过返回false(不需要审核)
//     */
//    @Override
//    @Deprecated
//    public boolean matchingAuditRule(Map<String, String> map,Map<String,Object> paramData) throws Exception {
//        boolean ret = false;
//      try {
//          String userID = map.get(CherryConstants.OS_ACTOR_TYPE_USER);
//          String positionID = map.get(CherryConstants.OS_ACTOR_TYPE_POSITION);
//          String departID = map.get(CherryConstants.OS_ACTOR_TYPE_DEPART);
//          String ruleStr = map.get(CherryConstants.OS_META_Rule);
//          String employeeID = map.get(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE);
//          if (ruleStr == null || "".equals(ruleStr)) {
//              // 没有配置规则，无需审核
//              return false;
//          }
//          Map ruleMap = (Map) JSONUtil.deserialize(ruleStr);
//          String thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
//          if("1".equals(thirdPartyFlag)){
//              //如果是第三方完成，则强制设为需要审核
//              return true;
//          }
//          
//          List ruleList =  (List)ruleMap.get("RuleContext");
//          
//          //String sortNo ="";        
//          for(int i=0;i<ruleList.size();i++){
//              Map tmpMap = (Map)ruleList.get(i);
//              //sortNo = (String)tmpMap.get("SortNo");
//              if(isNeedAudit(userID,positionID,departID,employeeID,tmpMap,paramData))
//              {
//                  return true;
//              }
//          }
//
//      } catch (Exception e) {
//          throw new Exception("ECM00048");
//      }
//
//        // TODO Auto-generated method stub
//        return ret;
//    }
    
//    /**
//     * 取得是否需要审核
//     * @param userID
//     * @param positionID
//     * @param departID
//     * @param employeeID
//     * @param praMap
//     * @param paramData
//     * @return
//     */
//    @Deprecated
//    private boolean isNeedAudit(String userID,String positionID ,String departID,String employeeID,Map praMap,Map<String,Object> paramData){        
//        String creatType = String.valueOf(praMap.get("RoleTypeCreater"));
//        String creatValue =String.valueOf(praMap.get("RoleValueCreater"));
//        String auditType = String.valueOf(praMap.get("RoleTypeAuditor"));
//        String auditValue = String.valueOf(praMap.get("RoleValueAuditor"));
//        String privilegeFlag = ConvertUtil.getString(praMap.get("RolePrivilegeFlag"));
//        //当发起者类型=审核者类型，发起者=审核者，不需要审核
//        if(creatType.equals(auditType)){
//            if(creatValue.equals(auditValue)){
//                return false;
//            }
//        }
//        //按审核者类型判断，当对应类型的发起者=审核者，不需要审核
//        if("U".equals(auditType)){
//            if(userID.equals(auditValue)){
//                return false;
//            }
//        }else if("P".equals(auditType)){
//            if(positionID.equals(auditValue)){
//                return false;
//            }
//        }else if("D".equals(auditType)){
//            if(departID.equals(auditValue)){
//                return false;
//            }
//        }
//        //简单的判定，包括了业务规则
//        //
//        if("U".equals(creatType)){//如果规则设定：   发起者为用户            
//            if(userID.equals(creatValue)||"ALL".equals(creatValue)){
//                if("U".equals(auditType)){
//                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核                    
//                    return !userID.equals(auditValue);                  
//                }else if("P".equals(auditType)){
//                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
//                    return hasBossByPosition(employeeID,auditValue,privilegeFlag,paramData);
//                }else if("D".equals(auditType)){
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    return !departID.equals(auditValue);
//                }               
//            }
//        }else if("P".equals(creatType)||"ALL".equals(creatValue)){//如果规则设定：   发起者为岗位            
//            if(positionID.equals(creatValue)){
//                if("U".equals(auditType)){
//                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核                    
//                    return !userID.equals(auditValue);                  
//                }else if("P".equals(auditType)){
//                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
//                    return hasBossByPosition(employeeID,auditValue,privilegeFlag,paramData);
//                }else if("D".equals(auditType)){
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    return !departID.equals(auditValue);
//                }
//            }
//        }else if("D".equals(creatType)||"ALL".equals(creatValue)){//如果规则设定：  发起者为部门         
//            //如果创建者的部门是 规则中的发起部门
//            if(departID.equals(creatValue)){                
//                if("U".equals(auditType)){
//                    //如果审核者的类型为用户，且审核者为创建者，那么不用审核,否则需要审核                    
//                    return !userID.equals(auditValue);                  
//                }else if("P".equals(auditType)){
//                    //如果审核者的类型为岗位，则查看发起者是否有该岗位的上司，如果有则需要审核，否则不需要审核
//                    return hasBossByPosition(employeeID,auditValue,privilegeFlag,paramData);
//                }else if("D".equals(auditType)){
//                    //如果审核者的类型为部门，且审核者为创建者所属的部门，那么不用审核,否则需要审核
//                    return !departID.equals(auditValue);
//                }               
//            }
//        }       
//        return false;
//    }
    
//  /**
//  * 判断用户是否符合规则限制，如果符合
//  * @param userID
//  * @param positionID
//  * @param departID
//  * @param rule
//  * @param cherryTaskInstance
//  * @return
//  * @throws JSONException 
//  */
// @Deprecated
// private boolean canDoAction(UserInfo userinfo,PropertySet ps,String rule,CherryTaskInstance cherryTaskInstance,Map<String,Object> paramData) throws JSONException{
//     
//     boolean nullRule = false;
//     if(rule==null||"".equals(rule)||"null".equals(rule)){
//         nullRule =true;
//     }
//     
//     Map ruleMap = null;
//     String thirdPartyFlag = "";
//     String ruleType = "";
//     String ruleContext = "";
//     if(!nullRule){
//         ruleMap =(Map) JSONUtil.deserialize(rule);
//         thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
//         ruleType = String.valueOf(ruleMap.get("RuleType"));
//         ruleContext = String.valueOf(ruleMap.get("RuleContext"));
//     }
//     if("1".equals(thirdPartyFlag)){
//         //如果是第三方完成，则本系统中不能操作
//         return false;
//     }
//     
//     if(null==ruleContext||"null".equals(ruleContext)||"".equals(ruleContext)||"[]".equals(ruleContext)){
//         nullRule =true;
//     }
//     
//     //如果规则为空，则表示没有限制，可以执行（除了某些特殊的业务类型有默认的限制）
//     if(nullRule){
//         String currOperate = cherryTaskInstance.getCurrentOperate();
//         String proType = cherryTaskInstance.getProType();
//         //产品的业务
//         if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
//             
//             Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(ps.getInt("BIN_ProductDeliverID"),userinfo.getLanguage());
//             //发货和收货部门ID
//             String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
//             String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
//             //如果是收货
//             if(currOperate.equals(CherryConstants.OPERATE_RD)){
//                 //如果用户的部门ID是收货部门的ID，则可以进行操作,否则不能                
//                 return orgIDReceive.equals(String.valueOf(userinfo.getBIN_OrganizationID()));                   
//             }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
//                 //如果是发货
//                 //如果用户的部门ID是发货部门的ID，则可以进行操作
//                 return orgIDSend.equals(String.valueOf(userinfo.getBIN_OrganizationID()));
//             }           
//         }else if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
//             //促销品业务
//             if(currOperate.equals(CherryConstants.OPERATE_RD)){
//                 //如果是收货
//                 Map<String,Object> pramMap = new HashMap<String,Object>();
//                 pramMap.put("BIN_PromotionDeliverID", ps.getInt("BIN_PromotionDeliverID"));
//                 pramMap.put("language", userinfo.getLanguage());
//                 Map<String, Object> ret = binolsscm04_bl.getPromotionDeliverMain(pramMap);
//                 //收货部门ID
//                 String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
//                 //如果用户的部门ID是收货部门的ID，则可以进行操作
//                 return orgIDReceive.equals(String.valueOf(userinfo.getBIN_OrganizationID()));                   
//             }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
//                 //如果是发货
//                 Map<String,Object> pramMap = new HashMap<String,Object>();
//                 pramMap.put("BIN_PromotionDeliverID", ps.getInt("BIN_PromotionDeliverID"));
//                 pramMap.put("language", userinfo.getLanguage());
//                 Map<String, Object> ret = binolsscm04_bl.getPromotionDeliverMain(pramMap);
//                 //发货部门ID
//                 String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
//                 //如果用户的部门ID是发货部门的ID，则可以进行操作
//                 return orgIDSend.equals(String.valueOf(userinfo.getBIN_OrganizationID()));                  
//             }else if(currOperate.equals(CherryConstants.OPERATE_LG)){
//                 //如果是调出确认
//                 //取调出部门
//                 return ps.getInt("BIN_OrganizationIDAccept")==userinfo.getBIN_OrganizationID();
//             }
//         }
//         return true;
//     }
//     //以下为规则存在时的处理
//
//     String creatU = cherryTaskInstance.getBillCreator_User();
//     String creatP = cherryTaskInstance.getBillCreator_Position();
//     String creatD = cherryTaskInstance.getBillCreator_Depart();
//     String actorU = String.valueOf(userinfo.getBIN_UserID());
//     String actorP = String.valueOf(userinfo.getBIN_PositionCategoryID());
//     String actorD = String.valueOf(userinfo.getBIN_OrganizationID());
//     String creatE = cherryTaskInstance.getBillCreator();
// 
//     
//     List ruleList =  (List)ruleMap.get("RuleContext");
//     
//     //如果规则的类型为2（简单类型，只限定任务的参与者）
//     if(CherryConstants.OS_RULETYPE_EASY.equals(ruleType)){
//         String actorType;
//         String actorValue;
//         for(int i=0;i<ruleList.size();i++){
//             Map tmpMap = (Map)ruleList.get(i);
//             actorType = String.valueOf(tmpMap.get("ActorType"));
//             actorValue = String.valueOf(tmpMap.get("ActorValue"));
//             //判断规则是否有效（有时候虽然设置了规则，但是违背了某些既定的业务逻辑，会无效）
//             if(isEfficientTypeOne(creatU,creatP,creatD,actorType,actorValue))
//             {
//                 //规则有效，就判断当前登录者是否符合该规则
//                 if(isMachingRuleTypeOne(actorU,actorP,actorD,actorType,actorValue)){
//                     return true;
//                 }
//                 //return false;
//             }else{
//                 //TODO:规则无效时，不用处理
//             }
//         }
//     }else if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
//         //如果规则类型为3(复杂类型，审核审批配置)
//         String creatType;
//         String creatValue;
//         String auditType;
//         String auditValue;
//         String privilegeFlag;
//         for(int i=0;i<ruleList.size();i++){
//             Map tmpMap = (Map)ruleList.get(i);
//              creatType = String.valueOf(tmpMap.get("RoleTypeCreater"));
//              creatValue = String.valueOf(tmpMap.get("RoleValueCreater"));
//              auditType = String.valueOf(tmpMap.get("RoleTypeAuditor"));
//              auditValue = String.valueOf(tmpMap.get("RoleValueAuditor"));
//              privilegeFlag = ConvertUtil.getString(tmpMap.get("RolePrivilegeFlag"));
//             //判断规则是否有效
//             if(isEfficientTypeTwo(creatU,creatP,creatD,creatType,creatValue,auditType,auditValue,creatE,privilegeFlag,paramData))
//             {
//                 //规则有效，就判断当前登录者是否符合该规则
//                 if(isMachingRuleTypeTwo(actorU,actorP,actorD,creatU,creatP,creatD,creatE,auditType,auditValue,privilegeFlag,paramData)){
//                     return true;
//                 }
//                 return false;
//             }else{
//                 //TODO:规则无效时，不用处理
//             }
//         }
//     }
//     return false;
// }
    
//  /**
//  * 判断简单规则（RuleType=2）是否生效
//  * @param creatU
//  * @param creatP
//  * @param creatD
//  * @param actorType
//  * @param actorValue
//  * @return
//  */
// @Deprecated
// private boolean isEfficientTypeOne(String creatU,String creatP,String creatD,String actorType,String actorValue){
//     //目前认为，只要配置了规则就都是有效的
//     //以后可以在这里扩充业务逻辑
//     return true;
// }
    
//  /**
//  * 判断复杂规则是否有效（RuleType=3）
//  * @param creatU 实例创建者的用户ID
//  * @param creatP 实例创建者的岗位类型ID
//  * @param creatD 实例创建者的所属部门ID
//  * @param ruleCType 规则中的发起者类型
//  * @param ruleCValue 规则中的发起者ID
//  * @param rulePType 规则中的参与者类型
//  * @param rulePValue 规则中的参与者ID
//  * @param creatE 实例创建者的员工ID
//  * @param privilegeFlag 权限范围
//  * @return 有效：true 无效：false
//  */
// @Deprecated
// private boolean isEfficientTypeTwo(String creatU,String creatP,String creatD,String ruleCType,String ruleCValue,String rulePType,String rulePValue,String creatE,String privilegeFlag,Map<String,Object> paramData){
//     //如果规则设定：   发起者的类型为为用户  
//     if("U".equals(ruleCType)){
//         //实例创建者是规则中的发起者
//         if(creatU.equals(ruleCValue)||"ALL".equals(ruleCValue)){
//             if("U".equals(rulePType)){
//                 //如果规则中参与者的类型为用户，且参与者为创建者，那么该规则无效，否则有效              
//                 return !creatU.equals(rulePValue);                  
//             }else if("P".equals(rulePType)){
//                 //如果规则中参与者的类型为岗位，则查看创建者是否有该岗位的上司，如果有则有效，否则无效
//                 return hasBossByPosition(creatE,rulePValue,privilegeFlag,paramData);
//             }else if("D".equals(rulePType)){
//                 //如果规则中参与者的类型为部门，且参与者为创建者所属的部门，那么该规则无效，否则有效
//                 return !creatD.equals(rulePValue);
//             }               
//         }else{
//             //实例创建者不是规则中的发起者，该条规则无效
//             return false;
//         }
//     }else if("P".equals(ruleCType)){
//         //如果规则设定：   发起者的类型为岗位           
//         if(creatP.equals(ruleCValue)||"ALL".equals(ruleCValue)){
//             if("U".equals(rulePType)){
//                 //如果规则中参与者的类型为用户，且参与者为创建者，那么该规则无效，否则有效                  
//                 return !creatU.equals(rulePValue);              
//             }else if("P".equals(rulePType)){
//                 //如果规则中参与者的类型为岗位，则查看创建者是否有该岗位的上司，如果有则有效，否则无效
//                 return hasBossByPosition(creatE,rulePValue,privilegeFlag,paramData);
//             }else if("D".equals(rulePType)){
//                 //如果规则中参与者的类型为部门，且参与者为创建者所属的部门，那么该规则无效，否则有效
//                 return !creatD.equals(rulePValue);
//             }
//         }
//     }else if("D".equals(ruleCType)){
//         //如果规则设定：  发起者为部门           
//         //如果创建者的部门是 规则中的发起部门
//         if(creatD.equals(ruleCValue)||"ALL".equals(ruleCValue)){                
//             if("U".equals(rulePType)){
//                 //如果规则中参与者的类型为用户，且参与者为创建者，那么该规则无效，否则有效                  
//                 return !creatU.equals(rulePValue);                  
//             }else if("P".equals(rulePType)){
//                 //如果规则中参与者的类型为岗位，则查看创建者是否有该岗位的上司，如果有则有效，否则无效
//                 return hasBossByPosition(creatE,rulePValue,privilegeFlag,paramData);
//             }else if("D".equals(rulePType)){
//                 //如果规则中参与者的类型为部门，且参与者为创建者所属的部门，那么该规则无效，否则有效
//                 return !creatD.equals(rulePValue);
//             }               
//         }
//     }       
//     return false;
// }
    
//  /**
//  * 判断当前登录者是否满足复杂规则(RuleType=3)
//  * @param actorU
//  * @param actorP
//  * @param actorD
//  * @param creatU
//  * @param creatP
//  * @param creatD
//  * @param creatE
//  * @param auditType
//  * @param auditValue
//  * @param privilegeFlag
//  * @return
//  */
// @Deprecated
// private boolean isMachingRuleTypeTwo(String actorU,String actorP,String actorD,String creatU,String creatP,String creatD,String creatE,String auditType,String auditValue,String privilegeFlag,Map<String,Object> paramData){
//     if("U".equals(auditType)){
//         return actorU.equals(auditValue);
//     }else if("P".equals(auditType)){
//         if(actorP.equals(auditValue)){
//             //判断登录者是否是创建者的上级
//             return hasChildUser(actorU,creatE,privilegeFlag,paramData);
//         }else{
//             return false;
//         }
//     }else if("D".equals(auditType)){
//         return actorD.equals(auditValue);
//     }
//     return false;
// }
    
//  /**
//  * 查询employeeID对应的员工有没有岗位为positionID的上司
//  * @param employeeID
//  * @param positionID
//  * @param privilegeFlag
//  * @return
//  */
// @Deprecated
// private boolean hasBossByPosition(String employeeID,String positionID,String privilegeFlag,Map<String,Object> paramData){
//     Map<String, Object> map = new HashMap<String, Object>();
//     map.put("BIN_EmployeeID", employeeID);
//     map.put("BIN_PositionCategoryID", positionID);
//       if("".equals(privilegeFlag)){
//           //privilegeFlag没有设置时，权限范围为管辖
//           map.put("PrivilegeFlag", CherryConstants.OS_PRIVILEGEFLAG_FOLLOW);
//       }else if(!CherryConstants.OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)){
//           map.put("PrivilegeFlag", privilegeFlag);
//       }
//       List<Map<String,Object>> employeePrivilegeList = new ArrayList<Map<String,Object>>();
//       if(null == paramData.get("bossList")){
//           List<Map<String,Object>> bossList = binolcm19_Service.getBossByEmployeeID(map);
//           paramData.put("bossList", bossList);
//       }
//       employeePrivilegeList = (List)paramData.get("bossList");
//       privilegeFlag = ConvertUtil.getString(map.get("PrivilegeFlag"));
//       if(null != employeePrivilegeList && !employeePrivilegeList.isEmpty()){
//           for(int i=0;i<employeePrivilegeList.size();i++){
//               String curSubEmployeeID = ConvertUtil.getString(employeePrivilegeList.get(i).get("BIN_SubEmployeeID"));
//               String currivilegeFlag = ConvertUtil.getString(employeePrivilegeList.get(i).get("PrivilegeFlag"));
//               if("".equals(privilegeFlag) && employeeID.equals(curSubEmployeeID)){
//                   return true;
//               }else if(employeeID.equals(curSubEmployeeID) && privilegeFlag.equals(currivilegeFlag)){
//                   return true;
//               }
//           }
//       }
//     return false;
// }
}
