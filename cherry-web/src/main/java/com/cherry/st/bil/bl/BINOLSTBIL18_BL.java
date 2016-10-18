/*  
 * @(#)BINOLSTBIL18_BL.java     1.0 2012/11/28      
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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL18_IF;
import com.cherry.st.bil.service.BINOLSTBIL18_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

/**
 * 
 * 产品调拨申请单明细一览BL
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
public class BINOLSTBIL18_BL implements BINOLSTBIL18_IF{
    
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLSTCM00_BL")
    private BINOLSTCM00_IF binOLSTCM00_BL;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
    
    @Resource(name="binOLSTBIL18_Service")
	private BINOLSTBIL18_Service binOLSTBIL18_Service;
	
	@Override
	public void tran_doaction(BINOLSTBIL18_Form form, UserInfo userInfo) throws Exception {
		if (CherryConstants.OPERATE_AC_AUDIT.equals(form.getOperateType())) {
		    int ret = tran_save(form,userInfo);
		    if(ret == 0){
		        throw new CherryException("ECM00038");
		    }
			// 审核模式，推动工作流 【同意】【废弃】
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put("entryID", form.getEntryID());
			pramMap.put("actionID", form.getActionID());
			pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
    		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put("CurrentUnit", "BINOLSTBIL18");
			pramMap.put("ProductAllocationForm", form);
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("BrandCode", userInfo.getBrandCode());
			pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
			pramMap.put("OpComments", form.getOpComments());
			binOLSTCM00_BL.DoAction(pramMap);
		}else if(CherryConstants.OPERATE_LG.equals(form.getOperateType())){
		    // 调出确认模式，推动工作流 【调出】【拒绝】
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL18");
            pramMap.put("ProductAllocationForm", form);
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());
            pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
            pramMap.put("OpComments", form.getOpComments());
            binOLSTCM00_BL.DoAction(pramMap);
            
            //当状态改为调入
            long workFlowID = Long.parseLong(form.getEntryID());
            PropertySet ps = workflow.getPropertySet(workFlowID);
            String currentOperate = ps.getString("OS_Current_Operate");
            if(currentOperate.equals(CherryConstants.OPERATE_BG)){
                //取出调入步骤当前有效的action
                String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
                String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
                //配置项 云POS是否自动执行调入确认
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("organizationId", form.getOrganizationID());
                Map<String,Object> counterInfo = binOLCM19_BL.getCounterInfo(paramMap);
                if(null != counterInfo && ConvertUtil.getString(counterInfo.get("posFlag")).equals("0")){
                    String configValue = binOLCM14_BL.getConfigValue("1298", organizationInfoID, brandInfoId);
                    if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
                        int[] actionArr = workflow.getAvailableActions(workFlowID, null);
                        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(workFlowID));
                        if (actionArr != null && actionArr.length > 0) {
                            Map<String,Object> metaMapTMp = null;
                            int actionID = 0;
                            for (int j = 0; j < actionArr.length; j++) {
                                ActionDescriptor ad = wd.getAction(actionArr[j]);
                                metaMapTMp = ad.getMetaAttributes();
                                //找到带有调入标志的action
                                if(ConvertUtil.getString(metaMapTMp.get("OS_ButtonNameCode")).equals("os.allocationin")){
                                    actionID = ad.getId();
                                    break;
                                }
                            }
                            if(actionID != 0){
                                pramMap.put("actionID", actionID);
                                binOLSTCM00_BL.DoAction(pramMap);
                            }
                        }
                    }
                }
            }
		}else if(CherryConstants.OPERATE_BG.equals(form.getOperateType())){
	        // 调入确认模式，推动工作流 【调入】
            Map<String, Object> pramMap = new HashMap<String, Object>();
            pramMap.put("entryID", form.getEntryID());
            pramMap.put("actionID", form.getActionID());
            pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
            pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
            pramMap.put("CurrentUnit", "BINOLSTBIL18");
            pramMap.put("ProductAllocationForm", form);
            pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
            pramMap.put("BrandCode", userInfo.getBrandCode());
            pramMap.put("OrganizationInfoCode", userInfo.getOrganizationInfoCode());
            pramMap.put("OpComments", form.getOpComments());
            binOLSTCM00_BL.DoAction(pramMap);
		}
	}
	
	@Override
    public int tran_save(BINOLSTBIL18_Form form, UserInfo userInfo) throws Exception {
	    boolean auditLGFlag = false;
	    if(ConvertUtil.getString(form.getAuditLGFlag()).equals("YES") && !ConvertUtil.getString(form.getProductAllocationOutID()).equals("")){
	        auditLGFlag = true;
	    }
	    int billID = CherryUtil.obj2int(form.getProductAllocationID());
	    if(!auditLGFlag){
	        Map<String,Object> param = new HashMap<String,Object>();
	        param.put("BIN_ProductAllocationID", billID);
	        binOLSTBIL18_Service.deleteProductAllocationDetail(param);
	    }else{
	        billID = CherryUtil.obj2int(form.getProductAllocationOutID());
	        Map<String,Object> param = new HashMap<String,Object>();
	        param.put("BIN_ProductAllocationOutID", billID);
	        binOLSTBIL18_Service.deleteProductAllocationOutDetail(param);
	    }

        //实体仓库（默认取调入方）
        int depotId = CherryUtil.obj2int(form.getInventoryInfoIDIn());
        //逻辑仓库（默认取调入方）
        int loginDepotId = CherryUtil.obj2int(form.getLogicInventoryInfoIDIn());
        if(auditLGFlag){
            depotId = CherryUtil.obj2int(form.getInventoryInfoIDOut());
            loginDepotId = CherryUtil.obj2int(form.getLogicInventoryInfoIDOut());
        }
        
        //产品厂商编码
        String[] productVendorIDArr = form.getPrtVendorId();
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
            //明细      调入/调出仓库ID
            tempMap.put("BIN_InventoryInfoID", depotId);
            //明细     调入/调出逻辑仓库ID
            tempMap.put("BIN_LogicInventoryInfoID", loginDepotId);
            //明细      调入/调出仓库库位ID
            tempMap.put("BIN_StorageLocationInfoID",0);
            //明细     有效区分
            tempMap.put("ValidFlag", "1");
            //明细     理由
            tempMap.put("Comments", reasonArr[i]);
            //明细     共通字段
            tempMap.put("CreatedBy", userInfo.getBIN_UserID());
            tempMap.put("CreatePGM", "BINOLSTBIL18");
            tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
            tempMap.put("UpdatePGM", "BINOLSTBIL18");
            
            detailList.add(tempMap);
        }
        if(auditLGFlag){
            binOLSTCM16_BL.insertProductAllocationOutDetail(billID, detailList);
        }else{
            binOLSTCM16_BL.insertProductAllocationDetail(billID, detailList);
        }
        
        mainData.put("BIN_ProductAllocationID", billID);
        //主表    总数量
        mainData.put("TotalQuantity", totalQuantity);
        //主表    总金额
        mainData.put("TotalAmount", totalAmount);
        //主表     日期
        mainData.put("UpdatedBy", userInfo.getBIN_UserID());
        mainData.put("UpdatePGM", "BINOLSTBIL18");
        mainData.put("OldUpdateTime", form.getUpdateTime());
        mainData.put("OldModifyCount", form.getModifyCount());
        int ret = 0;
        if(auditLGFlag){
            mainData.put("BIN_ProductAllocationOutID", billID);
            ret = binOLSTCM16_BL.updateProductAllocationOut(mainData);
        }else{
            ret = binOLSTCM16_BL.updateProductAllocation(mainData);
        }
        return ret;
    }
	
    @Override
    public void tran_submit(BINOLSTBIL18_Form form, UserInfo userInfo) throws Exception {
        int ret = tran_save(form,userInfo);
        if(ret == 0){
            throw new CherryException("ECM00038");
        }
        int productAllocationID = CherryUtil.obj2int(form.getProductAllocationID());
        Map<String,Object> productAllocationMainData = binOLSTCM16_BL.getProductAllocationMainData(productAllocationID, null);
        
        //准备参数，开始工作流
        Map<String,Object> pramMap = new HashMap<String,Object>();
        pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_BG);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLID,productAllocationID);
        pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
        pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
        pramMap.put("CurrentUnit", "BINOLSTBIL18");
        pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
        pramMap.put("UserInfo", userInfo);
        pramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
        pramMap.put("BrandCode",userInfo.getBrandCode());
        pramMap.put("BIN_OrganizationIDIn", productAllocationMainData.get("BIN_OrganizationIDIn"));
        pramMap.put("BIN_OrganizationIDOut", productAllocationMainData.get("BIN_OrganizationIDOut"));
        binOLSTCM00_BL.StartOSWorkFlow(pramMap);
    }
}