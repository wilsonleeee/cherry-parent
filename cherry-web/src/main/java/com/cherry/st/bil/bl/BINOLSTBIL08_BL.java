/*  
 * @(#)BINOLSTBIL06_BL.java     1.0 2011/10/20      
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
package com.cherry.st.bil.bl;

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
import com.cherry.st.bil.form.BINOLSTBIL08_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL08_IF;
import com.cherry.st.bil.service.BINOLSTBIL08_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.common.service.BINOLSTCM04_Service;

/**
 * 
 * 产品移库单详细IF
 * @author zhanghuyi
 * @version 1.0 2011.10.20
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL08_BL extends SsBaseBussinessLogic implements BINOLSTBIL08_IF{
	@Resource(name="binOLSTBIL08_Service")
	private BINOLSTBIL08_Service binOLSTBIL08_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
    
	@Resource(name="binOLSTCM04_BL")
	private BINOLSTCM04_IF binOLSTCM04_BL;
	
	@Resource(name="binOLSTCM04_Service")
	private BINOLSTCM04_Service binOLSTCM04_Service;
	
    @Override
    public int tran_delete(BINOLSTBIL08_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductShiftID", form.getProductShiftID());
        //根据移库单ID删除从表数据
        binOLSTBIL08_Service.deleteProductShiftDetail(param);
        //根据移库单ID删除主表数据
         binOLSTBIL08_Service.deleteProductShift(param);
    	return 0;
    }
    
    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveOrder(BINOLSTBIL08_Form form, UserInfo userInfo){

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductShiftID", form.getProductShiftID());
        //删除该ID下移库单从表数据
        binOLSTBIL08_Service.deleteProductShiftDetail(param);
        //一次操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //插入移库单明细数据
        List list = new ArrayList();
        String[] productVendorIDArr = form.getProductVendorIDArr();
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
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> shiftDetail = new HashMap<String,Object>();
            shiftDetail.put("BIN_ProductShiftID", form.getProductShiftID());
            shiftDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
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
            shiftDetail.put("CreatePGM", "BINOLSTBIL08");
            shiftDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            shiftDetail.put("UpdatePGM", "BINOLSTBIL08");
            list.add(shiftDetail);
        }
        binOLSTCM04_Service.insertToProductShiftDetail(list);
        //更新移库单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL08");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        return binOLSTCM04_BL.updateProductShiftMain(param);
    }   
    
    @Override
    public int tran_save(BINOLSTBIL08_Form form, UserInfo userInfo) throws Exception{
        return saveOrder(form,userInfo);
    }

    @Override
    public int tran_submit(BINOLSTBIL08_Form form, UserInfo userInfo) throws Exception {
    	int ret =0;
    	if("2".equals(form.getOperateType())){
    		//非工作流编辑模式下提交，则先保存单据，再开始工作流
    		ret = saveOrder(form,userInfo);
    		if(ret==0){
    			throw new CherryException("ECM00038");
    		}
    		
    		//准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_MV);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProductShiftID());
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL08");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userInfo);
            binOLSTCM00_BL.StartOSWorkFlow(pramMap);	
    	}else if(CherryConstants.OPERATE_LS_EDIT.equals(form.getOperateType())){
//    		//工作流中的编辑模式，先保存单据，再推动工作流
//    	    Map<String,Object> pramMap = new HashMap<String,Object>();
//    		pramMap.put("entryID", entryID);
//    		pramMap.put("actionID",actionID);
//    		pramMap.put("BIN_EmployeeID", userinfo.getBIN_EmployeeID());
//    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userinfo.getBIN_UserID());
//    		pramMap.put("CurrentUnit", "BINOLSTSFH02");
//    		binOLSTCM00_BL.DoAction(pramMap);
    	}
        return ret;
    }

	@Override
	public void tran_doaction(BINOLSTBIL08_Form form, UserInfo userInfo) throws Exception {
		if (CherryConstants.OPERATE_MV_AUDIT.equals(form.getOperateType()) || CherryConstants.OPERATE_MV_AUDIT2.equals(form.getOperateType())) {
			// 审核/二审模式，推动工作流
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL08");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
	        pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
		}else if (CherryConstants.OPERATE_MV_EDIT.equals(form.getOperateType())) {
			// 编辑模式 【提交】【 删除】是读取工作流配置，【保存】按钮是固定有的，执行的是tran_save
			// 先保存单据，再推动工作流
			int count = saveOrder(form, userInfo);
			if (count == 0) {
				throw new CherryException("ECM00038");
			}
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL08");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}
	
}
