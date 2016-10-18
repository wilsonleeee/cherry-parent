/*  
 * @(#)ProFlowOD_FN.java    1.0 2012-06-27   
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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * 
 * 产品订货工作流（订、发、收）处理类
 * @author niushunjie
 * @version 1.0 2012.06.27
 *
 */
public class ProFlowOD_FN implements FunctionProvider{

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;
    
    @Resource(name="binOLSTCM02_BL")
    private BINOLSTCM02_IF binOLSTCM02_BL;
    
    @Resource(name="binOLSTCM03_BL")
    private BINOLSTCM03_IF binOLSTCM03_BL;
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        
        if("startFlow".equals(method)){
            startFlow(arg0, arg1, propertyset);
        }else if("OD_CreateSD".equals(method)){
            OD_CreateSD(arg0, arg1, propertyset);
        }else if("OD_delete".equals(method)){
            OD_delete(arg0, arg1, propertyset);
        }else if("SD_delete".equals(method)){
            SD_delete(arg0, arg1, propertyset);
        }else if("submitHandle".equals(method)){
            submitHandle(arg0, arg1, propertyset);
        }else if("SD_sendMQ".equals(method)){
            SD_sendMQ(arg0, arg1, propertyset);
        }else if("SD_changeStock".equals(method)){
            SD_changeStock(arg0, arg1, propertyset);
        }else if("SD_update".equals(method)){
            SD_update(arg0, arg1, propertyset);
        }else if("IO_update".equals(method)){
            IO_update(arg0, arg1, propertyset);
        }else if("SD_create".equals(method)){
            SD_create(arg0, arg1, propertyset);
        }
    }

    /**
     * 启动工作流
     * @param mainData
     * @param detailList
     * @return
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){    
        //将订单编辑者放入流程变量中
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        int orderID = Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
        //后续的流程中，会根据这个属性判断柜台是否需要同步，来控制单据是否要导出给第三方
        propertyset.setString("BIN_OrganizationID", String.valueOf(mainData.get("ODInOrganizationID")));
        propertyset.setInt("BIN_ProductOrderID", orderID);
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));

        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long entryID = entry.getId();
        //订单写入工作流ID
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //订发收单主表ID      
        pramData.put("BIN_ProductOrderID", orderID);
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，订发收单主表中是没有实例ID的，这一步写入；
        pramData.put("WorkFlowID", entryID);      
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM02_BL.updateProductOrderMain(pramData);
        
        Map<String,Object> orderMap = binOLSTCM02_BL.getProductOrderMainData(orderID, null);
        mainData.put("BillNo", orderMap.get("OrderNoIF"));
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_OD);
        paramMap.put("OpCode", CherryConstants.OPERATE_OD_CREATE);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowOD_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
    }

    /**
     * 订单提交
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
        if("MQ".equals(mqFlag)){
            //来自MQ的订单自动提交
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
        }else{
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
    }
    
	/**
	 * 生成发货单（老方法，用于生成发货单后再审核，兼容老工作流的方法，以后新品牌生成发货单都用SD_create）
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
    @Deprecated
	private void OD_CreateSD(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		int orderID = propertyset.getInt("BIN_ProductOrderID");
		pramData.put("BIN_ProductOrderID", orderID);
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
		int deliverID = binOLSTCM03_BL.createProductDeliverByOrder(pramData);
            
		propertyset.setInt("BIN_ProductDeliverID", deliverID);
		//发货单生成后都是审核发货单
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_SD);
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(deliverID));
	}
	 
	/**
     * 废弃订单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void OD_delete(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int orderID = propertyset.getInt("BIN_ProductOrderID");
        //订发收单主表ID      
        pramData.put("BIN_ProductOrderID", orderID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM02_BL.updateProductOrderMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
	
    /**
     * 废弃发货单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_delete(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int deliverID = propertyset.getInt("BIN_ProductDeliverID");
        Map<String,Object> deliverMap = binOLSTCM03_BL.getProductDeliverMainData(deliverID, null);
        //发货单主表ID
        pramData.put("BIN_ProductDeliverID", deliverID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM03_BL.updateProductDeliverMain(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
        
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("WorkFlowID", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_SD);
        paramMap.put("OpCode", CherryConstants.OPERATE_SD_DELETE);
        paramMap.put("OpResult", "104");
        paramMap.put("OpComments", mainData.get("OpComments"));
        paramMap.put("CreatedBy", mainData.get("BIN_UserID"));
        paramMap.put("CreatePGM", "ProFlowOD_FN");
        paramMap.put("UpdatedBy", mainData.get("BIN_UserID"));
        paramMap.put("UpdatePGM", "ProFlowOD_FN");
        paramMap.put("TableName", "Inventory.BIN_ProductDeliver");
        paramMap.put("BillID", mainData.get(CherryConstants.OS_MAINKEY_BILLID));
        paramMap.put("BillNo", deliverMap.get("DeliverNoIF"));
        paramMap.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
        paramMap.put("BIN_OrganizationID", mainData.get("BIN_OrganizationID"));
        binOLCM22_BL.insertInventoryOpLog(paramMap);
    }
    
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_sendMQ(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        //如果是发货到柜台，则发送MQ
        Map<String, Object> deliverInfo = binOLSTCM03_BL.getProductDeliverMainData(propertyset.getInt("BIN_ProductDeliverID"),null);
        String receiveDepartID = String.valueOf(deliverInfo.get("BIN_OrganizationIDReceive"));
        if(binOLSTCM07_BL.checkOrganizationType(receiveDepartID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(deliverInfo.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(deliverInfo.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            binOLSTCM07_BL.sendMQDeliverSend(new int[]{propertyset.getInt("BIN_ProductDeliverID")}, mqMap);
        }
    }
	
	/**
	 * 发货方更改库存
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void SD_changeStock(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		//根据发货单ID写出入库表，并更改库存
		binOLSTCM03_BL.createProductInOutByDeliverID(pramData);		
	}
	
	/**
     * 更新发货单主单、明细单
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_update(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        //发货单主表ID      
        pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("BINOLSTSFH05_Form", mainData.get("BINOLSTSFH05_Form"));
        if(null != mainData.get("BINOLSTSFH05_Form")){
            binOLSTCM03_BL.updateProductDeliverByForm(pramData);
        }
    }
    
    /**
     * 更新入出库单（只限工作流中有发货单ID）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void IO_update(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
                
        Map<String, Object> pramData =  new HashMap<String, Object>();
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("TradeType", tradeType);
        if(tradeType.equals(CherryConstants.BUSINESS_TYPE_SD)){
            Map<String,Object> deliverMain = binOLSTCM03_BL.getProductDeliverMainData(propertyset.getInt("BIN_ProductDeliverID"), null);
            pramData.put("RelevanceNo", deliverMain.get("DeliverNoIF"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            binOLSTCM03_BL.updateProductInOut(pramData);
        }
    }
    
    /**
     * 生成发货单（新方法，订单审核后才生成发货单）
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void SD_create(Map arg0, Map arg1, PropertySet propertyset){
        //判断是否已经创建发货单，如果没有则根据订单生成发货单。
        //用于在订单二审通过后区分审核结果来自第三方还是新后台
        //新后台二审后需要调用本方法生成发货单，而第三方审核时接收MQ消息已经生成发货单。
        int productDeliverID = 0;
        try{
            productDeliverID = propertyset.getInt("BIN_ProductDeliverID");
        }catch(Exception e){

        }
        if(productDeliverID == 0){
            Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
            Map<String, Object> pramData = new HashMap<String, Object>();
            int orderID = propertyset.getInt("BIN_ProductOrderID");
            pramData.put("BIN_ProductOrderID", orderID);
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            if(null == mainData.get("OrderForm")){
            	/**
                 * 根据订货单数据直接生成发货单。
                 * 	1、如果订货单明细中的价格被编辑过，则以编辑后的价格作为发货价格；
                 *  2、如果没有被编辑过，则根据配置项是否使用成本价格作为发货价格；
                 */
                productDeliverID = binOLSTCM03_BL.createProductDeliverByOrder(pramData);
            }else{
                pramData.put("OrderForm", mainData.get("OrderForm"));
                /**
                 * 根据表单数据生成发货单。
                 *  1、如果订货单明细中的价格被编辑过，则以最近一次编辑后的价格（发货也可编辑，以此时间的编辑价格）作为发货价格；
                 *  2、如果没有被编辑过，则根据配置项是否使用成本价格作为发货价格；
                 */
                productDeliverID = binOLSTCM03_BL.createProductDeliverByForm(pramData);
            }
                
            propertyset.setInt("BIN_ProductDeliverID", productDeliverID);
            //发货单生成后都是处理发货单
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_SD);
            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(productDeliverID));
        }
    }
}
