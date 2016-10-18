/*  
 * @(#)ProFlowCR_FN.java    1.0 2012.08.23
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
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
 * 产品盘点申请工作流
 * 
 * @author niushunjie
 * @version 1.0 2012.08.23
 */
public class ProFlowCR_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM06_BL")
	private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;

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
        }else if("createCR".equals(method)){
            createCR(arg0,  arg1,  propertyset);
        }else if("sendAuditMQ".equals(method)){
            try {
                sendAuditMQ(arg0,  arg1,  propertyset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if("changeCounterStock".equals(method)){
            changeCounterStock(arg0,  arg1,  propertyset);
        }else if("createCA".equals(method)) {
        	createCA(arg0, arg1, propertyset);
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
        paramMap.put("CurrentUnit", "ProFlowCR_FN");
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
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProStocktakeRequestID", propertyset.getInt("BIN_ProStocktakeRequestID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
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
        int proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
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
     * 新建产品盘点申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void createCR(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        
        BINOLSTBIL16_Form form = (BINOLSTBIL16_Form) mainData.get("ProStocktakeRequestForm");
        if(null != form){
            int billID = propertyset.getInt("BIN_ProStocktakeRequestID");
            pramData.put("BIN_ProStocktakeRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            pramData.put("ProStocktakeRequestForm", form);
            int newBillID = binOLSTCM14_BL.createProStocktakeRequestByForm(pramData);
                
            propertyset.setInt("BIN_ProStocktakeRequestID", newBillID);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_CR);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }else{
            int billID = propertyset.getInt("BIN_ProStocktakeRequestID");
            pramData.put("BIN_ProStocktakeRequestID", billID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            int newBillID = binOLSTCM14_BL.createProStocktakeRequest(pramData);
                
            propertyset.setInt("BIN_ProStocktakeRequestID", newBillID);
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
        int proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
        Map<String, Object> proStocktakeRequest = binOLSTCM14_BL.getProStocktakeRequestMainData(proStocktakeRequestID,null);
        String organizationID = String.valueOf(proStocktakeRequest.get("BIN_OrganizationID"));
        String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        //如果是发送盘点审核到柜台，则发送MQ
        if(binOLSTCM07_BL.checkOrganizationType(organizationID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(proStocktakeRequest.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(proStocktakeRequest.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",userID);
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            binOLSTCM14_BL.sendMQ(new int[]{proStocktakeRequestID}, mqMap);
            
            //写入确认盘点
            propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_CR_CONFIRM, CherryConstants.OS_ACTOR_TYPE_DEPART+organizationID);
        } 
    }
    
    /**
     * 生成盘点单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws Exception
     */
    private void createCA(Map arg0, Map arg1, PropertySet propertyset) {
    	Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        int proStocktakeRequestID = propertyset.getInt("BIN_ProStocktakeRequestID");
        // 此处为盘点确认单
        Map<String, Object> proStocktakeRequest = binOLSTCM14_BL.getProStocktakeRequestMainData(proStocktakeRequestID,null);
        String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
    	 //判断盘点申请流程
        if(null != proStocktakeRequest && !proStocktakeRequest.isEmpty()){
            //盘点申请流程
            String workFlowID = ConvertUtil.getString(proStocktakeRequest.get("WorkFlowID"));
            Map<String, Object> mainParamMap = new HashMap<String, Object>();
            mainParamMap.putAll(proStocktakeRequest);
            mainParamMap.put("CreatedBy", userID);
            mainParamMap.put("UpdatedBy", userID);
            mainParamMap.put("CreatePGM", "ProFlowCR_FN");
            mainParamMap.put("UpdatePGM", "ProFlowCR_FN");
            mainParamMap.put("BIN_ProStocktakeRequestID",proStocktakeRequestID);
            
            int productStockTakingID = this.auditStocktakeReqAndCrtComfirm(mainParamMap);
            long osID = Long.parseLong(workFlowID);
            PropertySet ps = workflow.getPropertySet(osID);
            // 记录生成的盘点单
            ps.setInt("BIN_ProductStockTakingID", productStockTakingID);
            
        }
    }
    
	/**
	 * 盘点申请审核通过自动确认盘点，并生成盘点单
	 * @param detailDataList
	 * @param map
	 * @return
	 */
	private int auditStocktakeReqAndCrtComfirm(Map<String,Object> map){
		// 此处为审核单据ID
        int proStocktakeRequestID = CherryUtil.obj2int(map.get("BIN_ProStocktakeRequestID"));
        List<Map<String,Object>> proStocktakeRequestDetailData = binOLSTCM14_BL.getProStocktakeRequestDetailData(proStocktakeRequestID, null);
        String handleType = ConvertUtil.getString(proStocktakeRequestDetailData.get(0).get("HandleType"));
        
        Map<String, Object> mainData = new HashMap<String, Object>();
        mainData.put("BIN_OrganizationInfoID", map.get("BIN_OrganizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("BIN_BrandInfoID"));
        // 生成的盘点单的关联单为盘点申请的原始单据号（即盘点确认单的关联单号）
        mainData.put("RelevanceNo", map.get("RelevanceNo"));
        mainData.put("BIN_OrganizationID", map.get("BIN_OrganizationID"));
        mainData.put("BIN_EmployeeID", map.get("BIN_EmployeeID"));
        mainData.put("BIN_OrganizationIDDX", map.get("BIN_OrganizationID"));
        mainData.put("BIN_EmployeeIDDX", map.get("BIN_EmployeeID"));
        mainData.put("BIN_EmployeeIDAudit", map.get("BIN_EmployeeIDAudit"));
        // 总数量与总金额通过明细计算后得到
//        mainData.put("TotalQuantity", map.get("TotalQuantity"));
//        mainData.put("TotalAmount", map.get("TotalAmount"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("Type", map.get("StocktakeType"));
        mainData.put("TradeType", CherryConstants.OS_BILLTYPE_CA);
        mainData.put("Comments", map.get("Comments"));
        mainData.put("StockReason", map.get("StockReason"));
        mainData.put("Date", map.get("Date"));
        mainData.put("TradeTime", map.get("TradeTime"));
        mainData.put("WorkFlowID", map.get("WorkFlowID"));
        mainData.put("CreatedBy", map.get("CreatedBy"));
        mainData.put("CreatePGM", map.get("CreatePGM"));
        mainData.put("UpdatedBy", map.get("UpdatedBy"));
        mainData.put("UpdatePGM", map.get("UpdatePGM"));
        
        //盘差总数量、盘差总金额通过明细计算后得到
        //盘差总数量
        int totalQuantity = 0;
        //盘差总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<proStocktakeRequestDetailData.size();i++){
            Map<String,Object> detailDataMap = proStocktakeRequestDetailData.get(i);
            detailDataMap.put("DetailNo", i+1);
            int bookQuantity = CherryUtil.obj2int(detailDataMap.get("BookQuantity"));
            int gainQuantity = CherryUtil.obj2int(detailDataMap.get("GainQuantity"));
            detailDataMap.put("Quantity", bookQuantity);
            detailDataMap.put("GainQuantity", gainQuantity);
            detailDataMap.put("Price", detailDataMap.get("Price"));
            detailDataMap.put("HandleType", handleType);
            detailDataMap.put("CreatedBy", map.get("CreatedBy"));
            detailDataMap.put("CreatePGM", map.get("CreatePGM"));
            detailDataMap.put("UpdatedBy", map.get("UpdatedBy"));
            detailDataMap.put("UpdatePGM", map.get("UpdatePGM"));
            
            totalQuantity += CherryUtil.obj2int(gainQuantity);
            BigDecimal price = new BigDecimal(0);
            if (detailDataMap.get("Price")!=null && !"".equals(detailDataMap.get("Price"))){
                price = new BigDecimal(Double.parseDouble(ConvertUtil.getString(detailDataMap.get("Price"))));
            }
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(CherryUtil.obj2int(gainQuantity))));
            
        }
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        
        int billID = binOLSTCM06_BL.insertStockTakingAll(mainData, proStocktakeRequestDetailData);
        Map<String,Object> stockTakingMainData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
        Map<String,Object> logMap = new HashMap<String,Object>();
        //工作流实例ID
        logMap.put("WorkFlowID",map.get("WorkFlowID"));
        //操作部门--申请部门即确认部门
        logMap.put("BIN_OrganizationID",map.get("BIN_OrganizationID"));
        //操作员工--申请人员即确认人员
        logMap.put("BIN_EmployeeID",map.get("BIN_EmployeeID")); 
        //操作业务类型
        logMap.put("TradeType","CR");
         //表名
        logMap.put("TableName", "Inventory.BIN_ProductStockTaking");
        //单据ID--审核单据ID
        logMap.put("BillID",billID);      
        //单据编号--盘点单code
        logMap.put("BillNo", stockTakingMainData.get("StockTakingNoIF"));
        //操作代码
        logMap.put("OpCode","144");
        //操作结果
        logMap.put("OpResult","100");
        //操作时间--生成
        logMap.put("OpDate",stockTakingMainData.get("UpdateTime"));
        //作成者   
        logMap.put("CreatedBy",map.get("BIN_EmployeeID")); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",map.get("BIN_EmployeeID")); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);

        return billID;
	}
}
