/*  
 * @(#)ProFlowAC_FN.java    1.0 2012.11.27
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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.st.bil.form.BINOLSTBIL18_Form;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 
 * 产品调拨工作流
 * 
 * @author niushunjie
 * @version 1.0 2012.11.27
 */
public class ProFlowAC_FN implements FunctionProvider{

    @Resource(name="binOLCM19_BL")
    private BINOLCM19_IF binOLCM19_BL;
    
    @Resource(name="binOLCM22_BL")
    private BINOLCM22_IF binOLCM22_BL;

    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;

    @Override
    public void execute(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException {
        String method = String.valueOf(arg1.get("method"));
        if("startFlow".equals(method)){
            startFlow(arg0,  arg1,  propertyset);
        }else if("submitHandle".equals(method)){
            submitHandle(arg0,  arg1,  propertyset);
        }else if("updateAC".equals(method)){
            updateAC(arg0,  arg1,  propertyset);
        }else if("deleteAC".equals(method)){
            deleteAC(arg0,  arg1,  propertyset);
        }else if("createBill".equals(method)){
            createBill(arg0,  arg1,  propertyset);
        }else if("changeStock".equals(method)){
            changeStock(arg0,  arg1,  propertyset);
        }else if("updateLG".equals(method)){
            updateLG(arg0,  arg1,  propertyset);
        }else if("deleteLG".equals(method)){
            deleteLG(arg0,  arg1,  propertyset);
        }else if("sendMQ".equals(method)){
            try {
                sendMQ(arg0,  arg1,  propertyset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入产品退库申请单主表和明细表数据，启动工作流
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void startFlow(Map arg0, Map arg1, PropertySet propertyset){
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        
        //产品类型：产品
        propertyset.setString(CherryConstants.OS_MAINKEY_PROTYPE, CherryConstants.OS_MAINKEY_PROTYPE_PRODUCT);
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLTYPE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE)));
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID)));
        //单据生成者的用户ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));       
        //单据生成者的岗位ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));       
        //单据生成者的所属部门ID
        propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
        //调拨申请单ID
        propertyset.setInt("BIN_ProductAllocationID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
        //设置调出执行者为调出部门
        propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_LG, CherryConstants.OS_ACTOR_TYPE_DEPART+CherryUtil.obj2int(mainData.get("BIN_OrganizationIDOut")));
        //设置调入执行者为调入部门
        propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_BG, CherryConstants.OS_ACTOR_TYPE_DEPART+CherryUtil.obj2int(mainData.get("BIN_OrganizationIDIn")));
        
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long entryID = entry.getId();
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_BG);
        paramMap.put("OpCode", CherryConstants.OPERATE_AC);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProFlowAC_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
    }

    /**
     * 产品调拨提交
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void submitHandle(Map arg0, Map arg1, PropertySet propertyset) throws InvalidInputException, WorkflowException {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long osid = entry.getId();
        
        //工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
        //在刚开始工作流时，调拨申请主表中是没有实例ID的，这一步写入；
        Map<String,Object> pramData = new HashMap<String,Object>();
        pramData.put("BIN_ProductAllocationID", propertyset.getInt("BIN_ProductAllocationID"));
        pramData.put("WorkFlowID", entry.getId());
        pramData.put("UpdatedBy", mainData.get("BIN_UserID"));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        binOLSTCM16_BL.updateProductAllocation(pramData);
        
        //取出当前有效的action
        int[] actionArr = workflow.getAvailableActions(osid, null);
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(osid));
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
     * 修改产品调拨单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateAC(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductAllocationID", propertyset.getInt("BIN_ProductAllocationID"));
        pramData.put("VerifiedFlag", verifiedFlag);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
            pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        }
        if(!"".equals(tradeStatus)){
            pramData.put("TradeStatus", tradeStatus);
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM16_BL.updateProductAllocation(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
        if(CherryConstants.AUDIT_FLAG_AGREE.equals(verifiedFlag)){
            String auditDateTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
            mainData.put("auditDateTime", auditDateTime);
        }
    }
    
    /**
     * 废弃产品调拨申请单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteAC(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        int billID = propertyset.getInt("BIN_ProductAllocationID");
        //调入申请单主表ID
        pramData.put("BIN_ProductAllocationID", billID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        if(!tradeStatus.equals("")){
            pramData.put("TradeStatus", tradeStatus);
        }
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM16_BL.updateProductAllocation(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 废弃产品调出单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void deleteLG(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData =  new HashMap<String, Object>();
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        int billID = propertyset.getInt("BIN_ProductAllocationOutID");
        //调出单主表ID
        pramData.put("BIN_ProductAllocationOutID", billID);
        //有效区分  无效
        //pramData.put("ValidFlag", "0");
        //审核区分-废弃
        pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
        pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM16_BL.updateProductAllocationOut(pramData);
        mainData.put(CherryConstants.OS_CHANGE_COUNT, ret);
    }
    
    /**
     * 新建产品调入/调出单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void createBill(Map arg0, Map arg1, PropertySet propertyset) {
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        pramData.put("TradeType", tradeType);
        pramData.put("TradeStatus", tradeStatus);
        
        BINOLSTBIL18_Form form = (BINOLSTBIL18_Form) mainData.get("ProductAllocationForm");
        int newBillID = 0;
        int billID = 0;
        if(null != form){
            if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
                //调入确认
                billID = propertyset.getInt("BIN_ProductAllocationOutID");
                pramData.put("BIN_ProductAllocationOutID", billID);
            }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
                //调出确认
                billID = propertyset.getInt("BIN_ProductAllocationID");
                pramData.put("BIN_ProductAllocationID", billID);
            }
            if(!verifiedFlag.equals("")){
                pramData.put("VerifiedFlag", verifiedFlag);
            }
            pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
            pramData.put("CreatePGM", mainData.get("CurrentUnit"));
            pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
            pramData.put("ProductAllocationForm", form);
            newBillID = binOLSTCM16_BL.createProductAllocationByForm(pramData);
        }else{
            //（柜台调出/调入）
            if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
                //调入确认
                String mqTradeType = ConvertUtil.getString(mainData.get("MQ_TRADETYPE"));
                if(mqTradeType.equals(MessageConstants.MSG_ALLOCATION_IN_CONFRIM)){
                    //调入确认MQ时生成调入单
                    Map<String,Object> mainData_BC = (Map<String, Object>) mainData.get("MainData_BC");
                    List<Map<String,Object>> detailData_BC = (List<Map<String, Object>>) mainData.get("DetailData_BC");
                    newBillID = binOLSTCM16_BL.insertProductAllocationInAll(mainData_BC, detailData_BC);
                }else{
                    //调出确认MQ时生成调入单
                    billID = propertyset.getInt("BIN_ProductAllocationID");
                    pramData.put("BIN_ProductAllocationID", billID);
                    billID = propertyset.getInt("BIN_ProductAllocationOutID");
                    pramData.put("BIN_ProductAllocationOutID", billID);
                    pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
                    pramData.put("CreatePGM", mainData.get("CurrentUnit"));
                    pramData.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
                    newBillID = binOLSTCM16_BL.createProductAllocationInByOut(pramData);
                }
            }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
                //调出确认
                Map<String,Object> mainTableData = (Map<String, Object>) mainData.get("LGMainTableData");
                mainTableData.put("TradeStatus", tradeStatus);
                List<Map<String,Object>> detailTableData = (List<Map<String, Object>>) mainData.get("LGDetailTableData");
                newBillID = binOLSTCM16_BL.insertProductAllocationOutAll(mainTableData,detailTableData);
            }
        }
        
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            propertyset.setInt("BIN_ProductAllocationInID", newBillID);
//            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_BG);
//            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            propertyset.setInt("BIN_ProductAllocationOutID", newBillID);
//            propertyset.setString(CherryConstants.OS_MAINKEY_BILLTYPE,CherryConstants.OS_BILLTYPE_LG);
//            propertyset.setString(CherryConstants.OS_MAINKEY_BILLID, String.valueOf(newBillID));
        }
    }
       
    /**
     * 更改库存
     * @param arg0
     * @param arg1
     * @param propertyset
     */
    private void changeStock(Map arg0, Map arg1, PropertySet propertyset) {
        String tradeType = ConvertUtil.getString(arg1.get("TradeType"));
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("TradeType", tradeType);
        if(CherryConstants.OS_BILLTYPE_BG.equals(tradeType)){
            //调入确认
            int productAllocationInID = propertyset.getInt("BIN_ProductAllocationInID");
            int productAllocationOutID = propertyset.getInt("BIN_ProductAllocationOutID");
            pramData.put("BIN_ProductAllocationInID", productAllocationInID);
            pramData.put("BIN_ProductAllocationOutID", productAllocationOutID);
        }else if(CherryConstants.OS_BILLTYPE_LG.equals(tradeType)){
            //调出确认
            int productAllocationOutID = propertyset.getInt("BIN_ProductAllocationOutID");
            int productAllocationID = propertyset.getInt("BIN_ProductAllocationID");
            pramData.put("BIN_ProductAllocationOutID", productAllocationOutID);
            pramData.put("BIN_ProductAllocationID", productAllocationID);
        }
        pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("CreatePGM", mainData.get("CurrentUnit"));
        pramData.put("TradeDateTime", mainData.get("tradeDateTime"));
        binOLSTCM16_BL.changeStock(pramData);
    }
    
    /**
     * 修改产品调出单
     * @param arg0
     * @param arg1
     * @param propertyset
     * @throws WorkflowException 
     * @throws InvalidInputException 
     */
    private void updateLG(Map arg0, Map arg1, PropertySet propertyset) {
        String verifiedFlag = ConvertUtil.getString(arg1.get("VerifiedFlag"));
        String tradeStatus = ConvertUtil.getString(arg1.get("TradeStatus"));
        
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
        Map<String, Object> pramData = new HashMap<String, Object>();
        pramData.put("BIN_ProductAllocationOutID", propertyset.getInt("BIN_ProductAllocationOutID"));
        if(!"".equals(tradeStatus)){
            pramData.put("TradeStatus", tradeStatus);
        }
        if(!verifiedFlag.equals("")){
            pramData.put("VerifiedFlag", verifiedFlag);
        }
        pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
        pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
        pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
        pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
        int ret = binOLSTCM16_BL.updateProductAllocationOut(pramData);
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
    private void sendMQ(Map arg0, Map arg1, PropertySet propertyset) throws Exception{
        WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");
        long entryID = entry.getId();
        IBatisPropertySet ips = (IBatisPropertySet) workflow.getPropertySet(entryID);
        Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData"); 
        int billID = propertyset.getInt("BIN_ProductAllocationID");
        Map<String, Object> productAllocation = binOLSTCM16_BL.getProductAllocationMainData(billID,null);
        String verifiedFlag = ConvertUtil.getString(productAllocation.get("VerifiedFlag"));
        String organizationIDIn = ConvertUtil.getString(productAllocation.get("BIN_OrganizationIDIn"));
        String organizationIDOut = ConvertUtil.getString(productAllocation.get("BIN_OrganizationIDOut"));
        // 发起调入申请的BA的代号
        String baCode = ConvertUtil.getString(productAllocation.get("EmployeeCode"));
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("organizationId", organizationIDIn);
        Map<String,Object> departInInfo = binOLCM19_BL.getCounterInfo(paramMap);
        
        //调入部门是否是柜台标志
        boolean isDepartInCNT = false;
        //调出部门是否是柜台标志
        boolean isDepartOutCNT = false;
        
        //调入部门是否是云POS（在isDepartInCNT=false时无效）
        boolean isDepartInCPOS = false;
        //调出部门是否是云POS（在isDepartOutCNT=false时无效）
        boolean isDepartOutCPOS = false;
        
        if(null != departInInfo && !departInInfo.isEmpty()){
            isDepartInCNT = true;
            String posFlag = ConvertUtil.getString(departInInfo.get("posFlag"));
            if(posFlag.equals("0")){
                isDepartInCPOS = true;
            }
        }
        paramMap.put("organizationId", organizationIDOut);
        Map<String,Object> departOutInfo = binOLCM19_BL.getCounterInfo(paramMap);
        if(null != departOutInfo && !departOutInfo.isEmpty()){
            isDepartOutCNT = true;
            String posFlag = ConvertUtil.getString(departOutInfo.get("posFlag"));
            if(posFlag.equals("0")){
                isDepartOutCPOS = true;
            }
        }
        //发送MQ消息体的子类型
        String mqSubType = "";
        //发送MQ消息体的业务状态
        String tradeState = "OK";
        String mqType = ConvertUtil.getString(arg1.get("MQType"));
        //审核单据（BG调入申请单/LG调出确认单）为空时作为LG，仅在MQType=Audit_OK或MQType=Audit_NG时有效
        String auditBill = ConvertUtil.getString(arg1.get("AuditBill"));
        //需要发送MQ标志
        boolean needSend = false;
        if(mqType.equals("Issue_BG")){
            if(isDepartInCNT && isDepartInCPOS && isDepartOutCNT && !isDepartOutCPOS){
                //调入方：云POS，调出方：终端POS机
                needSend = true;
                //云POS向终端POS做调入申请，老后台MQ监听程序接收到此MQ后，应该产生一张调入单，调出方柜台可对此单进行调出操作（操作后还是会发送调出的MQ，等待后台审核，不会立即改变库存）。 
                mqSubType = "CTDR";
                tradeState = "OK";
            }
        }else if(mqType.equals("Audit_OK") || mqType.equals("Audit_NG")){
            //调出ID
            if(isDepartInCNT && !isDepartInCPOS && isDepartOutCNT && !isDepartOutCPOS){
                //调入方：终端POS机，调出方：终端POS机
                needSend = true;
                //终端POS之间的调入调出被审核通过，老后台MQ监听程序需要改两方库存。 
                mqSubType = "T2T";
                if(!auditBill.equals("BG")){
                	// 调出确认单
                    billID = propertyset.getInt("BIN_ProductAllocationOutID");
                }
            }else if(isDepartInCNT && isDepartInCPOS && isDepartOutCNT && !isDepartOutCPOS){
                //调入方：云POS，调出方：终端POS机
                needSend = true;
                //云POS向终端POS做调入申请，最终又被审核通过，老后台MQ监听程序需要处理调出方的库存（调入方是云POS不在老后台管库存）。 
                mqSubType = "C2T";
                if(!auditBill.equals("BG")){
                	// 调出确认单
                    billID = propertyset.getInt("BIN_ProductAllocationOutID");
                }
            }else if(isDepartInCNT && !isDepartInCPOS && isDepartOutCNT && isDepartOutCPOS){
                //调入方：终端POS机，调出方：云POS
                needSend = true;
                //终端POS向云POS做调入申请，最终又被审核通过，老后台MQ监听程序需要处理调入方的库存（调出方是云POS不在老后台管库存）。 
                mqSubType = "T2C";
            }
            
            if(mqType.equals("Audit_OK")){
                tradeState = "OK";
            }else if(mqType.equals("Audit_NG")){
                tradeState = "NG";
            }
        }else if(mqType.equals("Decline_LG")){
            tradeState = "NG";
            if(isDepartInCNT && !isDepartInCPOS && isDepartOutCNT && isDepartOutCPOS){
                //调入方：终端POS机，调出方：云POS
                needSend = true;
                //终端POS向云POS做调入申请，云POS直接拒绝掉（还未到审核步骤）
                mqSubType = "T2C";
            }
        }
        
        //终端库存变化标志
        String stockChange = "";
        if((mqSubType.equals("T2T") || mqSubType.equals("T2C") || mqSubType.equals("C2T")) && tradeState.equals("OK")){
            //审核调入申请单不会处理库存（调入申请》审核》调出确认》调入确认）
            //审核调出单才会处理库存（调入申请》调出确认》审核》调入确认/自动调入确认）
            //从PS表里判断是否有字段BIN_ProductAllocationInID，存在说明两方库存都扣，否则只扣调出方
            //OUT：扣调出方库存
            //ALL：扣调入方、调出方库存
            //值为空，不扣库存。
            if(!auditBill.equals("BG")){
                Collection collection = ips.getKeys("BIN_ProductAllocationInID");
                if(collection.size() == 0){
                    stockChange = "OUT";
                }else{
                    stockChange = "ALL";
                }
            }
        }
        
        if(needSend){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",String.valueOf(productAllocation.get("BIN_OrganizationInfoID")));
            mqMap.put("BIN_BrandInfoID",String.valueOf(productAllocation.get("BIN_BrandInfoID")));
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            mqMap.put("OrganizationCodeIn", ConvertUtil.getString(productAllocation.get("DepartCodeIn")));//调入部门编号
            mqMap.put("OrganizationCodeOut", ConvertUtil.getString(productAllocation.get("DepartCodeOut")));//调出部门编号
            mqMap.put("MQ_SubType", mqSubType);
            mqMap.put("TradeState", tradeState);
            String auditDateTime = ConvertUtil.getString(mainData.get("auditDateTime"));
            if("".equals(auditDateTime)){
                auditDateTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
            }
            mqMap.put("auditDateTime",auditDateTime);
            mqMap.put("AuditBill", auditBill);
            mqMap.put("StockChange", stockChange);
            // 发起调入申请的BA的代号
            mqMap.put("BACode", baCode);
            binOLSTCM16_BL.sendMQ(new int[]{billID}, mqMap);
        }
    }
}