/*  
 * @(#)BINOLSTCM14_BL.java     1.0 2012/8/23      
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM25_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.service.BINOLSTCM14_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品盘点申请单操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
public class BINOLSTCM14_BL implements BINOLSTCM14_IF{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM25_BL")
    private BINOLCM25_BL binOLCM25_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM06_BL")
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource(name="binOLSTCM14_Service")
    private BINOLSTCM14_Service binOLSTCM14_Service;
    
    @Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;
    

    
    /**
     * 给盘点申请单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    @Override
    public Map<String, Object> getProStocktakeRequestMainData(int proStocktakeRequestID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        map.put("language", language);
        return binOLSTCM14_Service.getProStocktakeRequestMainData(map);
    }
    
    /**
     * 给盘点申请主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getProStocktakeRequestDetailData(int proStocktakeRequestID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
        map.put("language", language);
        return binOLSTCM14_Service.getProStocktakeRequestDetailData(map);
    }

    /**
     * 将盘点申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    @Override
    public int insertProStocktakeRequestAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
    	// 对于已经存在的盘点申请单--明细先删除后插入，主单更新
        int proStocktakeRequestID = ConvertUtil.getInt(mainData.get("BIN_ProStocktakeRequestID"));
        if(0 == proStocktakeRequestID) {
	        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
	        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
	        String billNo = ConvertUtil.getString(mainData.get("StockTakingNo"));
	        String billNoIF = ConvertUtil.getString(mainData.get("StockTakingNoIF"));
	        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
	        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
	        if("".equals(bussType)){
	        	bussType = CherryConstants.OS_BILLTYPE_CR;
	        }
	        //如果billNo不存在调用共通生成单据号
	        if(null == billNo || "".equals(billNo)){
	            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
	            mainData.put("StockTakingNo", billNo);
	        }
	        if(null == billNoIF || "".equals(billNoIF)){
	            mainData.put("StockTakingNoIF", billNo);
	        }
	        //插入产品盘点申请单主表
	        proStocktakeRequestID = binOLSTCM14_Service.insertProStocktakeRequest(mainData);
	        
	        for(int i=0;i<detailList.size();i++){
	            Map<String,Object> proReturnReqDetail = detailList.get(i);
	            proReturnReqDetail.put("BIN_ProStocktakeRequestID", proStocktakeRequestID); 
	            
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
	        binOLSTCM14_Service.insertProStocktakeRequestDetail(detailList);
        } else {
        	Map<String,Object> param = new HashMap<String,Object>();
            param.put("BIN_ProStocktakeRequestID", proStocktakeRequestID);
            this.deleteProStocktakeRequestDetail(param);
            for(int i=0;i<detailList.size();i++){
	            Map<String,Object> proReturnReqDetail = detailList.get(i);
	            proReturnReqDetail.put("BIN_ProStocktakeRequestID", proStocktakeRequestID); 
	            
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
            // 重新插入盘点申请明细数据
	        binOLSTCM14_Service.insertProStocktakeRequestDetail(detailList);
	        param.put("WorkFlowID", mainData.get("WorkFlowID"));
	        param.put("UpdatedBy", mainData.get("UpdatedBy"));
	        param.put("UpdatePGM", mainData.get("UpdatePGM"));
	        // 更新主单
	        this.updateProStocktakeRequest(param);
        }
        
        
        return proStocktakeRequestID;
    }

    /**
     * 修改盘点申请单主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateProStocktakeRequest(Map<String, Object> praMap) {
        return binOLSTCM14_Service.updateProStocktakeRequest(praMap);
    }
    
    /**
     * 清空盘点申请单明细数据
     * @param map
     */
    @Override
    public void deleteProStocktakeRequestDetail(Map<String, Object> map) {
    	binOLSTCM14_Service.deleteProStocktakeRequestDetail(map);
    }
    
    /**
     * 根据画面表单创建盘点申请单
     * @param praMap
     * @return
     */
    @Override
    public int createProStocktakeRequestByForm(Map<String, Object> praMap) {
        int proStocktakeRequestID = CherryUtil.obj2int(praMap.get("BIN_ProStocktakeRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        String verifiedFlag = ConvertUtil.getString(praMap.get("VerifiedFlag"));
        if("".equals(verifiedFlag)){
            verifiedFlag = CherryConstants.CRAUDIT_FLAG_AGREE;
        }
        
        BINOLSTBIL16_Form form = (BINOLSTBIL16_Form) praMap.get("ProStocktakeRequestForm");
        
        //取得盘点申请单主表
        Map<String,Object> proStocktakeReqMap = getProStocktakeRequestMainData(proStocktakeRequestID,null);
        
        Map<String,Object> proStocktakeReqMainData = new HashMap<String,Object>();
        List<Map<String,Object>> proStocktakeReqDetail = new ArrayList<Map<String,Object>>();
        
        //一次盘点申请操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        String inventoryInfoID = form.getInventoryInfoID();
        String logicInventoryInfoID = form.getLogicInventoryInfoID();
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] bookQuantityArr = form.getBookQuantityArr();
        String[] checkQuantityArr = form.getCheckQuantityArr();
//        String[] gainQuantityArr = form.getGainQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
//        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
//        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
//        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
//        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        String[] handleTypeArr = form.getHandleTypeArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            int gainQuantity = CherryUtil.string2int(checkQuantityArr[i]) - CherryUtil.string2int(bookQuantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*gainQuantity;
            totalAmount += money;
            totalQuantity += gainQuantity;
            
            Map<String,Object> detailMap = new HashMap<String,Object>();
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
        
        proStocktakeReqMainData.put("BIN_OrganizationInfoID", proStocktakeReqMap.get("BIN_OrganizationInfoID"));
        proStocktakeReqMainData.put("BIN_BrandInfoID", proStocktakeReqMap.get("BIN_BrandInfoID"));
        proStocktakeReqMainData.put("RelevanceNo", proStocktakeReqMap.get("StockTakingNoIF"));
        proStocktakeReqMainData.put("BIN_OrganizationID", proStocktakeReqMap.get("BIN_OrganizationID"));
        proStocktakeReqMainData.put("BIN_InventoryInfoID", proStocktakeReqMap.get("BIN_InventoryInfoID"));
        proStocktakeReqMainData.put("BIN_LogicInventoryInfoID", proStocktakeReqMap.get("BIN_LogicInventoryInfoID"));
        proStocktakeReqMainData.put("BIN_StorageLocationInfoID", 0);
        proStocktakeReqMainData.put("TotalQuantity", totalQuantity);
        proStocktakeReqMainData.put("TotalAmount", totalAmount);
        proStocktakeReqMainData.put("VerifiedFlag", verifiedFlag);
        proStocktakeReqMainData.put("StocktakeType", proStocktakeReqMap.get("StocktakeType"));
        proStocktakeReqMainData.put("TradeType", CherryConstants.OS_BILLTYPE_CJ);
        proStocktakeReqMainData.put("IsBatch", "0");
        proStocktakeReqMainData.put("BIN_LogisticInfoID", 0);
        proStocktakeReqMainData.put("Comments", proStocktakeReqMap.get("Comments"));
        proStocktakeReqMainData.put("StockReason", proStocktakeReqMap.get("StockReason"));
        proStocktakeReqMainData.put("BIN_EmployeeID", proStocktakeReqMap.get("BIN_EmployeeID"));
        proStocktakeReqMainData.put("BIN_EmployeeIDAudit", proStocktakeReqMap.get("BIN_EmployeeIDAudit"));
        proStocktakeReqMainData.put("Date", proStocktakeReqMap.get("Date"));
        proStocktakeReqMainData.put("TradeTime", proStocktakeReqMap.get("TradeTime"));
        proStocktakeReqMainData.put("WorkFlowID", proStocktakeReqMap.get("WorkFlowID"));
        proStocktakeReqMainData.put("CreatedBy", createdBy);
        proStocktakeReqMainData.put("CreatePGM", createPGM);
        proStocktakeReqMainData.put("UpdatedBy", createdBy);
        proStocktakeReqMainData.put("UpdatePGM", createPGM);
        
        int newProStocktakeRequestID = insertProStocktakeRequestAll(proStocktakeReqMainData, proStocktakeReqDetail);
        return newProStocktakeRequestID;
    }
    
    /**
     * 根据盘点申请单创建盘点申请单
     * @param praMap
     * @return
     */
    @Override
    public int createProStocktakeRequest(Map<String, Object> praMap) {
        int proStocktakeRequestID = CherryUtil.obj2int(praMap.get("BIN_ProStocktakeRequestID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        String verifiedFlag = ConvertUtil.getString(praMap.get("VerifiedFlag"));
        if("".equals(verifiedFlag)){
            verifiedFlag = CherryConstants.CRAUDIT_FLAG_AGREE;
        }
        
        //取得盘点申请单主表
        Map<String,Object> proStocktakeReqMap = getProStocktakeRequestMainData(proStocktakeRequestID,null);
        
        List<Map<String,Object>> proStocktakeReqDetail = getProStocktakeRequestDetailData(proStocktakeRequestID,null);
        for(int i=0;i<proStocktakeReqDetail.size();i++){
        	Map<String,Object> temp = proStocktakeReqDetail.get(i);
        	temp.put("CreatedBy", createdBy);
        	temp.put("CreatePGM", createPGM);
        	temp.put("UpdatedBy", createdBy);
        	temp.put("UpdatePGM", createPGM);
        }
        
        proStocktakeReqMap.put("RelevanceNo", proStocktakeReqMap.get("StockTakingNoIF"));
        proStocktakeReqMap.put("StockTakingNo", null);
        proStocktakeReqMap.put("StockTakingNoIF", null);
        proStocktakeReqMap.put("VerifiedFlag", verifiedFlag);
        proStocktakeReqMap.put("TradeType", CherryConstants.OS_BILLTYPE_CJ);
        proStocktakeReqMap.put("CreatedBy", createdBy);
        proStocktakeReqMap.put("CreatePGM", createPGM);
        proStocktakeReqMap.put("UpdatedBy", createdBy);
        proStocktakeReqMap.put("UpdatePGM", createPGM);
        
        int newProStocktakeRequestID = insertProStocktakeRequestAll(proStocktakeReqMap, proStocktakeReqDetail);
        return newProStocktakeRequestID;
    }
       
    /**
     * 写入出库表，并修改库存(根据盘点单)
     * @param praMap
     * @return
     */
    @Override
    public void changeStockByCA(Map<String, Object> praMap) {
        int proStocktakeRequestID = CherryUtil.obj2int(praMap.get("BIN_ProStocktakeRequestID"));
        int productStockTakingID = CherryUtil.obj2int(praMap.get("BIN_ProductStockTakingID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        int inventoryInfoID = 0;
        int logicInventoryInfoID = 0;
        
        //取得产品盘点单据表
        Map<String,Object> mainData = binOLSTCM06_BL.getStockTakingMainData(productStockTakingID,null);
        //取得产品盘点申请单据表
        Map<String,Object> mainProStocktakeRequestData = getProStocktakeRequestMainData(proStocktakeRequestID, null);
        mainData.put("RelevanceNo", mainData.get("StockTakingNoIF"));
        mainData.put("BIN_OrganizationID", mainData.get("BIN_OrganizationID"));
        int totalQuantity = CherryUtil.obj2int(mainData.get("TotalQuantity"));
        mainData.put("StockType",totalQuantity>=0?"0":"1");
        if(totalQuantity<0){
            mainData.put("TotalQuantity", totalQuantity*-1);
            BigDecimal totalAmount = new BigDecimal(ConvertUtil.getString(mainData.get("TotalAmount")));
            mainData.put("TotalAmount", totalAmount.multiply(new BigDecimal(-1)));
        }
        mainData.put("TradeType",CherryConstants.BUSINESS_TYPE_CA);
        inventoryInfoID = CherryUtil.obj2int(mainProStocktakeRequestData.get("BIN_InventoryInfoID"));
        logicInventoryInfoID = CherryUtil.obj2int(mainProStocktakeRequestData.get("BIN_LogicInventoryInfoID"));
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        mainData.put("StockInOutDate", praMap.get("TradeDateTime"));
        mainData.put("StockInOutTime", praMap.get("TradeDateTime"));
        
        //取得产品盘点申请单据明细表
        List<Map<String,Object>> detailList = binOLSTCM06_BL.getStockTakingDetailData(productStockTakingID,null);

        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            temp.put("DetailNo", i+1);
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("BIN_StorageLocationInfoID", 0);
            int gainQuantity = CherryUtil.obj2int(temp.get("GainQuantity"));
            temp.put("StockType", gainQuantity>=0?"0":"1");
            temp.put("Quantity", Math.abs(gainQuantity));
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
     * 判断盘点申请单号存在
     * @param relevantNo
     * @return
     */
    @Override
    public List<Map<String, Object>> selProStocktakeRequest(String relevantNo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("relevantNo", relevantNo);
        List<Map<String,Object>> list = binOLSTCM14_Service.selProStocktakeRequest(param);
        return list;
    }
    
    /**
     * 发送盘点审核MQ
     * @param proStocktakeRequestIDArr
     * @param pramMap
     * @throws CherryMQException 
     * @throws Exception 
     */
    @Override
    public void sendMQ(int[] proStocktakeRequestIDArr, Map<String, String> pramMap) throws Exception{
        for(int i=0;i<proStocktakeRequestIDArr.length;i++){
            Map<String,Object> mainData = getProStocktakeRequestMainData(proStocktakeRequestIDArr[i], null);
            List<Map<String,Object>> detailList = getProStocktakeRequestDetailData(proStocktakeRequestIDArr[i],null);
            
            String checkDateTime = binOLSTCM14_Service.getConvertSysDate();
            
            Map<String,Object> mainDataMap = new HashMap<String,Object>();
            mainDataMap.put("BrandCode", ConvertUtil.getString(pramMap.get("BrandCode")));
            mainDataMap.put("TradeNoIF", ConvertUtil.getString(mainData.get("StockTakingNoIF")));
            mainDataMap.put("ModifyCounts", "");
            
            String auditResult = "OK";
            String verifiedFlag = ConvertUtil.getString(mainData.get("VerifiedFlag"));
            if(CherryConstants.CRAUDIT_FLAG_AGREE.equals(verifiedFlag) || CherryConstants.CRAUDIT_FLAG_AGREE2.equals(verifiedFlag)){
                mainDataMap.put("RelevantNo", ConvertUtil.getString(mainData.get("RelevanceNo")));
            }else{
                mainDataMap.put("RelevantNo", ConvertUtil.getString(mainData.get("StockTakingNoIF")));
                auditResult = "NG";
            }
            mainDataMap.put("TradeType", CherryConstants.OS_BILLTYPE_CJ);
            mainDataMap.put("AuditResult", auditResult);
            mainDataMap.put("SubType", ConvertUtil.getString(mainData.get("StocktakeType")));
            mainDataMap.put("CounterCode", ConvertUtil.getString(pramMap.get("OrganizationCode")));
            mainDataMap.put("BAcode", ConvertUtil.getString(mainData.get("EmployeeCode")));
            mainDataMap.put("InventoryTypeCode", ConvertUtil.getString(mainData.get("LogicInventoryCode")));
            mainDataMap.put("TotalQuantity", ConvertUtil.getString(mainData.get("TotalQuantity")));
            mainDataMap.put("TotalAmount", ConvertUtil.getString(mainData.get("TotalAmount")));
            mainDataMap.put("Reason", ConvertUtil.getString(mainData.get("Comments")));
//            //从操作日志表第一条取出盘点申请时间
//            Map<String,Object> param = new HashMap<String,Object>();
//            param.put("WorkFlowID", mainData.get("WorkFlowID"));
//            List<Map<String,Object>> opLogList = binOLCM25_BL.selInventoryOpLog(param);
//            String tradeDate = "";
//            String tradeTime = "";
//            if(null != opLogList && opLogList.size()>0){
//                String opDateTime = ConvertUtil.getString(opLogList.get(0).get("OpDate"));
//                tradeDate = opDateTime.split(" ")[0];
//                tradeTime = opDateTime.split(" ")[1];
//            }
            mainDataMap.put("TradeDate", ConvertUtil.getString(mainData.get("Date")));
            mainDataMap.put("TradeTime", ConvertUtil.getString(mainData.get("TradeTime")));
            mainDataMap.put("CheckDate", checkDateTime.split(" ")[0]);
            mainDataMap.put("CheckTime", checkDateTime.split(" ")[1]);
            List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
            for(int j=0;j<detailList.size();j++){
                Map<String,Object> curDetailMap = detailList.get(j);
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("BAcode", ConvertUtil.getString(mainData.get("EmployeeCode")));
                temp.put("InventoryTypeCode", ConvertUtil.getString(mainData.get("LogicInventoryCode")));
                temp.put("Unitcode", ConvertUtil.getString(curDetailMap.get("UnitCode")));
                temp.put("Barcode", ConvertUtil.getString(curDetailMap.get("BarCode")));
                temp.put("BookQuantity", ConvertUtil.getString(curDetailMap.get("BookQuantity")));
                temp.put("GainQuantity", ConvertUtil.getString(curDetailMap.get("GainQuantity")));
                temp.put("Price", ConvertUtil.getString(curDetailMap.get("Price")));
                temp.put("Reason", ConvertUtil.getString(curDetailMap.get("Comments")));
                temp.put("HandleType", ConvertUtil.getString(curDetailMap.get("HandleType")));
                detailDataList.add(temp);
            }
            Map<String,Object> dataLine = new HashMap<String,Object>();
            dataLine.put("MainData", mainDataMap);
            dataLine.put("DetailDataDTOList", detailDataList);
            
            Map<String,Object> msgDataMap = new HashMap<String,Object>();
            msgDataMap.put(CherryConstants.MESSAGE_VERSION_TITLE, MessageConstants.MESSAGE_VERSION_CJ);
            msgDataMap.put(CherryConstants.MESSAGE_DATATYPE_TITLE, CherryConstants.DATATYPE_APPLICATION_JSON);
            msgDataMap.put(CherryConstants.DATALINE_JSON_XML,dataLine);
    
            // MQ消息 DTO
            MQInfoDTO mqInfoDTO = new MQInfoDTO();
            //消息发送队列名
            mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
            // 单据号
            mqInfoDTO.setBillCode(ConvertUtil.getString(mainData.get("StockTakingNoIF")));
            // 单据类型
            mqInfoDTO.setBillType(CherryConstants.OS_BILLTYPE_CJ);
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
            dbObject.put("TradeType", CherryConstants.OS_BILLTYPE_CJ);
            // 单据号
            dbObject.put("TradeNoIF", ConvertUtil.getString(mainData.get("StockTakingNoIF")));
            // 修改次数
            dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
            // 发生时间
            dbObject.put("OccurTime", checkDateTime);
            // 事件内容
            dbObject.put("Content", mqInfoDTO.getData());
            // 业务流水
            mqInfoDTO.setDbObject(dbObject);
            
            try {
                binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
            } catch (Exception e) {
                throw new CherryMQException(e.getMessage()+";There is an error that has occured in BINOLMQCOM01_BL's sendMQMsg");
            }
        }
    }
    
    /**
     * pos机确认盘点后，要来完成工作流
     * @param mainData
     * @throws WorkflowException 
     * @throws InvalidInputException 
     * @throws NumberFormatException 
     */
    @Override
    public void posConfirmCAFinishFlow(Map<String, Object> mainData) throws Exception{
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
     * 查询盘点申请单号关联的审核单信息
     * @param tradeNoIF
     * @return
     */
    @Override
    public List<Map<String, Object>> selProStocktakeRequestCR(String tradeNoIF) {
    	Map<String,Object> param = new HashMap<String,Object>();
        param.put("tradeNoIF", tradeNoIF);
        List<Map<String,Object>> list = binOLSTCM14_Service.selProStocktakeRequest(param);
        return list;
    }
}
