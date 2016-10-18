/*	
 * @(#)BINOLSSCM08_BL.java     1.0 2012/09/27		
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
package com.cherry.ss.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM07_IF;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.common.service.BINOLSSCM08_Service;

/**
 * 
 * 促销品移库操作共通BL
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSCM08_BL implements BINOLSSCM08_IF{
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSSCM08_Service")
    private BINOLSSCM08_Service binOLSSCM08_Service;
    
    @Resource(name="binOLSSCM07_BL")
    private BINOLSSCM07_IF binOLSSCM07_BL;
    
    @Override
    public int insertPrmShiftAll(Map<String, Object> mainData,List<Map<String, Object>> detailList) {
        String billNo = ConvertUtil.getString(mainData.get("BillNo"));
        //判断单据号是否在参数中存在，如果不存在则生成
        if("".equals(billNo)){
            //组织ID
            String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
            //品牌ID
            String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
            //程序ID
            String name = "BINOLSSCM08";
            //调用共通生成单据号
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_MV);
            //将生成的单据号放到mainData中
            mainData.put("BillNo", billNo);
        }           
        if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
            mainData.put("BillNoIF", billNo);
        }
        
        //调用service往移库主表中插入数据并返回自增移库主记录ID
        int promotionShiftID = binOLSSCM08_Service.insertPromotionShift(mainData);
        
        //遍历detailList，将上面返回的productShiftId加到每个map中
        for(int index = 0; index < detailList.size() ; index ++){
            Map<String,Object> tempMap = detailList.get(index);
            tempMap.put("BIN_PromotionShiftID", promotionShiftID);
            if(null == tempMap.get("BIN_ProductVendorPackageID")){
                tempMap.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == tempMap.get("FromLogicInventoryInfoID")){
                tempMap.put("FromLogicInventoryInfoID", 0);
            }
            if(null == tempMap.get("FromStorageLocationInfoID")){
                tempMap.put("FromStorageLocationInfoID", 0);
            }
            if(null == tempMap.get("ToLogicInventoryInfoID")){
                tempMap.put("ToLogicInventoryInfoID", 0);
            }
            if(null == tempMap.get("ToStorageLocationInfoID")){
                tempMap.put("ToStorageLocationInfoID", 0);
            }
        }
        //调用service往移库明细表中插入数据
        binOLSSCM08_Service.insertPromotionShiftDetail(detailList); 
        return promotionShiftID;
    }

    @Override
    public int updatePrmShiftMain(Map<String, Object> mainData) {
        return binOLSSCM08_Service.updatePromotionShiftMain(mainData);
    }
    
    @Override
    public int deletePromotionShiftDetail(Map<String, Object> mainData) {
        return binOLSSCM08_Service.deletePromotionShiftDetail(mainData);
    }
    
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int promotionShiftID = Integer.parseInt(String.valueOf(praMap.get("BIN_PromotionShiftID")));
        Map<String, Object> mainData = getPrmShiftMainData(promotionShiftID);

        //一张移库单对应两张入出库单，这里是对应出库单
        Map<String, Object> praMainOut = new HashMap<String, Object>();
        praMainOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
        praMainOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
        praMainOut.put("RelevantNo",mainData.get("BillNoIF"));
        praMainOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
        praMainOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
        praMainOut.put("TotalQuantity",mainData.get("TotalQuantity"));
        praMainOut.put("TotalAmount","-"+mainData.get("TotalAmount"));
        praMainOut.put("WorkFlowID",mainData.get("WorkFlowID"));
        praMainOut.put("StockType",CherryConstants.STOCK_TYPE_OUT);
        praMainOut.put("TradeType",CherryConstants.BUSINESS_TYPE_MV);
        praMainOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
        praMainOut.put("ChangeFlag",0);
        praMainOut.put("Comments", mainData.get("Comments"));
        praMainOut.put("CreatedBy",praMap.get("CreatedBy"));
        praMainOut.put("CreatePGM",praMap.get("CreatePGM"));
        praMainOut.put("UpdatedBy",praMap.get("CreatedBy"));
        praMainOut.put("UpdatePGM",praMap.get("CreatePGM"));
        
        //出库单明细
        List<Map<String, Object>> pramListOut = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> detailList = getPrmShiftDetailData(promotionShiftID);
        for (int i = 0; i < detailList.size(); i++) {
            HashMap<String, Object> pramTemp = new HashMap<String, Object>();
            Map<String, Object> temp = detailList.get(i);
            // 促销品厂商ID
            pramTemp.put("BIN_PromotionProductVendorID", temp.get("BIN_PromotionProductVendorID"));
            // 明细连番
            pramTemp.put("DetailNo", temp.get("DetailNo"));
            // 数量
            pramTemp.put("Quantity", temp.get("Quantity"));
            // 价格
            pramTemp.put("Price", temp.get("Price"));
            // 包装类型ID
            pramTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
            // 入出库区分
            pramTemp.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // 实体仓库ID
            pramTemp.put("BIN_InventoryInfoID", temp.get("FromDepotInfoID"));
            // 逻辑仓库ID
            pramTemp.put("BIN_LogicInventoryInfoID", temp.get("FromLogicInventoryInfoID"));
            // 仓库库位ID
            pramTemp.put("BIN_StorageLocationInfoID", temp.get("FromStorageLocationInfoID"));
            // 入出库理由
            pramTemp.put("Comments", temp.get("Comments"));
            // 作成者
            pramTemp.put("CreatedBy", praMap.get("CreatedBy"));
            // 作成程序名
            pramTemp.put("CreatePGM", praMap.get("CreatePGM"));
            // 更新者
            pramTemp.put("UpdatedBy", praMap.get("CreatedBy"));
            // 更新程序名
            pramTemp.put("UpdatePGM", praMap.get("CreatePGM"));
            pramListOut.add(pramTemp);
        }
            
        int inOutID1 = binOLSSCM07_BL.insertPromotionStockInOutAll(praMainOut, pramListOut);
        
        //入库记录
        praMainOut.remove("TradeNo");
        praMainOut.remove("TradeNoIF");
        praMainOut.put("StockType",CherryConstants.STOCK_TYPE_IN);
        praMainOut.put("TotalAmount", String.valueOf(praMainOut.get("TotalAmount")).substring(1, String.valueOf(praMainOut.get("TotalAmount")).length()));
        pramListOut.clear();
        for (int i = 0; i < detailList.size(); i++) {
            HashMap<String, Object> pramTemp = new HashMap<String, Object>();
            Map<String, Object> temp = detailList.get(i);
            // 产品厂商ID
            pramTemp.put("BIN_PromotionProductVendorID", temp.get("BIN_PromotionProductVendorID"));
            // 明细连番
            pramTemp.put("DetailNo", temp.get("DetailNo"));
            // 数量
            pramTemp.put("Quantity", temp.get("Quantity"));
            // 价格
            pramTemp.put("Price", temp.get("Price"));
            // 包装类型ID
            pramTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
            // 入出库区分
            pramTemp.put("StockType", CherryConstants.STOCK_TYPE_IN);
            // 实体仓库ID
            pramTemp.put("BIN_InventoryInfoID", temp.get("ToDepotInfoID"));
            // 逻辑仓库ID
            pramTemp.put("BIN_LogicInventoryInfoID", temp.get("ToLogicInventoryInfoID"));
            // 仓库库位ID
            pramTemp.put("BIN_StorageLocationInfoID", temp.get("ToStorageLocationInfoID"));
            // 入出库理由
            pramTemp.put("Comments", temp.get("Comments"));
            // 作成者
            pramTemp.put("CreatedBy", praMap.get("CreatedBy"));
            // 作成程序名
            pramTemp.put("CreatePGM", praMap.get("CreatePGM"));
            // 更新者
            pramTemp.put("UpdatedBy", praMap.get("CreatedBy"));
            // 更新程序名
            pramTemp.put("UpdatePGM", praMap.get("CreatePGM"));
            pramListOut.add(pramTemp);
        }
        int inOutID2 = binOLSSCM07_BL.insertPromotionStockInOutAll(praMainOut, pramListOut);
        
        HashMap<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("BIN_PromotionStockInOutID", inOutID1);
        stockMap.put("CreatedBy", praMap.get("CreatedBy"));
        stockMap.put("CreatePGM", praMap.get("CreatePGM"));
        binOLSSCM07_BL.changeStock(stockMap);
        stockMap.put("BIN_PromotionStockInOutID", inOutID2);
        binOLSSCM07_BL.changeStock(stockMap);
    }

    @Override
    public Map<String, Object> getPrmShiftMainData(int prmShiftMainID,String... language) {
        Map<String, Object> pram = new HashMap<String,Object>();
        pram.put("BIN_PromotionShiftID", prmShiftMainID);
        if(language.length > 0){
            pram.put("language", language[0]);
        }
        return binOLSSCM08_Service.getPromotionShiftMainData(pram);
    }

    @Override
    public List<Map<String, Object>> getPrmShiftDetailData(int prmShiftMainID,String... language) {
        Map<String, Object> pram = new HashMap<String,Object>();
        pram.put("BIN_PromotionShiftID", prmShiftMainID);
        if(language.length > 0){
            pram.put("language", language[0]);
        }
        return binOLSSCM08_Service.getPromotionShiftDetailData(pram);
    }
}