/*  
 * @(#)BINOLSTBIL06_Action.java     1.0 2011/10/20      
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
package com.cherry.st.bil.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL06_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品报损单详细Action
 * @author niushunjie
 * @version 1.0 2011.10.20
 */
public class BINOLSTBIL06_Action extends BaseAction implements
ModelDriven<BINOLSTBIL06_Form>{
	
    private static final long serialVersionUID = -4019348084644424175L;

    @Resource(name="binOLSTCM05_BL")
	private BINOLSTCM05_IF binOLSTCM05_BL;
	
	/** 参数FORM */
	private BINOLSTBIL06_Form form = new BINOLSTBIL06_Form();
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;
	
	@Resource(name="binOLSTBIL06_BL")
	private BINOLSTBIL06_IF binOLSTBIL06_BL;

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws JSONException 
	 * 
	 */
	public String init() throws Exception {
		int outboundFreeID = 0;
		//判断是top页打开任务
		if(null == form.getOutboundFreeID() || "".equals(form.getOutboundFreeID())){
			//取得URL中的参数信息
	     	String entryID= request.getParameter("entryID");
	     	String billID= request.getParameter("mainOrderID");
	     	outboundFreeID = Integer.parseInt(billID);
	     	form.setWorkFlowID(entryID);
	     	form.setOutboundFreeID(billID);
		}else{
		    outboundFreeID = CherryUtil.string2int(form.getOutboundFreeID());
		}
		
		String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
		//取得报损单概要信息 和详细信息
		Map<String,Object> mainMap = binOLSTCM05_BL.getOutboundFreeMainData(outboundFreeID,language);
		List<Map<String,Object>> detailList = binOLSTCM05_BL.getOutboundFreeDetailData(outboundFreeID,language);		
		
		//取详单第一条仓库ID、逻辑仓库ID
        if(null != detailList && detailList.size()>0){
            mainMap.put("BIN_DepotInfoID", detailList.get(0).get("BIN_DepotInfoID"));
            mainMap.put("BIN_LogicInventoryInfoID", detailList.get(0).get("BIN_LogicInventoryInfoID"));
            mainMap.put("DepotCodeName", detailList.get(0).get("DepotCodeName"));
            mainMap.put("LogicInventoryName", detailList.get(0).get("LogicInventoryName"));
        }
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        
		//工作流相关操作  决定画面以哪种模式展现
		String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
		String operateFlag = getPageOperateFlag(workFlowID,mainMap);
		form.setWorkFlowID(workFlowID);
		form.setOperateType(operateFlag);

        if("2".equals(operateFlag) || CherryConstants.OPERATE_LS_EDIT.equals(operateFlag)){
            //取得实体仓库List
            String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationID"));
            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
            form.setDepotsInfoList(depotsInfoList);
            
            Map<String,Object> logicPram =  new HashMap<String,Object>();
            logicPram.put("BIN_BrandInfoID", brandInfoId);
//            logicPram.put("BusinessType", CherryConstants.OPERATE_LS);
            logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_LS);
            logicPram.put("Type", "0");
            logicPram.put("language", language);
            logicPram.put("ProductType", "1");
            //取得逻辑仓库List
//            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
            form.setLogicDepotsInfoList(logicDepotsList);
        }
        
        form.setOutboundFreeMainData(mainMap);
        
		form.setOutboundFreeDetailData(detailList);
		
		return SUCCESS;
	}
	
    @Override
    public BINOLSTBIL06_Form getModel() {
        return form;
    }
	
	/**
	 * 目前该明细画面有四种展现模式
	 * 1：明细查看模式
	 * 2：非工作流的编辑模式
	 * 111：工作流审核模式
	 * 112：工作流编辑模式
	 * 
	 * @param workFlowID 工作流ID
	 * @param mainData 主单数据
	 * @return
	 */
	private String getPageOperateFlag(String workFlowID,Map<String,Object> mainData) {
		//查看明细模式  按钮有【关闭】
		String ret="1";
		 if(null==workFlowID||"".equals(workFlowID)){
             //当审核状态为审核通过时为operateFlag=1，查看明细模式
             String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
             if(!CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
                 //如果没有工作流实例ID，则说明是草稿状态的订单 ，为非工作流的编辑模式
                 //按钮有【保存】【提交】【删除】【关闭】
                 ret="2";
             }
		 }else{
		     // 用户信息
		     UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			 //取得当前可执行的action
			 ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(Long.parseLong(workFlowID), userInfo);
			 if (adArr != null && adArr.length > 0) {
			     // 如果存在可执行action，说明工作流尚未结束
			     // 取得当前的业务操作
			     String currentOperation = binOLCM19_BL.getCurrentOperation(Long.parseLong(workFlowID));
			     if (CherryConstants.OPERATE_LS_AUDIT.equals(currentOperation)) {
			         //报损单审核模式   按钮有【通过】【退回】【删除】【关闭】
			         request.setAttribute("ActionDescriptor", adArr);
			         ret= currentOperation;
			     } else if (CherryConstants.OPERATE_LS_EDIT.equals(currentOperation)) {
			         //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
			         request.setAttribute("ActionDescriptor", adArr);
			         ret= currentOperation;
			     }else if (CherryConstants.OPERATE_LS_AUDIT2.equals(currentOperation)) {
                     //报损单二审模式   按钮有【通过】【退回】【删除】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
			     }
			 }
		 }		 
		 return ret;
	}
	
	/**
	 * 非工作流中的保存订单
	 * @return
	 * @throws Exception 
	 */
	public String save() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    try{
	        binOLSTBIL06_BL.tran_save(form, userInfo);
	    }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
	    }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
	}
	
	/**
     * 非工作流中的删除订单
     * @return
	 * @throws Exception 
     */
	public String delete() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL06_BL.tran_delete(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
	}

	/**
     * 非工作流中的提交订单
     * @return
	 * @throws Exception 
     */
	public String submit() throws Exception{
	    UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        try{
            binOLSTBIL06_BL.tran_submit(form,userInfo);
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            } else {
                throw e;
            }
        }
        this.addActionMessage(getText("ICM00002"));     
        return CherryConstants.GLOBAL_ACCTION_RESULT_BODY; 
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
    		binOLSTBIL06_BL.tran_doaction(form,userInfo);
            this.addActionMessage(getText("ICM00002")); 
            return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
        }catch(Exception e){
            if (e instanceof CherryException) {
                CherryException temp = (CherryException) e;
                this.addActionError(temp.getErrMessage());
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }
            throw e;
        }
	}
	
    /**
     * 通过Ajax取得指定部门所拥有的仓库
     * @throws Exception
     */
    public void getDepotByAjax() throws Exception{
        String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
        String organizationid = request.getParameter("organizationid");
        List<Map<String,Object>> list = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
        ConvertUtil.setResponseByAjax(response, list);
    }
    
    /**
     * 通过Ajax取得指定仓库的库存
     * @throws Exception
     */
    public void getStockCountByAjax() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductVendorID", request.getParameter("productVendorId"));
        map.put("BIN_DepotInfoID", request.getParameter("depotInfoId"));
        map.put("BIN_LogicInventoryInfoID", request.getParameter("logicDepotsInfoId"));
        map.put("FrozenFlag", "1");
        int stockCount = binOLCM20_BL.getProductStock(map);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("currentIndex", request.getParameter("currentIndex"));
        resultMap.put("Quantity", stockCount);
        resultMap.put("hasproductflag", 1);
        resultList.add(resultMap);
        ConvertUtil.setResponseByAjax(response, resultList);
    }

    /**
     * 验证数据
     */
    public void validateDoaction(){
        String actionID = request.getParameter("actionid").toString();
        if("71".equals(actionID)){
            if(null == form.getDepotInfoID() || "".equals(form.getDepotInfoID())){
                this.addActionError(getText("EST00021",new String[]{getText("PST00009")}));
                return;
            }
            if(null == form.getProductVendorIDArr() || form.getProductVendorIDArr().length <= 0){
                this.addActionError(getText("EST00022"));
                return;
            }
            for(int i=0;i<form.getProductVendorIDArr().length;i++){
                if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || !CherryChecker.isPositiveAndNegative(form.getQuantityArr()[i]) || "0".equals(form.getQuantityArr()[i]) ){
                    this.addActionError(getText("EST00008"));
                    return;
                }
            }
        }
    }
}
