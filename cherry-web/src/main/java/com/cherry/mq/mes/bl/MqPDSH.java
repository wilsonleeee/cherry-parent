/*		
 * @(#)MqPDSH.java     1.0 2016-08-16		
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
package com.cherry.mq.mes.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM22_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM25_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.ibatis.IBatisPropertySet;

/**
 * 
 * @ClassName: MqPDSH 
 * @Description: (盘点审核) 
 * @author liyuan
 * @version v1.0.0 2016-08-16
 *
 */
public class MqPDSH implements MqReceiver_IF {
	
	private static final Logger logger = LoggerFactory.getLogger(MqPDSH.class);

	@Resource(name="workflow")
    private Workflow workflow;
	
	@Resource(name="binBEMQMES96_BL")
	private BINBEMQMES96_BL binBEMQMES96_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLSTCM14_BL")
	private BINOLSTCM14_IF binOLSTCM14_BL;
	
	@Resource(name="binOLSTCM06_BL")
	private BINOLSTCM06_IF binOLSTCM06_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_IF binOLCM22_BL;

	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;

	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;

	@Resource(name="binOLCM22_BL")
	private BINOLCM22_BL binolcm22_bl;

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;    

	@Resource(name="binOLCM25_BL")
    private BINOLCM25_BL binOLCM25_BL;
    
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {
		
		map.put("content", "盘点审核处理");
		// 关联的盘点申请原单主数据
		List<Map<String, Object>> proStocktakeRequest = null;
		String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
		if("".equals(relevantNo)) {
			MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, "RelevantNo"));
		} else {
			// 根据关联单号取得关联的盘点申请原单主数据【此共通方法增加了盘点类型的查询】
			proStocktakeRequest = binOLSTCM14_BL.selProStocktakeRequest(relevantNo);
			if(null == proStocktakeRequest || proStocktakeRequest.size() == 0) {
				MessageUtil.addMessageWarning(map, "没有关联的盘点申请单");
			}			
		}
		// 查询盘点申请原数据，将该数据放入MAP中进行检查
		Map<String, Object> oldBillInfo = proStocktakeRequest.get(0);
		// 部门代号
		map.put("departCode", oldBillInfo.get("DepartCode"));
		// 操作者员工代号
		map.put("employeeCode", oldBillInfo.get("EmployeeCode"));
		// 明细行盘差数量的合计
		map.put("totalQuantity", oldBillInfo.get("TotalQuantity"));
		// 明细行盘差金额的合计
		map.put("totalAmount", oldBillInfo.get("TotalAmount"));
		// 备注、理由
		map.put("comments", oldBillInfo.get("Comments"));
		List<Map<String, Object>> proStocktakeRequestDetail = binOLSTCM14_BL.getProStocktakeRequestDetailData(ConvertUtil.getInt(oldBillInfo.get("BIN_ProStocktakeRequestID")), null);
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		for(Map<String,Object> detail : proStocktakeRequestDetail) {
			Map<String, Object> newDetail = new HashMap<String, Object>();
			// ProductId	产品ID	有则填
			newDetail.put("productId", "");
			//Unitcode	厂商编码	必填
			newDetail.put("unitcode", ConvertUtil.getString(detail.get("UnitCode")));
			//Barcode	商品条码	必填
			newDetail.put("barcode", ConvertUtil.getString(detail.get("BarCode")));
			//LogicInventoryCode	仓库类型（逻辑仓库）；没有时填默认仓库类型	有则填
			newDetail.put("logicInventoryCode", ConvertUtil.getString(detail.get("LogicInventoryCode")));
			//BookQuantity	账面数量	必填，正负均可，以实际账面数量为准
			newDetail.put("bookQuantity", ConvertUtil.getString(detail.get("BookQuantity")));
			//GainQuantity	盘差	必填，实盘数量-账面数量。正负均可
			newDetail.put("gainQuantity", ConvertUtil.getString(detail.get("GainQuantity")));
			//Price	价格	必填。在忽略价格只注重数量的情况下，填写0.00
			newDetail.put("price", ConvertUtil.getString(detail.get("Price")));
			//Comments	明细行中的备注
			newDetail.put("comments", ConvertUtil.getString(detail.get("Comments")));
			detailList.add(newDetail);
		}
		// 转换明细
		map.put("detailList", detailList);
		
		// 校验部门、员工、产品明细信息
		this.checkAndSetData(map);
		
		// 设置操作程序名称
		this.setUpdateInfoMapKey(map);
		
		// 自动执行同意/自动执行废弃
		String subType = ConvertUtil.getString(map.get("subType"));
        if(CherryConstants.CRAUDIT_FLAG_CONFIRM.equals(subType)){
            //盘点审核流程
        	autoExecute(proStocktakeRequest,map,"autoAgree");
            return;
        }else {
            //盘点申请废弃  
        	autoExecute(proStocktakeRequest,map,"autoInvalid");
            return;
        }
		// 处理盘点确认数据
		//this.analyzeStocktakeConfirmData(proStocktakeRequest, map);
		
	}
	/**
	 * 
	 * @param proStocktakeRequest : 盘点申请单据数据
	 * @param map ：MQ消息体数据（包含确认的主数据与明细数据）
	 * @throws Exception
	 */
	private void autoExecute(List<Map<String, Object>> proStocktakeRequest, Map<String, Object> map,String actionType) throws Exception {
        //判断调拨流程
		String workFlowID = ConvertUtil.getString(proStocktakeRequest.get(0).get("WorkFlowID"));
        //调用工作流
        long osID = Long.parseLong(workFlowID);
        
		ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
		int actionID = 0;
		if(null != adArr && adArr.length>0){
			Map<String,Object> metaMapTMp = null;
			for (int j = 0; j < adArr.length; j++) {
				metaMapTMp = adArr[j].getMetaAttributes();
				//找到带有OS_DefaultAction元素的action
				if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
					String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
					if(actionType.equals(defaultAction)){
						ActionDescriptor ad = adArr[j];
						actionID = ad.getId();
						break;
					}
				}
			}
			if(actionID == 0){
				MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，无法找到当前能执行Action");
			}
		}else{
			MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
					"涉及主要参数：工作流ID\""+osID+"\"");
		}
		//查询用户表获得用户ID
        Map userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        } 
        UserInfo userInfo = new UserInfo();
        userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        mainDataMap.put("entryID", osID);
        mainDataMap.put("actionID", actionID);
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
        mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
        mainDataMap.put("BrandCode", map.get("brandCode").toString());
        mainDataMap.put("CurrentUnit", "MqPDSH");
        mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainDataMap.put("UserInfo", userInfo);
        mainDataMap.put("OrganizationCode",map.get("departCode"));//部门编号
	    	 
		//自动执行SUB Action
		doSubAction(mainDataMap, osID, actionID);
	}

	/**
	 * 自动执行SUB Action
	 * @param mainData
	 * @param entryID
	 * @param actionID
	 * @throws WorkflowException 
	 * @throws InvalidInputException 
	 */
	private void doSubAction(Map<String, Object> mainData, long entryID,
			int actionID) throws InvalidInputException, WorkflowException {
		 //业务类型
		Map<String, Object> pramData = new HashMap<String, Object> ();
		pramData.put("mainData", mainData);		

		workflow.doAction_single(entryID, actionID, pramData);
		
		// PropertySet ps = workflow.getPropertySet(entryID);
		IBatisPropertySet ps = (IBatisPropertySet) workflow
				.getPropertySet(entryID);
		Map<String, Object> propertyMap = (Map<String, Object>) ps.getMap(null,
				PropertySet.STRING);

		// 从流程文件中读取操作代码，操作结果代码
		String opCode = ConvertUtil.getString(propertyMap
				.get(CherryConstants.OS_MAINKEY_CURRENT_OPERATE));
		String opResult = null;
		WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow
				.getWorkflowName(entryID));
		ActionDescriptor ad = wd.getAction(actionID);
		Map metaMap = ad.getMetaAttributes();
		if (metaMap != null) {
			if (metaMap.get(CherryConstants.OS_META_OperateCode) != null) {
				opCode = metaMap.get(CherryConstants.OS_META_OperateCode)
						.toString();
			}
			if (metaMap.get(CherryConstants.OS_META_OperateResultCode) != null) {
				opResult = metaMap.get(
						CherryConstants.OS_META_OperateResultCode).toString();
			}
		}
		String osButtonNameCode = ConvertUtil.getString(metaMap
				.get(CherryConstants.OS_META_ButtonNameCode));

		String billType = ConvertUtil.getString(propertyMap
				.get(CherryConstants.OS_MAINKEY_BILLTYPE));
		int billID = CherryUtil.obj2int(propertyMap
				.get(CherryConstants.OS_MAINKEY_BILLID));
		String billNo = null;
		String tableName = null;
		// 单据对应的履历ID，每个业务的履历表都不一样
		String historyBillID = null;
		Map<String, Object> proMainData = new HashMap<String, Object>();

		billID = ps.getInt("BIN_ProStocktakeRequestID");
		proMainData = binOLSTCM14_BL.getProStocktakeRequestMainData(billID,
				null);
		billNo = ConvertUtil.getString(proMainData.get("StockTakingNoIF"));
		tableName = "Inventory.BIN_ProStocktakeRequest";

		Map<String, Object> logMap = new HashMap<String, Object>();
		// 工作流实例ID
		logMap.put("WorkFlowID", entryID);
		// 操作部门
		logMap.put("BIN_OrganizationID",
				mainData.get(CherryConstants.OS_ACTOR_TYPE_DEPART));
		// 操作员工
		logMap.put("BIN_EmployeeID", mainData.get("BIN_EmployeeID"));
		// 操作业务类型
		logMap.put("TradeType", billType);
		// 表名
		logMap.put("TableName", tableName);
		// 单据ID
		logMap.put("BillID", billID);
		// 单据编号
		logMap.put("BillNo", billNo);
		// 单据对应的履历单据ID
		logMap.put("HistoryBillID", historyBillID);
		// 操作代码
		logMap.put("OpCode", opCode);
		// 操作结果
		logMap.put("OpResult", opResult);
		// 操作时间
		String tradeDateTime = ConvertUtil.getString(mainData
				.get("tradeDateTime"));
		if (!"".equals(tradeDateTime)) {
			logMap.put("OpDate", tradeDateTime);
		}
		// 操作备注
		logMap.put("OpComments", mainData.get("OpComments"));
		// 作成者
		logMap.put("CreatedBy",
				mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		// 作成程序名
		logMap.put("CreatePGM", "OSWorkFlow");
		// 更新者
		logMap.put("UpdatedBy",
				mainData.get(CherryConstants.OS_ACTOR_TYPE_USER));
		// 更新程序名
		logMap.put("UpdatePGM", "OSWorkFlow");
		// OS_ButtonNameCode
		logMap.put("OS_ButtonNameCode", osButtonNameCode);
		binolcm22_bl.insertInventoryOpLog(logMap);

		// 推送业务提示
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("TradeType", "osworkflow");
		Map<String, Object> employeeIDMap = new HashMap<String, Object>();
		String employeeID = ConvertUtil.getString(propertyMap
				.get("OS_BillCreator"));
		employeeIDMap.put(employeeID, binOLCM00_BL.getLoginName(employeeID));
		msgParam.put("EmployeeIDMap", employeeIDMap);// 接收消息（单据创建者）
		// msgParam.put("OrgCode", userInfo.getOrgCode());
		// msgParam.put("BrandCode", userInfo.getBrandCode());
		msgParam.put("BillID", billID);
		msgParam.put("BillNo", billNo);
		msgParam.put("OpCode", opCode);
		msgParam.put("OpResult", opResult);
		// 单据URL
		msgParam.put("OpenBillURL",
				binOLCM25_BL.getBillURL(tableName, opCode, ""));

		JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
	}
	/**
	 * 
	 * @param proStocktakeRequest : 盘点申请单据数据
	 * @param map ：MQ消息体数据（包含确认的主数据与明细数据）
	 * @throws Exception
	 */
	private void analyzeStocktakeConfirmData(List<Map<String, Object>> proStocktakeRequest, Map<String, Object> map) throws Exception {
		String workFlowID = ConvertUtil.getString(proStocktakeRequest.get(0).get("WorkFlowID"));
		map.put("WorkFlowID", workFlowID);
        map.put("BIN_EmployeeIDAudit", ConvertUtil.getString(proStocktakeRequest.get(0).get("BIN_EmployeeIDAudit")));
        map.put("BIN_ProStocktakeRequestID", proStocktakeRequest.get(0).get("BIN_ProStocktakeRequestID"));
        map.put("StocktakeType", proStocktakeRequest.get(0).get("StocktakeType"));
        // 盘点申请中的备注及盘点原因code同时写入到盘点单中
        if("".equals(ConvertUtil.getString(map.get("comments")))){
        	// MQ消息中备注为空则取盘点申请表中的原始数据
        	map.put("reason", proStocktakeRequest.get(0).get("Comments"));
        }
        // 盈亏处理MQ中没有StockReason字段，故取盘点申请表中的原始数据
        map.put("stockReason", proStocktakeRequest.get(0).get("StockReason"));
        
        // 终端确认（或者废弃）盘点，生成盘点单（废弃：生成没有明细的盘点单）
        int productStockTakingID = this.proStocktakingByPROYA(map);
        
        long osID = Long.parseLong(workFlowID);
        PropertySet ps = workflow.getPropertySet(osID);
        ps.setInt("BIN_ProductStockTakingID", productStockTakingID);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("WorkFlowID", workFlowID);
        param.put("BIN_ProductStockTakingID", productStockTakingID);
        param.put("TradeDateTime", map.get("tradeDateTime"));
        binOLSTCM14_BL.posConfirmCAFinishFlow(param);
        
        return;
	}
	
	/**
	 * 终端做盘点确认（或者废弃）
	 * 生成盘点单并写操作日志表(废弃的盘点单没有明细只有主单)
	 * @param map : mq消息参数，包含明细数据
	 * @return int : 生成的盘点单ID
	 * @throws Exception
	 */
	private int proStocktakingByPROYA(Map<String, Object> map) throws Exception {
        String subType = ConvertUtil.getString(map.get("subType"));
        if(!CherryConstants.CRAUDIT_FLAG_CONFIRM.equals(subType)) {
        	// 废弃，不生成盘点，直接将盘点申请单进行废弃
            Map<String, Object> pramData =  new HashMap<String, Object>();
            //盘点申请单主表ID
            pramData.put("BIN_ProStocktakeRequestID", map.get("BIN_ProStocktakeRequestID"));
            //审核区分-废弃
            pramData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_DISCARD);
            pramData.put("UpdatedBy", map.get("updatedBy"));
            pramData.put("UpdatePGM", map.get("updatePGM"));
            binOLSTCM14_BL.updateProStocktakeRequest(pramData);
            
            // 盘点申请单关联的审核单（废弃时需要将审核单也废弃）
         	List<Map<String, Object>> proStocktakeRequestRJ = binOLSTCM14_BL.selProStocktakeRequestCR(ConvertUtil.getString(map.get("relevantNo")));
         	if(null != proStocktakeRequestRJ) {
         		for(Map<String, Object> proStocktakeRequestRJMap : proStocktakeRequestRJ) {
         			pramData.put("BIN_ProStocktakeRequestID", proStocktakeRequestRJMap.get("BIN_ProStocktakeRequestID"));
         			binOLSTCM14_BL.updateProStocktakeRequest(pramData);
         		}
         	}
        	
        	Map<String,Object> logMap = new HashMap<String,Object>();
            //  工作流实例ID
            logMap.put("WorkFlowID",map.get("WorkFlowID"));
            //操作部门
            logMap.put("BIN_OrganizationID",map.get("organizationID"));
            //操作员工
            logMap.put("BIN_EmployeeID",map.get("employeeID")); 
            //操作业务类型--该字段始终填写流程起始步骤的类型代号
            logMap.put("TradeType","CR");
             //表名
            logMap.put("TableName", "Inventory.BIN_ProStocktakeRequest");
            //单据ID
            logMap.put("BillID",map.get("BIN_ProStocktakeRequestID"));      
            //单据编号
            logMap.put("BillNo", map.get("relevantNo"));
            //操作代码--柜台废弃盘点
            logMap.put("OpCode","205");
            //操作结果--已废弃
            logMap.put("OpResult","110");
            //操作时间
            logMap.put("OpDate", map.get("tradeDateTime"));
            //作成者   
            logMap.put("CreatedBy", map.get("createdBy")); 
            //作成程序名
            logMap.put("CreatePGM", map.get("createPGM"));
            //更新者
            logMap.put("UpdatedBy", map.get("createdBy")); 
            //更新程序名
            logMap.put("UpdatePGM", map.get("updatePGM"));   
            binOLCM22_BL.insertInventoryOpLog(logMap);
        	return 0;
        } else {
        	// 确认盘点，将审核状态变更为审核通过（‘Y’）
            Map<String, Object> pramData =  new HashMap<String, Object>();
            //盘点申请单主表ID
            pramData.put("BIN_ProStocktakeRequestID", map.get("BIN_ProStocktakeRequestID"));
            //审核区分-审核通过（‘Y’）
            pramData.put("VerifiedFlag", CherryConstants.CRAUDIT_FLAG_CONFIRM);
            pramData.put("UpdatedBy", map.get("updatedBy"));
            pramData.put("UpdatePGM", map.get("updatePGM"));
            binOLSTCM14_BL.updateProStocktakeRequest(pramData);
        }
        
        // MQ消息体中的明细数据
 		List<Map<String, Object>> detailMqDataList = (List<Map<String, Object>>)map.get("detailList");
 		Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
	    mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
	    mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
	    mainData.put("StockTakingNoIF",map.get("tradeNoIF"));
	    mainData.put("RelevanceNo", map.get("relevantNo"));
	    mainData.put("BIN_OrganizationID", map.get("organizationID"));
	    mainData.put("BIN_EmployeeID", map.get("employeeID"));
	    mainData.put("BIN_OrganizationIDDX", map.get("organizationID"));
	    mainData.put("BIN_EmployeeIDDX", map.get("employeeID"));
	    mainData.put("BIN_EmployeeIDAudit", map.get("BIN_EmployeeIDAudit"));
	    mainData.put("VerifiedFlag", CherryConstants.CRAUDIT_FLAG_CONFIRM);
	    mainData.put("Type", map.get("StocktakeType"));
	    mainData.put("TradeType", CherryConstants.OS_BILLTYPE_CA);
	    mainData.put("Comments", map.get("reason"));
	    mainData.put("StockReason", map.get("stockReason"));
	    mainData.put("Date", map.get("tradeDate"));
	    mainData.put("TradeTime", map.get("tradeTime"));
	    mainData.put("WorkFlowID", map.get("WorkFlowID"));
	    mainData.put("CreatedBy", map.get("createdBy"));
	    mainData.put("CreatePGM", map.get("createPGM"));
	    mainData.put("UpdatedBy", map.get("updatedBy"));
	    mainData.put("UpdatePGM", map.get("updatePGM"));
	    
	    //盘差总数量
        int totalQuantity = 0;
        //盘差总金额
        BigDecimal totalAmount = new BigDecimal(0);
        if(null != detailMqDataList && detailMqDataList.size() > 0) {
        	// 明细存在----盘点确认
	        for(int i=0;i<detailMqDataList.size();i++){
	            Map<String,Object> detailDataMap = detailMqDataList.get(i);
	            Map<String,Object> detailMap = new HashMap<String,Object>();
	            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
	            detailMap.put("DetailNo", i+1);
	            int bookQuantity = CherryUtil.obj2int(detailDataMap.get("bookQuantity"));
	            int gainQuantity = CherryUtil.obj2int(detailDataMap.get("gainQuantity"));
	            detailMap.put("Quantity", bookQuantity);
	            detailMap.put("GainQuantity", gainQuantity);
	            detailMap.put("Price", detailDataMap.get("price"));
	            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
	            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
	            detailMap.put("Comments", detailDataMap.get("comments"));
	            detailMap.put("HandleType", detailDataMap.get("handleType"));
	            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
	            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
	            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
	            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
	            detailList.add(detailMap);
	            
	            totalQuantity += CherryUtil.obj2int(gainQuantity);
	            BigDecimal price = new BigDecimal(0);
	            if (detailMap.get("Price")!=null && !"".equals(detailMap.get("Price"))){
	                price = new BigDecimal(Double.parseDouble((String)detailMap.get("Price")));
	            }
	            totalAmount = totalAmount.add(price.multiply(new BigDecimal(CherryUtil.obj2int(gainQuantity))));
	        }
        }
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        // 此方法支持明细为空（不能为NULL）的情况，
        int billID = binOLSTCM06_BL.insertStockTakingAll(mainData, detailList);
        Map<String,Object> stockTakingMainData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
        Map<String,Object> logMap = new HashMap<String,Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",map.get("WorkFlowID"));
        //操作部门
        logMap.put("BIN_OrganizationID",map.get("organizationID"));
        //操作员工
        logMap.put("BIN_EmployeeID",map.get("employeeID")); 
        //操作业务类型--该字段始终填写流程起始步骤的类型代号
        logMap.put("TradeType","CR");
         //表名
        logMap.put("TableName", "Inventory.BIN_ProductStockTaking");
        //单据ID
        logMap.put("BillID",billID);      
        //单据编号
        logMap.put("BillNo", stockTakingMainData.get("StockTakingNoIF"));
        //操作代码--柜台确认盘点（1131CODE值，144：柜台确认盘点；205：柜台废弃盘点）
        logMap.put("OpCode","OK".equals(subType)? "144" : "205");
        //操作结果--生成单据
        logMap.put("OpResult","100");
        //操作时间
        logMap.put("OpDate",map.get("tradeDateTime"));
        //作成者   
        logMap.put("CreatedBy",mainData.get("BIN_EmployeeID")); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get("BIN_EmployeeID")); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);

        return billID;
	}
	
	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "-2");
		map.put("createPGM", "MqPDSH");
		map.put("updatedBy", "-2");
		map.put("updatePGM", "MqPDSH");
	}

	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {
		
		// 获取部门信息
		Map<String, Object> departInfo = binBEMQMES96_BL.getDepartInfo(map);
		String organizationID = "";
		if(null == departInfo || departInfo.isEmpty()) {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map,"部门号为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_06);
		} else {
			organizationID = ConvertUtil.getString(departInfo.get("organizationID"));
			// 设定部门ID
			map.put("organizationID", organizationID);
			// 设定部门名称
			map.put("departName", departInfo.get("departName"));
			// 用后即焚
			departInfo.clear();
			departInfo = null;
		}
		
		// 获取员工信息
		Map<String, Object> employeeInfo = binBEMQMES96_BL.getEmployeeInfo(map);
		if(null == employeeInfo || employeeInfo.isEmpty()) {
			// 没有查询到相关员工信息
			MessageUtil.addMessageWarning(map,"员工号为\""+map.get("employeeCode")+"\""+MessageConstants.MSG_ERROR_07);
		} else {
			// 设定员工ID
			map.put("employeeID", employeeInfo.get("employeeID"));
			// 设定员工姓名
			map.put("employeeName", employeeInfo.get("employeeName"));
			//设定岗位ID
			map.put("positionCategoryID", employeeInfo.get("positionCategoryID"));
			// 设定员工岗位
			map.put("categoryName", employeeInfo.get("categoryName"));
			// 用后即焚
			employeeInfo.clear();
			employeeInfo = null;
		}
		
		// 查询仓库信息
		List<Map<String, Object>> depotList = binBEMQMES96_BL.getDepotsByDepartID(organizationID);
//		List<Map<String, Object>> depotList = binOLCM18_BL.getDepotsByDepartID(organizationID, "");
		if(depotList != null && !depotList.isEmpty()){
			// 设定实体仓库ID
			map.put("inventoryInfoID", depotList.get(0).get("BIN_DepotInfoID"));
			// 用后即焚
			depotList.clear();
			depotList = null;
		}
		
		if("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))){
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"部门为\""+map.get("departCode")+"\""+MessageConstants.MSG_ERROR_36);
		}
		
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		if (!"".equals(tradeTime)) {
			//String tradeTimeHMS = tradeTime.substring(11, tradeTime.length());
			// 设定交易时间
			map.put("tradeDateTime",tradeDate + " "+tradeTime);
			// 设定HH:mm:SS格式的时间
			map.put("tradeTime", tradeTime);
		}
		
		String subType = ConvertUtil.getString(map.get("subType"));
		// 废弃时无明细，不必校验明细数据
		if(CherryConstants.CRAUDIT_FLAG_CONFIRM.equals(subType)) {
			// 校验明细中的产品及仓库相关信息
			List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailList");
			
			if(null == detailDataList || detailDataList.size() == 0) {
				// 盘点业务时无明细数据且SubType未定义，或者是其他业务没有相应明细
				MessageUtil.addMessageWarning(map, "盘点确认时无明细数据");
			}
			
			for(int i=0; i < detailDataList.size(); i++) {
				Map<String, Object> detailDataMap = detailDataList.get(i);
				// 厂商编码
				if("".equals(ConvertUtil.getString(detailDataMap.get("unitcode")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Unitcode"));
	            }
				// 商品条码
	            if("".equals(ConvertUtil.getString(detailDataMap.get("barcode")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Barcode"));
	            }
				// 账面数量	            
	            if("".equals(ConvertUtil.getString(detailDataMap.get("bookQuantity")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "BookQuantity"));
	            }
				// 盘差
	            if("".equals(ConvertUtil.getString(detailDataMap.get("gainQuantity")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "GainQuantity"));
	            }
				// 价格
	            if("".equals(ConvertUtil.getString(detailDataMap.get("price")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Price"));
	            }
				// 明细连番
	            detailDataMap.put("detailNo", i + 1);
				// 员工ID
	            detailDataMap.put("employeeID", map.get("employeeID"));
				// 实体仓库ID
	            detailDataMap.put("inventoryInfoID", map.get("inventoryInfoID"));
	
	            //逻辑仓库
	            Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
	            logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
	            logicInventoryInfoMap.put("LogicInventoryCode", detailDataMap.get("logicInventoryCode"));
	            logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
	            logicInventoryInfoMap.put("language", null);
	            Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
	            if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
	                int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
	                // 设定逻辑仓库ID
	                detailDataMap.put("logicInventoryInfoID", logicInventoryInfoID);
	            }else{
	                // 没有查询到相关逻辑仓库信息
	                MessageUtil.addMessageWarning(map,"逻辑仓库为\""+detailDataMap.get("logicInventoryCode")+"\""+MessageConstants.MSG_ERROR_37);
	            }
				
				binBEMQMES96_BL.setDetailProPrmIDInfo(detailDataMap, map);
				
				//设定包装类型ID  仓库库位ID
				detailDataMap.put("productVendorPackageID", 0);
				detailDataMap.put("storageLocationInfoID", 0);
			}
		}
	}
}
