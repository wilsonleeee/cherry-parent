/*	
 * @(#)BINOLSSPRM62_BL.java     1.0 2012/09/27		
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
import com.cherry.ss.common.service.BINOLSSCM08_Service;
import com.cherry.ss.prm.form.BINOLSSPRM62_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM62_IF;
import com.cherry.ss.prm.service.BINOLSSPRM62_Service;

/**
 * 
 * 促销品移库详细BL
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public class BINOLSSPRM62_BL implements BINOLSSPRM62_IF{

    @Resource(name="binOLSSCM00_BL")
    private BINOLSSCM00_BL binOLSSCM00_BL;
    
    @Resource(name="binOLSSCM08_BL")
    private BINOLSSCM08_IF binOLSSCM08_BL;
    
    @Resource(name="binOLSSCM08_Service")
    private BINOLSSCM08_Service binOLSSCM08_Service;
    
    @Resource(name="binOLSSPRM62_Service")
    private BINOLSSPRM62_Service binOLSSPRM62_Service;
    
    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveOrder(BINOLSSPRM62_Form form, UserInfo userInfo){
        String currentUnit = "BINOLSSPRM62";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_PromotionShiftID", form.getPromotionShiftID());
        //删除该ID下移库单从表数据
        binOLSSPRM62_Service.deletePromotionShiftDetail(param);
        //一次操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //插入移库单明细数据
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String[] prmVendorIDArr = form.getPrmVendorIDArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] fromDepotInfoIDArr = form.getFromDepotInfoIDArr();
        String[] fromLogicInventoryInfoIDArr = form.getFromLogicInventoryInfoIDArr();
        String[] fromStorageLocationInfoIDArr = form.getFromStorageLocationInfoIDArr();
        String[] toDepotInfoIDArr = form.getToDepotInfoIDArr();
        String[] toLogicInventoryInfoIDArr = form.getToLogicInventoryInfoIDArr();
        String[] toStorageLocationInfoIDArr = form.getToStorageLocationInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        for(int i=0;i<prmVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> shiftDetail = new HashMap<String,Object>();
            shiftDetail.put("BIN_PromotionShiftID", form.getPromotionShiftID());
            shiftDetail.put("BIN_PromotionProductVendorID", prmVendorIDArr[i]);
            shiftDetail.put("DetailNo", i+1);
            shiftDetail.put("Quantity", quantityArr[i]);
            shiftDetail.put("Price", priceUnitArr[i]);
            shiftDetail.put("BIN_ProductVendorPackageID", productVendorPackageIDArr[i]);
            shiftDetail.put("FromDepotInfoID", fromDepotInfoIDArr[i]);
            shiftDetail.put("FromLogicInventoryInfoID", fromLogicInventoryInfoIDArr[i]);
            shiftDetail.put("FromStorageLocationInfoID", fromStorageLocationInfoIDArr[i]);
            shiftDetail.put("ToDepotInfoID", toDepotInfoIDArr[i]);
            shiftDetail.put("ToLogicInventoryInfoID", toLogicInventoryInfoIDArr[i]);
            shiftDetail.put("ToStorageLocationInfoID", toStorageLocationInfoIDArr[i]);
            shiftDetail.put("Comments", commentsArr[i]);
            shiftDetail.put("CreatedBy", userInfo.getBIN_UserID());
            shiftDetail.put("CreatePGM", currentUnit);
            shiftDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            shiftDetail.put("UpdatePGM", currentUnit);
            list.add(shiftDetail);
        }
        binOLSSCM08_Service.insertPromotionShiftDetail(list);
        //更新移库单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", currentUnit);
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        return binOLSSCM08_BL.updatePrmShiftMain(param);
    }
    
    @Override
    public void tran_doaction(BINOLSSPRM62_Form form, UserInfo userInfo)
            throws Exception {
        if (CherryConstants.OPERATE_MV_AUDIT.equals(form.getOperateType())) {
            // 先保存单据，再推动工作流
            int count = saveOrder(form, userInfo);
            if (count == 0) {
                throw new CherryException("ECM00038");
            }
            // 审核模式，推动工作流
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSSPRM62");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("OpComments", form.getOpComments());
            binOLSSCM00_BL.DoAction(pramMap);
        }
    }
}