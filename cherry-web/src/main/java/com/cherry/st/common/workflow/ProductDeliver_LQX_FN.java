/*  
 * @(#)ProductDeliver_LQX_FN.java    1.0 2012-2-21     
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

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH05_IF;
import com.cherry.st.sfh.service.BINOLSTSFH05_Service;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowEntry;

/**
 * K3发货操作工作流处理类
 * @author niushunjie
 *
 */
@Deprecated
public class ProductDeliver_LQX_FN implements FunctionProvider {

    @Resource
    private Workflow workflow;
    @Resource
    private BINOLCM22_IF binOLCM22_BL;
	@Resource
	private BINOLSTCM03_IF binOLSTCM03_BL;
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	@Resource
	private BINOLSTCM07_BL binOLSTCM07_BL;
	@Resource
	private BINOLSTSFH05_IF binOLSTSFH05_BL;
	@Resource
	private BINOLSTCM11_IF binOLSTCM11_BL;
	@Resource
	private BINOLSTSFH05_Service binOLSTSFH05_Service;

	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet propertyset)
			throws WorkflowException {
		String method = String.valueOf(arg1.get("method"));
		if("startFlow".equals(method)){
			//开始工作流
			startFlow(arg0,  arg1,  propertyset);			
		}else if("submitAudit".equals(method)){
			//提交审核
			submitAudit( arg0,  arg1,  propertyset);
		}else if("auditHandle".equals(method)){
			//判断是否需要审核
			auditHandle( arg0,  arg1,  propertyset);
		}else if("auditAgreeAuto".equals(method)){
			//自动审核通过
			auditAgreeAuto( arg0,  arg1,  propertyset);
		}else if("stockInOut".equals(method)){
			//生成入出库单并更改库存
			stockInOut( arg0,  arg1,  propertyset);
		}else if("auditAgreeHand".equals(method)){
			//人工审核通过
			auditAgreeHand(arg0,  arg1,  propertyset);
		}else if("auditDisAgreeHand".equals(method)){
			//人工审核退回
			auditDisAgreeHand(arg0,  arg1,  propertyset);
		}else if("productSend".equals(method)){
			//发货
			productSend(arg0,  arg1,  propertyset);
		}else if("acceptDeliver".equals(method)){
			acceptDeliver(arg0,  arg1,  propertyset);
		}
	}
	
	/**
	 * 插入发货单主表和明细表数据
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
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SD_EDIT, CherryConstants.OS_ACTOR_TYPE_USER+String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));

		propertyset.setInt("BIN_ProductDeliverID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
		//设定收货任务执行者为收货部门
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_RD, CherryConstants.OS_ACTOR_TYPE_DEPART+String.valueOf(mainData.get("BIN_OrganizationIDReceive")));
		//单据生成者的用户ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_USER, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));		
		//单据生成者的岗位ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_POSITION, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION)));		
		//单据生成者的所属部门ID
		propertyset.setString(CherryConstants.OS_MAINKEY_BILLCREATOR_DEPART, String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART)));
	       
        SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("workFlowId", entryID);
        paramMap.put("TradeType", CherryConstants.OS_BILLTYPE_SD);
        paramMap.put("OpCode", CherryConstants.OPERATE_SD);
        paramMap.put("OpResult", "100");
        paramMap.put("CurrentUnit", "ProductDeliver_LQX_FN");
        binOLCM22_BL.insertInventoryOpLog(mainData, paramMap);
	}

	/**
	 * 提交审核
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void submitAudit(Map arg0, Map arg1, PropertySet propertyset){	
		WorkflowEntry entry = (WorkflowEntry)arg0.get("entry");	
		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//发货单主表ID		
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		//审核区分  已提交审核
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
		//工作流实例ID 在工作流中的编辑后再提交时，实例ID早已生成，但是再次更新也不会受影响；
		//在刚开始工作流时，发货单主表中是没有实例ID的，这一步写入；
		pramData.put("WorkFlowID", entry.getId());		
		pramData.put("UpdatedBy", mainData.get("BIN_UserID"));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_UNDO);
		pramData.put("DeliverType", mainData.get("DeliverType"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
		
		propertyset.setInt("BIN_ProductDeliverID", Integer.parseInt(String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_BILLID))));
	}
	
	/**
	 * 判断是否需要审核，如不需要审核，则自动执行审核步骤，否则，等待人工介入
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditHandle(Map arg0, Map arg1, PropertySet propertyset){	
		//查询发货单的审核者		
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put(CherryConstants.OS_MAINKEY_CURRENT_OPERATE, CherryConstants.OPERATE_SD_AUDIT);
		pramData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, mainData.get(CherryConstants.OS_ACTOR_TYPE_POSITION));
		pramData.put(CherryConstants.OS_ACTOR_TYPE_USER, mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("BIN_BrandInfoID", mainData.get("BIN_BrandInfoID"));
		pramData.put("AuditDate", binOLSTSFH05_Service.getDateYMD());
		
		//查询发货单的审核者		
		String auditActors  = binOLCM14_BL.getActorsString(pramData);
		propertyset.setString(CherryConstants.OS_ACTOR+CherryConstants.OPERATE_SD_AUDIT, auditActors);	
	}
	
	/**
	 * 自动审核  通过
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditAgreeAuto(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		Map<String, Object> pramData =  new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("VerifiedFlag",  CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		//pramData.put("Date", binOLSTSFH05_Service.getDateYMD());
		pramData.put("AuditDate", binOLSTSFH05_Service.getDateYMD());
		pramData.put("DeliverType", mainData.get("DeliverType"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
		
		SimpleWorkflowEntry swe = (SimpleWorkflowEntry) arg0.get("entry");
		long entryID = swe.getId();
		int actionID = Integer.parseInt(String.valueOf(arg0.get("actionId")));
		PropertySet ps = workflow.getPropertySet(entryID);
        //从流程文件中读取操作代码，操作结果代码
        String opCode =ps.getString(CherryConstants.OS_MAINKEY_CURRENT_OPERATE);
        String opResult = null;
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(entryID));
        ActionDescriptor ad = wd.getAction(actionID);
        Map metaMap = ad.getMetaAttributes();       
        if(metaMap!=null){
            if(metaMap.get(CherryConstants.OS_META_OperateCode)!=null){
                opCode = metaMap.get(CherryConstants.OS_META_OperateCode).toString();
            }
            if(metaMap.get(CherryConstants.OS_META_OperateResultCode)!=null){
                opResult = metaMap.get(CherryConstants.OS_META_OperateResultCode).toString();
            }
        }
        Map<String, Object> logMap = new HashMap<String, Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",entryID);
        //操作部门
        logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
        //操作员工
        logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));    
        //操作业务类型
        logMap.put("TradeType",ps.getString(CherryConstants.OS_MAINKEY_BILLTYPE));
        //单据ID
        logMap.put("BillID",ps.getString(CherryConstants.OS_MAINKEY_BILLID));       
        //操作代码
        logMap.put("OpCode",opCode);
        //操作结果
        logMap.put("OpResult",opResult);        
        //作成者   
        logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);
	}
	
	/**
	 * 人工审核(通过)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//入出库单主表ID		
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		//审核区分  审核通过
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		pramData.put("DeliverType", mainData.get("DeliverType"));
		//pramData.put("Date", mainData.get("Date"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
	}
	
	/**
	 * 人工审核(退回)
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void auditDisAgreeHand(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		
		Map<String, Object> pramData =  new HashMap<String, Object>();
		//入出库单主表ID		
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		//审核区分  审核退回
		pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISAGREE);
		pramData.put("UpdatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("UpdatePGM", mainData.get("CurrentUnit"));
		pramData.put("OldUpdateTime", mainData.get("OldUpdateTime"));
		pramData.put("OldModifyCount", mainData.get("OldModifyCount"));
		pramData.put("BIN_EmployeeIDAudit", mainData.get("BIN_EmployeeID"));
		pramData.put("DeliverType", mainData.get("DeliverType"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
	}
	
	/**
	 * 发货
	 * 更新产品发货单的状态，需要发送MQ的时候则发送MQ
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 * @throws WorkflowException 
	 */
	private void productSend(Map arg0, Map arg1, PropertySet propertyset) throws WorkflowException{
		int productDeliverID = propertyset.getInt("BIN_ProductDeliverID");
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		//更新发货单状态 为已发货
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", productDeliverID);
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
		pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_SEND);
		pramData.put("Date", mainData.get("Date"));
		pramData.put("DeliverType", mainData.get("DeliverType"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
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
			mqMap.put("OrganizationCode",ConvertUtil.getString(mainData.get("OrganizationCode")));
			binOLSTCM07_BL.sendMQDeliverSend_K3(new int[]{propertyset.getInt("BIN_ProductDeliverID")}, mqMap);
		}
		
		SimpleWorkflowEntry swt = (SimpleWorkflowEntry) arg0.get("entry");
        long entryID = swt.getId();
        int actionID = Integer.parseInt(String.valueOf(arg0.get("actionId")));
        PropertySet ps = workflow.getPropertySet(entryID);
        //从流程文件中读取操作代码，操作结果代码
        String opCode =ps.getString(CherryConstants.OS_MAINKEY_CURRENT_OPERATE);
        String opResult = null;
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(entryID));
        ActionDescriptor ad = wd.getAction(actionID);
        Map metaMap = ad.getMetaAttributes();       
        if(metaMap!=null){
            if(metaMap.get(CherryConstants.OS_META_OperateCode)!=null){
                opCode = metaMap.get(CherryConstants.OS_META_OperateCode).toString();
            }
            if(metaMap.get(CherryConstants.OS_META_OperateResultCode)!=null){
                opResult = metaMap.get(CherryConstants.OS_META_OperateResultCode).toString();
            }
        }
        Map<String, Object> logMap = new HashMap<String, Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",entryID);
        //操作部门
        logMap.put("BIN_OrganizationID",mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
        //操作员工
        logMap.put("BIN_EmployeeID",mainData.get("BIN_EmployeeID"));    
        //操作业务类型
        logMap.put("TradeType",ps.getString(CherryConstants.OS_MAINKEY_BILLTYPE));
        //单据ID
        logMap.put("BillID",ps.getString(CherryConstants.OS_MAINKEY_BILLID));       
        //操作代码
        logMap.put("OpCode",opCode);
        //操作结果
        logMap.put("OpResult",opResult);        
        //作成者   
        logMap.put("CreatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);
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
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("CreatedBy", mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		pramData.put("CreatePGM", mainData.get("CurrentUnit"));
		binOLSTCM03_BL.createProductInOutByDeliverID(pramData);
	}
	
	/**
	 * 收货（有通过画面收货和pos机收货,但是对于这两种收货这边不做特别处理,只是将发货单的状态更新为已收货即可）
	 * @param arg0
	 * @param arg1
	 * @param propertyset
	 */
	private void acceptDeliver(Map arg0, Map arg1, PropertySet propertyset){
		Map<String, Object> mainData = (Map<String, Object>)arg0.get("mainData");
		String userID = "";
		String employeeID = "";
		String operateFlag = "";
		//是通过后台画面操作，还是通过pos机操作
		if(null != mainData){
			userID = String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
			operateFlag = String.valueOf(mainData.get(CherryConstants.OS_MAINKEY_OPERATE_FLAG));
			employeeID = String.valueOf(mainData.get("BIN_EmployeeID"));
		}
		 if(CherryConstants.OS_MAINKEY_OPERATE_POS.equals(operateFlag)){
				//pos机操作
				
		}else if(CherryConstants.OS_MAINKEY_OPERATE_BACK.equals(operateFlag)){
				//如果是后台操作
				
				//生成入库单
				BINOLSTSFH05_Form form = (BINOLSTSFH05_Form)mainData.get("BINOLSTSFH05_Form");
				int maindeliverid =binOLSTSFH05_BL.receiveDeliverByForm(form, "OSWorkFlow", Integer.parseInt(userID),Integer.parseInt(employeeID));
				
				//将收货数据写入入出库表
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("BIN_ProductReceiveID", maindeliverid);
				paramMap.put("CreatedBy", mainData.get("BIN_UserID"));
				paramMap.put("CreatePGM", mainData.get("CurrentUnit"));
				int stockid = binOLSTCM11_BL.createProductInOutByReceiveID(paramMap);
				
			}
		//更新发货单状态
		Map<String, Object> pramData = new HashMap<String, Object>();
		pramData.put("BIN_ProductDeliverID", propertyset.getInt("BIN_ProductDeliverID"));
		pramData.put("TradeStatus", CherryConstants.BILLTYPE_PRO_SD_RECIVE);
		pramData.put("UpdatedBy", userID);
		pramData.put("UpdatePGM", "OSWorkFlow");
		pramData.put("DeliverType", mainData.get("DeliverType"));
		binOLSTCM03_BL.updateProductDeliverMain(pramData);
	}
}
