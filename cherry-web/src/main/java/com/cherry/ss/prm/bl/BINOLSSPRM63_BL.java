/*
 * @(#)BINOLSSPRM63_BL.java     1.0 2013/01/25
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
package com.cherry.ss.prm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.cherry.ss.prm.interfaces.BINOLSSPRM63_IF;
import com.cherry.ss.prm.service.BINOLSSPRM63_Service;

/**
 * 
 * 入库BL
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class BINOLSSPRM63_BL  extends SsBaseBussinessLogic implements BINOLSSPRM63_IF{
    @Resource(name="binOLSSCM00_BL")
    private BINOLSSCM00_BL binOLSSCM00_BL;
    
    @Resource(name="binOLSSCM09_BL")
    private BINOLSSCM09_IF binOLSSCM09_BL;
    
    @Resource(name="binOLSSPRM63_Service")
    private BINOLSSPRM63_Service binOLSSPRM63_Service;
    
    @Override
    public int tran_save(Map<String, Object> map, List<String[]> list,UserInfo userinfo) throws Exception {
        //从list中获取各种参数数组
        String[] prmVendorIdArr = list.get(0);
//        String[] batchNoArr = list.get(1);
        String[] quantityArr = list.get(1);
        String[] reasonArr = list.get(2);
        String[] priceArr = list.get(3);
        
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
        for(int i = 0 ; i < prmVendorIdArr.length ; i++){
            totalQuantity += Integer.parseInt(quantityArr[i]);
        }
        //总金额
        double totalAmount = 0.0;
        for(int i = 0 ; i < prmVendorIdArr.length ; i++){
            totalAmount += Double.parseDouble(priceArr[i])*Integer.parseInt(quantityArr[i]);
        }
        try{
            //申明map存放移库主表数据
            Map<String,Object> mainData = new HashMap<String,Object>();
            mainData.putAll(sessionMap);
            //往来单位
            mainData.put("BIN_BussinessPartnerID", map.get("bussinessPartnerId"));
            //入库部门
            mainData.put("BIN_OrganizationID", map.get("organizationId"));
            //操作员
            mainData.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
            //总数量
            mainData.put("TotalQuantity", totalQuantity);
            //总金额
            mainData.put("TotalAmount", totalAmount);
            //申请总数量
            mainData.put("PreTotalQuantity", totalQuantity);
            //申请总金额
            mainData.put("PreTotalAmount", totalAmount);
            //物流ID
            mainData.put("BIN_LogisticInfoID", "0");
            //备注
            mainData.put("Comments", map.get("comments"));
            //审核区分
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
            //入库状态
            mainData.put("TradeStatus", CherryConstants.BILLTYPE_GR_UNDO);
            //备注
            mainData.put("Comments", map.get("comments"));
            //入库时间
            mainData.put("InDepotDate", map.get("inDepotDate"));
            
            List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
            
            for(int i=0;i<prmVendorIdArr.length;i++){
                Map<String,Object> productDetail = new HashMap<String,Object>();
                productDetail.putAll(sessionMap);
                //产品厂商ID
                productDetail.put("BIN_PromotionProductVendorID", prmVendorIdArr[i]);
                //明细连番
                productDetail.put("DetailNo", i+1);
                //数量
                productDetail.put("Quantity", quantityArr[i]);
                //申请数量
                productDetail.put("PreQuantity", quantityArr[i]);
                //价格
                productDetail.put("Price", priceArr[i]);
                //包装类型ID
//                productDetail.put("BIN_ProductVendorPackageID", "0");
                //实体仓库ID
                productDetail.put("BIN_InventoryInfoID", map.get("depotInfoId"));
                //逻辑仓库ID
                productDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(map.get("logicInventoryInfoId")));
                //仓库库位ID
                productDetail.put("BIN_StorageLocationInfoID", "0");
                //备注
                productDetail.put("Comments", reasonArr[i]);
                //入出库区分
                productDetail.put("StockType", CherryConstants.STOCK_TYPE_IN);

                detailList.add(productDetail);
            }
            
            int prmInDepotId = 0;
            prmInDepotId = binOLSSCM09_BL.insertPrmInDepotAll(mainData, detailList);
            if(prmInDepotId == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }
            
            return prmInDepotId;
        }catch(Exception e){
            if(e instanceof CherryException){
                throw (CherryException)e;
            }else{
                throw new CherryException(e.getMessage());
            }
        }
    }
    
    @Override
    public int tran_submit(Map<String, Object> map, List<String[]> list,UserInfo userinfo) throws Exception {
        int prmInDepotId = tran_save(map, list, userinfo);
        if(prmInDepotId == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
            
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, prmInDepotId);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
        pramMap.put("CurrentUnit", "BINOLSSPRM63");
        pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo",userinfo);
        binOLSSCM00_BL.StartOSWorkFlow(pramMap); 
        
        return prmInDepotId;
    }
    
    public String getDepart(Map<String, Object> map){
        return binOLSSPRM63_Service.getDepart(map);
    }
}