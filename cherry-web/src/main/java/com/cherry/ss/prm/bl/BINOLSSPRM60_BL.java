/*	
 * @(#)BINOLSSPRM60_BL.java     1.0 2012/09/27		
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
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM08_IF;
import com.cherry.ss.prm.interfaces.BINOLSSPRM60_IF;

/**
 * 
 * 促销品移库BL
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM60_BL implements BINOLSSPRM60_IF{
    
    @Resource(name="binOLSSCM00_BL")
    private BINOLSSCM00_BL binOLSSCM00_BL;
    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    @Override
    public int tran_saveShiftRecord(Map<String, Object> map,
            List<String[]> list, UserInfo userinfo) throws Exception {
        //从list中获取各种参数数组
        String[] prmVendorIdArr = list.get(0);
        String[] quantityArr = list.get(1);
        String[] commentsArr = list.get(2);
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
        double totalAmount = 0.00;
        for(int i = 0 ; i < prmVendorIdArr.length ; i++){
            int quantity = Integer.parseInt(quantityArr[i]);
            double price = Double.parseDouble(priceArr[i]);
            totalQuantity += quantity;
            totalAmount += quantity*price;
        }
        try{
            //申明map存放移库主表数据
            Map<String,Object> shiftMain = new HashMap<String,Object>();
            //sessionMap
            shiftMain.putAll(sessionMap);
            //总数量
            shiftMain.put("TotalQuantity", totalQuantity);
            //总金额
            shiftMain.put("TotalAmount", totalAmount);
            //操作员
            shiftMain.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
            //审核区分
            shiftMain.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
            //备注
            shiftMain.put("Comments", map.get("comments"));
            //操作日期
            shiftMain.put("OperateDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));
            //移库部门
            shiftMain.put("BIN_OrganizationID", map.get("organizationId"));
            //业务类型
            shiftMain.put("BusinessType", CherryConstants.BUSINESS_TYPE_MV);
            
            //申明list，存放移库从表数据
            List<Map<String,Object>> shiftList = new ArrayList<Map<String,Object>>();
            
            for(int i = 0 ; i < prmVendorIdArr.length ; i++)
            {
                Map<String,Object> tempMap = new HashMap<String,Object>();
                //
                tempMap.putAll(sessionMap);
                //明细连番
                tempMap.put("DetailNo", i+1);
                //出库实体仓库
                tempMap.put("FromDepotInfoID", map.get("depotInfoId"));
                //出库逻辑仓库
                tempMap.put("FromLogicInventoryInfoID", map.get("fromLogicInventoryInfoId"));
                //入库实体仓库
                tempMap.put("ToDepotInfoID", map.get("depotInfoId"));
                //入库逻辑仓库
                tempMap.put("ToLogicInventoryInfoID", map.get("toLogicInventoryInfoId"));
                //促销品厂商ID
                tempMap.put("BIN_PromotionProductVendorID", prmVendorIdArr[i]);
                //数量
                tempMap.put("Quantity", quantityArr[i]);
                //备注
                tempMap.put("Comments", commentsArr[i]);
                //价格
                tempMap.put("Price", priceArr[i]);
                //
                shiftList.add(tempMap);
            }
            //将移库单据插入数据库中
            int shiftID = 0;
            shiftID = binOLSSCM08_BL.insertPrmShiftAll(shiftMain, shiftList);
            
            if(shiftID == 0){
                //抛出自定义异常：操作失败！
                throw new CherryException("ISS00005");
            }
            
            //准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_MV);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, shiftID);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userinfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userinfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userinfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSSPRM60");
            pramMap.put("BIN_BrandInfoID", userinfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userinfo);
            binOLSSCM00_BL.StartOSWorkFlow(pramMap);
            
            return shiftID;
        }catch(Exception e){
            if(e instanceof CherryException){
                throw (CherryException)e;
            }else{
                throw new CherryException(e.getMessage());
            }
        }
    }
}