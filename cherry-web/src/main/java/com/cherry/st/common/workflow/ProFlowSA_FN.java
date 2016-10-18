/*  
 * @(#)ProFlowRA_FN.java    1.0 2012.07.24
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL20_Form;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.bl.BINOLSTCM09_BL;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.cherry.st.common.service.BINOLSTCM21_Service;
import com.opensymphony.module.propertyset.PropertyException;
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
 * 销售退货申请工作流
 * 
 * @author NANJUNBO
 * @version 1.0 2012.07.24
 */
public class ProFlowSA_FN implements FunctionProvider{

    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
    @Resource(name="binOLSTCM09_BL")
    private BINOLSTCM09_BL binOLSTCM09_BL;
    
    @Resource(name="binOLSTCM21_BL")
    private BINOLSTCM21_IF binOLSTCM21_BL;
    
    @Resource(name="binOLSTCM21_Service")
    private BINOLSTCM21_Service binOLSTCM21_Service;

    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        if("startFlow".equals(method)){
            startFlow(arg0,  arg1,  propertyset);
        }else if("deleteSA".equals(method)){
            deleteSA(arg0,  arg1,  propertyset);
        }else if("createSA".equals(method)){
            createSA(arg0,  arg1,  propertyset);
        }else if("updateSA".equals(method)){
            updateSA(arg0,  arg1,  propertyset);
        }else if("sendAuditMQ".equals(method)){
            try {
                sendAuditMQ(arg0,  arg1,  propertyset);
            } catch (PropertyException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入产品退库申请单主表和明细表数据（MQ），后台退库申请到这里时已经有BillID，启动工作流
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        int saleReturnRequestID = CherryUtil.obj2int(mainData.get(CherryConstants.OS_MAINKEY_BILLID));

        //来自后台
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_SaleReturnRequestID", saleReturnRequestID);
        param.put("WorkFlowID", entryID);
        param.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        param.put("UpdatePGM", mainData.get("CurrentUnit"));
        binOLSTCM21_BL.updateSaleReturnRequest(param);

        
        mainData.put(CherryConstants.OS_MAINKEY_BILLID, saleReturnRequestID);
        mainData.put("BillNo", mainData.get("BillNo"));
        mainData.put("TableName", "Sale.BIN_SaleReturnRequest");
        
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, ConvertUtil.getString(saleReturnRequestID));
        propertyset.setInt("BIN_SaleReturnRequestID", saleReturnRequestID);
        propertyset.setInt("SA_BIN_SaleReturnRequestID", saleReturnRequestID);
   
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));

        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_SA);
        paramMap.put("OpCode", CherryConstants.OPERATE_SA_CREATE);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowSA_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
    }
    
    /**
     * 新建销售退货申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void createSA(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();           
      
        int newBillID = 0;      
        int billID = propertyset.getInt("BIN_SaleReturnRequestID");
        pramData.put("BIN_SaleReturnRequestID", billID);
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        //pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
        newBillID = binOLSTCM21_BL.createSaleReturnRequest(pramData);
            
        propertyset.setInt("BIN_SaleReturnRequestID", newBillID);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_SA);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        
        propertyset.setInt("SJ_BIN_SaleReturnRequestID", newBillID);
    }
    
    /**
     * 废弃产品退库申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteSA(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
//        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        int saleReturnRequestID = 0;
        if(tradeType.equals(CherryConstants.OS_BILLTYPE_SA)){
        	saleReturnRequestID = propertyset.getInt("SA_BIN_SaleReturnRequestID");
        }else if(tradeType.equals(CherryConstants.OS_BILLTYPE_SJ)){
        	saleReturnRequestID = propertyset.getInt("SJ_BIN_SaleReturnRequestID");
        }else{
        	saleReturnRequestID = propertyset.getInt("BIN_SaleReturnRequestID");
        }
        //发货单主表ID
        pramData.put("BIN_SaleReturnRequestID", saleReturnRequestID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeIDAudit"));
        pramData.put("Reason", mainData.get("OpComments"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM21_BL.updateSaleReturnRequest(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    private void updateSA(Map arg0, Map arg1, PropertySet propertyset) {
    	 String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
         String synchFlag = ConvertUtil.getString(arg1.get("SynchFlag")); 
         
         Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
         Map<String, Object> pramData = new HashMap<String, Object>();
         pramData.put("BIN_SaleReturnRequestID", propertyset.getInt("BIN_SaleReturnRequestID"));
         if(!"".equals(verifiedFlag)){
             pramData.put("VerifiedFlag", verifiedFlag);
         }
         pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeIDAudit"));
         pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
         pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
         //同步数据标志
         if(!"".equals(synchFlag)){
             pramData.put("SynchFlag", synchFlag);
         }
         int ret = binOLSTCM21_BL.updateSaleReturnRequest(pramData);
         mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    /**
     * 发送MQ
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws Exception 
     * @throws PropertyException 
     */
    private void sendAuditMQ(Map arg0, Map arg1, PropertySet propertyset) throws PropertyException, Exception{
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        int saleReturnRequestID = propertyset.getInt("BIN_SaleReturnRequestID");
        Map<String, Object> saleReturnRequest = binOLSTCM21_BL.getSaleReturnRequestMainData(saleReturnRequestID,null);
        String organizationID = String.valueOf(saleReturnRequest.get("BIN_OrganizationID"));
        //如果是发送退库审核到柜台，则发送MQ
        if(binOLSTCM07_BL.checkOrganizationType(organizationID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(saleReturnRequest.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(saleReturnRequest.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(saleReturnRequest.get("CounterCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            mqMap.put("BillNoIF",String.valueOf(saleReturnRequest.get("BillNoIF")));
            binOLSTCM21_BL.sendMQ(new int[]{saleReturnRequestID}, mqMap);
            
            //写入确认退库
            propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SA_CONFIRM, CherryConstants.OS_ACTOR_TYPE_DEPART+organizationID);
        }
    }
    

}
