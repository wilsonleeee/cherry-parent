/*
 * @(#)BINOLSTIOS10_BL.java     1.0 2013/08/16
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
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.ios.form.BINOLSTIOS10_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS10_IF;
import com.cherry.st.ios.service.BINOLSTIOS10_Service;

/**
 * 
 * 退库申请BL
 * 
 * @author niushunjie
 * @version 1.0 2013.08.16
 */
public class BINOLSTIOS10_BL  extends SsBaseBussinessLogic implements BINOLSTIOS10_IF{
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTIOS10_Service")
    private BINOLSTIOS10_Service binOLSTIOS10_Service;
    
    @Override
    public int tran_submit(BINOLSTIOS10_Form form,UserInfo userInfo) throws Exception {
        int billID = tran_save(form, userInfo);
        if(billID == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
        
        // 准备参数，开始工作流
        Map<String, Object> pramMap = new HashMap<String, Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
        pramMap.put("CurrentUnit", "BINOLSTIOS10");
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode", userInfo.getBrandCode());
        pramMap.put("BIN_OrganizationID", form.getOutOrganizationID());
        Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationID(), null);
        if(null != departmentInfo && !departmentInfo.isEmpty()){
            pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//退库部门编号
        }
        binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        return billID;
    }
    
    @Override
    public int tran_save(BINOLSTIOS10_Form form,UserInfo userInfo) throws Exception {
        //审核区分  未提交审核
        String verifiedFlag =CherryConstants.RAAUDIT_FLAG_UNSUBMIT;

        //退库部门ID
        String outOrganizationID =  form.getOutOrganizationID();
        //退库实体仓库
        int outInventoryInfoID = CherryUtil.obj2int(form.getOutInventoryInfoID());
        //退库逻辑仓库
        int outLogicInventoryInfoID = CherryUtil.obj2int(form.getOutLogicInventoryInfoID());
        //接收退库部门 
        String inOrganizationID = form.getInOrganizationID();
        //完善用户信息
        binOLCM01_BL.completeUserInfo(userInfo, outOrganizationID,"BINOLSTIOS10");
        //产品厂商编码
        String[] productVendorIDArr = form.getPrtVendorId();
        //产品单价
        String[] priceUnitArr = form.getPriceArr();
        //退库数量
        String[] quantityArr =form.getQuantityArr();
        //退库原因
        String[] reasonArr = form.getReasonArr();
        
        //接收退库方仓库
        //调用共通取得退库方的所有仓库，调用共通取得接收退库方仓库对应的接收退库方仓库，比较这两个List，取出在两边都存在的仓库ID作为接收退库方仓库
        int inventoryInfoIDAccept = 0;
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("DepotID", outInventoryInfoID);
        param.put("InOutFlag", "OUT");
        param.put("BusinessType", CherryConstants.OPERATE_RR);
        param.put("language", "");
        List<Map<String,Object>> inDepotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
        List<Map<String,Object>> outDepotByDepartList = binOLCM18_BL.getDepotsByDepartID(outOrganizationID,"");
        if(null != inDepotList && inDepotList.size()>0){
            inventoryInfoIDAccept = CherryUtil.obj2int(inDepotList.get(0).get("BIN_DepotInfoID"));
            if(null != outDepotByDepartList && outDepotByDepartList.size()>0){
                for(int i=0;i<inDepotList.size();i++){
                    int curDepotID = CherryUtil.obj2int(inDepotList.get(i).get("BIN_DepotInfoID"));
                    for(int j=0;j<outDepotByDepartList.size();j++){
                        int curDepotByDepartID = CherryUtil.obj2int(outDepotByDepartList.get(j).get("BIN_DepotInfoID"));
                        if(curDepotID == curDepotByDepartID){
                            inventoryInfoIDAccept = curDepotID;
                            break;
                        }
                    }
                }
            }
        }
        
        //接收退库方逻辑仓库
        int logicInventoryInfoIDAccept = 0;
        Map<String, Object> outLogicParam = new HashMap<String, Object>();
        outLogicParam.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        outLogicParam.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
        outLogicParam.put("Type", "0");
        outLogicParam.put("ProductType", "1");
        outLogicParam.put("language", "");
        List<Map<String,Object>> outLogicList = binOLCM18_BL.getLogicDepotByBusiness(outLogicParam);
        if(null != outLogicList && outLogicList.size()>0){
            logicInventoryInfoIDAccept = CherryUtil.obj2int(outLogicList.get(0).get("BIN_LogicInventoryInfoID"));
        }
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        int totalQuantity = 0;
        double totalAmount = 0;
        for(int i=0;i<productVendorIDArr.length;i++){
            int quantity = CherryUtil.string2int(quantityArr[i]);
            totalQuantity += quantity;
            totalAmount += CherryUtil.string2double(priceUnitArr[i])*quantity;
            
            HashMap<String,Object> tempMap = new HashMap<String,Object>();
            //明细       产品厂商编码
            tempMap.put("BIN_ProductVendorID", productVendorIDArr[i]);
            //明细    番号
            tempMap.put("DetailNo", i+1);
            //明细       退库数量
            tempMap.put("Quantity", quantity);
            //明细       价格
            tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
            //明细       包装类型ID
            tempMap.put("BIN_ProductVendorPackageID", 0);
            //明细      退库仓库ID
            tempMap.put("BIN_InventoryInfoID", outInventoryInfoID);
            //明细     退库逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoID", outLogicInventoryInfoID);
            //明细      接收退库仓库ID
            tempMap.put("BIN_InventoryInfoIDReceive", inventoryInfoIDAccept);
            //明细     接收退库逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDAccept);
            //明细    退库仓库库位ID
            tempMap.put("BIN_StorageLocationInfoID",0);
            //明细     有效区分
            tempMap.put("ValidFlag", "1");
            //明细     理由
            tempMap.put("Reason", reasonArr[i]);
            //明细     共通字段
            tempMap.put("CreatedBy", userInfo.getBIN_UserID());
            tempMap.put("CreatePGM", "BINOLSTIOS10");
            tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
            tempMap.put("UpdatePGM", "BINOLSTIOS10");
            
            detailList.add(tempMap);
        }

        //主表       组织ID
        mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        //主表       品牌ID
        mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        //主表      退库方部门
        mainData.put("BIN_OrganizationID", outOrganizationID);
        //主表    退库方仓库
        mainData.put("BIN_InventoryInfoID", outInventoryInfoID);
        //主表    退库方逻辑仓库
        mainData.put("BIN_LogicInventoryInfoID", outLogicInventoryInfoID);
        //主表     库存库位ID
        mainData.put("BIN_StorageLocationInfoID", 0);
        //主表    接受方部门
        mainData.put("BIN_OrganizationIDReceive", inOrganizationID);
        //主表     接受方仓库ID
        mainData.put("BIN_InventoryInfoIDReceive", inventoryInfoIDAccept);
        //主表     接受方逻辑仓库ID
        mainData.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDAccept);
        //主表     制单员工
        String tradeEmployeeID = ConvertUtil.getString(form.getTradeEmployeeID());
        if(tradeEmployeeID.equals("")){
            mainData.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        }else{
            mainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
        }
        //主表     下单部门
        mainData.put("BIN_OrganizationIDDX", outOrganizationID);
        //主表     下单员工
        mainData.put("BIN_EmployeeIDDX", userInfo.getBIN_EmployeeID());
        //主表    总数量
        mainData.put("TotalQuantity", totalQuantity);
        //主表    总金额
        mainData.put("TotalAmount", totalAmount);
        //主表    审核区分
        mainData.put("VerifiedFlag", verifiedFlag);
        //主表    业务类型
        mainData.put("TradeType", "RA");
        //主表   流程类型（短流程）
        mainData.put("Model", "M2");
        //主表   物流ID
        mainData.put("BIN_LogisticInfoID", 0);
        //主表      原因
        mainData.put("Reason", form.getReason());
        //主表    理由
        mainData.put("Comment", null);
        //主表     日期
        mainData.put("TradeDate", binOLSTIOS10_Service.getDateYMD());
        mainData.put("CreatedBy", userInfo.getBIN_UserID());
        mainData.put("CreatePGM", "BINOLSTIOS10");
        mainData.put("UpdatedBy", userInfo.getBIN_UserID());
        mainData.put("UpdatePGM", "BINOLSTIOS10");
        int billID = binOLSTCM13_BL.insertProReturnRequestAll(mainData, detailList);
        return billID;
    }
    
    @Override
    public String getDepart(Map<String, Object> map){
        return binOLSTIOS10_Service.getDepart(map);
    }

    @Override
    public List<Map<String, Object>> searchProductList(Map<String, Object> map) {
        // 取得产品List
        return binOLSTIOS10_Service.searchProductList(map);
    }
}