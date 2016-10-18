/*  
 * @(#)BINOLSTBIL14_Action.java     1.0 2012/7/24      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM20_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL14_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品退库申请单详细Action
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
public class BINOLSTBIL14_Action extends BaseAction implements
ModelDriven<BINOLSTBIL14_Form>{

    private static final long serialVersionUID = 6518126125051124296L;

    /** 参数FORM */
    private BINOLSTBIL14_Form form = new BINOLSTBIL14_Form();

    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;

    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;

    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;

    @Resource(name="binOLSTBIL14_BL")
    private BINOLSTBIL14_IF binOLSTBIL14_BL;
    
    @Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
    
	@Resource(name="binOLCM20_BL")
	private BINOLCM20_IF binOLCM20_BL;

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
        int proReturnRequestID = 0;
        //判断是top页打开任务
        if(null == form.getProReturnRequestID() || "".equals(form.getProReturnRequestID())){
            //取得URL中的参数信息
            String entryID= request.getParameter("entryID");
            String billID= request.getParameter("mainOrderID");
            proReturnRequestID = Integer.parseInt(billID);
            form.setWorkFlowID(entryID);
            form.setProReturnRequestID(billID);
        }else{
            proReturnRequestID = CherryUtil.string2int(form.getProReturnRequestID());
        }

        String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
        
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        String organizationInfoID =  String.valueOf(userInfo.getBIN_OrganizationInfoID());
        String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
        
        //取得退库申请单概要信息 和详细信息
        Map<String,Object> mainMap = binOLSTCM13_BL.getProReturnRequestMainData(proReturnRequestID,language);
        Map<String,Object> otherParam = new HashMap<String,Object>();
        otherParam.put("BIN_OrganizationInfoID", organizationInfoID);
        otherParam.put("BIN_BrandInfoID", brandInfoId);
        List<Map<String,Object>> detailList = binOLSTCM13_BL.getProReturnReqDetailData(proReturnRequestID,language,otherParam);	
        
        //工作流相关操作  决定画面以哪种模式展现
        String workFlowID = ConvertUtil.getString(mainMap.get("WorkFlowID"));
        String operateFlag = getPageOperateFlag(workFlowID,mainMap);
        form.setWorkFlowID(workFlowID);
        form.setOperateType(operateFlag);
        
        if ("2".equals(operateFlag)
                || CherryConstants.OPERATE_RA_AUDIT.equals(operateFlag)
                || CherryConstants.OPERATE_RA_AUDIT2.equals(operateFlag)
                || CherryConstants.OPERATE_RA_AUDIT3.equals(operateFlag)
                || CherryConstants.OPERATE_RA_AUDIT4.equals(operateFlag)
                || CherryConstants.OPERATE_RA_LOGISTICSCONFIRM.equals(operateFlag)
                || CherryConstants.OPERATE_RA_CONFIRM.equals(operateFlag)
                || CherryConstants.OPERATE_RD.equals(operateFlag)) {
            //取得实体仓库List
            String organizationid = ConvertUtil.getString(mainMap.get("BIN_OrganizationIDReceive"));
            List<Map<String,Object>> depotsInfoList = binOLCM18_BL.getDepotsByDepartID(organizationid, language);
            form.setDepotsInfoList(depotsInfoList);
            
            Map<String,Object> logicPram =  new HashMap<String,Object>();
            logicPram.put("BIN_BrandInfoID", brandInfoId);
//            logicPram.put("BusinessType", CherryConstants.OPERATE_RR);
            logicPram.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
            logicPram.put("Type", "0");
            logicPram.put("language", language);
            logicPram.put("ProductType", "1");
            //取得逻辑仓库List
//            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusinessType(logicPram);
            List<Map<String,Object>> logicDepotsList = binOLCM18_BL.getLogicDepotByBusiness(logicPram);
            form.setLogicDepotsInfoList(logicDepotsList);
            
            // 获取产品库存
            int defaultDepotInfoID = 0;
    		int defaultLogicInventoryInfoID = 0;
            for(int i=0;i<detailList.size();i++){
	            Map<String,Object> temp = detailList.get(i);
	            Map<String,Object> pram = new HashMap<String,Object>();
	            pram.put("BIN_ProductVendorID", CherryUtil.obj2int(temp.get("BIN_ProductVendorID")));
	            String inventoryInfo = ConvertUtil.getString(temp.get("BIN_InventoryInfoID"));
	            if(null == inventoryInfo || "".equals(inventoryInfo)){
	                pram.put("BIN_DepotInfoID", defaultDepotInfoID);
	                temp.put("BIN_InventoryInfoID", defaultDepotInfoID);
	            }else{
	                pram.put("BIN_DepotInfoID", inventoryInfo);
	            }
	            String logicInventoryInfoID = ConvertUtil.getString(temp.get("BIN_LogicInventoryInfoID"));
	            if(null == logicInventoryInfoID || "".equals(logicInventoryInfoID)){
	                pram.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
	                temp.put("BIN_LogicInventoryInfoID", defaultLogicInventoryInfoID);
	            }else{
	                pram.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
	            }
	            // 不扣除冻结库存
	            pram.put("FrozenFlag", "2");
	            int stockQuantity = binOLCM20_BL.getProductStock(pram);
	            temp.put("StockQuantity", stockQuantity);
	            detailList.set(i, temp);
	        }
        }
        
        form.setProReturnReqMainData(mainMap);
        form.setProReturnReqDetailData(detailList);
        //配置项 产品发货使用价格（销售价格/会员价格/结算价格）
        String configValue = binOLCM14_BL.getConfigValue("1130", organizationInfoID, brandInfoId);
        if(configValue.equals("")){
            configValue = "SalePrice";
        }
        form.setSysConfigUsePrice(configValue);
        
        //取得系统配置项库存是否允许为负
        configValue = binOLCM14_BL.getConfigValue("1109", organizationInfoID, brandInfoId);
        form.setCheckStockFlag(configValue);
        
        return SUCCESS;
    }
    
    /**
     * 通过Ajax取得指定仓库的库存（多选）
     * @throws Exception
     */
    public void getStockCountByAjax() throws Exception{
        String[] productVendorIdArr = request.getParameterValues("productVendorId");
        String[] currentIndexArr = request.getParameterValues("currentIndex");
        
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        if(null != productVendorIdArr){
            for(int i=0;i<productVendorIdArr.length;i++){
                // 不扣除冻结库存
            	map.put("FrozenFlag", "2");
                map.put("BIN_ProductVendorID", productVendorIdArr[i]);
                map.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getInventoryInfoID()));
                map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicInventoryInfoID()));
                int stockCount = binOLCM20_BL.getProductStock(map);
                
                Map<String,Object> resultMap = new HashMap<String,Object>();
                resultMap.put("currentIndex", currentIndexArr[i]);
                resultMap.put("Quantity", stockCount);
                resultMap.put("BIN_ProductVendorID", productVendorIdArr[i]);
                resultList.add(resultMap);
            }
        }
        
        ConvertUtil.setResponseByAjax(response, resultList);
    }

    @Override
    public BINOLSTBIL14_Form getModel() {
        return form;
    }

    /**
     * 该明细画面展现模式
     * 1：明细查看模式
     * 2：非工作流的编辑模式
     * 131：工作流审核模式
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
                 if (CherryConstants.OPERATE_RA_AUDIT.equals(currentOperation)
                        || CherryConstants.OPERATE_RA_AUDIT2.equals(currentOperation)
                        || CherryConstants.OPERATE_RA_AUDIT3.equals(currentOperation)
                        || CherryConstants.OPERATE_RA_AUDIT4.equals(currentOperation)) {
                     //退库申请单审核/二审/三审/四审模式   按钮有【同意】【修改】【废弃】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 }else if(CherryConstants.OPERATE_RA_LOGISTICSCONFIRM.equals(currentOperation)){
                     //退库申请单物流确认模式   按钮有【同意】【修改】【废弃】【关闭】
                     request.setAttribute("ActionDescriptor", adArr);
                     ret= currentOperation;
                 }else if(CherryConstants.OPERATE_RA_CONFIRM.equals(currentOperation)){
                     //按钮只能在RJ审核后申请单显示
                     String tradeType = ConvertUtil.getString(mainData.get("TradeType"));
                     if(tradeType.equals(CherryConstants.OS_BILLTYPE_RJ)){
                         //确认退库模式   按钮有【确认退库】【废弃】【关闭】
                         request.setAttribute("ActionDescriptor", adArr);
                         ret= currentOperation;
                     }
                 }else if(CherryConstants.OPERATE_RD.equals(currentOperation)){
                     //退库申请单收货模式   按钮有【收货】
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
            binOLSTBIL14_BL.tran_save(form, userInfo);
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
        	if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            binOLSTBIL14_BL.tran_submit(form,userInfo);
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
        String actionID = request.getParameter("actionid").toString();
        boolean isNeedCheck = !"902".equals(actionID) && !"504".equals(actionID) && !"554".equals(actionID) && !"525".equals(actionID);
        //取得系统配置项库存是否允许为负
        boolean configValue = binOLCM14_BL.isConfigOpen("1109", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID());
        if(!configValue && isNeedCheck){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("BIN_DepotInfoID", CherryUtil.obj2int(form.getInventoryInfoID()));
            paramMap.put("BIN_LogicInventoryInfoID",CherryUtil.obj2int(form.getLogicInventoryInfoID()));
            paramMap.put("FrozenFlag", "1");// 不扣除冻结库存
            paramMap.put("ProductType", "1");// 产品
            paramMap.put("IDArr", form.getPrtVendorId());
            paramMap.put("QuantityArr", form.getQuantityArr());
            isCorrect = binOLCM20_BL.isStockGTQuantity(paramMap);
            if(!isCorrect){
            	// 退库数量大于当前库存数量，不允许退库！
                this.addActionError(getText("EST00044"));
                return isCorrect;
            }
        }

        return isCorrect;
    }
    
    /**
     * 工作流中的各种动作入口方法
     * @return
     * @throws Exception
     */
    public String doaction() throws Exception{
        try{
            if(!validateDoaction()){
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            
            if (!validateStock()) {
                return CherryConstants.GLOBAL_ACCTION_RESULT;
            }
            String entryID = request.getParameter("entryid").toString();
            String actionID = request.getParameter("actionid").toString();
            form.setEntryID(entryID);
            form.setActionID(actionID);
            UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
            binOLSTBIL14_BL.tran_doaction(form,userInfo);
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
    public boolean validateDoaction() throws Exception{
        boolean flag = true;
        if(null == form.getOrganizationIDReceive() || "".equals(form.getOrganizationIDReceive())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00015")}));
            flag = false;
            return flag;
        }
        if(null == form.getInventoryInfoIDReceive() || "".equals(form.getInventoryInfoIDReceive())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00016")}));
            flag = false;
            return flag;
        }
        if(null == form.getLogicInventoryInfoIDReceive() || "".equals(form.getLogicInventoryInfoIDReceive())){
            this.addActionError(getText("EST00021",new String[]{getText("PST00017")}));
            flag = false;
            return flag;
        }
        if(null == form.getPrtVendorId() || form.getPrtVendorId().length == 0){
            this.addActionError(getText("EST00022"));
            flag = false;
            return flag;
        }
        for(int i=0;i<form.getPrtVendorId().length;i++){
            if(null == form.getQuantityArr()[i] || "".equals(form.getQuantityArr()[i]) || CherryUtil.obj2int(form.getQuantityArr()[i]) == 0){
                this.addActionError(getText("EST00008"));
                flag = false;
                return flag;
            }
        }
        return flag;
    }
}
