package com.cherry.cm.cmbussiness.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.cherry.st.common.service.BINOLSTCM21_Service;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;


/**
 * 工作流操作共通类
 * @author dingyongchang
 *
 */
public class WorkFlowCommon_FN implements FunctionProvider{

	@Resource(name="workflow")
	private Workflow workflow;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binOLSSCM04_BL;
	
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_IF binOLSSCM09_BL;
	
    @Resource(name="binOLSTCM08_BL")
    private BINOLSTCM08_IF binOLSTCM08_BL;
	
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;
	
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLSTCM21_BL")
    private BINOLSTCM21_IF binOLSTCM21_BL;
    
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("auditHandle".equals(method)){
			auditHandle( arg0,  arg1,  propertyset);
		}else if("updateOD".equals(method)){
		    updateOD(arg0,  arg1,  propertyset);
		}else if("updateSD".equals(method)){
		    updateSD(arg0,  arg1,  propertyset);
        }else if("doActionHandle".equals(method)){
            doActionHandle(arg0,  arg1,  propertyset);
        }else if("setPSParticipant".equals(method)){
            setPSParticipant(arg0,  arg1,  propertyset);
        }else if("setParticipant".equals(method)){
            setParticipant(arg0,  arg1,  propertyset);
        }else if("deleteUserTask".equals(method)){
            deleteUserTask(arg0,  arg1,  propertyset);
        }
	}

	/**
	 * 将业务中的基本信息写入到工作流的的PropertySet表中
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		//产品，促销品区分
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
		//单据类型
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
		//单据ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		//单据生成者的员工ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));		
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
		//单据生成者的所属部门名称
//		propertyset.setString(CherryConstants.OS_PSKEY_DEPART_NAME, String.valueOf(mainData.get(CherryConstants.OS_PSKEY_DEPART_NAME)));
//		//单据生成者的名字
//		propertyset.setString(CherryConstants.OS_PSKEY_CREATOR_NAME, String.valueOf(mainData.get(CherryConstants.OS_PSKEY_CREATOR_NAME)));
//		//单据编号
//		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCODE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCODE)));
		
		//同一个业务中可能产生多张单据，单据ID要再保存一次
		propertyset.setInt(String.valueOf(mainData.get("BillIDKey")),Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
	}

	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 * @throws InvalidInputException 
	 * @throws Exception 
	 */
	private void auditHandle(Map arg0, Map arg1, PropertySet propertyset) throws InvalidInputException, WorkflowException {	
	    IBatisPropertySet ips = (IBatisPropertySet) propertyset;
	    Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
		//当前的工作流实例
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
		long osid = entry.getId();
		//当前创建的步骤，工作流文件中要注意
		//List list = (List)arg0.get("createdStep");
		SimpleStep step = (SimpleStep)arg0.get("createdStep");
		//取得当前step的描述信息
		WorkflowDescriptor descriptor = (WorkflowDescriptor)arg0.get("descriptor");
		StepDescriptor sd = descriptor.getStep(step.getStepId());
		//取得当前step下的meta元素集合
		Map metaMap = sd.getMetaAttributes();
		String cherryRule ="";
		if(metaMap.containsKey(CherryConstants.OS_META_Rule)){
		 cherryRule = (String)metaMap.get(CherryConstants.OS_META_Rule);
		}
		Map<String,String> pramData =  new HashMap<String,String>();
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_USER)));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION)));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART)));
		pramData.put(CherryConstants.OS_META_Rule, cherryRule);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
				
		//boolean ret;
		String auditor = "";
		try {
            String osCurrentOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
            auditor = binOLCM19_BL.findMatchingAuditor(pramData);
            if(!"".equals(auditor)){
                //在工作流进行的过程中，如果某一步需要有人工参与，则将参与者取出放在PS表中。登录时获取任务的速度就会加快；
                String privilegeFlag = "";
                int privilegeFlagIndex = auditor.indexOf(CherryConstants.SESSION_PRIVILEGE_FLAG);
                if(privilegeFlagIndex > -1){
                    //岗位时写入权限标志
                    privilegeFlag = auditor.substring(privilegeFlagIndex+CherryConstants.SESSION_PRIVILEGE_FLAG.length());
                    propertyset.setString(CherryConstants.SESSION_PRIVILEGE_FLAG+osCurrentOperate, privilegeFlag);
                    auditor = auditor.substring(0, privilegeFlagIndex-1);
                }
                propertyset.setString(CherryConstants.OS_ACTOR+osCurrentOperate, auditor);
            }
		} catch (Exception e) {
			throw new WorkflowException(e.getMessage());
		}
		//如果不需要审核，则调用一个配置了OS_DefaultAction的action，调用doAction
		if("".equals(auditor)){
		    //判断是否允许自动审核通过（为了不影响以前没有AutoAuditFlag的工作流，autoAuditFlag必须不为空才有效，否则仍然自动审核通过）
	        String autoAuditFlag = "";
	        try {
	            Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(cherryRule);
	            autoAuditFlag = ConvertUtil.getString(ruleMap.get("AutoAuditFlag"));
	            if(autoAuditFlag.equals("false")){
	                return;
	            }
	        } catch (JSONException e) {
	            
	        }
		    
			//取出当前有效的action
			int[] actionArr = workflow.getAvailableActions(osid, null);
			WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osid));
			if (actionArr != null && actionArr.length > 0) {
				ActionDescriptor[] actionDsArr = new ActionDescriptor[actionArr.length];
				Map metaMapTMp = null;
				for (int j = 0; j < actionArr.length; j++) {
					ActionDescriptor ad = wd.getAction(actionArr[j]);
					metaMapTMp = ad.getMetaAttributes();
					//找到带有OS_DefaultAction元素的action
					if(metaMapTMp.containsKey("OS_DefaultAction")){
						Map<String, Object> input = new HashMap<String, Object>();
						input.put("mainData", arg0.get("mainData"));
						workflow.doAction_single(osid, ad.getId(), input);
						break;
			     	}
				}
			}
		}
	}
	
	/**
	 * 更新订单状态
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void updateOD(Map arg0, Map arg1, PropertySet propertyset){
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag"));
        
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //订发收单主表ID      
        pramData.put("BIN_ProductOrderID", propertyset.getInt("BIN_ProductOrderID"));
        //订单审核区分
        if(!"".equals(verifiedFlag)){
            pramData.put("VerifiedFlag", verifiedFlag);
            if(CherryConstants.ODAUDIT_FLAG_AGREE.equals(verifiedFlag) 
                    || CherryConstants.ODAUDIT_FLAG_AGREE1.equals(verifiedFlag) 
                    || CherryConstants.ODAUDIT_FLAG_DISAGREE1.equals(verifiedFlag) 
                    || CherryConstants.ODAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)
                    || CherryConstants.ODAUDIT_FLAG_AGREE_11.equals(verifiedFlag)
                    || CherryConstants.ODAUDIT_FLAG_AGREE3.equals(verifiedFlag)
                    || CherryConstants.ODAUDIT_FLAG_DISAGREE3.equals(verifiedFlag)
                    || CherryConstants.ODAUDIT_FLAG_AGREE4.equals(verifiedFlag)
                    || CherryConstants.ODAUDIT_FLAG_DISAGREE4.equals(verifiedFlag)
                    || "true".equals(mainData.get("UpdateAudit"))){
                pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
                pramData.put("AuditDate", binOLCM00_BL.getDateYMD()); 
            }
            //一审通过后写入订单编辑者
            if(CherryConstants.ODAUDIT_FLAG_AGREE1.equals(verifiedFlag)){
                propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_OD_EDIT,CherryConstants.OS_ACTOR_TYPE_USER+ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            }
        }
        //订单处理状态
        if(!"".equals(tradeStatus)){
            pramData.put("TradeStatus", tradeStatus);
        }
        //同步数据标志
        if(!"".equals(synchFlag)){
            pramData.put("SynchFlag", synchFlag);
        }
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，订发收单主表中是没有实例ID的，这一步写入；
        pramData.put("WorkFlowID", entry.getId());
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
	}
	
	/**
     * 更新发货单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateSD(Map arg0, Map arg1, PropertySet propertyset){
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        int productDeliverID = propertyset.getInt("BIN_ProductDeliverID");
        pramData.put("BIN_ProductDeliverID", productDeliverID);
        if(!"".equals("verifiedFlag")){
            pramData.put("VerifiedFlag", verifiedFlag);
            //判断原审核状态为发货单二审中且更新后状态为审核中（二审退回）
            boolean flagDisagree2 = false;
            Map<String,Object> mainDataSD = binOLSTCM03_BL.getProductDeliverMainData(productDeliverID, null);
            if(ConvertUtil.getString(mainDataSD.get("VerifiedFlag")).equals(CherryConstants.SDAUDIT_FLAG_SUBMIT2) && verifiedFlag.equals(CherryConstants.AUDIT_FLAG_SUBMIT)){
                flagDisagree2 = true;
            }
            if (CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)
                    || CherryConstants.AUDIT_FLAG_DISAGREE.equals(verifiedFlag)
                    || CherryConstants.SDAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)
                    || CherryConstants.SDAUDIT_FLAG_AGREE2.equals(verifiedFlag)
                    || CherryConstants.SDAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)
                    || flagDisagree2) {
                pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
                pramData.put("AuditDate", binOLCM00_BL.getDateYMD());
            }
        }
        if(!"".equals(tradeStatus)){
            pramData.put("TradeStatus", tradeStatus);
            if(CherryConstants.BILLTYPE_PRO_SD_SEND.equals(tradeStatus)){
                //发货日期
                String date = ConvertUtil.getString(mainData.get("Date"));
                if("".equals(date)){
                    //自动执行发货
                    date = binOLCM00_BL.getDateYMD();
                }
                pramData.put("Date", date);
                //发货者
                String employeeID = ConvertUtil.getString(mainData.get("BIN_EmployeeID"));
                if("".equals(employeeID)){
                    //自动执行发货
                    UserInfo userInfo = (UserInfo)mainData.get("UserInfo");
                    if(null != userInfo){
                        employeeID = ConvertUtil.getString(userInfo.getBIN_EmployeeID());
                    }
                }
                pramData.put("BIN_EmployeeID", employeeID);
            }
        }
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，发货单主表中是没有实例ID的，这一步写入；
        pramData.put("WorkFlowID", entry.getId());
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM03_BL.updateProductDeliverMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 在方法中读取当前step下的<meta name="OS_Rule">元素据，
     * 如果不存在，或是存在，但是没有配置参与者，则遍历当前有效的action，
     * 找到配置了<meta name="OS_DefaultAction"></meta>的action后，调用doAction方法。
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void doActionHandle(Map arg0, Map arg1, PropertySet propertyset) throws InvalidInputException, WorkflowException{
        //当前的工作流实例
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        //当前创建的步骤，工作流文件中要注意
        SimpleStep step = (SimpleStep)arg0.get("createdStep");
        //取得当前step的描述信息
        WorkflowDescriptor descriptor = (WorkflowDescriptor)arg0.get("descriptor");
        StepDescriptor sd = descriptor.getStep(step.getStepId());
        //取出当前有效的action
        int[] actionArr = workflow.getAvailableActions(osid, null);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osid));
        //没有配置参与者标志
        boolean flag = false;
        //取得当前Step下的meta元素集合
        Map<String,Object> stepMetaMap = sd.getMetaAttributes();
        if(stepMetaMap.containsKey("OS_Rule")){
            String rule = ConvertUtil.getString(stepMetaMap.get("OS_Rule"));
            try {
                Map<String, Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(rule);
                List<Map<String,Object>> ruleList =  (List<Map<String,Object>>)ruleMap.get("RuleContext");
                if(null == ruleList || ruleList.isEmpty()){
                    flag = true;
                }
            } catch (JSONException e) {
                flag = true;
            }
        }else{
            flag = true;
        }
        if(flag){
            for (int j = 0; j < actionArr.length; j++) {
                ActionDescriptor ad = wd.getAction(actionArr[j]);
                //取得当前Action下的meta元素集合
                Map<String,Object> metaMap = ad.getMetaAttributes();
                //找到带有OS_DefaultAction元素的action
                if(metaMap.containsKey("OS_DefaultAction")){
                    Map<String, Object> input = new HashMap<String, Object>();
                    input.put("mainData", arg0.get("mainData"));
                    workflow.doAction_single(osid, ad.getId(), input);
                    break;
                }
            }
        }
    }
    
    /**
     * 把当前步骤执行者写到PS表
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void setPSParticipant(Map arg0, Map arg1, PropertySet propertyset){
        IBatisPropertySet ips = (IBatisPropertySet) propertyset;
        Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
        //当前的工作流实例
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        //当前创建的步骤，工作流文件中要注意
        SimpleStep step = (SimpleStep)arg0.get("createdStep");
        //取得当前step的描述信息
        WorkflowDescriptor descriptor = (WorkflowDescriptor)arg0.get("descriptor");
        StepDescriptor sd = descriptor.getStep(step.getStepId());
        
        Map<String,Object> metaAttr =  sd.getMetaAttributes();
        String osRule = ConvertUtil.getString(metaAttr.get("OS_Rule"));
        try {
            Map<String,Object> ruleMap = (Map<String,Object>) JSONUtil.deserialize(osRule);
            String ruleType = ConvertUtil.getString(ruleMap.get("RuleType"));
            List<Map<String,Object>> ruleContext = (List<Map<String,Object>>)ruleMap.get("RuleContext");
            String participant = "";
            StringBuffer sb = new StringBuffer();
            if(!CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
                //简单等其他模式。复杂模式的审核者在方法auditHandle()写入
                for(int i=0;i<ruleContext.size();i++){
                    String actorType = ConvertUtil.getString(ruleContext.get(i).get("ActorType"));
                    String actorValue = ConvertUtil.getString(ruleContext.get(i).get("ActorValue"));
                    if("U".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_USER).append(actorValue).append(",");
                    }else if("D".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(actorValue).append(",");
                    }else if("P".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_POSITION).append(actorValue).append(",");
                    }
                }
                //没有配置执行者
                if(ruleContext.isEmpty()){
                    String proType = ConvertUtil.getString(propertyMap.get("OS_ProType"));
                    String currOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
                    if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
                        //如果是收货
                        if(currOperate.equals(CherryConstants.OPERATE_RD)){
                            //产品的业务
                            Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(ips.getInt("BIN_ProductDeliverID"),null);
                            //发货和收货部门ID
                            String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
                            try{
                                //产品的业务
                                Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(ips.getInt("BIN_ProductDeliverID"),null);
                                //发货和收货部门ID
                                String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
//                                String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                                sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDSend).append(",");
                            }catch(Exception e){
                                String workFlowName = workflow.getWorkflowName(osid);
                                if(binOLSTCM02_BL.isProFlowOD_YT_FN(workFlowName)){
                                    //产品的业务
                                    Map<String, Object> ret = binOLSTCM02_BL.getProductOrderMainData(ips.getInt("BIN_ProductOrderID"),null);
                                    //发货和收货部门ID
                                    String orgIDSend = String.valueOf(ret.get("BIN_OrganizationIDAccept"));
//                                    String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationID"));
                                    sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDSend).append(",");
                                }
                            }
                        }else if(currOperate.equals(CherryConstants.OPERATE_SA)){
                            //产品的业务
                            Map<String, Object> ret = binOLSTCM21_BL.getSaleReturnRequestMainData(ips.getInt("BIN_SaleReturnRequestID"), null);
                            //收货部门ID
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                        }   
                    }else if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
                        //促销品业务
                        if(currOperate.equals(CherryConstants.OPERATE_RD)){
                            //如果是收货
                            Map<String,Object> pramMap = new HashMap<String,Object>();
                            pramMap.put("BIN_PromotionDeliverID", ips.getInt("BIN_PromotionDeliverID"));
                            pramMap.put("language", null);
                            Map<String, Object> ret = binOLSSCM04_BL.getPromotionDeliverMain(pramMap);
                            //收货部门ID
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
                            //如果是发货
                            Map<String,Object> pramMap = new HashMap<String,Object>();
                            pramMap.put("BIN_PromotionDeliverID", ips.getInt("BIN_PromotionDeliverID"));
                            pramMap.put("language", null);
                            Map<String, Object> ret = binOLSSCM04_BL.getPromotionDeliverMain(pramMap);
                            //发货部门ID
                            String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
                            //如果用户的部门ID是发货部门的ID，则可以进行操作
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDSend).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_LG)){
                            //如果是调出确认
                            int organizationIDAccept = ips.getInt("BIN_OrganizationIDAccept");
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationIDAccept).append(",");
                        } 
                    }
                }
            }
            participant = sb.toString();
            if(!"".equals(participant)){
                String osCurrentOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
                propertyset.setString(CherryConstants.OS_ACTOR+osCurrentOperate, participant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 取下一步审核者或执行者
     * @param arg0
     * @param arg1
     * @param propertyset
     * @return
     */
    private String getNextParticipant(Map arg0, Map arg1, PropertySet propertyset){
        IBatisPropertySet ips = (IBatisPropertySet) propertyset;
        Map<String, Object> propertyMap = (Map<String, Object>) ips.getMap (null, PropertySet.STRING);
        //当前的工作流实例
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        //当前创建的步骤，工作流文件中要注意
        SimpleStep step = (SimpleStep)arg0.get("createdStep");

        int currentStep = step.getStepId();
        //如果是退回操作需要发起人修改，参与者从dbo.OS_PROPERTYENTRY取
        //从OS_PROPERTYENTRY表取参与者标志
        boolean psFlag = false;
//        List<SimpleStep> stepList = workflow.getHistorySteps(osid);
//        for(int i=0;i<stepList.size();i++){
//            SimpleStep ss = stepList.get(i);
//            if(ss.getStepId() == currentStep){
//                psFlag = true;
//                break;
//            }
//        }
        String currentOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
        if(!psFlag){
            Map<String,Object> operateCodeMap = new HashMap<String,Object>();
            // 修改单据的操作CODE
            operateCodeMap.put(CherryConstants.OPERATE_GR_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_OT_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_GR_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_OT_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_OD_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_SD_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_RR_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_BG_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_LG_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_CA_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_MV_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_LS_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_RA_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_CR_EDIT, null);
            operateCodeMap.put(CherryConstants.OPERATE_SL_EDIT, null);
            if(operateCodeMap.containsKey(currentOperate)){
                psFlag = true;
            }            
        }
        if(psFlag){
            String participant = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_ACTOR+currentOperate));
            String psPrivilegeFlag = ConvertUtil.getString(propertyMap.get(CherryConstants.SESSION_PRIVILEGE_FLAG+currentOperate));
            StringBuffer sb = new StringBuffer();
            try{
                if(psPrivilegeFlag.length() == 1){
                    //兼容在PS表老数据
                    Map<Object,Object> privilegeFlagMap = new HashMap<Object,Object>();
                    privilegeFlagMap.put("1", psPrivilegeFlag);
                    psPrivilegeFlag = JSONUtil.serialize(privilegeFlagMap);
                }
                if(!"".equals(psPrivilegeFlag)){
                    //复杂模式
                    //审核者是岗位还是用岗位ID，不用转成用户ID
                    String[] participantArr = participant.split(",");
                    String employeeID = ConvertUtil.getString(propertyMap.get("OS_BillCreator"));
                    Map<String,Object> participantMap = (Map<String, Object>) JSONUtil.deserialize(psPrivilegeFlag);
                    for(int i=0;i<participantArr.length;i++){
                        if(participantArr[i].startsWith(CherryConstants.OS_ACTOR_TYPE_POSITION)){
                            String curAuditorID = participantArr[i].substring(CherryConstants.OS_ACTOR_TYPE_POSITION.length());
                            String curPrivilegeFlag = ConvertUtil.getString(participantMap.get(String.valueOf(i+1)));
                            sb.append(binOLCM19_BL.changePToU(false,false,employeeID, curAuditorID, curPrivilegeFlag));
                        }else{
                            sb.append(participantArr[i]);
                        }
                        //如果已经有逗号结尾这里不再加逗号
                        if(!sb.toString().endsWith(",")){
                            sb.append(",");
                        }
                    }
                    participant = sb.toString();
                }
            }catch(Exception e){
                
            }
            //participant = binOLCM19_BL.processingCommaString(participant);
            if("".equals(psPrivilegeFlag)){
                return participant;
            }else{
                return participant+CherryConstants.SESSION_PRIVILEGE_FLAG+psPrivilegeFlag;
            }
        }

        //取得当前step的描述信息
        WorkflowDescriptor descriptor = (WorkflowDescriptor)arg0.get("descriptor");
        StepDescriptor sd = descriptor.getStep(step.getStepId());
        //取得当前step下的meta元素集合
        Map<String,Object> metaMap = sd.getMetaAttributes();
        String cherryRule ="";
        if(metaMap.containsKey(CherryConstants.OS_META_Rule)){
            cherryRule = ConvertUtil.getString(metaMap.get(CherryConstants.OS_META_Rule));
        }
        if("".equals(cherryRule)){
            return "";
        }
        
        Map<String, Object> ruleMap = new HashMap<String,Object>();
        try {
            ruleMap = (Map<String,Object>) JSONUtil.deserialize(cherryRule);
        } catch (JSONException e1) {

        }
        String ruleType = ConvertUtil.getString(ruleMap.get("RuleType"));
        List<Map<String,Object>> ruleContext = (List<Map<String,Object>>)ruleMap.get("RuleContext");
        //审核者或执行者
        String auditor = "";
        try {
            if(CherryConstants.OS_RULETYPE_HARD.equals(ruleType)){
            	Map<String,String> pramData =  new HashMap<String,String>();
                pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_USER)));
                pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION)));
                pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART)));
                pramData.put(CherryConstants.OS_META_Rule, cherryRule);
                pramData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
                // 复杂模式
                auditor = binOLCM19_BL.findMatchingAuditor(pramData);
            }else{
            	String proType = ConvertUtil.getString(propertyMap.get("OS_ProType"));
                String currOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
                // 目前只支持促销品收货的代收模式，后面有需要时再逐一添加
            	if(CherryConstants.OS_RULETYPE_INSTEAD.equals(ruleType)){
            		if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
                        //促销品业务
                        if(currOperate.equals(CherryConstants.OPERATE_RD)){
                            //如果是收货
                            Map<String,String> pramData =  new HashMap<String,String>();
                            pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLRECEIVER_DEPART)));
                            pramData.put(CherryConstants.OS_META_Rule, cherryRule);
                            // 代收模式
                            auditor = binOLCM19_BL.findMatchingConfirmation(pramData);
                        }
            		}
                }
            	if(!"".equals(auditor)) {
            		return auditor;
            	}
                //简单等其他模式。
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<ruleContext.size();i++){
                    String actorType = ConvertUtil.getString(ruleContext.get(i).get("ActorType"));
                    String actorValue = ConvertUtil.getString(ruleContext.get(i).get("ActorValue"));
                    if("U".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_USER).append(actorValue).append(",");
                    }else if("D".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(actorValue).append(",");
                    }else if("P".equals(actorType)){
                        sb.append(CherryConstants.OS_ACTOR_TYPE_POSITION).append(actorValue).append(",");
                    }
                }
                //没有配置执行者
                if(ruleContext.isEmpty()){
                    if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(proType)){
                        //如果是收货
                        if(currOperate.equals(CherryConstants.OPERATE_RD)){
                            //产品的业务
                            Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(ips.getInt("BIN_ProductDeliverID"),null);
                            //发货和收货部门ID
                            String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
                            //产品的业务
                            Map<String, Object> ret = binOLSTCM03_BL.getProductDeliverMainData(ips.getInt("BIN_ProductDeliverID"),null);
                            //发货和收货部门ID
                            String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDSend).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_RA_CONFIRM)){
                            //确认退库
                            Map<String,Object> ret = binOLSTCM13_BL.getProReturnRequestMainData(ips.getInt("BIN_ProReturnRequestID"), null);
                            //部门ID
                            String orgID = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgID).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_CR_CONFIRM)){
                            //柜台确认盘点
                            Map<String,Object> ret = binOLSTCM14_BL.getProStocktakeRequestMainData(ips.getInt("BIN_ProStocktakeRequestID"), null);
                            //柜台的部门ID
                            String orgID = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgID).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_BG)){
                            //调入
                            Map<String,Object> ret = binOLSTCM16_BL.getProductAllocationMainData(ips.getInt("BIN_ProductAllocationID"), null);
                            //调入部门ID
                            String organizationID = String.valueOf(ret.get("BIN_OrganizationIDIn"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationID).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_LG)){
                            //调出
                            Map<String,Object> ret = binOLSTCM16_BL.getProductAllocationMainData(ips.getInt("BIN_ProductAllocationID"), null);
                            //调出部门ID
                            String organizationID = String.valueOf(ret.get("BIN_OrganizationIDOut"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationID).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_GR_CONFIRM)){
                            //确认入库
                            Map<String,Object> ret = binOLSTCM08_BL.getProductInDepotMainData(ips.getInt("BIN_ProductInDepotID"), null);
                            //入库部门ID
                            String organizationID = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationID).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_SA_CONFIRM)||currOperate.equals(CherryConstants.OPERATE_SA)){
                            //确认退货
                            Map<String,Object> ret = binOLSTCM21_BL.getSaleReturnRequestMainData(ips.getInt("BIN_SaleReturnRequestID"), null);
                            //部门ID
                            String orgID = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgID).append(",");
                        }
                    }else if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(proType)){
                        //促销品业务
                        if(currOperate.equals(CherryConstants.OPERATE_RD)){
                            //如果是收货
                            Map<String,Object> pramMap = new HashMap<String,Object>();
                            pramMap.put("BIN_PromotionDeliverID", ips.getInt("BIN_PromotionDeliverID"));
                            pramMap.put("language", null);
                            Map<String, Object> ret = binOLSSCM04_BL.getPromotionDeliverMain(pramMap);
                            //收货部门ID
                            String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_SD)){
                            //如果是发货
                            Map<String,Object> pramMap = new HashMap<String,Object>();
                            pramMap.put("BIN_PromotionDeliverID", ips.getInt("BIN_PromotionDeliverID"));
                            pramMap.put("language", null);
                            Map<String, Object> ret = binOLSSCM04_BL.getPromotionDeliverMain(pramMap);
                            //发货部门ID
                            String orgIDSend = String.valueOf(ret.get("BIN_OrganizationID"));
                            //如果用户的部门ID是发货部门的ID，则可以进行操作
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDSend).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_LG)){
                            //如果是调出确认
                            int organizationIDAccept = ips.getInt("BIN_OrganizationIDAccept");
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationIDAccept).append(",");
                        }else if(currOperate.equals(CherryConstants.OPERATE_GR_CONFIRM)){
                            //如果是确认入库
                            Map<String,Object> ret = binOLSSCM09_BL.getPrmInDepotMainData(ips.getInt("BIN_PrmInDepotID"), null);
                            //入库部门ID
                            String organizationID = String.valueOf(ret.get("BIN_OrganizationID"));
                            sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(organizationID).append(",");
                        }
                    }
                } else if("".equals(auditor)) {
                	// 配置了规则而没有找到满足条件的执行者,则默认使用接收部门的人员作为确认者
                	// 目前只支持对于促销品发货业务的收货
                	if(currOperate.equals(CherryConstants.OPERATE_RD)){
                        //如果是收货
                        Map<String,Object> pramMap = new HashMap<String,Object>();
                        pramMap.put("BIN_PromotionDeliverID", ips.getInt("BIN_PromotionDeliverID"));
                        pramMap.put("language", null);
                        Map<String, Object> ret = binOLSSCM04_BL.getPromotionDeliverMain(pramMap);
                        //收货部门ID
                        String orgIDReceive = String.valueOf(ret.get("BIN_OrganizationIDReceive"));
                        sb.append(CherryConstants.OS_ACTOR_TYPE_DEPART).append(orgIDReceive).append(",");
                    }
                }
                auditor = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return auditor;
    }
    
    /**
     * 相关任务写入任务表
     * （如果有某个步骤有方法（如auditHandle、doActionHandle）会执行workflow.doAction_single，本方法必须放在那个方法的前面）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void setParticipant(Map arg0, Map arg1, PropertySet propertyset){
        //当前的工作流实例
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
               
        String participant = getNextParticipant(arg0,arg1,propertyset);
        String privilegeFlag = "";

        participant = participant.replaceAll(CherryConstants.OS_ACTOR_TYPE_USER,"U");
        participant = participant.replaceAll(CherryConstants.OS_ACTOR_TYPE_POSITION,"P");
        participant = participant.replaceAll(CherryConstants.OS_ACTOR_TYPE_DEPART,"D");
        participant = participant.replaceAll("ThirdParty","T");
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("WorkFlowID", osid);
        param.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        param.put("CurrentUnit", mainData.get("CurrentUnit"));
        Map<String,Object> paramData = new HashMap<String,Object>();
        paramData.putAll(binOLCM19_BL.setInventoryUserTask(param));
        //分离参与者与权限标志
        int privilegeFlagIndex = participant.indexOf(CherryConstants.SESSION_PRIVILEGE_FLAG);
        if(privilegeFlagIndex > -1){
            privilegeFlag = participant.substring(privilegeFlagIndex+CherryConstants.SESSION_PRIVILEGE_FLAG.length());
            participant = participant.substring(0, privilegeFlagIndex);
        }
        //当前的执行者，形式如："P11,E543,D456,U564,"，可据此快速查询到当前登录用户所拥有的任务 P表示岗位ID，E表示员工ID，D表示部门ID，U表示用户ID，T表示第三方
        paramData.put("CurrentParticipant", participant);
        //对执行者的限制，比如当执行者是一个岗位的时候，会要求执行者和创建者之间有一定的关系：1：管辖；0：关注；A管辖和关注
        //创建者的部门类型，key为开头为RTC，加上index，没有这个key说明创建者是岗位
        //格式{"1":"1","2":"0","RTC1":"DT","3":"A","C1":"DT"}
        paramData.put("ParticipantLimit", privilegeFlag);
        int cnt = binOLCM19_BL.updateInventoryUserTask(paramData);
        if(cnt == 0){
            binOLCM19_BL.insertInventoryUserTask(paramData);
        }
    }
    
    /**
     * 删除相关任务
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void deleteUserTask(Map arg0, Map arg1, PropertySet propertyset){
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        Map<String,Object> paramData = new HashMap<String,Object>();
        paramData.put("WorkFlowID", entry.getId());
        binOLCM19_BL.deleteInventoryUserTask(paramData);
    }
}
