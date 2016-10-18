/*
 * @(#)PrmFlowGR_FN.java     1.0 2013/01/25
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
package com.cherry.ss.common.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.ss.common.interfaces.BINOLSSCM09_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;

/**
 * 
 * 促销品入库工作流
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public class PrmFlowGR_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
	
    @Resource(name="binOLSSCM05_BL")
    private BINOLSSCM05_BL binOLSSCM05_BL;
    
	@Resource(name="binOLSSCM09_BL")
	private BINOLSSCM09_IF binOLSSCM09_BL;
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		
		if("startFlow".equals(method)){
			startFlow(arg0,  arg1,  propertyset);			
		}else if("updateGR".equals(method)){
            updateGR(arg0,  arg1,  propertyset);
        }else if("stockInOut".equals(method)){
			stockInOut(arg0,  arg1,  propertyset);
		}else if("SD_sendMQ".equals(method)){
            SD_sendMQ(arg0, arg1, propertyset);
        }
	}
	/**
	 * 插入入出库单主表和明细表数据
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PROMOTION);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));	
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_GR_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
		propertyset.setInt("BIN_PrmInDepotID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
	
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
//        Map<String,Object> paramMap = new HashMap<String,Object>();
//        paramMap.put("workFlowId", entryID);
//        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_GR);
//        paramMap.put("OpCode", CherryConstants.OPERATE_GR);
//        paramMap.put("OpResult", "100");
//        paramMap.put("CurrentUnit", "PrmFlowGR_FN");
//        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);

        //入库单主表写入工作流ID
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_PrmInDepotID",mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        param.put("WorkFlowID", entryID);
        param.put("UpdatedBy", mainData.get("BIN_UserID"));
        param.put("UpdatePGM", mainData.get("CurrentUnit"));
        param.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        param.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSSCM09_BL.updatePrmInDepotMain(param);
	}
	
//	/**
//	 * 提交审核
//	 * @param arg0
//	 * @param arg1
//	 * @param propertyset
//	 */
//	private void submitAudit(Map arg0, Map arg1, PropertySet propertyset){	
//		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");	
//		
//		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
//		
//		Map<String, Object> pramData =  new HashMap<String, Object>();
//		//入出库单主表ID		
//		pramData.put("BIN_PrmInDepotID", propertyset.getInt("BIN_PrmInDepotID"));
//		//审核区分  已提交审核
//		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
//		//工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
//		//在刚开始工作流时，入出库单主表中是没有实例ID的，这一步写入；
//		pramData.put("WorkFlowID", entry.getId());		
//		pramData.put("UpdatedBy", mainData.get("BIN_UserID"));
//		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
//		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
//		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
//		binOLSSCM09_BL.updatePrmInDepotMain(pramData);
//		
//		propertyset.setInt("BIN_PrmInDepotID", CherryUtil.obj2int(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
//	}
	
    /**
     * 修改促销品入库单状态
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateGR(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_PrmInDepotID", propertyset.getInt("BIN_PrmInDepotID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag) || CherryConstants.AUDIT_FLAG_DISAGREE.equals(verifiedFlag)){
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSSCM09_BL.updatePrmInDepotMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
	
	/**
	 * 更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void stockInOut(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
	    int billID = propertyset.getInt("BIN_PrmInDepotID");
		pramData.put("BIN_PrmInDepotID", propertyset.getInt("BIN_PrmInDepotID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
	    pramData.put("StockInOutDate", mainData.get("tradeDateTime"));
	    pramData.put("StockInOutTime", mainData.get("tradeDateTime"));
		binOLSSCM09_BL.changeStock(pramData);
	    //已入库
		Map<String, Object> updPramData = new HashMap<String, Object>();
	    updPramData.put("BIN_PrmInDepotID", billID);
	    updPramData.put("TradeStatus", CherryConstants.BILLTYPE_GR_FINISH);
	    updPramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
	    updPramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		binOLSSCM09_BL.updatePrmInDepotMain(updPramData);
	}
	
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_sendMQ(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        //如果是后台对柜台进行入库，则发送MQ
        Map<String, Object> deliverInfo = binOLSSCM09_BL.getPrmInDepotMainData(propertyset.getInt("BIN_PrmInDepotID"),null);
        String receiveDepartID = String.valueOf(deliverInfo.get("BIN_OrganizationID"));
        if(binOLSSCM05_BL.checkOrganizationType(receiveDepartID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",ConvertUtil.getString(deliverInfo.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",ConvertUtil.getString(deliverInfo.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",ConvertUtil.getString(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",ConvertUtil.getString(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",ConvertUtil.getString(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",ConvertUtil.getString(mainData.get("OrganizationCode")));
            mqMap.put("DataFrom", "Inventory.BIN_PrmInDepot");//数据来源表，用于区分发货单还是入库单
            int userID = CherryUtil.obj2int(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            binOLSSCM05_BL.sendMQDeliverSend(new int[]{propertyset.getInt("BIN_PrmInDepotID")},userID, mqMap);
            propertyset.setString("SendToCounter", "YES");
        }else{
            propertyset.setString("SendToCounter", "NO");
        }
    }
}