/*  
 * @(#)BINOLSTBIL14_BL.java     1.0 2012/7/24      
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
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL14_IF;
import com.cherry.st.bil.service.BINOLSTBIL14_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.service.BINOLSTCM13_Service;

/**
 * 
 * 退库申请单明细一览BL
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL14_BL extends SsBaseBussinessLogic implements BINOLSTBIL14_IF{
	
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM13_BL")
    private BINOLSTCM13_IF binOLSTCM13_BL;
    
    @Resource(name="binOLSTCM13_Service")
    private BINOLSTCM13_Service binOLSTCM13_Service;
    
    @Resource(name="binOLSTBIL14_Service")
	private BINOLSTBIL14_Service binOLSTBIL14_Service;
	
	@Override
	public void tran_doaction(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception {
        if (CherryConstants.OPERATE_RA_AUDIT.equals(form.getOperateType())
                || CherryConstants.OPERATE_RA_AUDIT2.equals(form.getOperateType())
                || CherryConstants.OPERATE_RA_AUDIT3.equals(form.getOperateType())
                || CherryConstants.OPERATE_RA_AUDIT4.equals(form.getOperateType())
                || CherryConstants.OPERATE_RA_LOGISTICSCONFIRM.equals(form.getOperateType())
                || CherryConstants.OPERATE_RA_CONFIRM.equals(form.getOperateType())
                || CherryConstants.OPERATE_RD.equals(form.getOperateType())) {
			// 审核模式，推动工作流 【同意】【废弃】
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL14");
			pramMap.put("ProReturnRequestForm", form);
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("BrandCode", userInfo.getBrandCode());
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//退库部门编号
            }
			pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
			pramMap.put("OpComments", form.getOpComments());
			pramMap.put("BIN_OrganizationID", form.getOrganizationID());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}

    @Override
    public int tran_save(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProReturnRequestID", form.getProReturnRequestID());
        //删除该ID退库申请单从表数据
        binOLSTBIL14_Service.deleteProReturnReqDetail(param);
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //插入退库申请单明细数据
        String[] productVendorIDArr = form.getPrtVendorId();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        //String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        //String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
        //String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        //String[] inventoryInfoIDReceiveArr = form.getInventoryInfoIDReceiveArr();
        //String[] logicInventoryInfoIDReceiveArr = form.getLogicInventoryInfoIDReceiveArr();
        //String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] reasonArr = form.getReasonArr();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> productOrderDetail = new HashMap<String,Object>();
            productOrderDetail.put("BIN_ProReturnRequestID", form.getProReturnRequestID());
            productOrderDetail.put("DetailNo", i+1);
            productOrderDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productOrderDetail.put("Quantity", quantityArr[i]);
            productOrderDetail.put("Price", priceUnitArr[i]);
            productOrderDetail.put("BIN_ProductVendorPackageID", 0);
            productOrderDetail.put("BIN_InventoryInfoID", CherryUtil.obj2int(form.getInventoryInfoID()));
            productOrderDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getLogicInventoryInfoID()));
            productOrderDetail.put("BIN_InventoryInfoIDReceive", CherryUtil.obj2int(form.getInventoryInfoIDReceive()));
            productOrderDetail.put("BIN_LogicInventoryInfoIDReceive", CherryUtil.obj2int(form.getLogicInventoryInfoIDReceive()));
            productOrderDetail.put("BIN_StorageLocationInfoID", 0);
            productOrderDetail.put("Reason", reasonArr[i]);
            productOrderDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("CreatePGM", "BINOLSTBIL14");
            productOrderDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productOrderDetail.put("UpdatePGM", "BINOLSTBIL14");
            list.add(productOrderDetail);
        }
        binOLSTCM13_Service.insertProReturnReqDetail(list);
        
        //更新退库申请单主表
        param.put("BIN_OrganizationIDReceive", form.getOrganizationIDReceive());
        param.put("BIN_InventoryInfoIDReceive", form.getInventoryInfoIDReceive());
        param.put("BIN_LogicInventoryInfoIDReceive", form.getLogicInventoryInfoIDReceive());
        param.put("Reason", form.getReason());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL14");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        int ret = binOLSTCM13_BL.updateProReturnRequest(param);
        
        //启动工作流后保存写操作日志
        String entryID = ConvertUtil.getString(form.getEntryID());
        if(!"".equals(entryID)){
            int proReturnRequestID = CherryUtil.obj2int(form.getProReturnRequestID());
            Map<String,Object> proReturnRequestMainData = binOLSTCM13_BL.getProReturnRequestMainData(proReturnRequestID, null);
            Map<String, Object> logMap = new HashMap<String, Object>();
            //  工作流实例ID
            logMap.put("WorkFlowID",entryID);
            //操作部门
            logMap.put("BIN_OrganizationID",userInfo.getBIN_OrganizationID());
            //操作员工
            logMap.put("BIN_EmployeeID",userInfo.getBIN_EmployeeID());
            //操作业务类型
            logMap.put("TradeType",CherryConstants.OS_BILLTYPE_RA);
            //表名
            logMap.put("TableName", "Inventory.BIN_ProReturnRequest");
            //单据ID
            logMap.put("BillID",proReturnRequestID);
            //单据编号
            logMap.put("BillNo", proReturnRequestMainData.get("BillNoIF"));
            //操作代码
            logMap.put("OpCode","32");
            //操作结果
            logMap.put("OpResult","106");
            //作成者   
            logMap.put("CreatedBy",userInfo.getBIN_UserID());
            //作成程序名
            logMap.put("CreatePGM","BINOLSTBIL14");
            //更新者
            logMap.put("UpdatedBy",userInfo.getBIN_UserID());
            //更新程序名
            logMap.put("UpdatePGM","BINOLSTBIL14");
            binOLCM22_BL.insertInventoryOpLog(logMap);
        }
        return ret;
    }
    
    @Override
    public int tran_submit(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception {
        int ret =0;
        if("2".equals(form.getOperateType())){
            //非工作流编辑模式下提交，则先保存单据，再开始工作流
            ret = tran_save(form,userInfo);
            if(ret==0){
                throw new CherryException("ECM00038");
            }
            //准备参数，开始工作流
            Map<String,Object> pramMap = new HashMap<String,Object>();
            pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);
            pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProReturnRequestID());
            pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL14");
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("UserInfo", userInfo);
            pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());
            pramMap.put("BIN_OrganizationID", form.getOrganizationID());
            Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(form.getOrganizationID(), null);
            if(null != departmentInfo && !departmentInfo.isEmpty()){
                pramMap.put("OrganizationCode", departmentInfo.get("DepartCode"));//退库部门编号
            }
            binOLSTCM00_BL.StartOSWorkFlow(pramMap);    
        }
        return ret;
    }
}