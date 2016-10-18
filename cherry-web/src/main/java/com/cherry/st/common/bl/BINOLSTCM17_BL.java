/*  
 * @(#)BINOLSTCM17_BL.java     1.0 2012/12/06      
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM17_IF;
import com.cherry.st.common.service.BINOLSTCM17_Service;

/**
 * 出库业务相关操作BL
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public class BINOLSTCM17_BL implements BINOLSTCM17_IF{

    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSTCM01_BL")
    private BINOLSTCM01_IF binOLSTCM01_BL;
    
    @Resource(name="binOLSTCM17_Service")
    private BINOLSTCM17_Service binOLSTCM17_Service;
    
    /**
     * 
     *将产品出库信息写入入库主从表
     * 
     *@param mainData
     *@param detailList
     */
    @Override
    public int insertProductOutDepotAll(Map<String, Object> mainData,List<Map<String, Object>> detailList) {
        String billNo = ConvertUtil.getString(mainData.get("BillNo"));
        //判断单据号是否在参数中存在，如果不存在则生成
        if("".equals(billNo)){
            //组织ID
            String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
            //品牌ID
            String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
            //程序ID
            String name = "BINOLSTCM17";
            //调用共通生成单据号
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_OT);
            //将生成的单据号放到mainData中
            mainData.put("BillNo", billNo);
        }
        if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
            mainData.put("BillNoIF", billNo);
        }
        //插入出库主表数据
        int productOutDepotID = binOLSTCM17_Service.insertProductOutDepot(mainData);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> mapDetail = detailList.get(i);
            mapDetail.put("BIN_ProductOutDepotID", productOutDepotID);
            if(null == mapDetail.get("BIN_ProductVendorPackageID")){
                mapDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == mapDetail.get("BIN_LogicInventoryInfoID")){
                mapDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == mapDetail.get("BIN_StorageLocationInfoID")){
                mapDetail.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSTCM17_Service.insertProductOutDepotDetail(detailList);
        return productOutDepotID;
    }

    /**
     * 给出库单主ID，取得概要信息。
     * @param productOutDepotID
     * @return
     */
    @Override
    public Map<String, Object> getProductOutDepotMainData(int productOutDepotID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductOutDepotID", productOutDepotID);
        map.put("language", language);
        return binOLSTCM17_Service.getProductOutDepotMainData(map);
    }

    /**
     * 给定出库单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    @Override
    public List<Map<String, Object>> getProductOutDepotDetailData(int productOutDepotID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_ProductOutDepotID", productOutDepotID);
        map.put("language", language);
        return binOLSTCM17_Service.getProductOutDepotDetailData(map);
    }
    
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int productOutDepotID = Integer.parseInt(String.valueOf(praMap.get("BIN_ProductOutDepotID")));
        
        Map<String, Object> mainData = getProductOutDepotMainData(productOutDepotID,"");
        //入出库主表
        Map<String, Object> praMainInOut = new HashMap<String, Object>();
        
        praMainInOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
        praMainInOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
        praMainInOut.put("RelevanceNo",mainData.get("BillNoIF"));
        praMainInOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
        praMainInOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
        praMainInOut.put("TotalQuantity",mainData.get("TotalQuantity"));
        praMainInOut.put("TotalAmount",mainData.get("TotalAmount"));
        praMainInOut.put("WorkFlowID",mainData.get("WorkFlowID"));
        praMainInOut.put("StockType",CherryConstants.STOCK_TYPE_OUT);
        praMainInOut.put("TradeType",CherryConstants.BUSINESS_TYPE_OT);
        praMainInOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
        praMainInOut.put("ChangeCount",0);
        praMainInOut.put("Comments", mainData.get("Comments"));
        praMainInOut.put("CreatedBy",praMap.get("CreatedBy"));
        praMainInOut.put("CreatePGM",praMap.get("CreatePGM"));
        praMainInOut.put("UpdatedBy",praMap.get("CreatedBy"));
        praMainInOut.put("UpdatePGM",praMap.get("CreatePGM"));
        
        //入出库明细表
        List<Map<String, Object>> inOutDetailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> inDopdetailList = getProductOutDepotDetailData(productOutDepotID,"");
        for(int i=0;i<inDopdetailList.size();i++){
            Map<String, Object> temp = inDopdetailList.get(i);
            Map<String, Object> praTemp = new HashMap<String, Object>();
            // 产品厂商ID
            praTemp.put("BIN_ProductVendorID", temp.get("BIN_ProductVendorID"));
            //产品批次
            praTemp.put("BIN_ProductBatchID", temp.get("BIN_ProductBatchID"));
            // 明细连番
            praTemp.put("DetailNo", temp.get("DetailNo"));
            // 数量
            praTemp.put("Quantity", temp.get("Quantity"));
            // 价格
            praTemp.put("Price", temp.get("Price"));
            // 包装类型ID
            praTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
            // 入出库区分
            praTemp.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // 实体仓库ID
            praTemp.put("BIN_InventoryInfoID", temp.get("BIN_InventoryInfoID"));
            // 逻辑仓库ID
            praTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
            // 仓库库位ID
            praTemp.put("BIN_StorageLocationInfoID", temp.get("BIN_StorageLocationInfoID"));
            // 入出库理由
            praTemp.put("Comments", temp.get("Comments"));
            // 修改次数
            praTemp.put("ChangeCount", 0);
            // 作成者
            praTemp.put("CreatedBy", praMap.get("CreatedBy"));
            // 作成程序名
            praTemp.put("CreatePGM", praMap.get("CreatePGM"));
            // 更新者
            praTemp.put("UpdatedBy", praMap.get("CreatedBy"));
            // 更新程序名
            praTemp.put("UpdatePGM", praMap.get("CreatePGM"));
            inOutDetailList.add(praTemp);
        }
        int inOutID = binOLSTCM01_BL.insertProductInOutAll(praMainInOut, inOutDetailList);

        HashMap<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("BIN_ProductInOutID", inOutID);
        stockMap.put("CreatedBy", praMap.get("CreatedBy"));
        stockMap.put("CreatePGM", praMap.get("CreatePGM"));
        binOLSTCM01_BL.changeStock(stockMap);
    }
}