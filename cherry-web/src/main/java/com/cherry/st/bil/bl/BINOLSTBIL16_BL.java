/*  
 * @(#)BINOLSTBIL16_BL.java     1.0 2012/8/23      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.form.BINOLSTBIL16_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL16_IF;
import com.cherry.st.bil.service.BINOLSTBIL16_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.service.BINOLSTCM14_Service;

/**
 * 
 * 盘点申请单明细一览BL
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL16_BL extends SsBaseBussinessLogic implements BINOLSTBIL16_IF{
	
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM14_BL")
    private BINOLSTCM14_IF binOLSTCM14_BL;
    
    @Resource(name="binOLSTCM14_Service")
    private BINOLSTCM14_Service binOLSTCM14_Service;
    
    @Resource(name="binOLSTBIL16_Service")
	private BINOLSTBIL16_Service binOLSTBIL16_Service;
	
	@Override
	public void tran_doaction(BINOLSTBIL16_Form form, UserInfo userInfo) throws Exception {
		if (CherryConstants.OPERATE_CR_AUDIT.equals(form.getOperateType()) || CherryConstants.OPERATE_CR_AUDIT2.equals(form.getOperateType())) {
			// 审核模式，推动工作流 【同意】【废弃】
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL16");
			pramMap.put("ProStocktakeRequestForm", form);
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("BrandCode", userInfo.getBrandCode());
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//盘点部门编号
            }
			pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
			pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}

    @Override
    public int tran_save(BINOLSTBIL16_Form form, UserInfo userInfo) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProStocktakeRequestID", form.getProStocktakeRequestID());
        //删除该ID盘点申请单从表数据
        binOLSTBIL16_Service.deleteProStocktakeRequestDetail(param);
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        
        int inventoryInfoID = CherryUtil.obj2int(form.getInventoryInfoID());
        int logicInventoryInfoID = CherryUtil.obj2int(form.getLogicInventoryInfoID());
        
        //插入盘点申请单明细数据
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] bookQuantityArr = form.getBookQuantityArr();
        String[] checkQuantityArr = form.getCheckQuantityArr();
        String[] gainQuantityArr = (null == form.getGainQuantityArr() ? form.getQuantityArr() : form.getGainQuantityArr());
        String[] priceUnitArr = form.getPriceUnitArr();
//        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
//        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
//        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
//        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(gainQuantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> productOrderDetail = new HashMap<String,Object>();
            productOrderDetail.put("BIN_ProStocktakeRequestID", form.getProStocktakeRequestID());
            productOrderDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productOrderDetail.put("BookQuantity", bookQuantityArr[i]);
            productOrderDetail.put("CheckQuantity", checkQuantityArr[i]);
            productOrderDetail.put("GainQuantity", gainQuantityArr[i]);
            productOrderDetail.put("BIN_InventoryInfoID", inventoryInfoID);
            productOrderDetail.put("Price", priceUnitArr[i]);
            productOrderDetail.put("BIN_ProductVendorPackageID", 0);
            productOrderDetail.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            productOrderDetail.put("BIN_StorageLocationInfoID", 0);
            productOrderDetail.put("Comments", commentsArr[i]);
            productOrderDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("CreatePGM", CherryChecker.isNullOrEmpty(form.getActionID()) ? "BINOLSTBIL16":form.getActionID());
            productOrderDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("UpdatePGM", CherryChecker.isNullOrEmpty(form.getActionID()) ? "BINOLSTBIL16":form.getActionID());
            list.add(productOrderDetail);
        }
        binOLSTCM14_Service.insertProStocktakeRequestDetail(list);
        
        //更新盘点申请单主表
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", CherryChecker.isNullOrEmpty(form.getActionID()) ? "BINOLSTSFH03":form.getActionID());
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        int ret = binOLSTCM14_BL.updateProStocktakeRequest(param);
        return ret;
    }

    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        return binOLSTBIL16_Service.getSumInfo(map);
    }

    @Override
    public int tran_submit(BINOLSTBIL16_Form form, UserInfo userInfo) throws Exception {
        int ret =0;
        if("2".equals(form.getOperateType())){
            //非工作流编辑模式下提交，则先保存单据，再开始工作流
            ret = tran_save(form,userInfo);
            if(ret==0){
                throw new CherryException("ECM00038");
            }
            //准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CA);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProStocktakeRequestID());
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL16");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userInfo);
            binOLSTCM00_BL.StartOSWorkFlow(pramMap);    
        }
        return ret;
    }
}
