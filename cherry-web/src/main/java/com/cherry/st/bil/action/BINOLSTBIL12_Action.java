/*  
 * @(#)BINOLSTBIL12_Action.java     1.0 2011/11/2      
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL12_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品退库单详细Action
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
public class BINOLSTBIL12_Action extends BaseAction implements
ModelDriven<BINOLSTBIL12_Form>{

    private static final long serialVersionUID = -3918104129279178179L;

    @Resource(name="binOLSTCM09_BL")
	private BINOLSTCM09_IF binOLSTCM09_BL;
	
	/** 参数FORM */
	private BINOLSTBIL12_Form form = new BINOLSTBIL12_Form();
	
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLSTBIL12_BL")
	private BINOLSTBIL12_IF binOLSTBIL12_BL;

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
		int productReturnID = 0;
		//判断是top页打开任务
		if(null == form.getProductReturnID() || "".equals(form.getProductReturnID())){
			//取得URL中的参数信息
	     	String entryID= request.getParameter("entryID");
	     	String billID= request.getParameter("mainOrderID");
	     	productReturnID = Integer.parseInt(billID);
	     	form.setWorkFlowID(entryID);
	     	form.setProductReturnID(billID);
		}else{
		    productReturnID = CherryUtil.string2int(form.getProductReturnID());
		}
		
		String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
		//取得退库单概要信息 和详细信息
		Map<String,Object> mainMap = binOLSTCM09_BL.getProductReturnMainData(productReturnID,language);
		List<Map<String,Object>> detailList = binOLSTCM09_BL.getProductReturnDetailData(productReturnID,language);		
		
		//设置退库仓库、退库逻辑仓库、接受退库仓库、接受逻辑仓库
		Map<String,Object> inventoryInfo = binOLSTBIL12_BL.getInventoryInfo(mainMap,detailList);
		mainMap.put("BIN_DepotInfoID", inventoryInfo.get("BIN_DepotInfoID"));
		mainMap.put("BIN_LogicInventoryInfoID", inventoryInfo.get("BIN_LogicInventoryInfoID"));
		mainMap.put("DepotCodeName", inventoryInfo.get("DepotCodeName"));
		mainMap.put("LogicInventoryCodeName", inventoryInfo.get("LogicInventoryCodeName"));
        mainMap.put("BIN_DepotInfoIDReceive", inventoryInfo.get("BIN_DepotInfoIDReceive"));
        mainMap.put("BIN_LogicInventoryInfoIDReceive", inventoryInfo.get("BIN_LogicInventoryInfoIDReceive"));
        mainMap.put("DepotCodeNameReceive", inventoryInfo.get("DepotCodeNameReceive"));
        mainMap.put("LogicInventoryCodeNameReceive", inventoryInfo.get("LogicInventoryCodeNameReceive"));
		
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        
		//工作流相关操作  决定画面以哪种模式展现
		String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
		String operateFlag = getPageOperateFlag(workFlowID,mainMap);
		form.setWorkFlowID(workFlowID);
		form.setOperateType(operateFlag);

        if("2".equals(operateFlag) || CherryConstants.OPERATE_RR_EDIT.equals(operateFlag) || CherryConstants.OPERATE_RR_AUDIT.equals(operateFlag)){
            //取得接受退库实体仓库List
            String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDReceive"));
            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
            form.setDepotsInfoList(depotsInfoList);
            
            if(null != depotsInfoList && depotsInfoList.size()>0){
                mainMap.put("BIN_DepotInfoIDReceive", depotsInfoList.get(0).get("BIN_DepotInfoID"));
                mainMap.put("DepotCodeNameReceive", depotsInfoList.get(0).get("DepotCodeName"));
            }
            
            Map<String,Object> logicPram =  new HashMap<String,Object>();
            logicPram.put("BIN_BrandInfoID", brandInfoId);
//            logicPram.put("BusinessType", CherryConstants.OPERATE_RR);
            logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
            logicPram.put("Type", "0");
            logicPram.put("language", language);
            logicPram.put("ProductType", "1");
            //取得接受退库逻辑仓库List
//            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
            form.setLogicDepotsInfoList(logicDepotsList);
            
            if(null != logicDepotsList && logicDepotsList.size()>0){
                mainMap.put("BIN_LogicInventoryInfoIDReceive", logicDepotsList.get(0).get("BIN_LogicInventoryInfoID"));
                mainMap.put("LogicInventoryCodeNameReceive", logicDepotsList.get(0).get("LogicInventoryCodeName")); 
            }
        }
        
        form.setProductReturnMainData(mainMap);
        
        DecimalFormat df=new DecimalFormat("0.00");
        
        /** 根据产品的总成本价计算对应的平均成本价*/
        for(Map<String, Object> detail:detailList){
        	if(detail.get("totalCostPrice1")==null || detail.get("totalCostPrice1") ==""){
        		detail.put("costPrice1", "");
        	}else{
        		double totalCostPrice1 = Double.valueOf(detail.get("totalCostPrice1").toString());
        		int quantity = Integer.valueOf(detail.get("Quantity").toString());
        		if(quantity == 0){
        			detail.put("costPrice1", "");
				}else{					
					detail.put("costPrice1", df.format(totalCostPrice1/quantity));//默认四舍五入
				}        		
        	}
        }
        
		form.setProductReturnDetailData(detailList);
		
		return SUCCESS;
	}
	
    @Override
    public BINOLSTBIL12_Form getModel() {
        return form;
    }
	
	/**
	 * 目前该明细画面有四种展现模式
	 * 1：明细查看模式
	 * 2：非工作流的编辑模式
	 * 61：工作流审核模式
	 * 62：工作流编辑模式
	 * 
	 * @param workFlowID 工作流ID
	 * @param mainData 主单数据
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
			     if (CherryConstants.OPERATE_RR_AUDIT.equals(currentOperation)) {
			         //退库单审核模式   按钮有【通过】【退回】【删除】【关闭】
			         request.setAttribute("ActionDescriptor", adArr);
			         ret= currentOperation;
			     } else if (CherryConstants.OPERATE_RR_EDIT.equals(currentOperation)) {
			         //工作流中的编辑模式  按钮有【保存】【提交】【删除】【关闭】
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
	        binOLSTBIL12_BL.tran_save(form, userInfo);
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
            binOLSTBIL12_BL.tran_delete(form,userInfo);
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
            binOLSTBIL12_BL.tran_submit(form,userInfo);
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
            binOLSTBIL12_BL.tran_doaction(form,userInfo);
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
     * 验证数据
     */
    public void validateDoaction() throws Exception{
        boolean flag = true;
        if(null == form.getOrganizationID() || "".equals(form.getOrganizationID())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00003")}));
            flag = false;
        }
        if(flag){
            if(null == form.getOrganizationIDReceive() || "".equals(form.getOrganizationIDReceive())){
                this.addActionError(getText("EST00021",new String[]{getText("PST00004")}));
                flag = false;
            }
            if(flag){
                if(null == form.getInventoryInfoIDReceive() || "".equals(form.getInventoryInfoIDReceive())){
                    this.addActionError(getText("EST00021",new String[]{getText("PST00016")}));
                    flag = false;
                }
            }
            if(flag){
                if(null == form.getLogicInventoryInfoIDReceive() || "".equals(form.getLogicInventoryInfoIDReceive())){
                    this.addActionError(getText("EST00021",new String[]{getText("PST00017")}));
                    flag = false;
                }
            }
            if(flag){
                if(form.getProductVendorIDArr().length <= 0){
                    this.addActionError(getText("EST00022"));
                    flag = false;
                }
                if(flag){
                    for(int i=0;i<form.getProductVendorIDArr().length;i++){
                        if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i])){
                            this.addActionError(getText("EST00008"));
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
    }
}
