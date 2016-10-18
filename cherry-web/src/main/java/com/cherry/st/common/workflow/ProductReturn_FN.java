/*  
 * @(#)ProductReturn_FN.java     1.0 2011/11/2      
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
package com.cherry.st.common.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.service.BINOLSTCM09_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 退库操作工作流处理类
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
public class ProductReturn_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
	@Resource(name="binOLSTCM09_BL")
	private BINOLSTCM09_IF binOLSTCM09_BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTCM09_Service")
	private BINOLSTCM09_Service binOLSTCM09_Service;
	
    private static final Logger logger = LoggerFactory.getLogger(ProductReturn_FN.class);
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("RR_submitAudit".equals(method)){
		    RR_submitAudit( arg0,  arg1,  propertyset);
		}else if("RR_auditHandle".equals(method)){
		    RR_auditHandle( arg0,  arg1,  propertyset);
		}else if("RR_auditAgree".equals(method)){
			
		}else if("RR_auditAgreeHand".equals(method)){
		    RR_auditAgreeHand( arg0,  arg1,  propertyset);
		}else if("RR_auditDisAgreeHand".equals(method)){
		    RR_auditDisAgreeHand( arg0,  arg1,  propertyset);
		}else if("RR_auditAgreeAuto".equals(method)){
		    RR_auditAgreeAuto(arg0,  arg1,  propertyset);
		}else if("stockInOut".equals(method)){
            try {
                stockInOut(arg0,  arg1,  propertyset);
            } catch (Exception e) {
                logger.error("",e);
                throw new WorkflowException(e);
            }
		}else if("stockInOutCounter".equals(method)){
		    stockInOutCounter(arg0,  arg1,  propertyset);
		}else if("updateRR".equals(method)){
		    updateRR(arg0,  arg1,  propertyset);
		}
	}
	/**
	 * 插入退库单主表和明细表数据
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RR_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
		propertyset.setInt("BIN_ProductReturnID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
	
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        
        //在刚开始工作流时，退库主表中是没有实例ID的，这一步写入；
        Map<String,Object> pramData = new HashMap<String,Object>();
        pramData.put("BIN_ProductReturnID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        pramData.put("WorkFlowID", entryID);      
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM09_BL.updateProductReturnMain(pramData);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_RR);
        paramMap.put("OpCode", CherryConstants.OPERATE_RR);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProductReturn_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
	}
	
	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void RR_submitAudit(Map arg0, Map arg1, PropertySet propertyset){
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //退库单主表ID
        pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
        //审核区分  已提交审核
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，退库单主表中是没有实例ID的，这一步写入；
        pramData.put("WorkFlowID", entry.getId());      
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM09_BL.updateProductReturnMain(pramData);
	}
	
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void RR_auditHandle(Map arg0, Map arg1, PropertySet propertyset){
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_RR_AUDIT);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));

		//查询退库单的审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RR_AUDIT, auditActors);
	}
	
	/**
	 * 人工审核(通过)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void RR_auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//退库单主表ID		
		pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
		//审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM09_BL.updateProductReturnMain(pramData);
	}
	/**
	 * 人工审核(退回)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void RR_auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//退库单主表ID		
		pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		binOLSTCM09_BL.updateProductReturnMain(pramData);
	}
	
	/**
	 * 自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	@Deprecated
	private void RR_auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
		pramData.put("VerifiedFlag",  CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		binOLSTCM09_BL.updateProductReturnMain(pramData);
	}
	
	/**
	 * 写入出库表，并更改库存（柜台）
	 */
	private void stockInOutCounter(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        binOLSTCM09_BL.changeStock(pramData);
	}
	
	/**
	 * 写入出库表，并更改库存（接收方）
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws Exception 
	 */
	private void stockInOut(Map arg0, Map arg1, PropertySet propertyset) throws Exception{
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		//生成接收退库单，写入出库表，并修改库存
        int productReturnID = propertyset.getInt("BIN_ProductReturnID");
        Map<String, Object> mainRRData = binOLSTCM09_BL.getProductReturnMainData(productReturnID, null);
        Map<String, Object> mainARData = new HashMap<String, Object>();
        mainARData.putAll(mainRRData);
        mainARData.put("ReturnNo", null);
        mainARData.put("ReturnNoIF", null);
        mainARData.put("BIN_EmployeeID", CherryUtil.obj2int(mainRRData.get("BIN_EmployeeID")));
        mainARData.put("BIN_OrganizationIDDX", mainARData.get("BIN_OrganizationID"));
        mainARData.put("BIN_EmployeeIDDX", CherryUtil.obj2int(mainRRData.get("BIN_EmployeeID")));
        mainARData.put("BIN_EmployeeIDAudit ",null);
        mainARData.put("TotalAmount", Math.abs(CherryUtil.string2double(ConvertUtil.getString(mainRRData.get("TotalAmount")))));
        mainARData.put("TotalQuantity", Math.abs(CherryUtil.obj2int(mainRRData.get("TotalQuantity"))));
        mainARData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainARData.put("TradeType", CherryConstants.BUSINESS_TYPE_AR);
        mainARData.put("RelevanceNo", ConvertUtil.getString(mainRRData.get("ReturnNoIF")));
        //接收退库单日期为业务日期
        Map<String,Object> bussinessDateParam = new HashMap<String,Object>();
        bussinessDateParam.put("organizationInfoId", mainARData.get("BIN_OrganizationInfoID"));
        bussinessDateParam.put("brandInfoId", mainARData.get("BIN_BrandInfoID"));
        String bussinessDate = binOLSTCM09_Service.getBussinessDate(bussinessDateParam);
        mainARData.put("ReturnDate", bussinessDate);
        mainARData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        mainARData.put("CreatePGM", mainData.get("CurrentUnit"));
        mainARData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        mainARData.put("UpdatePGM", mainData.get("CurrentUnit"));
        
        int inventoryInfoID = 0;
        int logicInventoryInfoID = 0;
        
        List<Map<String,Object>> detailList = binOLSTCM09_BL.getProductReturnDetailData(productReturnID, null);
        
        if(null == mainData.get("BINOLSTBIL12_Form")){
            //自动确认退库
            //取得接收退库的实体仓库ID
            Map<String,Object> depotMap = new HashMap<String,Object>();
            depotMap.put("BIN_OrganizationInfoID",mainData.get("BIN_OrganizationInfoID"));
            depotMap.put("BIN_BrandInfoID",mainData.get("BIN_BrandInfoID"));
            depotMap.put("DepotID", detailList.get(0).get("BIN_InventoryInfoID"));
            depotMap.put("InOutFlag", "OUT");
            depotMap.put("BusinessType", CherryConstants.OPERATE_RR);
            List<Map<String,Object>> depotList = binOLCM18_BL.getOppositeDepotsByBussinessType(depotMap);
            if(null != depotList && depotList.size()>0){
                inventoryInfoID = CherryUtil.obj2int(depotList.get(0).get("BIN_DepotInfoID"));
            }
              
            //取得接收退库的逻辑仓库ID
            Map<String,Object> logicMap = new HashMap<String,Object>();
            logicMap.put("BIN_BrandInfoID", CherryUtil.obj2int(mainData.get("BIN_BrandInfoID")));
//            logicMap.put("BusinessType", CherryConstants.OPERATE_RR);
            logicMap.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
            logicMap.put("ProductType", "1");
            logicMap.put("Type", "0");
//            List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusinessType(logicMap);
            List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(logicMap);
            if(null != logicList && logicList.size()>0){
                logicInventoryInfoID = CherryUtil.obj2int(logicList.get(0).get("BIN_LogicInventoryInfoID"));
            }
            
            for(int i=0;i<detailList.size();i++){
                detailList.get(i).put("BIN_InventoryInfoID", inventoryInfoID);
                detailList.get(i).put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
                detailList.get(i).put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
                detailList.get(i).put("CreatePGM", mainData.get("CurrentUnit"));
                detailList.get(i).put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
                detailList.get(i).put("UpdatePGM", mainData.get("CurrentUnit"));
            }
        }else{
            //人工确认退库
            BINOLSTBIL12_Form form = (BINOLSTBIL12_Form) mainData.get("BINOLSTBIL12_Form");
            inventoryInfoID = CherryUtil.obj2int(form.getInventoryInfoIDReceive());
            logicInventoryInfoID = CherryUtil.obj2int(form.getLogicInventoryInfoIDReceive());
            
            String[] productVendorIDArr = form.getProductVendorIDArr();
            String[] quantityArr = form.getQuantityArr();
            String[] priceArr = form.getPriceUnitArr();
            String[] reasonArr = form.getReasonArr();
            detailList = new ArrayList<Map<String,Object>>();
            for(int i=0;i<productVendorIDArr.length;i++){
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("BIN_ProductVendorID", productVendorIDArr[i]);
                temp.put("DetailNo", i+1);
                temp.put("Quantity", quantityArr[i]);
                temp.put("Price", priceArr[i]);
                temp.put("BIN_ProductVendorPackageID", 0);
                temp.put("BIN_InventoryInfoID", inventoryInfoID);
                temp.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
                temp.put("BIN_StorageLocationInfoID", 0);
                temp.put("Reason", reasonArr[i]);
                temp.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
                temp.put("CreatePGM", mainData.get("CurrentUnit"));
                temp.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
                temp.put("UpdatePGM", mainData.get("CurrentUnit"));
                detailList.add(temp);
            }
        }

        int arId = binOLSTCM09_BL.insertProductReturnAll(mainARData, detailList);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_ProductReturnID", arId);
        param.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        param.put("CreatePGM", mainData.get("CurrentUnit"));
        binOLSTCM09_BL.changeStock(param);
	}
	
    /**
     * 修改产品退库单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateRR(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductReturnID", propertyset.getInt("BIN_ProductReturnID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM09_BL.updateProductReturnMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
}
