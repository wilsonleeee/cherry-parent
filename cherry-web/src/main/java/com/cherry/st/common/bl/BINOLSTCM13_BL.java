/*  
 * @(#)BINOLSTCM13_BL.java     1.0 2012/7/24      
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
package com.cherry.st.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.service.BINOLSTCM13_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品退库申请单操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
public class BINOLSTCM13_BL implements BINOLSTCM13_IF{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_IF binOLSTCM09_BL;
    
    @Resource(name="binOLSTCM13_Service")
    private BINOLSTCM13_Service binOLSTCM13_Service;
    
    @Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
    
    /**
     * 给退库申请单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    @Override
    public Map<String, Object> getProReturnRequestMainData(int proReturnRequestID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProReturnRequestID", proReturnRequestID);
        map.put("language", language);
        return binOLSTCM13_Service.getProReturnRequestMainData(map);
    }
    
    /**
     * 给退库申请主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getProReturnReqDetailData(int proReturnRequestID, String language,Map<String,Object> otherParam) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProReturnRequestID", proReturnRequestID);
        map.put("language", language);
        if(null != otherParam){
            //排序方式
            String organizationInfoID = ConvertUtil.getString(otherParam.get("BIN_OrganizationInfoID"));
            String brandInfoID = ConvertUtil.getString(otherParam.get("BIN_BrandInfoID"));
            String detailOrderBy = binOLCM14_BL.getConfigValue("1120", organizationInfoID, brandInfoID);
            map.put("detailOrderBy", detailOrderBy);
        }
        return binOLSTCM13_Service.getProReturnReqDetailData(map);
    }

    /**
     * 将退库申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    @Override
    public int insertProReturnRequestAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
        int proReturnRequestID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String billNo = ConvertUtil.getString(mainData.get("BillNo"));
        String billNoIF = ConvertUtil.getString(mainData.get("BillNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
        if("".equals(bussType)){
        	bussType = CherryConstants.OS_BILLTYPE_RA;
        }
        //如果billNo不存在调用共通生成单据号
        if(null == billNo || "".equals(billNo)){
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("BillNo", billNo);
        }
        if(null == billNoIF || "".equals(billNoIF)){
            mainData.put("BillNoIF", billNo);
        }
        if("".equals(ConvertUtil.getString(mainData.get("BIN_LogisticInfoID")))){
            mainData.put("BIN_LogisticInfoID", 0);
        }
        
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        
        //插入产品退库申请单主表
        proReturnRequestID = binOLSTCM13_Service.insertProReturnRequest(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> proReturnReqDetail = detailList.get(i);
            proReturnReqDetail.put("BIN_ProReturnRequestID", proReturnRequestID);
            proReturnReqDetail.put("DetailNo", i+1);
            
            if("".equals(ConvertUtil.getString(proReturnReqDetail.get("BIN_ProductVendorPackageID")))){
                proReturnReqDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(proReturnReqDetail.get("BIN_LogicInventoryInfoID")))){
                proReturnReqDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(proReturnReqDetail.get("BIN_StorageLocationInfoID")))){
                proReturnReqDetail.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM13_Service.insertProReturnReqDetail(detailList);
        
        return proReturnRequestID;
    }

    /**
     * 修改退库申请单主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateProReturnRequest(Map<String, Object> praMap) {
        return binOLSTCM13_Service.updateProReturnRequest(praMap);
    }
    
    /**
     * 根据画面表单创建退库申请单
     * @param praMap
     * @return
     */
    @Override
    public int createProReturnRequestByForm(Map<String, Object> praMap) {
        int proReturnRequestID = CherryUtil.obj2int(praMap.get("BIN_ProReturnRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        BINOLSTBIL14_Form form = (BINOLSTBIL14_Form) praMap.get("ProReturnRequestForm");
        
        //取得退库申请单主表
        Map<String,Object> proReturnReqMap = getProReturnRequestMainData(proReturnRequestID,null);
        
        Map<String,Object> proReturnReqMainData = new HashMap<String,Object>();
        List<Map<String,Object>> proReturnReqDetail = new ArrayList<Map<String,Object>>();
        
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
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
            detailMap.put("Quantity", quantityArr[i]);
            detailMap.put("Price", priceUnitArr[i]);
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", CherryUtil.obj2int(proReturnReqMap.get("BIN_InventoryInfoID")));
            detailMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(proReturnReqMap.get("BIN_LogicInventoryInfoID")));
            detailMap.put("BIN_InventoryInfoIDReceive", CherryUtil.obj2int(form.getInventoryInfoIDReceive()));
            detailMap.put("BIN_LogicInventoryInfoIDReceive", CherryUtil.obj2int(form.getLogicInventoryInfoIDReceive()));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Reason", reasonArr[i]);
            detailMap.put("CreatedBy", createdBy);
            detailMap.put("CreatePGM", createPGM);
            detailMap.put("UpdatedBy", createdBy);
            detailMap.put("UpdatePGM", createPGM);
            proReturnReqDetail.add(detailMap);
        }
        
        proReturnReqMainData.put("BIN_OrganizationInfoID", proReturnReqMap.get("BIN_OrganizationInfoID"));
        proReturnReqMainData.put("BIN_BrandInfoID", proReturnReqMap.get("BIN_BrandInfoID"));
        proReturnReqMainData.put("RelevanceNo", proReturnReqMap.get("BillNoIF"));
        proReturnReqMainData.put("BIN_OrganizationID", proReturnReqMap.get("BIN_OrganizationID"));
        proReturnReqMainData.put("BIN_InventoryInfoID", proReturnReqMap.get("BIN_InventoryInfoID"));
        proReturnReqMainData.put("BIN_LogicInventoryInfoID", proReturnReqMap.get("BIN_LogicInventoryInfoID"));
        proReturnReqMainData.put("BIN_OrganizationIDReceive", CherryUtil.obj2int(form.getOrganizationIDReceive()));
        proReturnReqMainData.put("BIN_InventoryInfoIDReceive", CherryUtil.obj2int(form.getInventoryInfoIDReceive()));
        proReturnReqMainData.put("BIN_LogicInventoryInfoIDReceive", CherryUtil.obj2int(form.getLogicInventoryInfoIDReceive()));
        proReturnReqMainData.put("BIN_StorageLocationInfoID", 0);
        proReturnReqMainData.put("TotalQuantity", totalQuantity);
        proReturnReqMainData.put("TotalAmount", totalAmount);
        proReturnReqMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        proReturnReqMainData.put("TradeType", CherryConstants.OS_BILLTYPE_RJ);
        proReturnReqMainData.put("BIN_LogisticInfoID", 0);
        proReturnReqMainData.put("Reason", proReturnReqMap.get("Reason"));
        proReturnReqMainData.put("Comment", proReturnReqMap.get("Comment"));
        proReturnReqMainData.put("BIN_EmployeeID", proReturnReqMap.get("BIN_EmployeeID"));
        proReturnReqMainData.put("BIN_OrganizationIDDX", proReturnReqMap.get("BIN_OrganizationIDDX"));
        proReturnReqMainData.put("BIN_EmployeeIDDX", proReturnReqMap.get("BIN_EmployeeIDDX"));
        proReturnReqMainData.put("BIN_EmployeeIDAudit", proReturnReqMap.get("BIN_EmployeeIDAudit"));
        proReturnReqMainData.put("TradeDate", proReturnReqMap.get("TradeDate"));
        proReturnReqMainData.put("WorkFlowID", proReturnReqMap.get("WorkFlowID"));
        proReturnReqMainData.put("Model", proReturnReqMap.get("Model"));
        proReturnReqMainData.put("CreatedBy", createdBy);
        proReturnReqMainData.put("CreatePGM", createPGM);
        proReturnReqMainData.put("UpdatedBy", createdBy);
        proReturnReqMainData.put("UpdatePGM", createPGM);
        
        int newProReturnRequestID = insertProReturnRequestAll(proReturnReqMainData, proReturnReqDetail);
        return newProReturnRequestID;
    }
    
    /**
     * 根据退库申请单创建退库申请单
     * @param praMap
     * @return
     */
    @Override
    public int createProReturnRequest(Map<String, Object> praMap) {
        int proReturnRequestID = CherryUtil.obj2int(praMap.get("BIN_ProReturnRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        
        //取得退库申请单主表
        Map<String,Object> proReturnReqMap = getProReturnRequestMainData(proReturnRequestID,null);
        
        List<Map<String,Object>> proReturnReqDetail = getProReturnReqDetailData(proReturnRequestID,null,null);
        for(int i=0;i<proReturnReqDetail.size();i++){
        	Map<String,Object> temp = proReturnReqDetail.get(i);
        	temp.put("CreatedBy", createdBy);
        	temp.put("CreatePGM", createPGM);
        	temp.put("UpdatedBy", createdBy);
        	temp.put("UpdatePGM", createPGM);
        }
        
        proReturnReqMap.put("RelevanceNo", proReturnReqMap.get("BillNoIF"));
        proReturnReqMap.put("BillNo", null);
        proReturnReqMap.put("BillNoIF", null);
        proReturnReqMap.put("TradeType", CherryConstants.OS_BILLTYPE_RJ);
        proReturnReqMap.put("CreatedBy", createdBy);
        proReturnReqMap.put("CreatePGM", createPGM);
        proReturnReqMap.put("UpdatedBy", createdBy);
        proReturnReqMap.put("UpdatePGM", createPGM);
        
        int newProReturnRequestID = insertProReturnRequestAll(proReturnReqMap, proReturnReqDetail);
        return newProReturnRequestID;
    }
    
    /**
     * 写入出库表，并修改库存
     * @param praMap
     * @return
     */
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int proReturnRequestID = CherryUtil.obj2int(praMap.get("BIN_ProReturnRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        
        //取得产品退库申请单据表
        Map<String,Object> mainData = getProReturnRequestMainData(proReturnRequestID,null);
        mainData.put("RelevanceNo", mainData.get("BillNoIF"));
        mainData.put("BIN_OrganizationID", mainData.get("BIN_OrganizationIDReceive"));
        mainData.put("StockType", CherryConstants.STOCK_TYPE_IN);
        mainData.put("TradeType",CherryConstants.BUSINESS_TYPE_AR);
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        mainData.put("StockInOutDate", praMap.get("TradeDateTime"));
        mainData.put("StockInOutTime", praMap.get("TradeDateTime"));
        //取得产品退库申请单据明细表
        List<Map<String,Object>> detailList = getProReturnReqDetailData(proReturnRequestID,null,null);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            int inventoryInfoID = CherryUtil.obj2int(temp.get("BIN_InventoryInfoIDReceive"));
            int logicInventoryInfoID = CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoIDReceive"));
            temp.put("DetailNo", i+1);
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("BIN_StorageLocationInfoID", 0);
            temp.put("StockType", CherryConstants.STOCK_TYPE_IN);
            temp.put("Comments", temp.get("Reason"));
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            detailList.set(i, temp);
        }
        int productInOutId = binOLSTCM01_BL.insertProductInOutAll(mainData,detailList);
        mainData.put("BIN_ProductInOutID", productInOutId);
        //更改库存
        binOLSTCM01_BL.changeStock(mainData);
    }
    
    /**
     * 写入出库表，并修改库存(根据退库单)
     * @param praMap
     * @return
     */
    @Override
    public void changeStockByRR(Map<String, Object> praMap) {
        int proReturnRequestID = CherryUtil.obj2int(praMap.get("BIN_ProReturnRequestID"));
        int productReturnID = CherryUtil.obj2int(praMap.get("BIN_ProductReturnID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        int inventoryInfoID = 0;
        int logicInventoryInfoID = 0;
        
        //区分更改接收方库存、更改柜台库存
        String isCounter = ConvertUtil.getString(praMap.get("isCounter"));
        
        //取得产品退库单据表
        Map<String,Object> mainData = binOLSTCM09_BL.getProductReturnMainData(productReturnID,null);
        //取得产品退库申请单据表
        Map<String,Object> mainProReturnRequestData = getProReturnRequestMainData(proReturnRequestID, null);
        mainData.put("RelevanceNo", mainData.get("ReturnNoIF"));
        if("YES".equals(isCounter)){
            mainData.put("BIN_OrganizationID", mainData.get("BIN_OrganizationID"));
            mainData.put("StockType",CherryConstants.STOCK_TYPE_OUT);
            mainData.put("TradeType",CherryConstants.BUSINESS_TYPE_RR);
            inventoryInfoID = CherryUtil.obj2int(mainProReturnRequestData.get("BIN_InventoryInfoID"));
            logicInventoryInfoID = CherryUtil.obj2int(mainProReturnRequestData.get("BIN_LogicInventoryInfoID"));
        }else{
            mainData.put("BIN_OrganizationID", mainData.get("BIN_OrganizationIDReceive"));
            mainData.put("StockType",CherryConstants.STOCK_TYPE_IN);
            mainData.put("TradeType",CherryConstants.BUSINESS_TYPE_AR);
            inventoryInfoID = CherryUtil.obj2int(mainProReturnRequestData.get("BIN_InventoryInfoIDReceive"));
            logicInventoryInfoID = CherryUtil.obj2int(mainProReturnRequestData.get("BIN_LogicInventoryInfoIDReceive"));
        }
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        mainData.put("StockInOutDate", praMap.get("TradeDateTime"));
        mainData.put("StockInOutTime", praMap.get("TradeDateTime"));
        
        //取得产品退库申请单据明细表
        List<Map<String,Object>> detailList = binOLSTCM09_BL.getProductReturnDetailData(productReturnID,null);

        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            temp.put("DetailNo", i+1);
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("BIN_StorageLocationInfoID", 0);
            temp.put("StockType", "YES".equals(isCounter)?"1":"0");
            temp.put("Comments", temp.get("Reason"));
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            detailList.set(i, temp);
        }
        int productInOutId = binOLSTCM01_BL.insertProductInOutAll(mainData,detailList);
        mainData.put("BIN_ProductInOutID", productInOutId);
        //更改库存
        binOLSTCM01_BL.changeStock(mainData);
    }
    
    /**
     * 判断申请单号存在
     * @param relevantNo
     * @return
     */
    @Override
    public List<Map<String, Object>> selProReturnRequest(String relevantNo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("relevantNo", relevantNo);
        List<Map<String,Object>> list = binOLSTCM13_Service.selProReturnRequest(param);
        return list;
    }
    
    /**
     * 查询退库申请单据(RJ)根据关联单号
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtReturnReqListByRelevanceNo(Map<String, Object> map){
        List<Map<String,Object>> list = binOLSTCM13_Service.selPrtReturnReqListByRelevanceNo(map);
        return list;
    }
    
    /**
     * 发送退库审核MQ
     * @param pramMap
     * @throws Exception 
     */
    @Override
    public void sendMQ(int[] proReturnRequestIDArr, Map<String, String> pramMap) throws Exception {
        for(int i=0;i<proReturnRequestIDArr.length;i++){
            Map<String,Object> mainData = getProReturnRequestMainData(proReturnRequestIDArr[i], null);
            List<Map<String,Object>> detailList = getProReturnReqDetailData(proReturnRequestIDArr[i],null,null);
            
            String tradeDateTime = binOLSTCM13_Service.getConvertSysDate();
            
            Map<String,Object> mainDataMap = new HashMap<String,Object>();
            mainDataMap.put("BrandCode", pramMap.get("BrandCode"));
            mainDataMap.put("TradeNoIF", ConvertUtil.getString(mainData.get("BillNoIF")));
            mainDataMap.put("ModifyCounts", "");
            mainDataMap.put("TradeType", CherryConstants.OS_BILLTYPE_RJ);
            String auditResult = "OK";
            String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
            if (CherryConstants.RAAUDIT_FLAG_AGREE.equals(verifiedFlag)
                    || CherryConstants.RAAUDIT_FLAG_AGREE2.equals(verifiedFlag)
                    || CherryConstants.RAAUDIT_FLAG_AGREE3.equals(verifiedFlag)
                    || CherryConstants.RAAUDIT_FLAG_AGREE4.equals(verifiedFlag)) {
                auditResult = "OK";
            } else {
                auditResult = "NG";
            }
            mainDataMap.put("SubType", auditResult);
            mainDataMap.put("CounterCode", ConvertUtil.getString(pramMap.get("OrganizationCode")));
            mainDataMap.put("TotalQuantity", ConvertUtil.getString(mainData.get("TotalQuantity")));
            mainDataMap.put("TotalAmount", ConvertUtil.getString(mainData.get("TotalAmount")));
            if("OK".equals(auditResult)){
                mainDataMap.put("RelevantNo", ConvertUtil.getString(mainData.get("RelevanceNo")));
            }else{
                mainDataMap.put("RelevantNo", ConvertUtil.getString(mainData.get("BillNoIF")));
            }
            mainDataMap.put("Reason", ConvertUtil.getString(mainData.get("Reason")));
            mainDataMap.put("TradeDate", tradeDateTime.split(" ")[0]);
            mainDataMap.put("TradeTime", tradeDateTime.split(" ")[1]);
            mainDataMap.put("Model", ConvertUtil.getString(mainData.get("Model")));
            //key为ProductVendorID，Value为合并相同产品的数量的Map
            Map<String,Object> sumDetailMap = new HashMap<String,Object>();
            //退库申请明细顺序
            List<String> detailOrderList = new ArrayList<String>();
            for(int j=0;j<detailList.size();j++){
                Map<String,Object> curDetailMap = detailList.get(j);
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("TradeNoIF", ConvertUtil.getString(mainData.get("BillNoIF")));
                temp.put("ModifyCounts", "");
                String employeeCode = ConvertUtil.getString(mainData.get("EmployeeCode"));
                temp.put("BAcode", !"".equals(employeeCode) ? employeeCode : "G00001"); // 如果BACode不存在，设置固定值"G00001"
                temp.put("StockType", CherryConstants.STOCK_TYPE_OUT);
                temp.put("Barcode", ConvertUtil.getString(curDetailMap.get("BarCode")));
                temp.put("Unitcode", ConvertUtil.getString(curDetailMap.get("UnitCode")));
                temp.put("InventoryTypeCode", ConvertUtil.getString(curDetailMap.get("LogicInventoryCode")));
                temp.put("Quantity", ConvertUtil.getString(curDetailMap.get("Quantity")));
                temp.put("Price", ConvertUtil.getString(curDetailMap.get("Price")));
                temp.put("Reason", ConvertUtil.getString(curDetailMap.get("Reason")));
                
                String curProductVendorID = ConvertUtil.getString(curDetailMap.get("BIN_ProductVendorID"));
                if(sumDetailMap.containsKey(curProductVendorID)){
                    //发送MQ时需要合并相同产品的数量，消息体里的值必须是字符串，否则消息体有多条相同产品或者值为非字符串，终端无法接收。
                    Map<String,Object> sumMap = (Map<String, Object>) sumDetailMap.get(curProductVendorID);
                    int curSumQuantity = CherryUtil.obj2int(sumMap.get("Quantity"))+CherryUtil.obj2int(temp.get("Quantity"));
                    sumMap.put("Quantity", ConvertUtil.getString(curSumQuantity));
                }else{
                    sumDetailMap.put(curProductVendorID, temp);
                    detailOrderList.add(curProductVendorID);
                }
            }
            List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
            //根据detailOrderList的顺序，取出已合并相同产品的明细加入detailDataList
            for(int k=0;k<detailOrderList.size();k++){
                detailDataList.add((Map<String, Object>) sumDetailMap.get(detailOrderList.get(k)));
            }
            Map<String,Object> dataLine = new HashMap<String,Object>();
            dataLine.put("MainData", mainDataMap);
            dataLine.put("DetailDataDTOList", detailDataList);
            
            Map<String,Object> msgDataMap = new HashMap<String,Object>();
            msgDataMap.put(CherryConstants.MESSAGE_VERSION_TITLE, MessageConstants.MESSAGE_VERSION_RJ);
            msgDataMap.put(CherryConstants.MESSAGE_DATATYPE_TITLE, CherryConstants.DATATYPE_APPLICATION_JSON);
            msgDataMap.put(CherryConstants.DATALINE_JSON_XML,dataLine);
    
            // MQ消息 DTO
            MQInfoDTO mqInfoDTO = new MQInfoDTO();
            //消息发送队列名
//            mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
            mqInfoDTO.setMsgQueueName("cherryToPosMsgQueue");
            // 单据号
            mqInfoDTO.setBillCode(ConvertUtil.getString(mainData.get("BillNoIF")));
            // 单据类型
            mqInfoDTO.setBillType(CherryConstants.OS_BILLTYPE_RJ);
            // 所属品牌
            mqInfoDTO.setBrandInfoId(CherryUtil.obj2int(mainData.get("BIN_BrandInfoID")));
            // 所属组织
            mqInfoDTO.setOrganizationInfoId(CherryUtil.obj2int(mainData.get("BIN_OrganizationInfoID")));
            // 柜台号
            mqInfoDTO.setCounterCode(ConvertUtil.getString(pramMap.get("OrganizationCode")));
            // 消息体
            mqInfoDTO.setMsgDataMap(msgDataMap);
            // 创建者
            mqInfoDTO.setCreatedBy(pramMap.get("BIN_UserID"));
            // 更新者
            mqInfoDTO.setUpdatedBy(pramMap.get("BIN_UserID"));
            // 创建模块
            mqInfoDTO.setCreatePGM(pramMap.get("CurrentUnit"));
            // 更新模块
            mqInfoDTO.setUpdatePGM(pramMap.get("CurrentUnit"));
            
            // 业务流水
            DBObject dbObject = new BasicDBObject();
            // 组织代号
            dbObject.put("OrgCode", pramMap.get("OrganizationInfoCode"));
            // 品牌代码，即品牌简称
            dbObject.put("BrandCode", pramMap.get("BrandCode"));
            // 业务类型
            dbObject.put("TradeType", CherryConstants.OS_BILLTYPE_RJ);
            // 单据号
            dbObject.put("TradeNoIF", ConvertUtil.getString(mainData.get("BillNoIF")));
            // 修改次数
            dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
            // 发生时间
            dbObject.put("OccurTime", tradeDateTime);
            // 事件内容
            dbObject.put("Content", mqInfoDTO.getData());
            // 业务流水
            mqInfoDTO.setDbObject(dbObject);
            
            binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
        }
    }
    
    /**
     * pos机确认退库后，要来完成工作流（根据工作流ID来结束相应的工作流）
     * @param mainData
     * @throws WorkflowException 
     * @throws InvalidInputException 
     * @throws NumberFormatException 
     */
    @Override
    public void posConfirmReturnFinishFlow(Map<String, Object> mainData) throws Exception{
        String flowID = String.valueOf(mainData.get("WorkFlowID"));
        if(flowID!=null&&!"".equals(flowID)&&!"null".equals(flowID)){
            int[] actionArr = workflow.getAvailableActions(Long.parseLong(flowID), null);
            if(actionArr==null||actionArr.length==0){
                return;
            }
            
            Map<String,Object> updateParam = new HashMap<String,Object>();
            updateParam.put(CherryConstants.OS_ACTOR_TYPE_USER, "MQ");
            updateParam.put("CurrentUnit", "MQ");
            updateParam.put(CherryConstants.OS_MAINKEY_OPERATE_FLAG, CherryConstants.OS_MAINKEY_OPERATE_POS);
            updateParam.put("TradeDateTime", mainData.get("TradeDateTime"));
            Map<String,Object> input = new HashMap<String,Object>();
            input.put("mainData", updateParam);

            workflow.doAction_single(Long.parseLong(flowID), actionArr[0], input);
        }
    }

    /**
     * K3创建退库申请单
     */
    @Override
    public int createProReturnRequest_K3(Map<String, Object> praMap) {
        Map<String,Object> returnReqMainData = (Map<String,Object>) praMap.get("returnReqMainData");
        List<Map<String,Object>> returnReqDetailList = (List<Map<String,Object>>) praMap.get("returnReqDetailList");
        int billID = insertProReturnRequestAll(returnReqMainData,returnReqDetailList);
        return billID;
    }
}
