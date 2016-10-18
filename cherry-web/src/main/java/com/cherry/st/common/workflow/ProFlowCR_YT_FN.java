/*  
 * @(#)ProFlowCR_YT_FN.java    1.0 2013.03.22
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
package com.cherry.st.common.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.common.bl.BINOLSTCM06_BL;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.service.BINOLSTCM14_Service;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 
 * 颖通产品盘点申请工作流
 * 
 * @author niushunjie
 * @version 1.0 2013.03.22
 */
public class ProFlowCR_YT_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM06_BL")
    private BINOLSTCM06_BL binOLSTCM06_BL;
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;
    
    @Resource(name="binOLSTCM14_Service")
    private BINOLSTCM14_Service binOLSTCM14_Service;

    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        if("startFlow".equals(method)){
            startFlow(arg0,  arg1,  propertyset);
        }else if("submitHandle".equals(method)){
            submitHandle(arg0,  arg1,  propertyset);
        }else if("updateCR".equals(method)){
            updateCR(arg0,  arg1,  propertyset);
        }else if("deleteCR".equals(method)){
            deleteCR(arg0,  arg1,  propertyset);
        }else if("createCJ".equals(method)){
            createCJ(arg0,  arg1,  propertyset);
        }else if("sendAuditMQ".equals(method)){
            try {
                sendAuditMQ(arg0,  arg1,  propertyset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if("changeCounterStock".equals(method)){
            changeCounterStock(arg0,  arg1,  propertyset);
        }else if("updateCJDetail".equals(method)){
            updateCJDetail(arg0,  arg1,  propertyset);
        }else if("updateCA".equals(method)){
            updateCA(arg0,  arg1,  propertyset);
        }else if("deleteCJ".equals(method)){
            deleteCJ(arg0,  arg1,  propertyset);
        }
    }

    /**
     * 插入产品盘点申请单主表和明细表数据，启动工作流
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String,Object> stocktakeReqMainData = (Map<String,Object>)mainData.get("stocktakeReqMainData");
        List<Map<String,Object>> stocktakeReqDetailList = (List<Map<String,Object>>)mainData.get("stocktakeReqDetailList");
       
        stocktakeReqMainData.put("WorkFlowID", entryID);
        int proStocktakeRequestID = binOLSTCM14_BL.insertProStocktakeRequestAll(stocktakeReqMainData, stocktakeReqDetailList);
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, proStocktakeRequestID);
        mainData.put("BillNo", stocktakeReqMainData.get("StockTakingNoIF"));
        mainData.put("TableName", "Inventory.BIN_ProStocktakeRequest");
        
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, ConvertUtil.getString(proStocktakeRequestID));
        propertyset.setInt("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        propertyset.setInt("CR_BIN_ProStocktakeRequestID", proStocktakeRequestID);
   
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_CR);
        paramMap.put("OpCode", CherryConstants.OPERATE_CR_CREATE);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowCR_YT_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
    }

    /**
     * 产品盘点申请单提交
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void submitHandle(Map arg0, Map arg1, PropertySet propertyset) throws InvalidInputException, WorkflowException {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String mqFlag = ConvertUtil.getString(mainData.get("CurrentUnit"));
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        //取出当前有效的action
        int[] actionArr = workflow.getAvailableActions(osid, null);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osid));
        if("MQ".equals(mqFlag) || "BINOLWSMNG07".equals(mqFlag)){
            //来自MQ或者来自云POS的单据自动提交
            for (int j = 0; j < actionArr.length; j++) {
                ActionDescriptor ad = wd.getAction(actionArr[j]);
                //取得当前Action下的meta元素集合
                Map<String,Object> metaMap = ad.getMetaAttributes();
                //找到带有OS_DefaultAction元素的action
                if(metaMap.containsKey("OS_DefaultAction")){
                    Map<String, Object> input = new HashMap<String, Object>();
                    input.put("mainData", arg0.get("mainData"));
                    workflow.doAction_single(osid, ad.getId(), input);
                }
            }
        }
    }
    
    /**
     * 修改产品盘点申请单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateCR(Map arg0, Map arg1, PropertySet propertyset) {
        String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag"));
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        int proStocktakeRequestID = 0;
        if(tradeType.equals(CherryConstants.OS_BILLTYPE_CR)){
            proStocktakeRequestID = propertyset.getInt("CR_BIN_ProStocktakeRequestID");
        }else if(tradeType.equals(CherryConstants.OS_BILLTYPE_CJ)){
            proStocktakeRequestID = propertyset.getInt("CJ_BIN_ProStocktakeRequestID");
        }else{
            proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
        }
        pramData.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        
        // 系统配置项取得处理参数
        Map<String, Object> proStockRequest = binOLSTCM14_BL.getProStocktakeRequestMainData(proStocktakeRequestID,null);
        mainData.put("BIN_OrganizationID", proStockRequest.get("BIN_OrganizationID"));
        
        if(!"".equals(verifiedFlag)){
            pramData.put("VerifiedFlag", verifiedFlag);
        }
        //同步数据标志
        if(!"".equals(synchFlag)){
            pramData.put("SynchFlag", synchFlag);
        }
        if(CherryConstants.CRAUDIT_FLAG_AGREE.equals(verifiedFlag) 
                || CherryConstants.CRAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)
                || CherryConstants.CRAUDIT_FLAG_AGREE2.equals(verifiedFlag)){
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM14_BL.updateProStocktakeRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 废弃产品盘点申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteCR(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        int proStocktakeRequestID = 0;
        if(tradeType.equals(CherryConstants.OS_BILLTYPE_CR)){
            proStocktakeRequestID = propertyset.getInt("CR_BIN_ProStocktakeRequestID");
        }else if(tradeType.equals(CherryConstants.OS_BILLTYPE_CJ)){
            proStocktakeRequestID = propertyset.getInt("CJ_BIN_ProStocktakeRequestID");
        }else{
            proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
        }
        //盘点申请单主表ID
        pramData.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM14_BL.updateProStocktakeRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 新建产品盘点确认单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void createCJ(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        
        BINOLSTBIL16_Form form = (BINOLSTBIL16_Form) mainData.get("ProStocktakeRequestForm");
        if(null != form){
            int billID = propertyset.getInt("BIN_ProStocktakeRequestID");
            pramData.put("BIN_ProStocktakeRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            pramData.put("ProStocktakeRequestForm", form);
            pramData.put("VerifiedFlag", verifiedFlag);
            int newBillID = binOLSTCM14_BL.createProStocktakeRequestByForm(pramData);
                
            propertyset.setInt("BIN_ProStocktakeRequestID", newBillID);
            propertyset.setInt("CJ_BIN_ProStocktakeRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_CR);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }else{
            int billID = propertyset.getInt("BIN_ProStocktakeRequestID");
            pramData.put("BIN_ProStocktakeRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("VerifiedFlag", verifiedFlag);
            int newBillID = binOLSTCM14_BL.createProStocktakeRequest(pramData);
                
            propertyset.setInt("BIN_ProStocktakeRequestID", newBillID);
            propertyset.setInt("CJ_BIN_ProStocktakeRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_CR);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }
    }
       
    /**
     * 更改柜台库存（根据盘点单）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void changeCounterStock(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProStocktakeRequestID", propertyset.getInt("BIN_ProStocktakeRequestID"));
        pramData.put("BIN_ProductStockTakingID", propertyset.getInt("BIN_ProductStockTakingID"));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        pramData.put("isCounter", "YES");
        pramData.put("TradeDateTime", mainData.get("TradeDateTime"));
        binOLSTCM14_BL.changeStockByCA(pramData);
    }
       
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws Exception 
     * @throws PropertyException 
     */
    private void sendAuditMQ(Map arg0, Map arg1, PropertySet propertyset) throws Exception{
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int proStocktakeRequestID = 0;
        if(CherryConstants.CRAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)){
            proStocktakeRequestID = propertyset.getInt("CR_BIN_ProStocktakeRequestID");
        }else{
            proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
        }
        Map<String, Object> proStockRequest = binOLSTCM14_BL.getProStocktakeRequestMainData(proStocktakeRequestID,null);
        String organizationID = String.valueOf(proStockRequest.get("BIN_OrganizationID"));
                
        //如果是发送盘点审核到柜台，则发送MQ
        if(binOLSTCM07_BL.checkOrganizationType(organizationID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(proStockRequest.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(proStockRequest.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            binOLSTCM14_BL.sendMQ(new int[]{proStocktakeRequestID}, mqMap);
            
            //写入确认盘点
            propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_CR_CONFIRM, CherryConstants.OS_ACTOR_TYPE_DEPART+organizationID);
        }
    }
    
    /**
     * 更新盘点确认单明细（先删后插）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateCJDetail(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        
        BINOLSTBIL16_Form form = (BINOLSTBIL16_Form) mainData.get("ProStocktakeRequestForm");
        if(null == form){
            return;
        }
        
        int billID = propertyset.getInt("BIN_ProStocktakeRequestID");
        pramData.put("BIN_ProStocktakeRequestID", billID);
        binOLSTCM14_Service.deleteProStocktakeRequestDetail(pramData);
        
        String createdBy = ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        String createPGM =ConvertUtil.getString(mainData.get("CurrentUnit"));
        
        //一次盘点申请操作的总数量（始终为正）
        int totalQuantity = 0;
        //总金额
        double totalAmount = 0.0;
        String inventoryInfoID = form.getInventoryInfoID();
        String logicInventoryInfoID = form.getLogicInventoryInfoID();
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] bookQuantityArr = form.getBookQuantityArr();
        String[] checkQuantityArr = form.getCheckQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] commentsArr = form.getCommentsArr();
        String[] handleTypeArr = form.getHandleTypeArr();
        List<Map<String,Object>> proStocktakeReqDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<productVendorIDArr.length;i++){
            int gainQuantity = CherryUtil.string2int(checkQuantityArr[i]) - CherryUtil.string2int(bookQuantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*gainQuantity;
            totalAmount += money;
            totalQuantity += gainQuantity;
            
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProStocktakeRequestID", billID);
            detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
            detailMap.put("BookQuantity", bookQuantityArr[i]);
            detailMap.put("CheckQuantity", checkQuantityArr[i]);
            detailMap.put("GainQuantity", gainQuantity);
            detailMap.put("Price", priceUnitArr[i]);
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", inventoryInfoID);
            detailMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", commentsArr[i]);
            detailMap.put("HandleType", handleTypeArr[i]);
            detailMap.put("CreatedBy", createdBy);
            detailMap.put("CreatePGM", createPGM);
            detailMap.put("UpdatedBy", createdBy);
            detailMap.put("UpdatePGM", createPGM);
            proStocktakeReqDetail.add(detailMap);
        }
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProStocktakeRequestID", billID);
        praMap.put("TotalQuantity", totalQuantity);
        praMap.put("TotalAmount", totalAmount);
        praMap.put("UpdatedBy", createdBy);
        praMap.put("UpdatePGM", createPGM);
        binOLSTCM14_BL.updateProStocktakeRequest(praMap);
        
        binOLSTCM14_Service.insertProStocktakeRequestDetail(proStocktakeReqDetail);
    }
    
    /**
     * 更新盘点单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateCA(Map arg0, Map arg1, PropertySet propertyset){
        String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag"));
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductStockTakingID", propertyset.getInt("BIN_ProductStockTakingID"));
        //同步数据标志
        if(!"".equals(synchFlag)){
            pramData.put("SynchFlag", synchFlag);
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM06_BL.updateStockTakingMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 删除产品盘点申请确认单，这个方法用于生成确认单之后，需要回到生成确认单之前的步骤。
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteCJ(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        int proStocktakeRequestID = 0;
        proStocktakeRequestID = propertyset.getInt("CJ_BIN_ProStocktakeRequestID");

        //盘点申请单主表ID
        pramData.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM14_BL.updateProStocktakeRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
        
        propertyset.setInt("BIN_ProStocktakeRequestID",propertyset.getInt("CR_BIN_ProStocktakeRequestID"));
        propertyset.remove("CJ_BIN_ProStocktakeRequestID");
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_CR);
    }
}