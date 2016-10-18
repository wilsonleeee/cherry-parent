/*  
 * @(#)BINOLSSCM09_IF.java    1.0 2013/01/25
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM07_IF;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.cherry.ss.common.service.BINOLSSCM09_Service;

/**
 * 
 * 入库共通BL
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSCM09_BL implements BINOLSSCM09_IF{

    @Resource(name="binOLSSCM07_BL")
    private BINOLSSCM07_IF binOLSSCM07_BL;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSSCM09_Service")
    private BINOLSSCM09_Service binOLSSCM09_Service;
    
    /**
     * 
     *将促销品入库信息写入入库主从表
     * 
     *@param mainData
     *@param detailList
     */
    public int insertPrmInDepotAll(Map<String, Object> mainData,
            List<Map<String, Object>> detailList) {
        String billNo = ConvertUtil.getString(mainData.get("BillNo"));
        //判断单据号是否在参数中存在，如果不存在则生成
        if("".equals(billNo)){
            //组织ID
            String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
            //品牌ID
            String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
            //程序ID
            String name = "BINOLSSCM09";
            //调用共通生成单据号
            billNo = binOLCM03_BL.getTicketNumber(organizationInfoId, brandInfoId, name, CherryConstants.BUSINESS_TYPE_GR);
            //将生成的单据号放到mainData中
            mainData.put("BillNo", billNo);
        }
        if("".equals(ConvertUtil.getString(mainData.get("BillNoIF")))){
            mainData.put("BillNoIF", billNo);
        }
        //插入入库主表数据
        int prmInDepotID = binOLSSCM09_Service.insertPrmInDepot(mainData);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> mapDetail = detailList.get(i);
            mapDetail.put("BIN_PrmInDepotID", prmInDepotID);
//            if(null == mapDetail.get("BIN_ProductVendorPackageID")){
//                mapDetail.put("BIN_ProductVendorPackageID", 0);
//            }
            if(null == mapDetail.get("BIN_LogicInventoryInfoID")){
                mapDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == mapDetail.get("BIN_StorageLocationInfoID")){
                mapDetail.put("BIN_StorageLocationInfoID", 0);
            }
        }
        binOLSSCM09_Service.insertPrmInDepotDetail(detailList);
        return prmInDepotID;
    }
    
    /**
     * 
     *修改入库单据主表数据。
     * 
     *@param praMap
     */
    @Override
    public int updatePrmInDepotMain(Map<String, Object> praMap) {
        return binOLSSCM09_Service.updatePrmInDepotMain(praMap);
    }
    
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int prmInDepotID = CherryUtil.obj2int(praMap.get("BIN_PrmInDepotID"));
        
        Map<String, Object> mainData = getPrmInDepotMainData(prmInDepotID,"");
        //入出库主表
        Map<String, Object> praMainInOut = new HashMap<String, Object>();
        
        praMainInOut.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
        praMainInOut.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
        praMainInOut.put("RelevantNo",mainData.get("BillNoIF"));
        praMainInOut.put("BIN_OrganizationID",mainData.get("BIN_OrganizationID"));
        praMainInOut.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));
        praMainInOut.put("TotalQuantity",mainData.get("TotalQuantity"));
        praMainInOut.put("TotalAmount",mainData.get("TotalAmount"));
        praMainInOut.put("WorkFlowID",mainData.get("WorkFlowID"));
        praMainInOut.put("StockType","0");
        praMainInOut.put("TradeType",CherryConstants.BUSINESS_TYPE_STORAGE_IN);
        praMainInOut.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
        praMainInOut.put("Comments", mainData.get("Comments"));
        praMainInOut.put("StockInOutDate", praMap.get("StockInOutDate"));
        praMainInOut.put("StockInOutTime", praMap.get("StockInOutTime"));
        praMainInOut.put("CreatedBy",praMap.get("CreatedBy"));
        praMainInOut.put("CreatePGM",praMap.get("CreatePGM"));
        praMainInOut.put("UpdatedBy",praMap.get("CreatedBy"));
        praMainInOut.put("UpdatePGM",praMap.get("CreatePGM"));
        
        //入出库明细表
        List<Map<String, Object>> inOutDetailList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> inDopdetailList = getPrmInDepotDetailData(prmInDepotID,"");
        for(int i=0;i<inDopdetailList.size();i++){
            Map<String, Object> temp = inDopdetailList.get(i);
            Map<String, Object> praTemp = new HashMap<String, Object>();
            // 促销品厂商ID
            praTemp.put("BIN_PromotionProductVendorID", temp.get("BIN_PromotionProductVendorID"));
            // 促销品批次
            praTemp.put("BIN_PromotionBatchID", temp.get("BIN_PromotionBatchID"));
            // 明细连番
            praTemp.put("DetailNo", temp.get("DetailNo"));
            // 数量
            praTemp.put("Quantity", temp.get("Quantity"));
            // 价格
            praTemp.put("Price", temp.get("Price"));
            // 包装类型ID
//            praTemp.put("BIN_ProductVendorPackageID", temp.get("BIN_ProductVendorPackageID"));
            // 入出库区分
            praTemp.put("StockType", CherryConstants.STOCK_TYPE_IN);
            // 实体仓库ID
            praTemp.put("BIN_InventoryInfoID", temp.get("BIN_InventoryInfoID"));
            // 逻辑仓库ID
            praTemp.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
            // 仓库库位ID
            praTemp.put("BIN_StorageLocationInfoID", temp.get("BIN_StorageLocationInfoID"));
            // 入出库理由
            praTemp.put("Comments", temp.get("Comments"));
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
        int inOutID = binOLSSCM07_BL.insertPromotionStockInOutAll(praMainInOut, inOutDetailList);

        HashMap<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("BIN_PromotionStockInOutID", inOutID);
        stockMap.put("CreatedBy", praMap.get("CreatedBy"));
        stockMap.put("CreatePGM", praMap.get("CreatePGM"));
        binOLSSCM07_BL.changeStock(stockMap);
    }

    /**
     * 
     *取得入库信息
     * 
     *@param praMap
     */
    @Override
    public Map<String, Object> getPrmInDepotMainData(int prmInDepotMainID,String language) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("BIN_PrmInDepotID", prmInDepotMainID);
        map.put("language", language);
        Map<String,Object> mainData = binOLSSCM09_Service.getPrmInDepotMainData(map);
        return mainData;
    }
    
    /**
     * 
     *取得入库明细信息
     * 
     *@param praMap
     */
    @Override
    public List<Map<String, Object>> getPrmInDepotDetailData(int prmInDepotMainID,String language) {
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("BIN_PrmInDepotID", prmInDepotMainID);
        map.put("language", language);
        List<Map<String, Object>> returnList = binOLSSCM09_Service.getPrmInDepotDetailData(map);
        return returnList;
    }
    
    /**
     * 判断入库单号存在
     * @param relevantNo
     * @return
     */
    @Override
    public List<Map<String, Object>> selPrmInDepot(String relevantNo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("relevantNo", relevantNo);
        List<Map<String,Object>> list = binOLSSCM09_Service.selPrmInDepot(param);
        return list;
    }
    
    /**
     * 给定入库单主ID，删除入库单明细
     * @param praMap
     * @return
     */
    @Override
    public int delPrmInDepotDetailData(Map<String,Object> praMap){
        return binOLSSCM09_Service.delPrmInDepotDetailData(praMap);
    }
    
    /**
     * 插入入库单明细表
     * @param list
     */
    @Override
    public void insertPrmInDepotDetail(List<Map<String,Object>> list){
        binOLSSCM09_Service.insertPrmInDepotDetail(list);
    }
}