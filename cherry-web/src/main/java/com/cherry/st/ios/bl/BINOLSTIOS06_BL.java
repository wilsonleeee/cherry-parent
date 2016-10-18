/*
 * @(#)BINOLSTIOS06_BL.java     1.0 2012/11/27
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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.ios.form.BINOLSTIOS06_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS06_IF;

/**
 * 
 * 产品调拨BL
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
public class BINOLSTIOS06_BL implements BINOLSTIOS06_IF{
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;

    @Override
    public int tran_save(BINOLSTIOS06_Form form, UserInfo userInfo) throws Exception {
        //审核区分  未提交审核
        String verifiedFlag =CherryConstants.AUDIT_FLAG_UNSUBMIT;
        //处理状态 未处理
        String tradeStatus = CherryConstants.BILLTYPE_PRO_BG_UNDO;

        //调入部门ID
        String inOrganizationID =  form.getInOrganizationID();
        //调入实体仓库
        int inDepotId = CherryUtil.obj2int(form.getInDepotID());
        //调入逻辑仓库
        int inLoginDepotId = CherryUtil.obj2int(form.getInLogicDepotID());
        //调出部门 
        String outOrganizationID = form.getOutOrganizationID();
        //完善用户信息
        binOLCM01_BL.completeUserInfo(userInfo, inOrganizationID,"BINOLSTIOS06");
        //产品厂商编码
        String[] productVendorIDArr = form.getProductVendorIDArr();
        //产品单价
        String[] priceUnitArr = form.getPriceUnitArr();
        //申请数量
        String[] quantityArr =form.getQuantityArr();
        //申请原因
        String[] reasonArr = form.getCommentsArr();
        
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
            //明细       发货数量
            tempMap.put("Quantity", quantity);
            //明细       价格
            tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
            //明细       包装类型ID
            tempMap.put("BIN_ProductVendorPackageID", 0);
            //明细      调入仓库ID
            tempMap.put("BIN_InventoryInfoID", inDepotId);
            //明细     调入逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoID", inLoginDepotId);
            //明细      调入仓库库位ID
            tempMap.put("BIN_StorageLocationInfoID",0);
            //明细     有效区分
            tempMap.put("ValidFlag", "1");
            //明细     理由
            tempMap.put("Comments", reasonArr[i]);
            //明细     共通字段
            tempMap.put("CreatedBy", userInfo.getBIN_UserID());
            tempMap.put("CreatePGM", "BINOLSTIOS06");
            tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
            tempMap.put("UpdatePGM", "BINOLSTIOS06");
            
            detailList.add(tempMap);
        }

        //主表      组织ID
        mainData.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        //主表       品牌ID
        mainData.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        //主表        调入部门
        mainData.put("BIN_OrganizationIDIn", inOrganizationID);
        //主表        调出部门
        mainData.put("BIN_OrganizationIDOut", outOrganizationID);
        //主表     制单员工
        String tradeEmployeeID = ConvertUtil.getString(form.getTradeEmployeeID());
        if(tradeEmployeeID.equals("")){
            mainData.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
        }else{
            mainData.put("BIN_EmployeeID", form.getTradeEmployeeID());
        }
        //主表    下单部门
        mainData.put("BIN_OrganizationIDDX", inOrganizationID);
        //主表    下单员工
        mainData.put("BIN_EmployeeIDDX", userInfo.getBIN_EmployeeID());
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
        mainData.put("Comments", form.getCommentsAll());
        //主表     日期
        mainData.put("Date", CherryUtil.getSysDateTime("yyyy-MM-dd"));
        mainData.put("CreatedBy", userInfo.getBIN_UserID());
        mainData.put("CreatePGM", "BINOLSTIOS06");
        mainData.put("UpdatedBy", userInfo.getBIN_UserID());
        mainData.put("UpdatePGM", "BINOLSTIOS06");
        int billID = binOLSTCM16_BL.insertProductAllocationAll(mainData, detailList);
        return billID;
    }

    @Override
    public int tran_submit(BINOLSTIOS06_Form form, UserInfo userInfo) throws Exception {
        int billID = tran_save(form, userInfo);
        if(billID == 0){
            //抛出自定义异常：操作失败！
            throw new CherryException("ISS00005");
        }
            
        // 准备参数，开始工作流
        Map<String, Object> pramMap = new HashMap<String, Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_BG);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID, billID);
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
        pramMap.put("CurrentUnit", "BINOLSTIOS06");
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode", userInfo.getBrandCode());
        pramMap.put("BIN_OrganizationIDIn", form.getInOrganizationID());
        pramMap.put("BIN_OrganizationIDOut", form.getOutOrganizationID());
        binOLSTCM00_BL.StartOSWorkFlow(pramMap);
        
        return billID;
    }
}
