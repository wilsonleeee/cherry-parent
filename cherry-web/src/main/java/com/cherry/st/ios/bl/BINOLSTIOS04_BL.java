/*
 * @(#)BINOLSTIOS04_BL.java     1.0 2011/9/28
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
package com.cherry.st.ios.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS04_IF;
import com.cherry.st.ios.service.BINOLSTIOS01_Service;
import com.cherry.st.ios.service.BINOLSTIOS04_Service;

/**
 * 
 * 商品盘点BL
 * 
 * @author niushunjie
 * @version 1.0 2011.9.28
 */
public class BINOLSTIOS04_BL  extends SsBaseBussinessLogic implements BINOLSTIOS04_IF{
    @Resource
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource
    private BINOLSTIOS01_Service binOLSTIOS01_Service;
    
    @Resource
    private BINOLSTIOS04_Service binOLSTIOS04_Service;
    
    /** 共通BL */
    @Resource
    private BINOLCM14_BL binOLCM14_BL;
    
    
    /**
     * 保存盘点信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception 
     */
    @Override
    public int tran_save(Map<String, Object> map, List<String[]> list,
            UserInfo userinfo) throws Exception {
        //从list中获取各种参数数组
        String[] productVendorIdArr = list.get(0);
        String[] batchNoArr = list.get(1);
        String[] quantityArr = list.get(2);
        String[] reasonArr = list.get(3);
        String[] priceArr = list.get(4);
        String[] gainCountArr = list.get(5);
        String[] bookCountArr = list.get(6);
        
        //申明sessionMap并在其中放入数据
        Map<String,Object> sessionMap = new HashMap<String,Object>();
        //创建者
        sessionMap.put("CreatedBy", map.get(CherryConstants.CREATEDBY));
        //创建程序
        sessionMap.put("CreatePGM", map.get(CherryConstants.CREATEPGM));
        //更新者
        sessionMap.put("UpdatedBy", map.get(CherryConstants.UPDATEDBY));
        //更新程序
        sessionMap.put("UpdatePGM", map.get(CherryConstants.UPDATEPGM));
        //所属组织
        sessionMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
        //品牌ID
        sessionMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
        
        // 盘点开始记录
        int startIndex = 0;
        // 总数量
        int totalQuantity = 0;
        // 总金额
        double totalAmount = 0.0;
        // 实盘数量是否允许负号
        String allowNegativeFlag = binOLCM14_BL.getConfigValue("1388",ConvertUtil.getString(userinfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userinfo.getBIN_BrandInfoID()));
        for(int i = startIndex ; i < productVendorIdArr.length ; i++){
        	// 实盘数量不允许为负时，需校验
        	if("1".equals(allowNegativeFlag)){
        		int gainCount =  ConvertUtil.getInt(quantityArr[i]);
        		if (gainCount<0){
        			throw new CherryException("EST00049");
        			}
        	}
            int tempCount = CherryUtil.string2int(quantityArr[i])-CherryUtil.string2int(bookCountArr[i]);
            double money = CherryUtil.string2double(priceArr[i])*tempCount;
            totalQuantity += tempCount;
            totalAmount += money;
        }
        
        try{
            //申明map存放盘点单主表数据
            Map<String,Object> mainData = new HashMap<String,Object>();
            mainData.putAll(sessionMap);
            //总数量
            mainData.put("TotalQuantity", totalQuantity);
            //总金额
            mainData.put("TotalAmount", totalAmount);
            //操作员
            String tradeEmployeeID = ConvertUtil.getString(map.get("TradeEmployeeID"));
            if(tradeEmployeeID.equals("")){
                mainData.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
            }else{
                mainData.put("BIN_EmployeeID", map.get("TradeEmployeeID"));
            }
            //下单部门
            mainData.put("BIN_OrganizationInfoIDDX", map.get("organizationId"));
            //下单员工
            mainData.put("BIN_EmployeeIDDX", userinfo.getBIN_EmployeeID());
            //审核区分
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
            //备注
            mainData.put("Comments", map.get("Comments"));
            //盘点部门
            mainData.put("BIN_OrganizationID", map.get("organizationId"));
            //业务类型
            mainData.put("TradeType", "GR");
            //盘点类型
            if("0".equals(ConvertUtil.getString(map.get("blindFlag")))){
                mainData.put("Type", "P1");
            }else{
                mainData.put("Type", "P2");//盲盘
            }
            mainData.put("IsBatch", map.get("IsBatch"));
            
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            for(int i=startIndex;i<productVendorIdArr.length;i++){
                int tempCount = CherryUtil.string2int(gainCountArr[i]);
                Map<String,Object> productDetail = new HashMap<String,Object>();
                productDetail.putAll(sessionMap);
                //明细连番
                productDetail.put("DetailNo", startIndex==0?i+1:i);
                //产品厂商ID
                productDetail.put("BIN_ProductVendorID", productVendorIdArr[i]);
                //实体仓库ID
                productDetail.put("BIN_InventoryInfoID", map.get("depotInfoId"));
                //逻辑仓库ID
                productDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(map.get("logicInventoryInfoId")));
                //包装类型ID
                productDetail.put("BIN_ProductVendorPackageID", "0");
                //仓库库位ID
                productDetail.put("BIN_StorageLocationInfoID", "0");
                //账面数量
                productDetail.put("Quantity", bookCountArr[i]);
                //备注
                productDetail.put("Comments", reasonArr[i]);
                //价格
                productDetail.put("Price", priceArr[i]);
                //批次号
                productDetail.put("BatchNo", batchNoArr[i]);
                //盘点处理方式
                if("".equals(quantityArr[i])||quantityArr[i]==null){
                    productDetail.put("HandleType", "1");
                    productDetail.put("GainQuantity", 0-CherryUtil.string2int(bookCountArr[i]));
                }else{
                    productDetail.put("HandleType", "2");
                    //盘差
                    productDetail.put("GainQuantity", tempCount);
                }
                
                if(null != batchNoArr[i] && !"".equals(batchNoArr[i])){
                    //取得批次ID
                    Map<String,Object> productBatch = binOLSTIOS01_Service.getProductBatchId(productDetail);
                    int productBatchId = 0;
                    if(null != productBatch){
                        productBatchId= CherryUtil.string2int(ConvertUtil.getString(productBatch.get("BIN_ProductBatchID")));
                    }
                    
                    //批次ID
                    productDetail.put("BIN_ProductBatchID", productBatchId);
                }
                
                detailList.add(productDetail);
            }
            int productStockTakingID = 0;
            
            productStockTakingID = binOLSTCM06_BL.insertStockTakingAll(mainData, detailList);
            
            if(productStockTakingID == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }
            
            //准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CA);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, productStockTakingID);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTIOS04");
            pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userinfo);
            binOLSTCM00_BL.StartOSWorkFlow(pramMap);
            return productStockTakingID;
        }catch(Exception e){
        	if(e instanceof CherryException){
        		throw (CherryException)e;
        	}else{
        		throw new CherryException(e.getMessage());
        	}
        }
    }
    
    
    /**
     * 暂存盘点信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception 
     */
    @Override
    public int tran_saveTemp(Map<String, Object> map, List<String[]> list,UserInfo userinfo) throws Exception {
        //从list中获取各种参数数组
        String[] productVendorIdArr = list.get(0);
        String[] batchNoArr = list.get(1);
        String[] quantityArr = list.get(2);
        String[] reasonArr = list.get(3);
        String[] priceArr = list.get(4);
        String[] gainCountArr = list.get(5);
        String[] bookCountArr = list.get(6);
        
        //申明sessionMap并在其中放入数据
        Map<String,Object> sessionMap = new HashMap<String,Object>();
        //创建者
        sessionMap.put("CreatedBy", map.get(CherryConstants.CREATEDBY));
        //创建程序
        sessionMap.put("CreatePGM", map.get(CherryConstants.CREATEPGM));
        //更新者
        sessionMap.put("UpdatedBy", map.get(CherryConstants.UPDATEDBY));
        //更新程序
        sessionMap.put("UpdatePGM", map.get(CherryConstants.UPDATEPGM));
        //所属组织
        sessionMap.put("BIN_OrganizationInfoID", map.get(CherryConstants.ORGANIZATIONINFOID));
        //品牌ID
        sessionMap.put("BIN_BrandInfoID", map.get(CherryConstants.BRANDINFOID));
        
        //盘点开始记录
        int startIndex = 0;
        
        //总数量
        int totalQuantity = 0;
        //总金额
        double totalAmount = 0.0;
     // 实盘数量是否允许负号
        String allowNegativeFlag = binOLCM14_BL.getConfigValue("1388",ConvertUtil.getString(userinfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userinfo.getBIN_BrandInfoID()));
        for(int i = startIndex ; i < productVendorIdArr.length ; i++){
        	// 实盘数量不允许为负时，需校验
        	if("1".equals(allowNegativeFlag)){
        		int gainCount =  ConvertUtil.getInt(quantityArr[i]);
        		if (gainCount<0){
        			throw new CherryException("EST00049");
        			}
        	}
            int tempCount = CherryUtil.string2int(quantityArr[i])-CherryUtil.string2int(bookCountArr[i]);
            double money = CherryUtil.string2double(priceArr[i])*tempCount;
            totalQuantity += tempCount;
            totalAmount += money;
        }
        
        try{
            //申明map存放盘点单主表数据
            Map<String,Object> mainData = new HashMap<String,Object>();
            mainData.putAll(sessionMap);
            //总数量
            mainData.put("TotalQuantity", totalQuantity);
            //总金额
            mainData.put("TotalAmount", totalAmount);
            //操作员
            String tradeEmployeeID = ConvertUtil.getString(map.get("TradeEmployeeID"));
            if(tradeEmployeeID.equals("")){
                mainData.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
            }else{
                mainData.put("BIN_EmployeeID", map.get("TradeEmployeeID"));
            }
            //下单部门
            mainData.put("BIN_OrganizationInfoIDDX", map.get("organizationId"));
            //下单员工
            mainData.put("BIN_EmployeeIDDX", userinfo.getBIN_EmployeeID());
            //审核区分
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
            //备注
            mainData.put("Comments", map.get("Comments"));
            //盘点部门
            mainData.put("BIN_OrganizationID", map.get("organizationId"));
            //业务类型
            mainData.put("TradeType", "GR");
            //盘点类型
            if("0".equals(ConvertUtil.getString(map.get("blindFlag")))){
                mainData.put("Type", "P1");
            }else{
                mainData.put("Type", "P2");//盲盘
            }
            mainData.put("IsBatch", map.get("IsBatch"));
            
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            for(int i=startIndex;i<productVendorIdArr.length;i++){
                int tempCount = CherryUtil.string2int(gainCountArr[i]);
                Map<String,Object> productDetail = new HashMap<String,Object>();
                productDetail.putAll(sessionMap);
                //明细连番
                productDetail.put("DetailNo", startIndex==0?i+1:i);
                //产品厂商ID
                productDetail.put("BIN_ProductVendorID", productVendorIdArr[i]);
                //实体仓库ID
                productDetail.put("BIN_InventoryInfoID", map.get("depotInfoId"));
                //逻辑仓库ID
                productDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(map.get("logicInventoryInfoId")));
                //包装类型ID
                productDetail.put("BIN_ProductVendorPackageID", "0");
                //仓库库位ID
                productDetail.put("BIN_StorageLocationInfoID", "0");
                //账面数量
                productDetail.put("Quantity", bookCountArr[i]);
                //备注
                productDetail.put("Comments", reasonArr[i]);
                //价格
                productDetail.put("Price", priceArr[i]);
                //批次号
                productDetail.put("BatchNo", batchNoArr[i]);
                //盘点处理方式
                if("".equals(quantityArr[i])||quantityArr[i]==null){
                    productDetail.put("HandleType", "1");
                    productDetail.put("GainQuantity", 0-CherryUtil.string2int(bookCountArr[i]));
                }else{
                    productDetail.put("HandleType", "2");
                    //盘差
                    productDetail.put("GainQuantity", tempCount);
                }
                
                if(null != batchNoArr[i] && !"".equals(batchNoArr[i])){
                    //取得批次ID
                    Map<String,Object> productBatch = binOLSTIOS01_Service.getProductBatchId(productDetail);
                    int productBatchId = 0;
                    if(null != productBatch){
                        productBatchId= CherryUtil.string2int(ConvertUtil.getString(productBatch.get("BIN_ProductBatchID")));
                    }
                    
                    //批次ID
                    productDetail.put("BIN_ProductBatchID", productBatchId);
                }
                
                detailList.add(productDetail);
            }
            int productStockTakingID = 0;
            
            productStockTakingID = binOLSTCM06_BL.insertStockTakingAll(mainData, detailList);
            
            if(productStockTakingID == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }            
            return productStockTakingID;
        }catch(Exception e){
        	if(e instanceof CherryException){
        		throw (CherryException)e;
        	}else{
        		throw new CherryException(e.getMessage());
        	}
        }
    }

    @Override
    public List<Map<String, Object>> getPrtCatPropertyList(Map<String, Object> map) {
        return binOLSTIOS04_Service.getPrtCatPropertyList(map);
    }
    
    @Override
    public List<Map<String, Object>> getPrtCatPropValueList(Map<String, Object> map) {
        return binOLSTIOS04_Service.getPrtCatPropValueList(map);
    }

    @Override
    public List<Map<String, Object>> getProductByBatchList(
            Map<String, Object> map) {
        return binOLSTIOS04_Service.getProductByBatchList(map);
    }

    @Override
    public List<Map<String, Object>> getProductStockList(Map<String, Object> map) {
        return binOLSTIOS04_Service.getProductStockList(map);
    }

}
