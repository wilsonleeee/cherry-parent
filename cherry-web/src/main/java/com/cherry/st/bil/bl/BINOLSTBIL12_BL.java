/*  
 * @(#)BINOLSTBIL12_BL.java     1.0 20111/1/2      
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.base.SsBaseBussinessLogic;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL12_IF;
import com.cherry.st.bil.service.BINOLSTBIL12_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.service.BINOLSTCM09_Service;

/**
 * 
 * 退库单明细一览
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL12_BL extends SsBaseBussinessLogic implements BINOLSTBIL12_IF{
	@Resource(name="binOLSTBIL12_Service")
	private BINOLSTBIL12_Service binOLSTBIL12_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
    
	@Resource(name="binOLSTCM09_BL")
	private BINOLSTCM09_IF binOLSTCM09_BL;
	
	@Resource(name="binOLSTCM09_Service")
	private BINOLSTCM09_Service binOLSTCM09_Service;
	
    @Override
    public int tran_delete(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductReturnID", form.getProductReturnID());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL12");
        param.put("UpdateTime", form.getUpdateTime());
        param.put("ModifyCount", form.getModifyCount());
        //伦理删除该ID退库单从表数据
        binOLSTBIL12_Service.deleteProductReturnDetailLogic(param);
        //伦理删除该ID退库单主表数据
        return binOLSTBIL12_Service.deleteProductReturnLogic(param);
    }
    
    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveOrder(BINOLSTBIL12_Form form, UserInfo userInfo){

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductReturnID", form.getProductReturnID());
        //删除该ID退库单从表数据
        binOLSTBIL12_Service.deleteProductReturnDetail(param);
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //插入退库单明细数据
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        String[] productVendorIDArr = form.getProductVendorIDArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] reasonArr = form.getReasonArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> productReturnDetail = new HashMap<String,Object>();
            productReturnDetail.put("BIN_ProductReturnID", form.getProductReturnID());
            productReturnDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            productReturnDetail.put("DetailNo", i+1);
            productReturnDetail.put("Quantity", quantityArr[i]);
            productReturnDetail.put("Price", priceUnitArr[i]);
            productReturnDetail.put("BIN_InventoryInfoID", CherryUtil.obj2int(inventoryInfoIDArr[i]));
            productReturnDetail.put("BIN_ProductVendorPackageID", CherryUtil.obj2int(productVendorPackageIDArr[i]));
            productReturnDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(logicInventoryInfoIDArr[i]));
            productReturnDetail.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(storageLocationInfoIDArr[i]));
            productReturnDetail.put("Reason", reasonArr[i]);
            productReturnDetail.put("CreatedBy", userInfo.getBIN_UserID());
            productReturnDetail.put("CreatePGM", "BINOLSTBIL12");
            productReturnDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            productReturnDetail.put("UpdatePGM", "BINOLSTBIL12");
            list.add(productReturnDetail);
        }
        binOLSTCM09_Service.insertProductReturnDetail(list);
        //更新退库单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL12");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
        return binOLSTCM09_BL.updateProductReturnMain(param);
    }   
    
    @Override
    public int tran_save(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception{
        return saveOrder(form,userInfo);
    }

    @Override
    public int tran_submit(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception {
    	int ret =0;
    	if("2".equals(form.getOperateType())){
    		//非工作流编辑模式下提交，则先保存单据，再开始工作流
    		ret = saveOrder(form,userInfo);
    		if(ret==0){
    			throw new CherryException("ECM00038");
    		}
    		//准备参数，开始工作流
    		Map<String,Object> pramMap = new HashMap<String,Object>();
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProductReturnID());
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
    		pramMap.put("CurrentUnit", "BINOLSTBIL12");
    		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
    		pramMap.put("UserInfo", userInfo);
    		binOLSTCM00_BL.StartOSWorkFlow(pramMap);	
    	}else if(CherryConstants.OPERATE_RR_EDIT.equals(form.getOperateType())){
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
	public void tran_doaction(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception {
		if (CherryConstants.OPERATE_RR_AUDIT.equals(form.getOperateType())) {
			// 审核模式，推动工作流
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL12");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("BINOLSTBIL12_Form", form);
			binOLSTCM00_BL.DoAction(pramMap);
		}else if (CherryConstants.OPERATE_RR_EDIT.equals(form.getOperateType())) {
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
			pramMap.put("CurrentUnit", "BINOLSTBIL12");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}

    @Override
    public Map<String, Object> getInventoryInfo(Map<String, Object> mainData,List<Map<String,Object>> detailList) throws Exception {
        Map<String,Object> inventoryInfo = new HashMap<String,Object>();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("ReturnNoIF", mainData.get("ReturnNoIF"));
        param.put("ReturnNo", mainData.get("ReturnNo"));
        if(CherryConstants.BUSINESS_TYPE_RR.equals(mainData.get("TradeType"))){
            if(null != detailList && detailList.size()>0){
                Map<String,Object> temp = detailList.get(0);
                inventoryInfo.put("BIN_DepotInfoID", temp.get("BIN_InventoryInfoID"));
                inventoryInfo.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
                inventoryInfo.put("DepotCodeName", temp.get("DepotCodeName"));
                inventoryInfo.put("LogicInventoryCodeName", temp.get("LogicInventoryCodeName"));
            }
            if(CherryConstants.AUDIT_FLAG_AGREE.equals(ConvertUtil.getString(mainData.get("VerifiedFlag")))){
                List<Map<String,Object>> list = binOLSTBIL12_Service.getARInventoryInfo(param);
                if(null != list && list.size()>0){
                    param.put("ReturnNoIF", list.get(0).get("ReturnNoIF"));
                    Map<String,Object> temp = list.get(0);
                    inventoryInfo.put("BIN_DepotInfoIDReceive", temp.get("BIN_InventoryInfoID"));
                    inventoryInfo.put("BIN_LogicInventoryInfoIDReceive", temp.get("BIN_LogicInventoryInfoID"));
                    inventoryInfo.put("DepotCodeNameReceive", temp.get("DepotCodeName"));
                    inventoryInfo.put("LogicInventoryCodeNameReceive", temp.get("LogicInventoryCodeName")); 
                }
                String depotInfoID = ConvertUtil.getString(inventoryInfo.get("BIN_DepotInfoID"));
                String depotInfoIDReceive = ConvertUtil.getString(inventoryInfo.get("BIN_DepotInfoIDReceive"));
                if(depotInfoID.equals(depotInfoIDReceive) || "".equals(depotInfoIDReceive)){
                    param.put("TradeType", "AR");
                    List<Map<String,Object>> inventoryInfoList = binOLSTBIL12_Service.getIOInventoryInfo(param);
                    if(null != inventoryInfoList && inventoryInfoList.size()>0){
                        Map<String,Object> temp = inventoryInfoList.get(0);
                        inventoryInfo.put("BIN_DepotInfoIDReceive", temp.get("BIN_InventoryInfoID"));
                        inventoryInfo.put("BIN_LogicInventoryInfoIDReceive", temp.get("BIN_LogicInventoryInfoID"));
                        inventoryInfo.put("DepotCodeNameReceive", temp.get("DepotCodeName"));
                        inventoryInfo.put("LogicInventoryCodeNameReceive", temp.get("LogicInventoryCodeName")); 
                    }  
                }
            }
        }
        else if(CherryConstants.BUSINESS_TYPE_AR.equals(mainData.get("TradeType"))){
            if(null != detailList && detailList.size()>0){
                Map<String,Object> temp = detailList.get(0);
                inventoryInfo.put("BIN_DepotInfoIDReceive", temp.get("BIN_InventoryInfoID"));
                inventoryInfo.put("BIN_LogicInventoryInfoIDReceive", temp.get("BIN_LogicInventoryInfoID"));
                inventoryInfo.put("DepotCodeNameReceive", temp.get("DepotCodeName"));
                inventoryInfo.put("LogicInventoryCodeNameReceive", temp.get("LogicInventoryCodeName")); 
            }
            param.put("ReturnNoIF", mainData.get("RelevanceNo"));
            List<Map<String,Object>> list = binOLSTBIL12_Service.getRRInventoryInfo(param);
            if(null != list && list.size()>0){
                Map<String,Object> temp = list.get(0);
                inventoryInfo.put("BIN_DepotInfoID", temp.get("BIN_InventoryInfoID"));
                inventoryInfo.put("BIN_LogicInventoryInfoID", temp.get("BIN_LogicInventoryInfoID"));
                inventoryInfo.put("DepotCodeName", temp.get("DepotCodeName"));
                inventoryInfo.put("LogicInventoryCodeName", temp.get("LogicInventoryCodeName"));
            }
            
            String depotInfoID = ConvertUtil.getString(inventoryInfo.get("BIN_DepotInfoID"));
            String depotInfoIDReceive = ConvertUtil.getString(inventoryInfo.get("BIN_DepotInfoIDReceive"));
            if(depotInfoID.equals(depotInfoIDReceive) || "".equals(depotInfoID)){
                param.put("TradeType", "AR");
                param.put("ReturnNoIF", mainData.get("ReturnNoIF"));
                List<Map<String,Object>> inventoryInfoList = binOLSTBIL12_Service.getIOInventoryInfo(param);
                if(null != inventoryInfoList && inventoryInfoList.size()>0){
                    Map<String,Object> temp = inventoryInfoList.get(0);
                    inventoryInfo.put("BIN_DepotInfoIDReceive", temp.get("BIN_InventoryInfoID"));
                    inventoryInfo.put("BIN_LogicInventoryInfoIDReceive", temp.get("BIN_LogicInventoryInfoID"));
                    inventoryInfo.put("DepotCodeNameReceive", temp.get("DepotCodeName"));
                    inventoryInfo.put("LogicInventoryCodeNameReceive", temp.get("LogicInventoryCodeName"));
                }
            }
        }
        return inventoryInfo;
    }
}
