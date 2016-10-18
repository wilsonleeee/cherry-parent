/*	
 * @(#)BINOLSSPRM56_Action.java     1.0 2010/11/29		
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM56_BL;
import com.cherry.ss.prm.form.BINOLSSPRM56_Form;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 调拨记录编辑
 * 
 * 
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM56_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM56_Form> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2409264522559128413L;

	@Resource(name="binOLSSPRM56_BL")
	private BINOLSSPRM56_BL binolssprm56BL;
	
	@Resource(name="binOLCM01_BL")
	private BINOLCM01_BL binolcm01BL;
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;

	/** 参数FORM */
	private BINOLSSPRM56_Form form = new BINOLSSPRM56_Form();
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binolcm19_bl;
	
	@Resource(name="binOLSSCM03_BL")
	private BINOLSSCM03_BL binolsscm03_bl;

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
    	//取得用户信息
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);  
    	
		// 参数MAP
		Map<String, Object> parmap = getSearchMap();
		// 调拨记录信息
		Map<String, Object> mainMap = binolsscm03_bl.getPromotionAllocationMain(parmap);
		form.setReturnInfo(mainMap);
		
		// 调拨详细记录LIST
		form.setReturnList(binolsscm03_bl.getPromotionAllocationDetail(parmap));
		
		//取得操作区分
		String employeeID = String.valueOf(mainMap.get("BIN_EmployeeID"));	
		String entryID = String.valueOf(mainMap.get("WorkFlowID"));		
		String flag = getPageOperateFlag(entryID,employeeID,userInfo,mainMap);
		form.setOperateType(flag);
		
		if(CherryConstants.OPERATE_LG.equals(flag)){
			//取得调出仓库
			form.setOutDepotList(getDepotList(String.valueOf(mainMap.get("BIN_OrganizationIDAccept")),userInfo.getLanguage()));
		
			//取得调出逻辑仓库
			List<Map<String,Object>> outLogicList = getLogicDepotList(CherryConstants.BUSINESS_TYPE_LG);
			form.setOutLogicList(outLogicList);
		}else if(CherryConstants.OPERATE_BG_EDIT.equals(flag)||"2".equals(flag)){
			//取得调入仓库
			String organizationID = String.valueOf(mainMap.get("BIN_OrganizationID"));
			form.setInDepotList(getDepotList(organizationID,userInfo.getLanguage()));
			
			//取得调入逻辑仓库
			List<Map<String,Object>> inLogicList = getLogicDepotList(CherryConstants.BUSINESS_TYPE_BG);
			form.setInLogicList(inLogicList);
		}
		form.setWorkFlowID(entryID);		
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
    	//取得用户信息
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	
		//取得URL中的参数信息
     	String entryID= request.getParameter("entryID");
     	String billID= request.getParameter("mainOrderID");
     	
     	form.setWorkFlowID(entryID);
    	
		// 参数MAP
		Map<String, Object> parmap = new HashMap<String, Object>();
		parmap.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		parmap.put("BIN_PromotionAllocationID", billID);
		// 调拨记录信息
		Map<String, Object> mainMap = binolsscm03_bl.getPromotionAllocationMain(parmap);
		form.setReturnInfo(mainMap);
		// 调拨详细记录LIST
		form.setReturnList(binolsscm03_bl.getPromotionAllocationDetail(parmap));
		
		//取得操作区分
		String employeeID = String.valueOf(mainMap.get("BIN_EmployeeID"));	
		String flag = getPageOperateFlag(entryID,employeeID,userInfo,mainMap);
		form.setOperateType(flag);
		
		if(CherryConstants.OPERATE_LG.equals(flag)){
			//取得调出方仓库
			form.setOutDepotList(getDepotList(String.valueOf(mainMap.get("BIN_OrganizationIDAccept")),userInfo.getLanguage()));
		
            //取得调出逻辑仓库
            List<Map<String,Object>> outLogicList = getLogicDepotList(CherryConstants.BUSINESS_TYPE_LG);
            form.setOutLogicList(outLogicList);
		}else if(CherryConstants.OPERATE_BG_EDIT.equals(flag)|| CherryConstants.OPERATE_BG_AUDIT.equals(flag) ||"2".equals(flag)){
			//取得调入仓库
			String organizationID = String.valueOf(mainMap.get("BIN_OrganizationID"));
			form.setInDepotList(getDepotList(organizationID,userInfo.getLanguage()));
		
            //取得调入逻辑仓库
            List<Map<String,Object>> inLogicList = getLogicDepotList(CherryConstants.BUSINESS_TYPE_BG);
            form.setInLogicList(inLogicList);
		}
		return SUCCESS;
	}
	
	/**
	 * 目前该明细画面有四种展现模式
	 * 1：明细查看模式【关闭】
	 * 2：非工作流的编辑模式【保存】【调入】【删除】【关闭】
	 * 71：工作流审核模式【通过】【退回】【关闭】
	 * 72：工作流编辑模式【保存】【调入】【删除】【关闭】
	 * 80：工作流中的调出模式  按钮有【调出】【关闭】
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
				if (CherryConstants.OPERATE_BG_AUDIT.equals(currentOperation)) {
					//发货单审核模式   按钮有【通过】【退回】【关闭】
					request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				} else if (CherryConstants.OPERATE_BG_EDIT.equals(currentOperation)) {
					//工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}else if (CherryConstants.OPERATE_LG.equals(currentOperation)) {
					//工作流中的调出模式  按钮有【调出】【关闭】
				    request.setAttribute("ActionDescriptor", adArr);
					ret= currentOperation;
				}
			}
		 }		 
		 return ret;
	}
	
	/**
	 * 工作流中的各种动作入口方法
	 * @return
	 * @throws Exception
	 */
	public String doaction() throws Exception{
	    try{
    		String entryID = request.getParameter("entryid").toString();
    		String actionID = request.getParameter("actionid").toString();
    		form.setEntryID(entryID);
    		form.setActionID(actionID);
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		binolssprm56BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002"));            
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else if (e instanceof WorkflowException) {
                this.addActionError(getText(e.getMessage()));
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }
            throw e;
        }
	}
    /**
     * 修改调入单，非工作流
     * @return
     * @throws JSONException
     */
    public String save() throws Exception{
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try{
    		binolssprm56BL.tran_saveAllocation(form,userInfo);
    	}catch(Exception ex){
    		if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw ex;
			}
    	}
    	this.addActionMessage(getText("ICM00002"));    	
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;        
    }
    /**
     * 提交调入单
     * @return
     * @throws JSONException
     */
    public String send() throws Exception{
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try{    		
    		binolssprm56BL.tran_sendAllocation(form,userInfo);
    	}catch(Exception ex){
    		if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else if (ex instanceof WorkflowException) {
                this.addActionError(getText(ex.getMessage()));
                return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw ex;
			}
    	}
    	this.addActionMessage(getText("ICM00002"));    	
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;        
    }
    
    /**
     * 删除调拨单数据
     * @return
     * @throws JSONException
     */
    public String delete() throws Exception{
    	UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try{
    		binolssprm56BL.tran_deleteAllocation(form,userInfo);
    	}catch(Exception ex){
    		if (ex instanceof CherryException) {
				CherryException temp = (CherryException) ex;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			} else {
				throw ex;
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
		// 调拨记录Id
		map.put("BIN_PromotionAllocationID", form.getProAllocationId());
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
	@Override
	public BINOLSSPRM56_Form getModel() {
		return form;
	}
	
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
     * 取得调出/调入逻辑仓库
     * @param organizationID
     * @param language
     * @param tradeType
     * @return
     * @throws Exception 
     */
    private List<Map<String, Object>> getLogicDepotList(String tradeType) throws Exception{
        //取得用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        
        Map<String,Object> logicParam = new HashMap<String,Object>();
        logicParam.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        logicParam.put("BusinessType", tradeType);
        logicParam.put("language", userInfo.getLanguage());
        //0：后台业务 1：终端业务
        logicParam.put("Type", "0");
        //1:正常产品 2：促销品
        logicParam.put("ProductType", "2");
        //共通取得逻辑仓库List
        List<Map<String,Object>> logicList = new ArrayList<Map<String,Object>>();
        try{
            logicList = binOLCM18_BL.getLogicDepotByBusiness(logicParam);
        }catch(Exception e){
            
        }
        return logicList;
    }
}
