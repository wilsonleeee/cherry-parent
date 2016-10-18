/*  
 * @(#)BINOLSTSFH01_BL.java     1.0 2012/11/13      
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
package com.cherry.st.sfh.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.sfh.form.BINOLSTSFH01_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH01_IF;
import com.cherry.st.sfh.service.BINOLSTSFH01_Service;

/**
 * 
 * 产品订货BL
 * 
 * @author niushunjie
 * @version 1.0 2012.11.13
 */
public class BINOLSTSFH01_BL implements BINOLSTSFH01_IF {
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_IF binOLCM18_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTSFH01_Service")
    private BINOLSTSFH01_Service binOLSTSFH01_Service;
    
    /**
     * 进行订货处理
     * @param form
     * @throws Exception 
     * @throws Exception 
     */
    @Override
    public int tran_order(BINOLSTSFH01_Form form,UserInfo userInfo) throws Exception{
        int orderMainID = tran_saveOrder(form, userInfo);
        if(orderMainID == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
        
        // 准备参数，开始工作流
        Map<String, Object> pramMap = new HashMap<String, Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, orderMainID);
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
        pramMap.put("CurrentUnit", "BINOLSTSFH01");
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode", userInfo.getBrandCode());
        Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOutOrganizationId(), null);
        if(null != departmentInfo && !departmentInfo.isEmpty()){
            pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
        }
        pramMap.put("ODInOrganizationID", form.getInOrganizationId());
        binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        return orderMainID;
    }
    
    /**
     * 保存订货单
     * @param form
     * @throws Exception 
     */
    @Override
    public int tran_saveOrder(BINOLSTSFH01_Form form,UserInfo userInfo) throws Exception{
        //审核区分  未提交审核
        String verifiedFlag =CherryConstants.AUDIT_FLAG_UNSUBMIT;
        //处理状态 未处理
        String tradeStatus = CherryConstants.BILLTYPE_PRO_OD_UNDO;

        //订货部门ID
        String inOrganizationId =  form.getInOrganizationId();
        //订货实体仓库
        int inDepotId = CherryUtil.obj2int(form.getInDepotId());
        //订货逻辑仓库
        int inLoginDepotId = CherryUtil.obj2int(form.getInLogicDepotId());
        //发货部门 
        String outOrganizationID = form.getOutOrganizationId();
        //订货类型
        String orderType = form.getOrderType();
        //期望发货日期
        String expectDeliverDate = form.getExpectDeliverDate();
        //完善用户信息
        binOLCM01_BL.completeUserInfo(userInfo, inOrganizationId,"BINOLSTSFH01");
        //产品厂商编码
        String[] productVendorIDArr = form.getProductVendorIDArr();
        //产品单价
        String[] priceUnitArr = form.getPriceUnitArr();
        //订货数量
        String[] quantityArr =form.getQuantityArr();
        //订货原因
        String[] reasonArr = form.getReasonArr();
        
        //发货方仓库
        //调用共通取得发货方的所有仓库，调用共通取得订货方仓库对应的发货方仓库，比较这两个List，取出在两边都存在的仓库ID作为发货方仓库
        int inventoryInfoIDAccept = 0;
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        param.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        param.put("DepotID", inDepotId);
        param.put("InOutFlag", "IN");
        param.put("BusinessType", CherryConstants.OPERATE_OD);
        param.put("language", "");
        List<Map<String,Object>> outDepotList = binOLCM18_BL.getOppositeDepotsByBussinessType(param);
        List<Map<String,Object>> outDepotByDepartList = binOLCM18_BL.getDepotsByDepartID(outOrganizationID,"");
        if(null != outDepotList && outDepotList.size()>0){
            inventoryInfoIDAccept = CherryUtil.obj2int(outDepotList.get(0).get("BIN_DepotInfoID"));
            if(null != outDepotByDepartList && outDepotByDepartList.size()>0){
                for(int i=0;i<outDepotList.size();i++){
                    int curDepotID = CherryUtil.obj2int(outDepotList.get(i).get("BIN_DepotInfoID"));
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
        
        //发货方逻辑仓库
        int logicInventoryInfoIDAccept = 0;
        Map<String, Object> outLogicParam = new HashMap<String, Object>();
        outLogicParam.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        outLogicParam.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_OD);
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
            //明细      建议数量
            tempMap.put("SuggestedQuantity", 0);
            //明细      申请数量
            tempMap.put("ApplyQuantity", quantity);
            //明细       核准数量
            tempMap.put("Quantity", quantity);
            //明细       价格
            tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
            //明细       包装类型ID
            tempMap.put("BIN_ProductVendorPackageID", 0);
            //明细      订货仓库ID
            tempMap.put("BIN_InventoryInfoID", inDepotId);
            //明细     订货逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoID", inLoginDepotId);
            //明细      订货仓库库位ID
            tempMap.put("BIN_StorageLocationInfoID",0);
            //明细     接受订货的仓库ID
            tempMap.put("BIN_InventoryInfoIDAccept",inventoryInfoIDAccept);
            //明细      接受订货的逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoIDAccept",logicInventoryInfoIDAccept);
            //明细     有效区分
            tempMap.put("ValidFlag", "1");
            //明细     理由
            tempMap.put("Comments", reasonArr[i]);
            //明细     共通字段
            tempMap.put("CreatedBy", userInfo.getBIN_UserID());
            tempMap.put("CreatePGM", "BINOLSTSFH01");
            tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
            tempMap.put("UpdatePGM", "BINOLSTSFH01");
            
            detailList.add(tempMap);
        }

        //主表       组织ID
        mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        //主表       品牌ID
        mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        //主表       订货类型
        mainData.put("OrderType", orderType);
        //主表      订货部门
        mainData.put("BIN_OrganizationID", inOrganizationId);
        //主表    订货仓库
        mainData.put("BIN_InventoryInfoID", inDepotId);
        //主表    订货逻辑仓库
        mainData.put("BIN_LogicInventoryInfoID", inLoginDepotId);
        //主表     库存库位ID
        mainData.put("BIN_StorageLocationInfoID", 0);
        //主表     接受订货部门
        mainData.put("BIN_OrganizationIDAccept", outOrganizationID);
        //主表     接受订货的仓库ID
        mainData.put("BIN_InventoryInfoIDAccept", inventoryInfoIDAccept);
        //主表     接受订货的逻辑仓库ID
        mainData.put("BIN_LogicInventoryInfoIDAccept", logicInventoryInfoIDAccept);
        //主表     制单员工
        String tradeEmployeeID = ConvertUtil.getString(form.getTradeEmployeeID());
        if(tradeEmployeeID.equals("")){
            mainData.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        }else{
            mainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
        }
        //主表     下单部门
        mainData.put("BIN_OrganizationIDDX", inOrganizationId);
        //主表     下单员工
        mainData.put("BIN_EmployeeIDDX", userInfo.getBIN_EmployeeID());
        //主表    建议总数量
        mainData.put("SuggestedQuantity", 0);
        //主表    申请总数量
        mainData.put("ApplyQuantity", totalQuantity);
        //主表    总数量
        mainData.put("TotalQuantity", totalQuantity);
        //主表    总金额
        mainData.put("TotalAmount", totalAmount);
        //主表    审核区分
        mainData.put("VerifiedFlag", verifiedFlag);
        //主表    处理状态
        mainData.put("TradeStatus", tradeStatus);
        //主表   物流ID
        mainData.put("BIN_LogisticInfoID", 0);
        //主表      原因
        mainData.put("Comments", form.getReasonAll());
        //主表     日期
        mainData.put("Date", form.getDate());
        //主表   订货时间
        mainData.put("OrderTime", CherryUtil.getSysDateTime("HH:mm:ss"));
        //主表 期望发货日期
        mainData.put("ExpectDeliverDate", expectDeliverDate);
        mainData.put("CreatedBy", userInfo.getBIN_UserID());
        mainData.put("CreatePGM", "BINOLSTSFH01");
        mainData.put("UpdatedBy", userInfo.getBIN_UserID());
        mainData.put("UpdatePGM", "BINOLSTSFH01");
        int orderMainID = binOLSTCM02_BL.insertProductOrderAll(mainData, detailList);
        return orderMainID;
    }

    @Override
    public String getBusinessDate(Map<String, Object> param) {
        String businessDate = binOLSTSFH01_Service.getBussinessDate(param);
        return businessDate;
    }
}