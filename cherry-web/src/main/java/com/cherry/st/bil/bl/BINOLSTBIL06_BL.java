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
import com.cherry.st.bil.form.BINOLSTBIL06_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL06_IF;
import com.cherry.st.bil.service.BINOLSTBIL06_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM05_IF;
import com.cherry.st.common.service.BINOLSTCM05_Service;

/**
 * 
 * 报损单明细一览
 * @author niushunjie
 * @version 1.0 2011.10.20
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL06_BL extends SsBaseBussinessLogic implements BINOLSTBIL06_IF{
	@Resource(name="binOLSTBIL06_Service")
	private BINOLSTBIL06_Service binOLSTBIL06_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
    
	@Resource(name="binOLSTCM05_BL")
	private BINOLSTCM05_IF binOLSTCM05_BL;
	
	@Resource(name="binOLSTCM05_Service")
	private BINOLSTCM05_Service binOLSTCM05_Service;
	
    @Override
    public int tran_delete(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception{
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OutboundFreeID", form.getOutboundFreeID());
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL06");
        param.put("UpdateTime", form.getUpdateTime());
        param.put("ModifyCount", form.getModifyCount());
        //伦理删除该ID报损单从表数据
        binOLSTBIL06_Service.deleteOutboundFreeDetailLogic(param);
        //伦理删除该ID报损单主表数据
        return binOLSTBIL06_Service.deleteOutboundFreeLogic(param);
    }
    
    /**
     * 保存或提交
     * @param form
     * @param userInfo
     * @param flag
     * @return
     */
    private int saveOrder(BINOLSTBIL06_Form form, UserInfo userInfo){

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OutboundFreeID", form.getOutboundFreeID());
        //删除该ID报损单从表数据
        binOLSTBIL06_Service.deleteOutboundFreeDetail(param);
        //一次发货操作的总数量（始终为正）
        int totalQuantity =0;
        //总金额
        double totalAmount =0.0;
        //插入报损单明细数据
        List list = new ArrayList();
        String[] productVendorIDArr = form.getProductVendorIDArr();
        String[] quantityArr = form.getQuantityArr();
        String[] priceUnitArr = form.getPriceUnitArr();
        String[] productVendorPackageIDArr = form.getProductVendorPackageIDArr();
        String[] inventoryInfoIDArr = form.getInventoryInfoIDArr();
        String[] logicInventoryInfoIDArr = form.getLogicInventoryInfoIDArr();
        String[] storageLocationInfoIDArr = form.getStorageLocationInfoIDArr();
        String[] commentsArr = form.getCommentsArr();
        for(int i=0;i<productVendorIDArr.length;i++){
            int tempCount = CherryUtil.string2int(quantityArr[i]);
            double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
            totalAmount += money;
            totalQuantity += tempCount;
            
            Map<String,Object> outboundFreeDetail = new HashMap<String,Object>();
            outboundFreeDetail.put("BIN_OutboundFreeID", form.getOutboundFreeID());
            outboundFreeDetail.put("BIN_ProductVendorID", productVendorIDArr[i]);
            outboundFreeDetail.put("DetailNo", i+1);
            outboundFreeDetail.put("Quantity", quantityArr[i]);
            outboundFreeDetail.put("BIN_DepotInfoID", inventoryInfoIDArr[i]);
            outboundFreeDetail.put("Price", priceUnitArr[i]);
            outboundFreeDetail.put("BIN_ProductVendorPackageID", CherryUtil.obj2int(productVendorPackageIDArr[i]));
            outboundFreeDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(logicInventoryInfoIDArr[i]));
            outboundFreeDetail.put("BIN_StorageLocationInfoID", CherryUtil.obj2int(storageLocationInfoIDArr[i]));
            outboundFreeDetail.put("Comments", commentsArr[i]);
            outboundFreeDetail.put("CreatedBy", userInfo.getBIN_UserID());
            outboundFreeDetail.put("CreatePGM", "BINOLSTBIL06");
            outboundFreeDetail.put("UpdatedBy", userInfo.getBIN_UserID());
            outboundFreeDetail.put("UpdatePGM", "BINOLSTBIL06");
            list.add(outboundFreeDetail);
        }
        binOLSTCM05_Service.insertOutboundFreeDetail(list);
        //更新报损单主表
        param.put("Comments", form.getComments());
        param.put("TotalQuantity", totalQuantity);
        param.put("TotalAmount", totalAmount);
        param.put("UpdatedBy", userInfo.getBIN_UserID());
        param.put("UpdatePGM", "BINOLSTBIL06");
        param.put("OldUpdateTime", form.getUpdateTime());
        param.put("OldModifyCount", form.getModifyCount());
//        Map map = new HashMap();
//        map.put("depotInfoId", form.getDepotInfoID());
//        int organId = getOrganIdByDepotID(map);
//        param.put("BIN_OrganizationID", organId);
        return binOLSTCM05_BL.updateOutboundFreeMain(param);
    }   
    
    @Override
    public int tran_save(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception{
        return saveOrder(form,userInfo);
    }

    @Override
    public int tran_submit(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception {
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
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getOutboundFreeID());
    		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
    		pramMap.put("CurrentUnit", "BINOLSTBIL06");
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
	public void tran_doaction(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception {
		if (CherryConstants.OPERATE_LS_AUDIT.equals(form.getOperateType()) || CherryConstants.OPERATE_LS_AUDIT2.equals(form.getOperateType())) {
			// 审核/二审模式，推动工作流
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL06");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
	        pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
		}else if (CherryConstants.OPERATE_LS_EDIT.equals(form.getOperateType())) {
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
			pramMap.put("CurrentUnit", "BINOLSTBIL06");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			binOLSTCM00_BL.DoAction(pramMap);
		}
	}
	
    /**
     * 取得实体仓库的所属部门
     * @return
     */
    public int getOrganIdByDepotID(Map<String, Object> map) {
        int organId = 0;
        List<Map<String, Object>> list = binOLSTBIL06_Service.getOrganIdByDepotInfoID(map);
        if(list.size()>0){
            organId = CherryUtil.obj2int(list.get(0).get("BIN_OrganizationID"));
        }
        return organId;
    }
}
