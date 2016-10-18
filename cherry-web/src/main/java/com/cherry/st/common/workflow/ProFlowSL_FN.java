/*  
 * @(#)ProFlowSL_FN.java    1.0 2014-04-14   
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM19_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 
 * 后台销售发货工作流处理类
 * @author niushunjie
 * @version 1.0 2014.04.14
 *
 */
public class ProFlowSL_FN implements FunctionProvider{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
    @Resource(name="binOLSTCM19_BL")
    private BINOLSTCM19_IF binOLSTCM19_BL;
    
    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        
        if("startFlow".equals(method)){
            startFlow(arg0, arg1, propertyset);
        }else if("deleteSL".equals(method)){
            deleteSL(arg0, arg1, propertyset);
        }else if("submitHandle".equals(method)){
            submitHandle(arg0, arg1, propertyset);
        }else if("changeStock".equals(method)){
            changeStock(arg0, arg1, propertyset);
        }else if("updateState".equals(method)){
            updateState(arg0, arg1, propertyset);
        }
    }

    /**
     * 启动工作流
     * @param mainData
     * @param detailList
     * @return
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){    

        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        int backstageSaleID = Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
        //将单据编辑者放入流程变量中
        propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SL_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));

        propertyset.setInt("BIN_BackstageSaleID", backstageSaleID);
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));

        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long entryID = entry.getId();
        //写入工作流ID
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //后台销售单主表ID      
        pramData.put("BIN_BackstageSaleID", backstageSaleID);
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，订发收单主表中是没有实例ID的，这一步写入；
        pramData.put("WorkFlowID", entryID);      
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM19_BL.updateBackstageSale(pramData);
        
        //把工作流ID写入履历主表
        pramData.put("BIN_BackstageSaleHistoryID", mainData.get("BIN_BackstageSaleHistoryID"));
        binOLSTCM19_BL.updateBackstageSaleHistory(pramData);
        
        Map<String,Object> orderMap = binOLSTCM19_BL.getBackstageSaleMainData(backstageSaleID, null);
        mainData.put("BillNo", orderMap.get("BillCode"));
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_NS);
        paramMap.put("OpCode", CherryConstants.OPERATE_SL);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowSL_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
        
        //客户类型写入工作流
        propertyset.setString("CustomerType", ConvertUtil.getString(orderMap.get("CustomerType")));
    }

    /**
     * 单据提交
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void submitHandle(Map arg0, Map arg1, PropertySet propertyset) throws InvalidInputException, WorkflowException{
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String mqFlag = ConvertUtil.getString(mainData.get("CurrentUnit"));
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        //取出当前有效的action
        int[] actionArr = workflow.getAvailableActions(osid, null);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osid));
        //来自后台
        for (int j = 0; j < actionArr.length; j++) {
            ActionDescriptor ad = wd.getAction(actionArr[j]);
            //取得当前Action下的meta元素集合
            Map<String,Object> metaMap = ad.getMetaAttributes();
            //找到带有OS_DefaultAction元素的action
            if(metaMap.containsKey("OS_DefaultAction")){
                Map<String, Object> input = new HashMap<String, Object>();
                input.put("mainData", arg0.get("mainData"));
                workflow.doAction_single(osid, ad.getId(), input);
            }
        }
    }
	 
	/**
     * 废弃后台销售单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void deleteSL(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int billID = propertyset.getInt("BIN_BackstageSaleID");
        //主表ID      
        pramData.put("BIN_BackstageSaleID", billID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        if(!"".equals(verifiedFlag)){
            pramData.put("BillState", verifiedFlag);
        }else{
            pramData.put("BillState", CherryConstants.AUDIT_FLAG_DISAGREE);
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM19_BL.updateBackstageSale(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
	
	/**
	 * 更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void changeStock(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_BackstageSaleID", propertyset.getInt("BIN_BackstageSaleID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));

        String stockType = ConvertUtil.getString(arg1.get("StockType"));
        pramData.put("StockType", stockType);
        
        String employeeID = null;
        if(mainData.get("UserInfo") != null){
            UserInfo userInfo =  (UserInfo) mainData.get("UserInfo");
            employeeID = ConvertUtil.getString(userInfo.getBIN_EmployeeID());
        }
        pramData.put("BIN_EmployeeID", employeeID);
		//根据销售单ID写出入库表，并更改库存
		binOLSTCM19_BL.createProductInOutByBackstageSaleID(pramData);		
	}
	
	/**
     * 更新后台销售状态
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void updateState(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //销售单主表ID      
        pramData.put("BIN_BackstageSaleID", propertyset.getInt("BIN_BackstageSaleID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        String billState = ConvertUtil.getString(arg1.get("BillState"));
        pramData.put("BillState", billState);
        if(billState.equals("99")){
            pramData.put("ActualFinishDate", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
        }
        binOLSTCM19_BL.updateBackstageSale(pramData);
        
        String backstageSaleHistoryID = ConvertUtil.getString(mainData.get("BIN_BackstageSaleHistoryID"));
        if(!backstageSaleHistoryID.equals("")){
            pramData.put("BIN_BackstageSaleHistoryID", backstageSaleHistoryID);
            binOLSTCM19_BL.updateBackstageSaleHistory(pramData);
        }
    }
}