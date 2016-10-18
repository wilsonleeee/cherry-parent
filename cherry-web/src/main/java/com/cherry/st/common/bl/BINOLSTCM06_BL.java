/*  
 * @(#)BINOLSTCM06_BL.java    1.0 2011.09.20
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

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.service.BINOLSTCM06_Service;

/**
 * 
 * 产品盘点业务共通Service
 * 
 * @author niushunjie
 * @version 1.0 2011.09.20
 */
public class BINOLSTCM06_BL implements BINOLSTCM06_IF{

    @Resource
    private BINOLCM03_BL binOLCM03_BL;

    @Resource
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource
    private BINOLSTCM06_Service binOLSTCM06_Service;
    
    /**
     * 将盘点信息写入盘点单据主从表。
     * @param mainData
     * @param detailList
     * @return
     */
    @Override
    public int insertStockTakingAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
        int productStockTakingID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String stockTakingNo = ConvertUtil.getString(mainData.get("StockTakingNo"));
        String stockTakingNoIF = ConvertUtil.getString(mainData.get("StockTakingNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType =CherryConstants.BUSINESS_TYPE_CA;
        //如果stockTakingNo不存在调用共通生成单据号
        if(null == stockTakingNo || "".equals(stockTakingNo)){
            stockTakingNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("StockTakingNo", stockTakingNo);
        }
        if(null == stockTakingNoIF || "".equals(stockTakingNoIF)){
            mainData.put("StockTakingNoIF", stockTakingNo);
        }
        
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        
        //插入产品盘点单主表
        productStockTakingID = binOLSTCM06_Service.insertProductStockTaking(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productTakingDetail = detailList.get(i);
            productTakingDetail.put("BIN_ProductTakingID", productStockTakingID); 
            if(null == productTakingDetail.get("BIN_ProductVendorPackageID")){
                productTakingDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == productTakingDetail.get("BIN_LogicInventoryInfoID")){
                productTakingDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productTakingDetail.get("BIN_StorageLocationInfoID")){
                productTakingDetail.put("BIN_StorageLocationInfoID", 0);
            }
            //插入产品盘点单明细表
            binOLSTCM06_Service.insertProductTakingDetail(productTakingDetail);
        }
        
        return productStockTakingID;
    }

    /**
     * 修改盘点单据主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateStockTakingMain(Map<String, Object> praMap) {
        return binOLSTCM06_Service.updateProductStockTakingMain(praMap);
    }

    /**
     * 根据盘点单据来写入出库记录主从表，并修改库存。
     * @param praMap
     */
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int productStockTakingID = CherryUtil.obj2int(praMap.get("BIN_ProductStockTakingID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        
        Map<String, Object> mainData = getStockTakingMainData(productStockTakingID,null);
        int totalQuantity = CherryUtil.obj2int(mainData.get("TotalQuantity"));
        
        //入出库主表
        Map<String, Object> praMainInOut = new HashMap<String, Object>();
        praMainInOut.putAll(mainData);
        praMainInOut.put("RelevanceNo", ConvertUtil.getString(mainData.get("StockTakingNo")));
        praMainInOut.put("TotalQuantity", Math.abs(totalQuantity));
        praMainInOut.put("StockType", totalQuantity>=0?"0":"1");
        praMainInOut.put("TradeType", "CA");
        praMainInOut.put("CreatedBy", createdBy);
        praMainInOut.put("CreatePGM", createPGM);
        praMainInOut.put("UpdatedBy", createdBy);
        praMainInOut.put("UpdatePGM", createPGM);
        
        //入出库明细表
        List<Map<String, Object>> inOutDetailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> stockTakingdetailList = getStockTakingDetailData(productStockTakingID,null);
        for(int i=0;i<stockTakingdetailList.size();i++){
            Map<String, Object> temp = stockTakingdetailList.get(i);
            int gainQuantity = CherryUtil.obj2int(temp.get("GainQuantity"));
            //temp.put("BIN_ProductBatchID","");
            temp.put("Quantity", Math.abs(gainQuantity));
            temp.put("StockType", gainQuantity>=0?"0":"1");
            temp.put("ChangeCount", "0");
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            inOutDetailList.add(temp);
        }
        int inOutID = binOLSTCM01_BL.insertProductInOutAll(praMainInOut, inOutDetailList);

        HashMap<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("BIN_ProductInOutID", inOutID);
        stockMap.put("CreatedBy", praMap.get("CreatedBy"));
        stockMap.put("CreatePGM", praMap.get("CreatePGM"));
        binOLSTCM01_BL.changeStock(stockMap);
        
    }

    /**
     * 给盘点单ID，取得概要信息。
     * @param productStockTakingID
     * @return
     */
    @Override
    public Map<String, Object> getStockTakingMainData(int productStockTakingID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductStockTakingID", productStockTakingID);
        return binOLSTCM06_Service.getProductStockTakingMainData(map);
    }
    
    /**
     * 给盘点单ID，取得明细信息。
     * @param productStockTakingID
     * @return
     */
    @Override
    public List<Map<String, Object>> getStockTakingDetailData(
            int productStockTakingID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductTakingID", productStockTakingID);
        return binOLSTCM06_Service.getProductStockTakingDetailData(map);
    }

    /**
     * 给盘点单ID，取得明细信息。
     * @param productStockTakingID
     * @param detailOrderBy
     * @return
     */
    @Override
    public List<Map<String, Object>> getStockTakingDetailDataByOrder(int productStockTakingID,String language,String detailOrderBy) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductTakingID", productStockTakingID);
        map.put("detailOrderBy", detailOrderBy);
        return binOLSTCM06_Service.getProductStockTakingDetailData(map);
    }
}
