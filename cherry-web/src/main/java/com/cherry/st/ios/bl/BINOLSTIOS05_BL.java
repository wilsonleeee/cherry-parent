/*
 * @(#)BINOLSTIOS05_BL.java     1.0 2011/8/31
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

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.ProductionConstants;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.ios.interfaces.BINOLSTIOS05_IF;
import com.cherry.st.ios.service.BINOLSTIOS01_Service;
import com.cherry.st.ios.service.BINOLSTIOS05_Service;

/**
 * 
 * 自由盘点BL
 * 
 * @author niushunjie
 * @version 1.0 2011.8.31
 */
public class BINOLSTIOS05_BL  extends SsBaseBussinessLogic implements BINOLSTIOS05_IF{
    @Resource
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource
    private BINOLSTCM06_IF binOLSTCM06_BL;
    
    @Resource
    private BINOLSTIOS01_Service binOLSTIOS01_Service;
    
    @Resource
    private BINOLSTIOS05_Service binOLSTIOS05_Service;

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
        String[] reasonArr = list.get(3);
        String[] priceArr = list.get(4);
        String[] gainCountArr = list.get(5);
        String[] bookCountArr = list.get(6);
        String[] htArr = list.get(7);
        
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
        	
        	//验证产品厂商ID
        	if(CherryChecker.isNullOrEmpty(productVendorIdArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.Product_ProductVendorID});
            }
        	
        	//验证批次号
        	if("true".equals(map.get("isBatchStockTaking")) && CherryChecker.isNullOrEmpty(batchNoArr[i], true)){
        		throw new CherryException("EST00026",new String[]{ProductionConstants.ProductTaking_BatchNo});
        	}
        	
        	//验证数量
        	if(CherryChecker.isNullOrEmpty(gainCountArr[i], true)){
            	throw new CherryException("EST00026",new String[]{ProductionConstants.ProductTaking_Quantity});
            }
        }
        
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
        
        //总数量
        int totalQuantity = 0;
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
            totalQuantity += Integer.parseInt(gainCountArr[i]);
        }
        //总金额
        double totalAmount = 0.0;
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
            totalAmount += Double.parseDouble(priceArr[i])*Integer.parseInt(gainCountArr[i]);
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
            //用于区分门店做的自由盘点或商品盘点
            String addType = ConvertUtil.getString(map.get("addType"));
            //盘点类型
            if("0".equals(ConvertUtil.getString(map.get("blindFlag")))){
                mainData.put("Type", "F1");//自由盘点
                if(addType.equals("all")){
                    mainData.put("Type", "P1");//商品盘点
                }
            }else{
                mainData.put("Type", "F2");//自由盘点盲盘
                if(addType.equals("all")){
                    mainData.put("Type", "P2");//商品盘点盲盘
                }
            }
            //是否按批次盘点
            mainData.put("IsBatch", "0");
            
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            //i=0为隐藏
            for(int i=0;i<productVendorIdArr.length;i++){
                Map<String,Object> productDetail = new HashMap<String,Object>();
                productDetail.putAll(sessionMap);
                //明细连番
                productDetail.put("DetailNo", i+1);
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
                productDetail.put("BatchNo", batchNoArr!=null?batchNoArr[i]:null);
                String handleType = "2";
                if(null != htArr && htArr.length > i && !ConvertUtil.getString(htArr[i]).equals("")){
                    handleType = htArr[i];
                }
                //盘点处理方式
                productDetail.put("HandleType", handleType);
                //盘差
                productDetail.put("GainQuantity", gainCountArr[i]);
                
                if(batchNoArr!=null && null != batchNoArr[i] && !"".equals(batchNoArr[i])){
                    //取得批次ID
                    Map<String,Object> productBatch = binOLSTIOS01_Service.getProductBatchId(productDetail);
                    int productBatchId = 0;
                    if(null != productBatch){
                        productBatchId= CherryUtil.string2int(ConvertUtil.getString(productBatch.get("BIN_ProductBatchID")));
                    }
                    //批次ID
                    productDetail.put("BIN_ProductBatchID", productBatchId);
                    
                    //是否按批次盘点 只要明细里有一个就设置为按批次盘点
                    mainData.put("IsBatch", "1");
                }
                
                detailList.add(productDetail);
            }

            int productStockTakingID = 0;
            productStockTakingID = binOLSTCM06_BL.insertStockTakingAll(mainData, detailList);
            
            if(productStockTakingID == 0){
            	//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
            }
            
//            //准备参数，开始工作流
//            Map<String,Object> pramMap = new HashMap<String,Object>();
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CA);
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, productStockTakingID);
//            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
//            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
//            pramMap.put("CurrentUnit", "BINOLSTIOS05");
//            pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
//            pramMap.put("UserInfo", userinfo);
//            binOLSTCM00_BL.StartOSWorkFlow(pramMap);
            
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
     * 取得实体仓库的所属部门
     * @return
     */
    @Override
    public int getOrganIdByDepotID(Map<String, Object> map) {
        int organId = 0;
        List<Map<String, Object>> list = binOLSTIOS05_Service.getOrganIdByDepotInfoID(map);
        if(list.size()>0){
            organId = CherryUtil.obj2int(list.get(0).get("BIN_OrganizationID"));
        }
        return organId;
    }

    @Override
    public int tran_submit(Map<String, Object> map, List<String[]> list, UserInfo userinfo) throws Exception {
        int billID = tran_save(map, list, userinfo);
        if(billID == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
            
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CA);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
        pramMap.put("CurrentUnit", "BINOLSTIOS05");
        pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo", userinfo);
        binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        
        return billID;
    }
    
}
