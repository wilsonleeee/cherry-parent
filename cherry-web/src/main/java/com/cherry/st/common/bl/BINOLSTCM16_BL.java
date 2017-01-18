/*  
 * @(#)BINOLSTCM16_BL.java     1.0 2012/11/27      
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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.common.service.BINOLSTCM16_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 产品调拨操作共通BL
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
public class BINOLSTCM16_BL implements BINOLSTCM16_IF{
       
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM16_Service")
    private  BINOLSTCM16_Service binOLSTCM16_Service;
    
    @Resource(name="binOLMQCOM01_BL")
    private BINOLMQCOM01_IF binOLMQCOM01_BL;

    @Override
    public int insertProductAllocationAll(Map<String, Object> mainData, List<Map<String, Object>> detailList) {
        int billID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String billNo = ConvertUtil.getString(mainData.get("AllocationrNo"));
        String billNoIF = ConvertUtil.getString(mainData.get("AllocationNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
        if("".equals(bussType)){
            bussType = CherryConstants.OS_BILLTYPE_BG;
        }
        //如果billNo不存在调用共通生成单据号
        if(null == billNo || "".equals(billNo)){
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("AllocationrNo", billNo);
        }
        if(null == billNoIF || "".equals(billNoIF)){
            mainData.put("AllocationNoIF", billNo);
        }
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        //插入产品调拨申请单主表
        billID = binOLSTCM16_Service.insertProductAllocation(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailMap = detailList.get(i);
            detailMap.put("BIN_ProductAllocationID", billID); 
            
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_ProductVendorPackageID")))){
                detailMap.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_LogicInventoryInfoID")))){
                detailMap.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_StorageLocationInfoID")))){
                detailMap.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM16_Service.insertProductAllocationDetail(detailList);
        
        return billID;
    }

    @Override
    public int updateProductAllocation(Map<String, Object> praMap) {
        return binOLSTCM16_Service.updateProductAllocation(praMap);
    }

    @Override
    public void insertProductAllocationDetail(int billID,List<Map<String, Object>> detailList){
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailMap = detailList.get(i);
            detailMap.put("BIN_ProductAllocationID", billID); 
            
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_ProductVendorPackageID")))){
                detailMap.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_LogicInventoryInfoID")))){
                detailMap.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_StorageLocationInfoID")))){
                detailMap.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM16_Service.insertProductAllocationDetail(detailList);
    }
    
    @Override
    public Map<String, Object> getProductAllocationMainData(int productAllocationID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationID", productAllocationID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationMainData(map);
    }

    @Override
    public List<Map<String, Object>> getProductAllocationDetailData(int productAllocationID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationID", productAllocationID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationDetailData(map);
    }

    @Override
    public int insertProductAllocationOutAll(Map<String, Object> mainData, List<Map<String, Object>> detailList) {
        int billID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String billNo = ConvertUtil.getString(mainData.get("AllocationOutNo"));
        String billNoIF = ConvertUtil.getString(mainData.get("AllocationOutNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
        if("".equals(bussType)){
            bussType = CherryConstants.OS_BILLTYPE_LG;
        }
        //如果billNo不存在调用共通生成单据号
        if(null == billNo || "".equals(billNo)){
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("AllocationOutNo", billNo);
        }
        if(null == billNoIF || "".equals(billNoIF)){
            mainData.put("AllocationOutNoIF", billNo);
        }
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationIDOut"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        //插入产品调出主表
        billID = binOLSTCM16_Service.insertProductAllocationOut(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailMap = detailList.get(i);
            detailMap.put("BIN_ProductAllocationOutID", billID); 
            
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_ProductVendorPackageID")))){
                detailMap.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_LogicInventoryInfoID")))){
                detailMap.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_StorageLocationInfoID")))){
                detailMap.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM16_Service.insertProductAllocationOutDetail(detailList);
        
        return billID;
    }

    @Override
    public int updateProductAllocationOut(Map<String, Object> praMap) {
        return binOLSTCM16_Service.updateProductAllocationOut(praMap);
    }
    
    @Override
    public void insertProductAllocationOutDetail(int billID,List<Map<String, Object>> detailList){
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailMap = detailList.get(i);
            detailMap.put("BIN_ProductAllocationOutID", billID); 
            
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_ProductVendorPackageID")))){
                detailMap.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_LogicInventoryInfoID")))){
                detailMap.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_StorageLocationInfoID")))){
                detailMap.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM16_Service.insertProductAllocationOutDetail(detailList);
    }
    
//    @Override
//    public void updateProductAllocationOutDetail(Map<String, Object> praMap) {
//        String productAllocationOutID = ConvertUtil.getString(praMap.get("BIN_ProductAllocationOutID"));
//        Map<String,Object> param = new HashMap<String,Object>();
//        param.put("BIN_ProductAllocationOutID", productAllocationOutID);
//        binOLSTCM16_Service.deleteProductAllocationOutDetail(param);
//        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
//        BINOLSTBIL18_Form form = (BINOLSTBIL18_Form) praMap.get("ProductAllocationForm");
//        
//        //调入实体仓库
//        int inDepotId = CherryUtil.obj2int(form.getInventoryInfoIDIn());
//        //调入逻辑仓库
//        int inLoginDepotId = CherryUtil.obj2int(form.getLogicInventoryInfoIDIn());
//        //产品厂商编码
//        String[] productVendorIDArr = form.getPrtVendorId();
//        //产品单价
//        String[] priceUnitArr = form.getPriceUnitArr();
//        //申请数量
//        String[] quantityArr =form.getQuantityArr();
//        //申请原因
//        String[] reasonArr = form.getCommentsArr();
//        int totalQuantity = 0;
//        double totalAmount = 0;
//        for(int i=0;i<productVendorIDArr.length;i++){
//            int quantity = CherryUtil.string2int(quantityArr[i]);
//            totalQuantity += quantity;
//            totalAmount += CherryUtil.string2double(priceUnitArr[i])*quantity;
//            
//            HashMap<String,Object> tempMap = new HashMap<String,Object>();
//            //明细       产品厂商编码
//            tempMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
//            //明细    番号
//            tempMap.put("DetailNo", i+1);
//            //明细       发货数量
//            tempMap.put("Quantity", quantity);
//            //明细       价格
//            tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//            //明细       包装类型ID
//            tempMap.put("BIN_ProductVendorPackageID", 0);
//            //明细      调入仓库ID
//            tempMap.put("BIN_InventoryInfoID", inDepotId);
//            //明细     调入逻辑仓库ID
//            tempMap.put("BIN_LogicInventoryInfoID", inLoginDepotId);
//            //明细      调入仓库库位ID
//            tempMap.put("BIN_StorageLocationInfoID",0);
//            //明细     有效区分
//            tempMap.put("ValidFlag", "1");
//            //明细     理由
//            tempMap.put("Comments", reasonArr[i]);
//            //明细     共通字段
//            tempMap.put("CreatedBy", "BINOLSTCM16");
//            tempMap.put("CreatePGM", "BINOLSTCM16");
//            tempMap.put("UpdatedBy", "BINOLSTCM16");
//            tempMap.put("UpdatePGM", "BINOLSTCM16");
//            
//            detailList.add(tempMap);
//        }
//        binOLSTCM16_Service.insertProductAllocationOutDetail(detailList);
//    }
    
    @Override
    public Map<String, Object> getProductAllocationOutMainData(int productAllocationOutID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationOutID", productAllocationOutID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationOutMainData(map);
    }

    @Override
    public List<Map<String, Object>> getProductAllocationOutDetailData(int productAllocationOutID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationOutID", productAllocationOutID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationOutDetailData(map);
    }

    @Override
    public int insertProductAllocationInAll(Map<String, Object> mainData, List<Map<String, Object>> detailList) {
        int billID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String billNo = ConvertUtil.getString(mainData.get("AllocationInNo"));
        String billNoIF = ConvertUtil.getString(mainData.get("AllocationInNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = ConvertUtil.getString(mainData.get("TradeType"));
        if("".equals(bussType)){
            bussType = CherryConstants.OS_BILLTYPE_BG;
        }
        //如果billNo不存在调用共通生成单据号
        if(null == billNo || "".equals(billNo)){
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("AllocationInNo", billNo);
        }
        if(null == billNoIF || "".equals(billNoIF)){
            mainData.put("AllocationInNoIF", billNo);
        }
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationIDIn"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        //插入产品调入主表
        billID = binOLSTCM16_Service.insertProductAllocationIn(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailMap = detailList.get(i);
            detailMap.put("BIN_ProductAllocationInID", billID); 
            
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_ProductVendorPackageID")))){
                detailMap.put("BIN_ProductVendorPackageID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_LogicInventoryInfoID")))){
                detailMap.put("BIN_LogicInventoryInfoID", 0);
            }
            if("".equals(ConvertUtil.getString(detailMap.get("BIN_StorageLocationInfoID")))){
                detailMap.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM16_Service.insertProductAllocationInDetail(detailList);
        
        return billID;
    }

    @Override
    public Map<String, Object> getProductAllocationInMainData(int productAllocationInID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationInID", productAllocationInID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationInMainData(map);
    }

    @Override
    public List<Map<String, Object>> getProductAllocationInDetailData(int productAllocationInID, String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductAllocationInID", productAllocationInID);
        map.put("language", language);
        return binOLSTCM16_Service.getProductAllocationInDetailData(map);
    }

    @Override
    public void changeStock(Map<String, Object> praMap) {
        int billID = 0;
        int organizationID = 0;
        String relevanceNo = "";
        String stockType = "";
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        String tradeType = ConvertUtil.getString(praMap.get("TradeType"));
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            //调入确认
            //取得产品调入单主表的单据号作为产品入出库的关联单号
            int productAllocationInID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationInID"));
            mainData = getProductAllocationInMainData(productAllocationInID,null);
            //调入部门
            organizationID = CherryUtil.obj2int(mainData.get("BIN_OrganizationIDIn"));
            relevanceNo = ConvertUtil.getString(mainData.get("AllocationInNoIF"));
            //取得产品调入单据明细表
            billID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationInID"));
            detailList = getProductAllocationInDetailData(billID,null);
            stockType = CherryConstants.STOCK_TYPE_IN;
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            //调出确认
            //取得产品调出单的单据号作为产品入出库记录的关联单号
            int productAllocationOutID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationOutID"));
            mainData = getProductAllocationOutMainData(productAllocationOutID,null);
            //调出部门
            organizationID = CherryUtil.obj2int(mainData.get("BIN_OrganizationIDOut"));
            relevanceNo = ConvertUtil.getString(mainData.get("AllocationOutNoIF"));
            //取得产品调出单据明细表
            billID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationOutID"));
            detailList = getProductAllocationOutDetailData(billID,null);
            stockType = CherryConstants.STOCK_TYPE_OUT;
        }
        mainData.put("RelevanceNo", relevanceNo);
        mainData.put("BIN_OrganizationID", organizationID);
        mainData.put("StockType", stockType);
        mainData.put("TradeType", tradeType);
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        mainData.put("StockInOutDate", praMap.get("TradeDateTime"));
        mainData.put("StockInOutTime", praMap.get("TradeDateTime"));
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            int inventoryInfoID = CherryUtil.obj2int(temp.get("BIN_InventoryInfoID"));
            int logicInventoryInfoID = CherryUtil.obj2int(temp.get("BIN_LogicInventoryInfoID"));
            temp.put("DetailNo", i+1);
            temp.put("BIN_InventoryInfoID", inventoryInfoID);
            temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            temp.put("BIN_StorageLocationInfoID", 0);
            temp.put("StockType", stockType);
            temp.put("Comments", temp.get("Comments"));
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

    @Override
    public int createProductAllocationByForm(Map<String, Object> praMap) {
        int billID = 0;
        //调入/调出实体仓库ID
        String inventoryInfoID = "";
        //调入/调出逻辑仓库ID
        String logicInventoryInfoID = "";
        String relevanceNo = "";
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        String tradeType = ConvertUtil.getString(praMap.get("TradeType"));
        String tradeStatus = ConvertUtil.getString(praMap.get("TradeStatus"));
        String verifiedFlag = ConvertUtil.getString(praMap.get("VerifiedFlag"));
        BINOLSTBIL18_Form form = (BINOLSTBIL18_Form) praMap.get("ProductAllocationForm");

        Map<String,Object> productAllocationMainData = new HashMap<String,Object>();
        List<Map<String,Object>> productAllocationDetail = new ArrayList<Map<String,Object>>();
        
        //一次调出/调入操作的总数量（始终为正）
        int totalQuantity = 0;
        //总金额
        double totalAmount = 0.0;
        
        Map<String,Object> productAllocationMap = new HashMap<String,Object>();
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            //调入确认
            billID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationOutID"));
            //取得调出单主表
            productAllocationMap = getProductAllocationOutMainData(billID,null);
            inventoryInfoID = form.getInventoryInfoIDIn();
            logicInventoryInfoID = form.getLogicInventoryInfoIDIn();
            relevanceNo = ConvertUtil.getString(productAllocationMap.get("AllocationOutNoIF"));
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            //调出确认
            billID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationID"));
            //取得调拨申请主表
            productAllocationMap = getProductAllocationMainData(billID,null);
            inventoryInfoID = form.getInventoryInfoIDOut();
            logicInventoryInfoID = form.getLogicInventoryInfoIDOut();
            relevanceNo = ConvertUtil.getString(productAllocationMap.get("AllocationNoIF"));
        }
        
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] commentsArr = form.getCommentsArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            int quantity = CherryUtil.obj2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*quantity;
            totalAmount += money;
            totalQuantity += quantity;
            
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
            detailMap.put("DetailNo", i+1);
            detailMap.put("Quantity", quantity);
            detailMap.put("Price", priceUnitArr[i]);
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", inventoryInfoID);
            detailMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", commentsArr[i]);
            detailMap.put("CreatedBy", createdBy);
            detailMap.put("CreatePGM", createPGM);
            detailMap.put("UpdatedBy", createdBy);
            detailMap.put("UpdatePGM", createPGM);
            productAllocationDetail.add(detailMap);
        }
        
        productAllocationMainData.put("BIN_OrganizationInfoID", productAllocationMap.get("BIN_OrganizationInfoID"));
        productAllocationMainData.put("BIN_BrandInfoID", productAllocationMap.get("BIN_BrandInfoID"));
        productAllocationMainData.put("RelevanceNo", relevanceNo);
        productAllocationMainData.put("BIN_OrganizationIDIn", productAllocationMap.get("BIN_OrganizationIDIn"));
        productAllocationMainData.put("BIN_OrganizationIDOut", productAllocationMap.get("BIN_OrganizationIDOut"));
        productAllocationMainData.put("TotalQuantity", totalQuantity);
        productAllocationMainData.put("TotalAmount", totalAmount);
        if(verifiedFlag.equals("")){
            productAllocationMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        }else{
            productAllocationMainData.put("VerifiedFlag", verifiedFlag);
        }
        productAllocationMainData.put("TradeStatus", tradeStatus);
        productAllocationMainData.put("BIN_LogisticInfoID", 0);
        productAllocationMainData.put("Comments", productAllocationMap.get("Comments"));
        String tradeEmployeeID = ConvertUtil.getString(form.getTradeEmployeeID());
        if(tradeEmployeeID.equals("")){
            productAllocationMainData.put("BIN_EmployeeID", praMap.get("BIN_EmployeeID"));
        }else{
            productAllocationMainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
        }
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            productAllocationMainData.put("BIN_OrganizationIDDX", productAllocationMap.get("BIN_OrganizationIDIn"));
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            productAllocationMainData.put("BIN_OrganizationIDDX", productAllocationMap.get("BIN_OrganizationIDOut"));
        }
        productAllocationMainData.put("BIN_EmployeeIDDX", praMap.get("BIN_EmployeeID"));
        productAllocationMainData.put("BIN_EmployeeIDAudit", productAllocationMap.get("BIN_EmployeeIDAudit"));
        productAllocationMainData.put("Date", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        productAllocationMainData.put("WorkFlowID", productAllocationMap.get("WorkFlowID"));
        productAllocationMainData.put("CreatedBy", createdBy);
        productAllocationMainData.put("CreatePGM", createPGM);
        productAllocationMainData.put("UpdatedBy", createdBy);
        productAllocationMainData.put("UpdatePGM", createPGM);
        
        int newBillID = 0;
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            newBillID = insertProductAllocationInAll(productAllocationMainData, productAllocationDetail);
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            newBillID = insertProductAllocationOutAll(productAllocationMainData, productAllocationDetail);
        }
        
        return newBillID;
    }
    
    @Override
    public int createProductAllocationInByOut(Map<String, Object> praMap) {
        //调入实体仓库ID
        String inventoryInfoID = "";
        //调入逻辑仓库ID
        String logicInventoryInfoID = "";
        String relevanceNo = "";
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        String tradeStatus = ConvertUtil.getString(praMap.get("TradeStatus"));

        Map<String,Object> productAllocationInMainData = new HashMap<String,Object>();
        List<Map<String,Object>> productAllocationInDetail = new ArrayList<Map<String,Object>>();
        
        int productAllocationID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationID"));
        int productAllocationOutID = CherryUtil.obj2int(praMap.get("BIN_ProductAllocationOutID"));

        //取得调入申请明细表
        List<Map<String,Object>> productAllocationDetailData = getProductAllocationDetailData(productAllocationID,null);

        //取得调出单主表
        Map<String,Object> productAllocationOutMap = getProductAllocationOutMainData(productAllocationOutID,null);
        //取得调出明细表
        List<Map<String,Object>> productAllocationOutDetailData = getProductAllocationOutDetailData(productAllocationOutID,null);

        //调入实体仓库ID、逻辑仓库ID取调拨申请明细第一条
        inventoryInfoID = ConvertUtil.getString(productAllocationDetailData.get(0).get("BIN_InventoryInfoID"));
        logicInventoryInfoID = ConvertUtil.getString(productAllocationDetailData.get(0).get("BIN_LogicInventoryInfoID"));
        relevanceNo = ConvertUtil.getString(productAllocationOutMap.get("AllocationOutNoIF"));
        
        for(int i=0;i<productAllocationOutDetailData.size();i++){
            Map<String,Object> detailMap = productAllocationOutDetailData.get(i);
            detailMap.put("BIN_InventoryInfoID", inventoryInfoID);
            detailMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("CreatedBy", createdBy);
            detailMap.put("CreatePGM", createPGM);
            detailMap.put("UpdatedBy", createdBy);
            detailMap.put("UpdatePGM", createPGM);
            productAllocationInDetail.add(detailMap);
        }
        
        productAllocationInMainData.putAll(productAllocationOutMap);
        productAllocationInMainData.put("RelevanceNo", relevanceNo);
        productAllocationInMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        productAllocationInMainData.put("TradeStatus", tradeStatus);
        productAllocationInMainData.put("Date", productAllocationOutMap.get("Date"));
        productAllocationInMainData.put("CreatedBy", createdBy);
        productAllocationInMainData.put("CreatePGM", createPGM);
        productAllocationInMainData.put("UpdatedBy", createdBy);
        productAllocationInMainData.put("UpdatePGM", createPGM);
        
        int newBillID = insertProductAllocationInAll(productAllocationInMainData, productAllocationInDetail);
        return newBillID;
    }
    
    /**
     * 发送调拨审核MQ
     * @param billIDArr
     * @param pramMap
     * @throws CherryMQException 
     * @throws Exception 
     */
    @Override
    public void sendMQ(int[] billIDArr, Map<String, String> pramMap) throws Exception{
        for(int i=0;i<billIDArr.length;i++){
            //CTDR:云POS向终端POS做调入申请，老后台MQ监听程序接收到此MQ后，应该产生一张调入单，调出方柜台可对此单进行调出操作（操作后还是会发送调出的MQ，等待后台审核，不会立即改变库存）。
            //T2T:终端POS之间的调入调出被审核通过，老后台MQ监听程序需要改两方库存。
            //C2T:云POS向终端POS做调入申请，最终又被审核通过，老后台MQ监听程序需要处理调出方的库存（调入方是云POS不在老后台管库存）。
            //T2C:终端POS向云POS申请调入，云POS做调出，最终审核通过，老后台MQ监听程序要处理调入方库存（调出方是云POS不在老后台管库存）
            //NG:①终端POS向云POS做调入申请，云POS直接拒绝掉（还未到审核步骤） ②审核不通过，废弃 以上两种情况下，MQ监听程序都是直接根据单据号将单据置为废弃状态，不操作库存
            String auditResult = ConvertUtil.getString(pramMap.get("MQ_SubType"));
            String tradeState = ConvertUtil.getString(pramMap.get("TradeState"));
            String auditBill = ConvertUtil.getString(pramMap.get("AuditBill"));
            String stockChange = ConvertUtil.getString(pramMap.get("StockChange"));
            Map<String,Object> mainData = new HashMap<String,Object>();
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            String billNoIF = "";
            String relevantNo = "";
            if(auditResult.equals("CTDR")){
                mainData = getProductAllocationMainData(billIDArr[i], null);
                detailList = getProductAllocationDetailData(billIDArr[i],null);
                billNoIF = ConvertUtil.getString(mainData.get("AllocationNoIF"));
            }else if(auditResult.equals("T2T") || auditResult.equals("C2T")){
                if(auditBill.equals("BG")){
                    mainData = getProductAllocationMainData(billIDArr[i], null);
                    detailList = getProductAllocationDetailData(billIDArr[i],null);
                    billNoIF = ConvertUtil.getString(mainData.get("AllocationNoIF"));
                    relevantNo = ConvertUtil.getString(mainData.get("AllocationNoIF"));
                }else{
                    mainData = getProductAllocationOutMainData(billIDArr[i], null);
                    detailList = getProductAllocationOutDetailData(billIDArr[i],null);
                    billNoIF = ConvertUtil.getString(mainData.get("AllocationOutNoIF"));
                    relevantNo = ConvertUtil.getString(mainData.get("RelevanceNo"));
                }
            }else if (auditResult.equals("T2C")){
                //云POS方拒绝没有调出单，审核者废弃有调出单，下发都是NG，这样不好判断，因此TradeNoIF，RelevantNo都放AllocationNoIF
                mainData = getProductAllocationMainData(billIDArr[i], null);
                detailList = getProductAllocationDetailData(billIDArr[i],null);
                billNoIF = ConvertUtil.getString(mainData.get("AllocationNoIF"));
                relevantNo = ConvertUtil.getString(mainData.get("AllocationNoIF"));
            }
            Map<String,Object> mainDataMap = new HashMap<String,Object>();
            mainDataMap.put("BrandCode", pramMap.get("BrandCode"));
            mainDataMap.put("TradeNoIF", billNoIF);
            mainDataMap.put("ModifyCounts", "");
            mainDataMap.put("TradeType", CherryConstants.OS_BILLTYPE_BJ);
            mainDataMap.put("SubType", auditResult);
            mainDataMap.put("TradeState", tradeState);
            mainDataMap.put("CounterCode", pramMap.get("OrganizationCodeIn"));//调入方柜台号
            mainDataMap.put("RelevantCounterCode", pramMap.get("OrganizationCodeOut"));//调出方柜台号
            mainDataMap.put("BAcode", ConvertUtil.getString(mainData.get("EmployeeCode")));
            mainDataMap.put("TotalQuantity", ConvertUtil.getString(mainData.get("TotalQuantity")));
            mainDataMap.put("TotalAmount", ConvertUtil.getString(mainData.get("TotalAmount")));
            mainDataMap.put("RelevantNo", relevantNo);
            mainDataMap.put("Reason", ConvertUtil.getString(mainData.get("Comments")));
            String auditDateTime = pramMap.get("auditDateTime");
            mainDataMap.put("TradeDate", auditDateTime.split(" ")[0]);
            mainDataMap.put("TradeTime", auditDateTime.split(" ")[1]);
            mainDataMap.put("StockChange", stockChange);
            List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
            for(int j=0;j<detailList.size();j++){
                Map<String,Object> curDetailMap = detailList.get(j);
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("Barcode", ConvertUtil.getString(curDetailMap.get("BarCode")));
                temp.put("Unitcode", ConvertUtil.getString(curDetailMap.get("UnitCode")));
                temp.put("InventoryTypeCode", ConvertUtil.getString(curDetailMap.get("LogicInventoryCode")));
                temp.put("Quantity", ConvertUtil.getString(curDetailMap.get("Quantity")));
                temp.put("Price", ConvertUtil.getString(curDetailMap.get("Price")));
                temp.put("Reason", ConvertUtil.getString(curDetailMap.get("Comments")));
                detailDataList.add(temp);
            }
            Map<String,Object> dataLine = new HashMap<String,Object>();
            dataLine.put("MainData", mainDataMap);
            dataLine.put("DetailDataDTOList", detailDataList);
            
            Map<String,Object> msgDataMap = new HashMap<String,Object>();
            msgDataMap.put(CherryConstants.MESSAGE_VERSION_TITLE, MessageConstants.MESSAGE_VERSION_BJ);
            msgDataMap.put(CherryConstants.MESSAGE_DATATYPE_TITLE, CherryConstants.DATATYPE_APPLICATION_JSON);
            msgDataMap.put(CherryConstants.DATALINE_JSON_XML,dataLine);
    
            // MQ消息 DTO
            MQInfoDTO mqInfoDTO = new MQInfoDTO();
            //消息发送队列名
            mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYTOPOSMSGQUEUE);
            // 单据号
            mqInfoDTO.setBillCode(billNoIF);
            // 单据类型
            mqInfoDTO.setBillType(CherryConstants.OS_BILLTYPE_BJ);
            // 所属品牌
            mqInfoDTO.setBrandInfoId(CherryUtil.obj2int(mainData.get("BIN_BrandInfoID")));
            // 所属组织
            mqInfoDTO.setOrganizationInfoId(CherryUtil.obj2int(mainData.get("BIN_OrganizationInfoID")));
            // 柜台号
            mqInfoDTO.setCounterCode(pramMap.get("OrganizationCodeOut"));//调出方柜台号
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
            dbObject.put("TradeType", CherryConstants.OS_BILLTYPE_BJ);
            // 单据号
            dbObject.put("TradeNoIF", billNoIF);
            // 修改次数
            dbObject.put("ModifyCounts", CherryConstants.DEFAULT_MODIFYCOUNTS);
            // 发生时间
            dbObject.put("OccurTime", auditDateTime);
            // 事件内容
            dbObject.put("Content", mqInfoDTO.getData());
            // 业务流水
            mqInfoDTO.setDbObject(dbObject);
            
            try {
                binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
            } catch (Exception e) {
                throw new CherryMQException(e.getMessage()+";There is an error that has occured in BINOLSTCM16_BL's sendMQMsg");
            }
        }
    }
    
    /**
     * 判断调出单号存在
     * @param billNo
     * @return
     */
    @Override
    public List<Map<String, Object>> selProductAllocationOut(String billNo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("billNo", billNo);
        List<Map<String,Object>> list = binOLSTCM16_Service.selProductAllocationOut(param);
        return list;
    }
}
