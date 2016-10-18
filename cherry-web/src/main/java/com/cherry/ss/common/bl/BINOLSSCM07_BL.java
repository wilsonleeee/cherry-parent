/*	
 * @(#)BINOLSSCM07_BL.java     1.0 2012/09/27		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.interfaces.BINOLSSCM07_IF;
import com.cherry.ss.common.service.BINOLSSCM07_Service;

/**
 * 
 * 促销品入出库操作共通BL
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSCM07_BL implements BINOLSSCM07_IF{

    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLSSCM07_Service")
    private BINOLSSCM07_Service binOLSSCM07_Service;
    
    
    /**
     * 将促销品入出库信息写入入出库主从表，并更新相应的库存记录。
     * @param mainData
     * @param detailList
     * @return 入出库主表的自增ID
     */
    @Override
    public int insertPromotionStockInOutAll(Map<String, Object> mainData, List<Map<String, Object>> detailList) {
        int promotionStockInOutID = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String tradeNo = ConvertUtil.getString(mainData.get("TradeNo"));
        String tradeNoIF = ConvertUtil.getString(mainData.get("TradeNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        
        //当明细数量全是0，入出库主表从表都不插入，返回0，其他情况只插入数量不是0的明细
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productInOutDetail = detailList.get(i);
            if(CherryUtil.obj2int(productInOutDetail.get("Quantity")) == 0){
                detailList.remove(i);
                i--;
                continue;
            }
        }
        if(detailList.size() == 0){
            return 0;
        }
        
        //如果tradeNo不存在调用共通生成单据号
        if("".equals(tradeNo)){
            tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,"IO");
            mainData.put("TradeNo", tradeNo);
        }
        if("".equals(tradeNoIF)){
            mainData.put("TradeNoIF", tradeNo);
        }
        if(null == mainData.get("StockInOutDate")){
            mainData.put("StockInOutDate", binOLSSCM07_Service.getDateYMD());
        }
        if(null == mainData.get("StockInOutTime")){
            mainData.put("StockInOutTime", binOLSSCM07_Service.getSYSDateConf());
        }
        //插入促销品入出库表
        promotionStockInOutID = binOLSSCM07_Service.insertPromotionStockInOut(mainData);
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> productInOutDetail = detailList.get(i);
            productInOutDetail.put("BIN_PromotionStockInOutID", promotionStockInOutID); 
            
            if(null == productInOutDetail.get("BIN_ProductVendorPackageID")){
                productInOutDetail.put("BIN_ProductVendorPackageID", 0);
            }
            if(null == productInOutDetail.get("BIN_LogicInventoryInfoID")){
                productInOutDetail.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == productInOutDetail.get("BIN_StorageLocationInfoID")){
                productInOutDetail.put("BIN_StorageLocationInfoID", 0);
            }
            
            productInOutDetail.put("DetailNo",i+1);
            
            //插入促销品入出库明细表
            binOLSSCM07_Service.insertPromotionStockDetail(productInOutDetail);
        }
        
        return promotionStockInOutID;
    }

    /**
     * 修改入出库单据主表数据。
     * @param praMap
     * @return 更新影响的数据行数
     */
    @Override
    public int updatePromotionStockInOutMain(Map<String, Object> praMap) {
        return binOLSSCM07_Service.updatePromotionStockInOutMain(praMap);
    }

    /**
     * 根据入出库单据修改库存。
     * @param praMap
     */
    @Override
    public void changeStock(Map<String, Object> praMap) {
        int promotionStockInOutID = CherryUtil.string2int(ConvertUtil.getString(praMap.get("BIN_PromotionStockInOutID")));
        String updatedBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String updatePGM = ConvertUtil.getString(praMap.get("CreatePGM"));
        List<Map<String,Object>> list = getPromotionStockDetailData(promotionStockInOutID,null);
        for(int i=0;i<list.size();i++){
            Map<String,Object> promotionStock = list.get(i);
            if(null == promotionStock.get("BIN_LogicInventoryInfoID")){
                promotionStock.put("BIN_LogicInventoryInfoID", 0);
            }
            if(null == promotionStock.get("BIN_StorageLocationInfoID")){
                promotionStock.put("BIN_StorageLocationInfoID", 0);
            }
            if(null == promotionStock.get("BIN_ProductVendorPackageID")){
                promotionStock.put("BIN_ProductVendorPackageID", 0);
            }
            promotionStock.put("UpdatedBy", updatedBy);
            promotionStock.put("UpdatePGM", updatePGM);
            int cnt = binOLSSCM07_Service.updatePromotionStock(promotionStock);
            promotionStock.put("CreatedBy", updatedBy);
            promotionStock.put("CreatePGM", updatePGM);
            if(cnt<1){
                binOLSSCM07_Service.insertPromotionStock(promotionStock);
            }
        }
    }
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param productInOutMainID
     * @return
     */
    @Override
    public Map<String,Object> getPromotionStockInOutMainData (int promotionStockInOutID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        map.put("language", language);
        return binOLSSCM07_Service.getPromotionStockInOutMainData(map);
    }

    /**
     * 给定入出库单主ID，取得明细信息。
     * @param productInOutMainID
     * @return
     */
    @Override
    public List<Map<String, Object>> getPromotionStockDetailData(int promotionStockInOutID,String language) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("BIN_PromotionStockInOutID", promotionStockInOutID);
        map.put("language", language);
        return binOLSSCM07_Service.getPromotionStockDetailData(map);
    }
}