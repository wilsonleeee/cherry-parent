/*  
 * @(#)ProFlowRA_FN.java    1.0 2012.07.24
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.bl.BINOLSTCM09_BL;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.service.BINOLSTCM13_Service;
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
 * 产品退库申请工作流
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
public class ProFlowRA_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_BL binOLSTCM09_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTCM13_Service")
    private BINOLSTCM13_Service binOLSTCM13_Service;

    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        if("startFlow".equals(method)){
            startFlow(arg0,  arg1,  propertyset);
        }else if("submitHandle".equals(method)){
            submitHandle(arg0,  arg1,  propertyset);
        }else if("updateRA".equals(method)){
            updateRA(arg0,  arg1,  propertyset);
        }else if("deleteRA".equals(method)){
            deleteRA(arg0,  arg1,  propertyset);
        }else if("createRA".equals(method)){
            createRA(arg0,  arg1,  propertyset);
        }else if("RA_changeStock".equals(method)){
            RA_changeStock(arg0,  arg1,  propertyset);
        }else if("sendAuditMQ".equals(method)){
            try {
                sendAuditMQ(arg0,  arg1,  propertyset);
            } catch (PropertyException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if("changeCounterStock".equals(method)){
            changeCounterStock(arg0,  arg1,  propertyset);
        }else if("changeReceiveStock".equals(method)){
            changeReceiveStock(arg0,  arg1,  propertyset);
        }else if("updateRADetail".equals(method)){
            updateRADetail(arg0,  arg1,  propertyset);
        }
    }

    /**
     * 插入产品退库申请单主表和明细表数据（MQ），后台退库申请到这里时已经有BillID，启动工作流
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String,Object> returnReqMainData = (Map<String,Object>)mainData.get("returnReqMainData");
        List<Map<String,Object>> returnReqDetailList = (List<Map<String,Object>>)mainData.get("returnReqDetailList");
        int proReturnRequestID = CherryUtil.obj2int(mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        if(proReturnRequestID>0){
            //来自后台
            returnReqMainData = binOLSTCM13_BL.getProReturnRequestMainData(proReturnRequestID, null);
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_ProReturnRequestID", proReturnRequestID);
            param.put("WorkFlowID", entryID);
            param.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            param.put("UpdatePGM", mainData.get("CurrentUnit"));
            binOLSTCM13_BL.updateProReturnRequest(param);
        }else{
            //来自MQ
            returnReqMainData.put("WorkFlowID", entryID);
            proReturnRequestID = binOLSTCM13_BL.insertProReturnRequestAll(returnReqMainData, returnReqDetailList);
        }
        
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, proReturnRequestID);
        mainData.put("BillNo", returnReqMainData.get("BillNoIF"));
        mainData.put("TableName", "Inventory.BIN_ProReturnRequest");
        
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, ConvertUtil.getString(proReturnRequestID));
        propertyset.setInt("BIN_ProReturnRequestID", proReturnRequestID);
        propertyset.setInt("RA_BIN_ProReturnRequestID", proReturnRequestID);
   
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
        //流程模型
        propertyset.setString("OS_Model", ConvertUtil.getString(returnReqMainData.get("Model")));
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_RA);
        paramMap.put("OpCode", CherryConstants.OPERATE_RA_CREATE);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowRA_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
    }

    /**
     * 产品退库申请单提交
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
        if("MQ".equals(mqFlag)){
            //来自MQ的单据自动提交
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
        }else{
            //来自后台
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
     * 修改产品退库申请单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateRA(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag")); 
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProReturnRequestID", propertyset.getInt("BIN_ProReturnRequestID"));
        if(!"".equals(verifiedFlag)){
            pramData.put("VerifiedFlag", verifiedFlag);
        }
        if (CherryConstants.RAAUDIT_FLAG_AGREE.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_AGREE2.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_SUBMIT3.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_AGREE3.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_SUBMIT4.equals(verifiedFlag)
                || CherryConstants.RAAUDIT_FLAG_AGREE4.equals(verifiedFlag)) {
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        Map<String,Object> returnReqMainData = (Map<String, Object>) mainData.get("returnReqMainData");
        if(null != returnReqMainData && !returnReqMainData.isEmpty()){
            String totalQuantity = ConvertUtil.getString(returnReqMainData.get("TotalQuantity"));
            String totalAmount = ConvertUtil.getString(returnReqMainData.get("TotalAmount"));
            if(!"".equals(totalQuantity)){
                pramData.put("TotalQuantity", totalQuantity);
            }
            if(!"".equals(totalAmount)){
                pramData.put("TotalAmount", totalAmount);
            }
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        //同步数据标志
        if(!"".equals(synchFlag)){
            pramData.put("SynchFlag", synchFlag);
        }
        int ret = binOLSTCM13_BL.updateProReturnRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 废弃产品退库申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteRA(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        int proReturnRequestID = 0;
        if(tradeType.equals(CherryConstants.OS_BILLTYPE_RA)){
            proReturnRequestID = propertyset.getInt("RA_BIN_ProReturnRequestID");
        }else if(tradeType.equals(CherryConstants.OS_BILLTYPE_RJ)){
            proReturnRequestID = propertyset.getInt("RJ_BIN_ProReturnRequestID");
        }else{
            proReturnRequestID = propertyset.getInt("BIN_ProReturnRequestID");
        }
        //发货单主表ID
        pramData.put("BIN_ProReturnRequestID", proReturnRequestID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM13_BL.updateProReturnRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 新建产品退库申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void createRA(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        
        BINOLSTBIL14_Form form = (BINOLSTBIL14_Form) mainData.get("ProReturnRequestForm");
        String KSFlag = ConvertUtil.getString(mainData.get("KSFlag"));
        int newBillID = 0;
        if(null != form){
            int billID = propertyset.getInt("BIN_ProReturnRequestID");
            pramData.put("BIN_ProReturnRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            pramData.put("ProReturnRequestForm", form);
            newBillID = binOLSTCM13_BL.createProReturnRequestByForm(pramData);
                
            propertyset.setInt("BIN_ProReturnRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_RA);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }else if(KSFlag.equals("KS")){
            //K3退库
            int billID = propertyset.getInt("BIN_ProReturnRequestID");
            pramData.put("BIN_ProReturnRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("returnReqMainData", mainData.get("returnReqMainData"));
            pramData.put("returnReqDetailList", mainData.get("returnReqDetailList"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            newBillID = binOLSTCM13_BL.createProReturnRequest_K3(pramData);
                
            propertyset.setInt("BIN_ProReturnRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_RA);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }else{
            int billID = propertyset.getInt("BIN_ProReturnRequestID");
            pramData.put("BIN_ProReturnRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            //pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            newBillID = binOLSTCM13_BL.createProReturnRequest(pramData);
                
            propertyset.setInt("BIN_ProReturnRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_RA);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }
        propertyset.setInt("RJ_BIN_ProReturnRequestID", newBillID);
    }
    
    /**
     * 写入出库表，并修改库存
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    @Deprecated
    private void RA_changeStock(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProReturnRequestID", propertyset.getInt("BIN_ProReturnRequestID"));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        binOLSTCM13_BL.changeStock(pramData);
    }
    
    /**
     * 创建退库单
     */
    private void createRR(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainDataMap = (Map<String, Object>)arg0.get("mainData");
        BINOLSTBIL14_Form form = (BINOLSTBIL14_Form) mainDataMap.get("ProReturnRequestForm");
        
        Map<String,Object> mainDataRA = binOLSTCM13_BL.getProReturnRequestMainData(CherryUtil.obj2int(form.getProReturnRequestID()), null);
        
        Map<String,Object> mainData = new HashMap<String,Object>();        
        mainData.put("BIN_OrganizationInfoID", mainDataRA.get("BIN_OrganizationInfoID"));
        mainData.put("BIN_BrandInfoID", mainDataRA.get("BIN_BrandInfoID"));
        //mainData.put("ReturnNoIF",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", form.getOrganizationID());
        mainData.put("BIN_OrganizationIDReceive", form.getOrganizationIDReceive());
        String tradeEmployee = ConvertUtil.getString(form.getTradeEmployeeID());
        if(tradeEmployee.equals("")){
            mainData.put("BIN_EmployeeID", mainDataMap.get("BIN_EmployeeID"));
        }else{
            mainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
        }
        mainData.put("BIN_OrganizationIDDX", form.getOrganizationID());
        mainData.put("BIN_EmployeeIDDX", mainDataMap.get("BIN_EmployeeID"));
        mainData.put("BIN_EmployeeIDAudit", mainDataRA.get("BIN_EmployeeIDAudit"));
//        mainData.put("TotalQuantity", mainDataRA.get("TotalQuantity"));
//        mainData.put("TotalAmount", mainDataRA.get("TotalAmount"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_RR);
        mainData.put("RelevanceNo", mainDataRA.get("BillNoIF"));
        mainData.put("Reason", mainDataRA.get("Reason"));
        mainData.put("ReturnDate", binOLSTCM13_Service.getDateYMD());
        mainData.put("WorkFlowID", mainDataRA.get("WorkFlowID"));
        mainData.put("CreatedBy", mainDataMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
        mainData.put("CreatePGM", mainDataMap.get("CurrentUnit"));
        mainData.put("UpdatedBy", mainDataMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
        mainData.put("UpdatePGM", mainDataMap.get("CurrentUnit"));
        
        List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] quantityArr = form.getQuantityArr();
        String[] priceArr = form.getPriceUnitArr();
        String[] reasonArr = form.getReasonArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
            detailMap.put("DetailNo", i+1);
            detailMap.put("Quantity", quantityArr[i]);
            detailMap.put("Price", priceArr[i]);
            detailMap.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            detailMap.put("BIN_InventoryInfoID", form.getInventoryInfoID());
            detailMap.put("BIN_LogicInventoryInfoID", form.getLogicInventoryInfoID());
            detailMap.put("Reason", reasonArr[i]);
            detailMap.put("CreatedBy", mainDataMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
            detailMap.put("CreatePGM", mainDataMap.get("CurrentUnit"));
            detailMap.put("UpdatedBy", mainDataMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
            detailMap.put("UpdatePGM", mainDataMap.get("CurrentUnit"));
            detailDataList.add(detailMap);
            
            //计算总数量、总金额。
            int quantity = CherryUtil.obj2int(detailMap.get("Quantity"));
            totalQuantity += quantity;
            if(null != detailMap.get("Price") && !"".equals(detailMap.get("Price"))){
                BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailMap.get("Price")));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
        }
        DecimalFormat df=new DecimalFormat("#0.00");
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", df.format(totalAmount));
        int prtReturnID = binOLSTCM09_BL.insertProductReturnAll(mainData, detailDataList);
        propertyset.setInt("BIN_ProductReturnID", prtReturnID);
    }
    
    /**
     * 更改柜台库存（根据退库单）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void changeCounterStock(Map arg0, Map arg1, PropertySet propertyset) {
        try{
            int productReturnID = propertyset.getInt("BIN_ProductReturnID");
        }catch(Exception e){
            createRR(arg0, arg1, propertyset);
        }
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProReturnRequestID", propertyset.getInt("BIN_ProReturnRequestID"));
        pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        pramData.put("isCounter", "YES");
        pramData.put("TradeDateTime", mainData.get("TradeDateTime"));
        binOLSTCM13_BL.changeStockByRR(pramData);
    }
    
    /**
     * 
     * 写入出库表，并修改库存更改库存（根据退库单）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void changeReceiveStock(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProReturnRequestID", propertyset.getInt("BIN_ProReturnRequestID"));
        pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        pramData.put("TradeDateTime", mainData.get("TradeDateTime"));
        binOLSTCM13_BL.changeStockByRR(pramData);
    }
    
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws Exception 
     * @throws PropertyException 
     */
    private void sendAuditMQ(Map arg0, Map arg1, PropertySet propertyset) throws PropertyException, Exception{
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        int proReturnRequestID = propertyset.getInt("BIN_ProReturnRequestID");
        Map<String, Object> proReturnRequest = binOLSTCM13_BL.getProReturnRequestMainData(proReturnRequestID,null);
        String organizationID = String.valueOf(proReturnRequest.get("BIN_OrganizationID"));
        //如果是发送退库审核到柜台，则发送MQ
        if(binOLSTCM07_BL.checkOrganizationType(organizationID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(proReturnRequest.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(proReturnRequest.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            binOLSTCM13_BL.sendMQ(new int[]{proReturnRequestID}, mqMap);
            
            //写入确认退库
            propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RA_CONFIRM, CherryConstants.OS_ACTOR_TYPE_DEPART+organizationID);
        }
    }
    
    /**
     * 更新退库确认单明细（先删后插）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateRADetail(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        BINOLSTBIL14_Form form = (BINOLSTBIL14_Form) mainData.get("ProReturnRequestForm");

        //对来自表单的数据进行处理，自动审核对数据不处理。
        if(null != form){
            Map<String, Object> pramData = new HashMap<String, Object>();
            int billID = propertyset.getInt("BIN_ProReturnRequestID");
            pramData.put("BIN_ProReturnRequestID", billID);
            binOLSTCM13_Service.deleteProReturnReqDetail(pramData);
            
            String createdBy = ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            String createPGM =ConvertUtil.getString(mainData.get("CurrentUnit"));
            
            //一次退库申请操作的总数量（始终为正）
            int totalQuantity =0;
            //总金额
            double totalAmount =0.0;
            String[] productVendorIDArr = form.getPrtVendorId();
            String[] quantityArr = form.getQuantityArr();
            String[] priceUnitArr = form.getPriceUnitArr();
            //String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
            //String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
            String[] reasonArr = form.getReasonArr();
            //String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
            //String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
            //String[] inventoryInfoIDReceiveArr = form.getInventoryInfoIDReceiveArr();
            //String[] logicInventoryInfoIDReceiveArr = form.getLogicInventoryInfoIDReceiveArr();
            List<Map<String,Object>> proReturnReqDetail = new ArrayList<Map<String,Object>>();
            for(int i=0;i<productVendorIDArr.length;i++){
                int tempCount = CherryUtil.string2int(quantityArr[i]);
                double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
                totalAmount += money;
                totalQuantity += tempCount;
                
                Map<String,Object> detailMap = new HashMap<String,Object>();
                detailMap.put("BIN_ProReturnRequestID", billID);
                detailMap.put("DetailNo",i+1);
                detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
                detailMap.put("Quantity", quantityArr[i]);
                detailMap.put("Price", priceUnitArr[i]);
                detailMap.put("BIN_ProductVendorPackageID", 0);
                detailMap.put("BIN_InventoryInfoID", form.getInventoryInfoID());
                detailMap.put("BIN_LogicInventoryInfoID", form.getLogicInventoryInfoID());
                detailMap.put("BIN_InventoryInfoIDReceive", form.getInventoryInfoIDReceive());
                detailMap.put("BIN_LogicInventoryInfoIDReceive", form.getLogicInventoryInfoIDReceive());
                detailMap.put("BIN_StorageLocationInfoID", 0);
                detailMap.put("Reason", reasonArr[i]);
                detailMap.put("CreatedBy", createdBy);
                detailMap.put("CreatePGM", createPGM);
                detailMap.put("UpdatedBy", createdBy);
                detailMap.put("UpdatePGM", createPGM);
                proReturnReqDetail.add(detailMap);
            }
            
            Map<String,Object> proReturnReqMainData = new HashMap<String,Object>();
            proReturnReqMainData.put("BIN_ProReturnRequestID", billID);
            proReturnReqMainData.put("BIN_OrganizationIDReceive", CherryUtil.obj2int(form.getOrganizationIDReceive()));
            proReturnReqMainData.put("BIN_InventoryInfoIDReceive", CherryUtil.obj2int(form.getInventoryInfoIDReceive()));
            proReturnReqMainData.put("BIN_LogicInventoryInfoIDReceive", CherryUtil.obj2int(form.getLogicInventoryInfoIDReceive()));
            proReturnReqMainData.put("TotalQuantity", totalQuantity);
            proReturnReqMainData.put("TotalAmount", totalAmount);
            proReturnReqMainData.put("UpdatedBy", createdBy);
            proReturnReqMainData.put("UpdatePGM", createPGM);
            binOLSTCM13_BL.updateProReturnRequest(proReturnReqMainData);
            
            binOLSTCM13_Service.insertProReturnReqDetail(proReturnReqDetail);
        }
    }
}
