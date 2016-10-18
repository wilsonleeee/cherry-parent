package com.cherry.cm.cmbussiness.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM19_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM25_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM25_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 单据详细流程显示
 * 
 * @author zhhuyi
 * 
 */
public class BINOLCM25_Action extends BaseAction implements
		ModelDriven<BINOLCM25_Form> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(BINOLCM25_Action.class);
	
	private BINOLCM25_Form form = new BINOLCM25_Form();

	@Resource(name="binOLCM25_BL")
	private BINOLCM25_BL binOLCM25_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_BL binOLCM19_BL;
	
	@Resource(name="workflow")
	private Workflow wf;

	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	// propertySet中属性值
	private Map<String,Object> propertyMap = null;

	/**
	 * 取得propertySet中的信息
	 * 
	 * @param workflowId
	 * @return
	 * 		map
	 */
	public Map<String,Object> getPropSet(long workflowId){
		if (propertyMap == null){
			// 存放当前step的信息
			IBatisPropertySet ps = (IBatisPropertySet) wf.getPropertySet(workflowId);
		    propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
		}
		return propertyMap;
	}

	/**
	 * 单据处理中的流程图显示(有table显示明细)
	 * 
	 * @param workflowId
	 * @return
	 * 		String
	 */
	public String detailWork() throws JSONException {
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put("WorkFlowID", form.getWorkFlowID());
		// 取得业务处理流水表信息
		List<Map<String, Object>> opLoglist = binOLCM25_BL.selInventoryOpLog(pramMap);
		for (Map<String, Object> tempMap : opLoglist) {
			tempMap.put("OpCodeValue", codeTable.getVal("1131", tempMap.get("OpCode")));
			tempMap.put("OpResultValue", codeTable.getVal("1152", tempMap.get("OpResult")));
			//兼容老数据，操作时间不存在取日志的创建时间
			if(null != tempMap.get("OpDate")){
			    tempMap.put("CreateTime",tempMap.get("OpDate"));
			}
//			if(tempMap.get("CreateTime")!=null){
//				String[] strs=tempMap.get("CreateTime").toString().split("\\.");
//				tempMap.put("CreateTime", tempMap.get("CreateTime").toString().substring(0, strs[0].length()));
//			}
			// 备注长度超过40显示"..."
			if(tempMap.get("OpComments")!=null){
				//取得消息体
    			String messageBody = (String)tempMap.get("OpComments");
    			String messageBody_temp = "";
    			char[] messageBodyArr = messageBody.toCharArray();
    			//取得消息体的长度
    			int  messageBodyLength = messageBodyArr.length;
    			//
    			int count = 0;
    			for(int j = 0 ; j < messageBodyLength ; j++){
    				//控制在30个汉字长度之内
    				if(count > 40){
    					messageBody_temp = messageBody.substring(0, j)+" ...";
    					break;
    				}
    				//如果是汉字则加2，否则加1
    				if(messageBodyArr[j] >= 0x0391 && messageBodyArr[j] <= 0xFFE5){
    					count += 2;
    				}else{
    					count ++;
    				}
    			}
    			if("".equals(messageBody_temp)){
    				messageBody_temp = messageBody;
    			}
    			tempMap.put("OpCommentsMore", messageBody_temp);
			}
			
            //设置单据跳转路径
            String tableName = ConvertUtil.getString(tempMap.get("TableName"));
            String billID = ConvertUtil.getString(tempMap.get("BillID"));
            String historyBillID = ConvertUtil.getString(tempMap.get("HistoryBillID"));
            String openBillURL = binOLCM25_BL.getBillURL(tableName,ConvertUtil.getString(tempMap.get("OpCode")),historyBillID);
            if(null != openBillURL && !"".equals(openBillURL)){
                if(!"".equals(historyBillID)){
                    tempMap.put("OpenBillURL",openBillURL+historyBillID);
                }else{
                    tempMap.put("OpenBillURL",openBillURL+billID);
                }
            }
		}
		// 工作流实例id
		long wf_id = form.getWorkFlowID();
		// 没有实例ID的情况直接抛出异常
		if(wf_id == 0){
			logger.error(getText("global.page.workException") + ":" + getText("global.page.noWorkFlowId"));
			form.setOpLoglist(opLoglist);
			return "ProOrOthException";
		}
		// 存放结果
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			// 取得工作流步骤信息
			resultMap = comGetSteps(wf_id);
	    	// 存放参与者
	    	Map<String, Object> audMap = getNextAuditor(wf_id);
	    	audMap = setNextInfo(wf_id,audMap);
	    	//单据URL
	    	audMap.put("currentURL", getCurrentURL(wf_id,ConvertUtil.getString(request.getHeader("Referer"))));
	        form.setAudMap(audMap);
		} catch (Exception e) {
			logger.error(getText("global.page.workException")+","+e.getMessage(),e);
			form.setOpLoglist(opLoglist);
			return "ProOrOthException";
		}
		form.setCurrentStepId(JSONUtil.serialize(resultMap.get("currentStepId")));
		form.setStepList(JSONUtil.serialize(resultMap.get("steps")));
		form.setOpLoglist(opLoglist);
		return SUCCESS;
	}

	/**
	 * 各种业务操作时的流程图显示
	 * 
	 * @param workflowId
	 * @return
	 */
	public void getWorkFlowSteps() throws Exception{
		// 当前工作流id
		long wf_id = form.getWorkFlowID();
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			// 取得工作流步骤信息
			resultMap = comGetSteps(wf_id);
			ConvertUtil.setResponseByAjax(response, resultMap);
		} catch (Exception e) {
			resultMap.put("currentStepId", new HashMap<String,Object>());
			resultMap.put("steps", new ArrayList<Map<String,Object>>());
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
	}

	/**
	 * 修改执行者后刷新下一步执行者现显示
	 * 
	 * @param 无
	 * @return
	 * 		SUCCESS
	 * @throws Exception 
	 */
	public String refreshAuditor () throws Exception {
		// 工作流实例id
		long wf_id = form.getWorkFlowID();
		Map<String,Object> audMap = getNextAuditor(wf_id);
		audMap = setNextInfo(wf_id,audMap);
		form.setAudMap(audMap);
		return SUCCESS;
	}
	
	/**
	 * 设置下一步操作信息
	 * @param audMap
	 * @return
	 */
	private Map<String,Object> setNextInfo(long wf_id,Map<String,Object> audMap){
	    if(null == audMap){
	        audMap = new HashMap<String,Object>();
	    }
        //下一步操作内容为
        String currentOperation = binOLCM19_BL.getCurrentOperation(wf_id);
        if(!"999".equals(currentOperation)){
            audMap.put("OS_TaskName", getText("OS.TaskName."+currentOperation));
        }
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(wf_id,userInfo);
        if (null != adArr && adArr.length > 0) {
            //下一步跳转URL（有操作权限才在画面上显示操作按钮）
            String operationURL = "";
            IBatisPropertySet ps = (IBatisPropertySet) wf.getPropertySet(wf_id);
            Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null, PropertySet.STRING);
            String OS_BillType = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLTYPE));
            String OS_ProType = ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_PROTYPE));
            if(CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT.equals(OS_ProType)){
                if(CherryConstants.OS_BILLTYPE_OD.equals(OS_BillType)){
                    operationURL = "st/BINOLSTSFH03_init.action?productOrderID="+ps.getInt("BIN_ProductOrderID");
                }else if(CherryConstants.OS_BILLTYPE_SD.equals(OS_BillType)){
                    operationURL = "st/BINOLSTSFH05_init.action?productDeliverId="+ps.getInt("BIN_ProductDeliverID");
                }else if(CherryConstants.OS_BILLTYPE_RR.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL12_init.action?productReturnID="+ps.getInt("BIN_ProductReturnID");
                }else if(CherryConstants.OS_BILLTYPE_GR.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL02_init.action?productInDepotId="+ps.getInt("BIN_ProductInDepotID");
                }else if(CherryConstants.OS_BILLTYPE_LS.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL06_init.action?outboundFreeID="+ps.getInt("BIN_OutboundFreeID");
                }else if(CherryConstants.OS_BILLTYPE_CA.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL10_init.action?stockTakingId="+ps.getInt("BIN_ProductStockTakingID");
                }else if(CherryConstants.OS_BILLTYPE_MV.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL08_init.action?productShiftID="+ps.getInt("BIN_ProductShiftID");
                }else if(CherryConstants.OS_BILLTYPE_RA.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL14_init.action?proReturnRequestID="+ps.getInt("BIN_ProReturnRequestID");
                }else if(CherryConstants.OS_BILLTYPE_CR.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL16_init.action?proStocktakeRequestID="+ps.getInt("BIN_ProStocktakeRequestID");
                }else if(CherryConstants.OS_BILLTYPE_BG.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL18_init.action?productAllocationID="+ps.getInt("BIN_ProductAllocationID");
                }else if(CherryConstants.OS_BILLTYPE_NS.equals(OS_BillType)){
                    operationURL = "st/BINOLSTSFH16_init.action?saleId="+ps.getInt("BIN_BackstageSaleID");
                }else if(CherryConstants.OS_BILLTYPE_SA.equals(OS_BillType)){
                    operationURL = "st/BINOLSTBIL20_init.action?saleReturnRequestID="+ps.getInt("BIN_SaleReturnRequestID");
                }
            }else if(CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION.equals(OS_ProType)){
                if(CherryConstants.OS_BILLTYPE_LG.equals(OS_BillType) || CherryConstants.OS_BILLTYPE_BG.equals(OS_BillType)){
                    operationURL = "ss/BINOLSSPRM56_init?proAllocationId="+ps.getInt("BIN_PromotionAllocationID");
                }else if(CherryConstants.OS_BILLTYPE_SD.equals(OS_BillType)){
                    operationURL = "ss/BINOLSSPRM52_init.action?deliverId="+ps.getInt("BIN_PromotionDeliverID");
                }else if(CherryConstants.OS_BILLTYPE_MV.equals(OS_BillType)){
                    operationURL = "ss/BINOLSSPRM62_init.action?promotionShiftID="+ps.getInt("BIN_PromotionShiftID");
                }else if(CherryConstants.OS_BILLTYPE_GR.equals(OS_BillType)){
                    operationURL = "ss/BINOLSSPRM65_init.action?prmInDepotID="+ps.getInt("BIN_PrmInDepotID");
                }
            }
            audMap.put("operationURL",operationURL);
        }
	    return audMap;
	}
	
    /**
     * 取得当前单据的URL
     * @param wf_id
     * @param referer
     * @return
     */
    private String getCurrentURL(long wf_id,String referer){
        IBatisPropertySet ps = (IBatisPropertySet) wf.getPropertySet(wf_id);
        String currentURL = "";
        try{
            if(referer.indexOf("BINOLSTSFH03_init")>-1){
                currentURL = "st/BINOLSTSFH03_init.action?productOrderID="+ps.getInt("BIN_ProductOrderID");
            }else if(referer.indexOf("BINOLSTSFH05_init")>-1){
                currentURL = "st/BINOLSTSFH05_init.action?productDeliverId="+ps.getInt("BIN_ProductDeliverID");
            }else if(referer.indexOf("BINOLSTBIL12_init")>-1){
                currentURL = "st/BINOLSTBIL12_init.action?productReturnID="+ps.getInt("BIN_ProductReturnID");
            }else if(referer.indexOf("BINOLSTBIL02_init")>-1){
                currentURL = "st/BINOLSTBIL02_init.action?productInDepotId="+ps.getInt("BIN_ProductInDepotID");
            }else if(referer.indexOf("BINOLSTBIL06_init")>-1){
                currentURL = "st/BINOLSTBIL06_init.action?outboundFreeID="+ps.getInt("BIN_OutboundFreeID");
            }else if(referer.indexOf("BINOLSTBIL10_init")>-1){
                currentURL = "st/BINOLSTBIL10_init.action?stockTakingId="+ps.getInt("BIN_ProductStockTakingID");
            }else if(referer.indexOf("BINOLSTBIL08_init")>-1){
                currentURL = "st/BINOLSTBIL08_init.action?productShiftID="+ps.getInt("BIN_ProductShiftID");
            }else if(referer.indexOf("BINOLSTBIL14_init")>-1){
                currentURL = "st/BINOLSTBIL14_init.action?proReturnRequestID="+ps.getInt("BIN_ProReturnRequestID");
            }else if(referer.indexOf("BINOLSTBIL16_init")>-1){
                currentURL = "st/BINOLSTBIL16_init.action?proStocktakeRequestID="+ps.getInt("BIN_ProStocktakeRequestID");
            }else if(referer.indexOf("BINOLSSPRM56_init")>-1){
                currentURL = "ss/BINOLSSPRM56_init?proAllocationId="+ps.getInt("BIN_PromotionAllocationID");
            }else if(referer.indexOf("BINOLSSPRM52_init")>-1){
                currentURL = "ss/BINOLSSPRM52_init.action?deliverId="+ps.getInt("BIN_PromotionDeliverID");
            }else if(referer.indexOf("BINOLSSPRM62_init")>-1){
                currentURL = "ss/BINOLSSPRM62_init.action?promotionShiftID="+ps.getInt("BIN_PromotionShiftID");
            }else if(referer.indexOf("BINOLSTBIL18_init")>-1){
                currentURL = "st/BINOLSTBIL18_init.action?productAllocationID="+ps.getInt("BIN_ProductAllocationID");
            }else if(referer.indexOf("BINOLSSPRM65_init")>-1){
                currentURL = "ss/BINOLSSPRM65_init.action?prmInDepotID="+ps.getInt("BIN_PrmInDepotID");
            }else if(referer.indexOf("BINOLSTSFH16_init")>-1){
                currentURL = "st/BINOLSTSFH16_init.action?saleId="+ps.getInt("BIN_BackstageSaleID");
            }else if(referer.indexOf("BINOLSTBIL20_init")>-1){
                currentURL = "st/BINOLSTBIL20_init.action?saleReturnRequestID="+ps.getInt("BIN_SaleReturnRequestID");
            }
        }catch(Exception e){
            
        }
        return currentURL;
    }

	/**
	 * 共通方法，取得当前工作流的基本信息(当前步骤以及所有的step信息)
	 * 
	 * @param workflowId
	 * @return
	 * 		map
	 * @throws Exception 
	 */
	private Map<String, Object> comGetSteps(long wf_id) throws Exception {
		try{
			Map<String,Object> resultMap = new HashMap<String,Object>();
			// 存放对应step的信息
			List<Map<String, Object>> steps = new ArrayList<Map<String, Object>>();
			// 存放对应当前step的信息
			Map<String, Object> currentStepId = new HashMap<String, Object>();
			// 取得propertySet中数据
			getPropSet(wf_id);
			// 取得当前步骤id，判断是否显示“修改执行者”按钮。工作流执行完成，修改执行者按钮不显示
			try{
				form.setCurrentOperateVal((String) propertyMap.get("OS_Current_Operate"));
			}catch (Exception e){
				logger.error(getText("global.page.currentOperateException")+","+e.getMessage(),e);
			}
			// 对于历史的工作流执行没有"OS_StepShowOrder"这个关键词的处理
			String currentShowOrder = (String) propertyMap.get("OS_StepShowOrder");
			if(null == currentShowOrder || "".equals(currentShowOrder)){
				// 取得当前将要执行的步骤
				List currentSteps = wf.getCurrentSteps(wf_id);
				if(null != currentSteps && !currentSteps.isEmpty()){
					// 从当前step表中取得当前步骤
				    SimpleStep ss = (SimpleStep) currentSteps.get(0);
					currentStepId.put("currentId", String.valueOf(ss.getStepId()));
				}else{
					// 已经结束的工作流给它一个较大的id
					currentStepId.put("currentId", "1000");
				}
			}else{
				currentStepId.put("currentShowOrder", (String) propertyMap.get("OS_StepShowOrder"));
			}
			// 读取当前工作流文件
			WorkflowDescriptor wd = wf.getWorkflowDescriptor(wf.getWorkflowName(wf_id));
			// 取得当前工作流文件的所有step
			List<StepDescriptor> stepDSList = wd.getSteps();
			Map<String, Object> mapTmp = null;
			// 循环step的list
			for(StepDescriptor stepDSMap : stepDSList){
				// 取得每个step中的meta元素
				Map<String, Object> metaAttributes = stepDSMap.getMetaAttributes();
				if(null != metaAttributes && !metaAttributes.isEmpty()){
					// 取得需要显示的流程的显示标志、显示顺序和显示文字
					if("1".equals(metaAttributes.get("OS_StepShowFlag"))){
						mapTmp = new HashMap<String, Object>();
						mapTmp.put("OS_StepShowFlag", metaAttributes.get("OS_StepShowFlag"));
						mapTmp.put("OS_StepShowOrder", metaAttributes.get("OS_StepShowOrder"));
						mapTmp.put("showText", codeTable.getVal("1153", metaAttributes.get("OS_StepShowText")));
						mapTmp.put("stepId", stepDSMap.getId());
						steps.add(mapTmp);
					}
				}
			}
			resultMap.put("currentStepId", currentStepId);
			resultMap.put("steps", steps);
			return resultMap;
		}catch (Exception e){
			throw e;
		}
		
	}
	
	/**
	 * 共通方法，取得下一步执行者
	 * 
	 * @param workflowId
	 * @return
	 * 		map
	 * @throws Exception 
	 */
	private Map<String, Object> getNextAuditor(long wf_id) throws Exception {
		// 取得propertySet中数据
		getPropSet(wf_id);
		// 取得当前步骤
        //String osCurrentOperate = ConvertUtil.getString(propertyMap.get("OS_Current_Operate"));
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("WorkFlowID", wf_id);
        List<Map<String,Object>> inventoryUserTaskList = binOLCM19_BL.getInventoryUserTaskByOSID(param);
        
        //String auditors = (String) propertyMap.get(CherryConstants.OS_ACTOR+osCurrentOperate);
        String auditors = "";
        String privilegeFlag = "";
        if(null != inventoryUserTaskList && inventoryUserTaskList.size()>0){
            // 取得当前步骤的参与者
            auditors = ConvertUtil.getString(inventoryUserTaskList.get(0).get("CurrentParticipant"));
            //取得对执行者的限制（复杂模式+审核者是岗位）
            privilegeFlag = ConvertUtil.getString(inventoryUserTaskList.get(0).get("ParticipantLimit"));
        }
        
        // 取得工作流文件中的规则内容
		String cherryRule ="";
    	// 存放参与者
    	Map<String, Object> audMap =  new HashMap<String, Object>();
    	// 从ps表中取不到时，有两种情况：1、审核者没有 2、发货或收货者没有
        if(null == auditors || "".equals(auditors)){
    		// 取得当前将要执行的步骤
    		List currentSteps = wf.getCurrentSteps(wf_id);
    		if(null != currentSteps && !currentSteps.isEmpty()){
    			// 从当前step表中取得当前步骤
    		    SimpleStep ss = (SimpleStep) currentSteps.get(0);
    			//取得所有step的描述信息
    			WorkflowDescriptor descriptor = (WorkflowDescriptor)wf.getWorkflowDescriptor(wf.getWorkflowName(wf_id));
    			// 取得当前step的描述信息
    			StepDescriptor sd = descriptor.getStep(ss.getStepId());
    			//取得当前step下的meta元素集合
    			Map metaMap = sd.getMetaAttributes();
    			if(metaMap.containsKey(CherryConstants.OS_META_Rule)){
    				cherryRule = (String)metaMap.get(CherryConstants.OS_META_Rule);
    			}
    			Map<String,String> pramData =  new HashMap<String,String>();
    			pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_USER)));
    			pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION)));
    			pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART)));
    			pramData.put(CherryConstants.OS_META_Rule, cherryRule);
    			pramData.put(CherryConstants.OS_ACTOR_TYPE_EMPLOYEE, ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
    			// 调用共通方法取得审核者
    			auditors = binOLCM19_BL.findMatchingAuditor(pramData);
    			// 取得发货或收货者
    			if(!"".equals(cherryRule) && (null == auditors || "".equals(auditors))){
        			// 取得规则信息
        			Map<String, Object> ruleMap = (Map<String, Object>) JSONUtil.deserialize(cherryRule);
        			// 取得第三方标志
                    String thirdPartyFlag = String.valueOf(ruleMap.get("ThirdPartyFlag"));
                    // 若是第三方审核，则不需要去取执行者信息
                    if("1".equals(thirdPartyFlag)){
                        //如果是第三方完成，则强制设为需要审核
        	        	audMap.put("ThirdParty", "1");
                    }else{
                    	List<Map<String, Object>> ruleList = (List<Map<String, Object>>) ruleMap.get("RuleContext");
            			for(Map<String, Object> auditorMap : ruleList){
            				if("U".equals(auditorMap.get("ActorType"))){
            					// 插入userId
                    			audMap.put(CherryConstants.OS_ACTOR_TYPE_USER, auditorMap.get("ActorValue"));
                    	        // 取得执行者的名字(用户名)
                	        	String userName = binOLCM25_BL.getUserName(audMap);
                	        	audMap.put("userName", userName);
            				}else if("P".equals(auditorMap.get("ActorType"))){
            					// 插入岗位id
                    			audMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, auditorMap.get("ActorValue"));
                    	        // 取得当前岗位权限标志
                    	        //String privilegeFlag = (String) propertyMap.get(CherryConstants.SESSION_PRIVILEGE_FLAG+osCurrentOperate);
                    	        if(null != privilegeFlag && !"".equals(privilegeFlag)){
                    	        	// 权限标志
            	        			audMap.put(CherryConstants.SESSION_PRIVILEGE_FLAG, privilegeFlag);
                    	        }
                    	        // 取得执行者的名字(岗位)
                	        	String categoryName = binOLCM25_BL.getCategoryName(audMap);
                	        	audMap.put("categoryName", categoryName);
            				}else if("D".equals(auditorMap.get("ActorType"))){
            					// 插入部门ID
                    			audMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, auditorMap.get("ActorValue"));
                    	        // 取得执行者的名字(部门)
                	        	String departName = binOLCM25_BL.getDepartName(audMap);
                	        	audMap.put("departName", departName);
            				} 				
            			}
                    }
    			}
    		}
		}
        // 解析通过ps表或审核者共通方法中获得的执行者
        if(null != auditors && !"".equals(auditors)){
            // 分离参与者(U1,P1,D1,)
            String[] auditor = auditors.split(",");
            Map<String,Object> privilegeFlagMap = new HashMap<String,Object>();
            if(!"".equals(privilegeFlag)){
                privilegeFlagMap = (Map<String, Object>) JSONUtil.deserialize(privilegeFlag);
            }
            
            // 循环取得每个参与者（参与者格式为U1或P1或D1）
            List<Map<String,Object>> participantList = new ArrayList<Map<String,Object>>();
            int index = 0;
            for(String aud : auditor){
                if(aud.startsWith(CherryConstants.OS_ACTOR_TYPE_U)){
                    // 字符串前面的英文字母U表示任务参与者类型为用户，后面的数字为任务参与者的用户ID
                    String value = aud.substring(CherryConstants.OS_ACTOR_TYPE_U.length());
                    // 取得执行者的名字(用户名)
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, value);
                    String userName = binOLCM25_BL.getUserName(paramMap);
                    Map<String,Object> participantMap = new HashMap<String,Object>();
                    participantMap.put(CherryConstants.OS_ACTOR_TYPE_USER, value);
                    participantMap.put("userName", userName);
                    //过滤已存在
                    boolean addFlag = true;
                    for(int j=0;j<participantList.size();j++){
                        if(ConvertUtil.getString(participantList.get(j).get("userName")).equals(userName)){
                            addFlag = false;
                            break;
                        }
                    }
                    if(addFlag){
                        participantList.add(participantMap);
                    }
                }else if(aud.startsWith(CherryConstants.OS_ACTOR_TYPE_P)){
                    // 字符串前面的英文字母P表示任务参与者类型为岗位，后面的数字为任务参与者的岗位ID
                    String value = aud.substring(CherryConstants.OS_ACTOR_TYPE_P.length());
                    // 取得执行者的名字(岗位)
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, value);
                    String categoryName = binOLCM25_BL.getCategoryName(paramMap);
                    Map<String,Object> participantMap = new HashMap<String,Object>();
                    participantMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, value);
                    participantMap.put("categoryName", categoryName);
                    // 取得当前岗位权限标志
                    String curPrivilegeFlag = ConvertUtil.getString(privilegeFlagMap.get(String.valueOf(index+1)));
                    participantMap.put("privilegeFlag", curPrivilegeFlag);
                    // 发起者身份类型
                    String roleTypeCreater = ConvertUtil.getString(privilegeFlagMap.get(CherryConstants.OS_ROLETYPE_CREATER + String.valueOf(index+1)));
                    participantMap.put("RoleTypeCreater", roleTypeCreater);
                    // 接收者身份类型
                    String roleTypeReceiver = ConvertUtil.getString(privilegeFlagMap.get(CherryConstants.OS_ROLETYPE_RECEIVER + String.valueOf(index+1)));
                    participantMap.put("RoleTypeReceiver", roleTypeReceiver);
                    // 确认者与接收者权限关系
                    if(!"".equals(roleTypeReceiver)) {
                    	participantMap.put("privilegeRecFlag", curPrivilegeFlag);
                    }
                    //过滤已存在
                    boolean addFlag = true;
                    for(int j=0;j<participantList.size();j++){
                        if(ConvertUtil.getString(participantList.get(j).get("categoryName")).equals(categoryName)){
                            if(ConvertUtil.getString(participantList.get(j).get("privilegeFlag")).equals(curPrivilegeFlag)){
                                addFlag = false;
                                break;  
                            }
                        }
                    }
                    if(addFlag){
                        participantList.add(participantMap);
                    }
                }else if(aud.startsWith(CherryConstants.OS_ACTOR_TYPE_D)){
                    // 字符串前面的英文字母D表示任务参与者类型为部门，后面的数字为任务参与者的部门ID
                    String value = aud.substring(CherryConstants.OS_ACTOR_TYPE_D.length());
                    // 取得执行者的名字(部门)
                    Map<String,Object> paramMap = new HashMap<String,Object>();
                    paramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, value);
                    String departName = binOLCM25_BL.getDepartName(paramMap);
                    Map<String,Object> participantMap = new HashMap<String,Object>();
                    participantMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, value);
                    participantMap.put("departName", departName);
                    //过滤已存在
                    boolean addFlag = true;
                    for(int j=0;j<participantList.size();j++){
                        if(ConvertUtil.getString(participantList.get(j).get("departName")).equals(departName)){
                            addFlag = false;
                            break;
                        }
                    }
                    if(addFlag){
                        participantList.add(participantMap);
                    }
                }else if(aud.startsWith(CherryConstants.OS_ACTOR_TYPE_T)){
                    // 第三方
                    Map<String,Object> participantMap = new HashMap<String,Object>();
                    participantMap.put("ThirdParty", "ThirdParty");
                    participantList.add(participantMap);
                }
                index ++;
        	}
        	audMap.put("participantList", participantList);
        }
        audMap.put("OS_BillCreator", ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        audMap.put("OS_BillCreator_Depart", ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART)));
        audMap.put("OS_BillReceiver_Depart", ConvertUtil.getString(propertyMap.get(CherryConstants.OS_MAINKEY_BILLRECEIVER_DEPART)));
        //查询具体可操作的人
        List<String> personList = binOLCM19_BL.getPersonList(audMap);
        audMap.put("personList", personList);
        
        return audMap;
	}
	
	@Override
	public BINOLCM25_Form getModel() {
		return form;
	}

}
