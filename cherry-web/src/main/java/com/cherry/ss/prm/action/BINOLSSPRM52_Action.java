/*	
 * @(#)BINOLSSPRM52_Action.java     1.0 2010/11/29		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM52_BL;
import com.cherry.ss.prm.form.BINOLSSPRM52_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品发货单明细画面
 * 提供的功能有 明细查看 编辑  发货 收货
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
*/
public class BINOLSSPRM52_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM52_Form> {
	private static final long serialVersionUID = -2409264522559128413L;

    /**异常日志*/
    private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM17_Action.class);
	
    @Resource(name="workflow")
    private Workflow workflow;
    
	@Resource(name="binOLSSPRM52_BL")
	private BINOLSSPRM52_BL binolssprm52BL;
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binolcm19_bl;
	
	@Resource(name="binOLSSCM01_BL")
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binolsscm04_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;

	/** 参数FORM */
	private BINOLSSPRM52_Form form = new BINOLSSPRM52_Form();

	/**
	 * <p>
	 * 画面初期显示（从一览画面弹出）
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		/**
    	 * 修改init方法,如果是收货状态,在页面加载的时候要取出逻辑仓库List
    	 * @author zhanggl
    	 * @date 2011-12-09
    	 * 
    	 * */
		
    	//取得用户信息
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	userInfo.setCurrentUnit("BINOLSSPRM52");
    	
    	String language = userInfo.getLanguage();
        String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 参数MAP
		Map<String, Object> parmap = getSearchMap();
		//initJbpm时BIN_PromotionDeliverID为null
		if(ConvertUtil.getString(parmap.get("BIN_PromotionDeliverID")).equals("")){
		    //取得URL中的参数信息
		    String entryID= request.getParameter("entryID");
		    form.setWorkFlowID(entryID);
		    String billID= request.getParameter("mainOrderID");
		    parmap.put("BIN_PromotionDeliverID", billID);
		}
		// 取得发货单概要信息和明细信息
		Map<String, Object> mainMap = binolsscm04_BL.getPromotionDeliverMain(parmap);
		form.setReturnInfo(mainMap);
		
		//取得操作区分
        String employeeID = String.valueOf(mainMap.get("BIN_EmployeeID"));  
        String entryID = String.valueOf(mainMap.get("WorkFlowID"));
        String flag = getPageOperateFlag(entryID,employeeID,userInfo,mainMap);
        form.setOperateType(flag);
		
        String lockSection = "";
        if(flag.equals("2")){
            lockSection = "0";
        }else if(flag.equals(CherryConstants.AUDIT_FLAG_SUBMIT)){
            lockSection = "1";
        }
        form.setLockSection(lockSection);
        
		List<Map<String,Object>> deliverDetailList = binolsscm04_BL.getDeliverDetailList(parmap);
		for(int i=0;i<deliverDetailList.size();i++){
		    if(flag.equals("2") || flag.equals(CherryConstants.AUDIT_FLAG_SUBMIT)){
		        //查询库存
		        Map<String,Object> paramMap = new HashMap<String,Object>();
		        paramMap.put("BIN_DepotInfoID", deliverDetailList.get(i).get("BIN_InventoryInfoID"));
	            paramMap.put("BIN_PromotionProductVendorID", deliverDetailList.get(i).get("BIN_PromotionProductVendorID"));
	            paramMap.put("BIN_LogicInventoryInfoID", deliverDetailList.get(i).get("BIN_LogicInventoryInfoID"));
	            paramMap.put("FrozenFlag", "2");
	            paramMap.put("LockSection", lockSection);
		        int stockQuantity = binOLSSCM01_BL.getPrmStock(paramMap);
		        deliverDetailList.get(i).put("NowCount", stockQuantity);
		    }
		}
		form.setReturnList(deliverDetailList);
		
		//取得发货仓库
		String organizationID = String.valueOf(mainMap.get("BIN_OrganizationID"));
		form.setOutDepotList(getDepotList(organizationID,userInfo.getLanguage()));
		
		if(CherryConstants.OPERATE_RD.equals(flag)){
			//取得收货实体仓库
			form.setInDepotList(getDepotList(String.valueOf(mainMap.get("BIN_OrganizationIDReceive")),userInfo.getLanguage()));
			
	        //判断是后台部门还是柜台
            String logicType = "0";
            String businessType = CherryConstants.LOGICDEPOT_BACKEND_RD;
            String organizationIDReceive = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDReceive"));
            Map<String,Object> departInfoMap = binolcm01BL.getDepartmentInfoByID(organizationIDReceive,language);
            if(null != departInfoMap){
                String organizationType = ConvertUtil.getString(departInfoMap.get("Type"));
                //终端
                if(organizationType.equals(CherryConstants.ORGANIZATION_TYPE_FOUR)){
                    logicType = "1";
                    businessType = CherryConstants.LOGICDEPOT_TERMINAL_GR;
                }
            }
			
			//取得收货逻辑仓库
			//取得逻辑仓库
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			//后台/终端逻辑仓库
			paramMap.put("Type", logicType);
			paramMap.put("language", language);
			paramMap.put("ProductType", "2");
			paramMap.put("BusinessType", businessType);
//			form.setInLoginDepotList(binOLCM18_BL.getLogicDepotByBusinessType(paramMap));
			form.setInLoginDepotList(binOLCM18_BL.getLogicDepotByBusiness(paramMap));
		}
		form.setWorkFlowID(entryID);
		
        //取得系统配置项库存是否允许为负
        String configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
        form.setCheckStockFlag(configValue);
		
		return SUCCESS;
	}
	/**
	 * <p>
	 * 画面初期显示（从top任务列表中弹出）
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String initJbpm() throws Exception {
//    	//取得用户信息
//    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);  
//    	userInfo.setCurrentUnit("BINOLSSPRM52");
//    	
//		//取得URL中的参数信息
//     	String entryID= request.getParameter("entryID");
//     	String billID= request.getParameter("mainOrderID");
//     	
//     	form.setWorkFlowID(entryID);
//
//		// 取得发货单概要信息和明细信息
//		Map<String, Object> parmap = new HashMap<String, Object>();
//		parmap.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
//		parmap.put("BIN_PromotionDeliverID", billID);
//		Map<String, Object> mainMap = binolsscm04_BL.getPromotionDeliverMain(parmap);
//		List<Map<String, Object>> detailList = binolsscm04_BL.getDeliverDetailList(parmap);
//		form.setReturnInfo(mainMap);		
//		form.setReturnList(detailList);
//		
//		//取得仓库(制单员编辑时可修改仓库)
//		String organizationID = String.valueOf(mainMap.get("BIN_OrganizationID"));
//		form.setOutDepotList(getDepotList(organizationID,userInfo.getLanguage()));		
//
//		//取得操作区分
//		String employeeID = String.valueOf(mainMap.get("BIN_EmployeeID"));	
//		String flag = getPageOperateFlag(entryID,employeeID,userInfo,mainMap);
//		form.setOperateType(flag);
//		
//		if("50".equals(flag)){
//			//取得收货仓库
//			form.setInDepotList(getDepotList(String.valueOf(mainMap.get("BIN_OrganizationIDReceive")),userInfo.getLanguage()));
//		}
//		
//		return SUCCESS;
	    return init();
	}
	
	/**
	 * 工作流中的各种动作入口方法
	 * @return
	 * @throws Exception
	 */
	public String doaction() throws Exception{
	    try{
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
    		String entryID = request.getParameter("entryid").toString();
    		String actionID = request.getParameter("actionid").toString();
    		form.setEntryID(entryID);
    		form.setActionID(actionID);
            String workFlowName = workflow.getWorkflowName(Long.parseLong(entryID));
            ActionDescriptor ad = workflow.getWorkflowDescriptor(workFlowName).getAction(CherryUtil.obj2int(actionID));
            Map<String,Object> metaAttributes = ad.getMetaAttributes();
            String operateCode = ConvertUtil.getString(metaAttributes.get("OS_OperateCode"));
            String operateResultCode = ConvertUtil.getString(metaAttributes.get("OS_OperateResultCode"));
            // OS_OperateResultCode 为这几个值时，需要检查库存 101审核通过 103再次提交 105已发货
            if ((operateResultCode.equals("101") || operateResultCode.equals("103") || operateResultCode.equals("105") 
                    || operateCode.equals("42")) && !validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		binolssprm52BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002"));            
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else if (e instanceof WorkflowException) {
                this.addActionError(getText(e.getMessage()));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }else{
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
	}
	
    /**
     * 保存发货单数据
     * 
     * @return
     * @throws JSONException
     */
    public String save() throws Exception{
        try{
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        	boolean ret = binolssprm52BL.tran_saveDeliver(form,userInfo);
        	if(!ret){
    			this.addActionError(getText("ECM00038"));
    			return CherryConstants.GLOBAL_ACCTION_RESULT;
        	}
        }catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            if (ex instanceof CherryException) {
                CherryException temp = (CherryException) ex;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            } else {
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
        }
    	this.addActionMessage(getText("ICM00002"));
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;        
    }
    
    /**
     * 点击发货按钮
     * 非工作流中的编辑模式才能调用到此方法
     * @return
     * @throws JSONException
     */
    public String send() throws Exception{
    	try{
            if(!validateForm()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binolcm01BL.completeUserInfo(userInfo, form.getOutOrganizationID(), "BINOLSSPRM52");
    		binolssprm52BL.tran_sendDeliver(form,userInfo);
    	}catch(Exception ex){
            logger.error(ex.getMessage(),ex);
    		if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
    	}
    	this.addActionMessage(getText("ICM00002"));    	
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;        
    }
 
    /**
     * 删除发货单数据
     * 非工作流中的编辑模式才能调用到此方法
     * @return
     * @throws JSONException
     */
    public String delete() throws Exception{
    	try{
    	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		binolssprm52BL.tran_deleteDeliver(form,userInfo);
    	}catch(Exception ex){
            logger.error(ex.getMessage(),ex);
    		if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
                //系统发生异常，请联系管理人员。
                this.addActionError(getText("ECM00036"));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
    	}
    	this.addActionMessage(getText("ICM00002"));    	
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;        
    }
    
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 发货单Id
		map.put("BIN_PromotionDeliverID", form.getDeliverId());
		return map;
	}
	
    /**
     * 根据指定的部门ID和语言信息取得仓库信息
     * @param organizationID
     * @param language
     * @return
     */
    private List<Map<String, Object>> getDepotList(String organizationID,String language){    	
    	List<Map<String, Object>> ret = binolcm01BL.getDepotList(organizationID, language);
    	return ret;
    }
    
	/**
	 * 目前该明细画面有四种展现模式
	 * 1：明细查看模式【关闭】
	 * 2：非工作流的编辑模式【保存】【发货】【删除】【关闭】
	 * 41：工作流审核模式【通过】【退回】【关闭】
	 * 42：工作流编辑模式【保存】【发货】【删除】【关闭】
	 * 50：工作流中的收货模式  按钮有【收货】【关闭】
	 * @param workFlowID  工作流ID
	 * @param employeeID  制单员
	 * @param userInfo 当前用户信息
	 * @param mainData 主单数据
	 * @return
	 */
	private String getPageOperateFlag(String workFlowID,String employeeID,UserInfo userInfo,Map<String,Object> mainData) {
		//查看明细模式  按钮有【关闭】
		String ret="1";
		 if(null==workFlowID||"".equals(workFlowID)||"null".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //如果没有工作流实例ID，则说明是草稿状态的订单
                 //如果当前登录者和制单员为同一人，则为非工作流的编辑模式，否则只能查看
                 if(employeeID.equals(String.valueOf(userInfo.getBIN_EmployeeID()))){
                     //按钮有【保存】【发货】【删除】【关闭】
                     ret="2"; 
                 }
             }
		 }else{
			 //取得当前可执行的action
			 ActionDescriptor[] adArr = binolcm19_bl.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
			if (adArr != null && adArr.length > 0) {
				// 如果存在可执行action，说明工作流尚未结束
				// 取得当前的业务操作
				String currentOperation = binolcm19_bl.getCurrentOperation(Long.parseLong(workFlowID));
				if (CherryConstants.OPERATE_SD_AUDIT.equals(currentOperation)) {
					//发货单审核模式   按钮有【通过】【退回】【关闭】
					request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				} else if (CherryConstants.OPERATE_SD_EDIT.equals(currentOperation)) {
					//工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if (CherryConstants.OPERATE_RD.equals(currentOperation)) {
					//工作流中的收货模式  按钮有【收货】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}
			}
		 }		 
		 return ret;
	}
	@Override
	public BINOLSSPRM52_Form getModel() {
		return form;
	}
	
	/**
	 * 验证数据
	 */
	public void validateDoaction(){
	    String actionID = request.getParameter("actionid").toString();
	    if("501".equals(actionID) || "502".equals(actionID) || "61".equals(actionID)){
            if(null == form.getPromotionProductVendorIDArr() || form.getPromotionProductVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            for(int i=0;i<form.getPromotionProductVendorIDArr().length;i++){
                if(null == form.getQuantityuArr()[i] || "".equals(form.getQuantityuArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityuArr()[i]) || "0".equals(form.getQuantityuArr()[i])){
                    this.addActionError(getText("EST00008"));
                    return;
                }
            }
	    }
	}
	
    /**
     * 根据系统配置项是否需要验证库存大于发货数量
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateStock() {
        boolean isCorrect = true;
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);

        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getOutDepotId()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getOutLoginDepotId()));
            paramMap.put("FrozenFlag", "1");//不扣除冻结库存
            paramMap.put("ProductType", "2");//促销品
            paramMap.put("IDArr", form.getPromotionProductVendorIDArr());
            paramMap.put("QuantityArr", form.getQuantityuArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
                this.addActionError(getText("EST00034"));
                return isCorrect;
            }
        }

        return isCorrect;
    }
    
    private boolean validateForm(){
        boolean isCorrect = true;
        String planArriveDate = ConvertUtil.getString(form.getPlanArriveDate());
        if(!planArriveDate.equals("")){
            if(!CherryChecker.checkDate(planArriveDate)){
                   this.addActionError(getText("ECM00008", new String[]{getText("PST00036")}));
                    isCorrect = false;
            }
        }
        return isCorrect;
    }
}
