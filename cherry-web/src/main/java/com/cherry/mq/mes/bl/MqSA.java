/*		
 * @(#)MqPP.java     1.0 2016-7-26 	
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.MqReceiver_IF;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.mq.mes.service.MqSA_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @ClassName: MqSA
 * @Description: TODO(退货申请) 
 * @author nanjunbo
 * @version v1.0.0 2016-8-23 
 *
 */
public class MqSA implements MqReceiver_IF {
	
	@Resource(name="binBEMQMES96_BL")
	private BINBEMQMES96_BL binBEMQMES96_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="mqSA_Service")
	private MqSA_Service mqSA_Service;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Override
	public void tran_execute(Map<String, Object> map) throws Exception {
		
		// 校验并设置相关参数
		this.checkAndSetData(map);
		
		// 设置操作程序名称
		this.setUpdateInfoMapKey(map);
		
		// 写客户退货主从表
		this.insertSaleRetrunReqAll(map);						
		
		this.addMessageLog(map);
        
        // 插入MongoDB
        this.addMongoDBBusLog(map);
        
        // 标记当前BL已经将MQ信息写入MongoDB与MQ收发日志表
        map.put("isInsertMongoDBBusLog", "1");
	}
	

	/**
	 * 插入退货申请单主从表信息
	 * @param map
	 * @throws Exception 
	 */
	private void insertSaleRetrunReqAll(Map<String, Object> map) throws Exception {
		Map<String, Object> saleRetrunReqMainMap = new HashMap<String, Object>();
		String subType = ConvertUtil.getString(map.get("subType"));
		saleRetrunReqMainMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		saleRetrunReqMainMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
		saleRetrunReqMainMap.put("BillCode", map.get("tradeNoIF"));
		saleRetrunReqMainMap.put("BillNoIF", map.get("tradeNoIF"));
		saleRetrunReqMainMap.put("RelevanceNo", ConvertUtil.getString(map.get("relevantNo")));
		saleRetrunReqMainMap.put("BIN_OrganizationID", map.get("organizationID"));
		saleRetrunReqMainMap.put("CounterCode", map.get("departCode"));
		saleRetrunReqMainMap.put("BIN_EmployeeID", map.get("employeeID"));
		saleRetrunReqMainMap.put("EmployeeCode", map.get("employeeCode"));
		saleRetrunReqMainMap.put("TradeDate", map.get("tradeDate"));
		saleRetrunReqMainMap.put("TradeType", "SA");
		saleRetrunReqMainMap.put("TradeTime", map.get("tradeTime"));
		saleRetrunReqMainMap.put("TotalQuantity", map.get("totalQuantity"));
		saleRetrunReqMainMap.put("TotalAmount", map.get("totalAmount"));
		saleRetrunReqMainMap.put("Comments", map.get("comments"));
		saleRetrunReqMainMap.put("MemberCode", map.get("memberCode"));
		saleRetrunReqMainMap.put("BIN_MemberInfoID", map.get("memberInfoID"));
		saleRetrunReqMainMap.put("VerifiedFlag", "1");
		saleRetrunReqMainMap.put("SynchFlag", "1");
		this.setUpdateInfoMapKey(saleRetrunReqMainMap);
		int saleRetrunReqMainID = mqSA_Service.insertSaleRetrunReqMain(saleRetrunReqMainMap);
		
		List<Map<String, Object>> saleRetrunReqDetailList = (List<Map<String, Object>>)map.get("detailList");
		for(Map<String, Object> saleRetrunReqDetailMap : saleRetrunReqDetailList) {
			saleRetrunReqDetailMap.put("saleRetrunReqMainID", saleRetrunReqMainID);
			saleRetrunReqDetailMap.put("employeeCode",  map.get("employeeCode"));
			// 设置操作程序名称
			this.setUpdateInfoMapKey(saleRetrunReqDetailMap);
		}
		mqSA_Service.insertSaleRetrunReqDetail(saleRetrunReqDetailList);
		
		if(map.containsKey("payList") && !"".equals(ConvertUtil.getString(map.get("payList")))) {
			// 支付方式明细数据
			List<Map<String, Object>> payDetailList = (List<Map<String, Object>>) map.get("payList");
			for (int i=0;i<payDetailList.size();i++){
				Map<String, Object> payDetailMap = payDetailList.get(i);
				// 设定销售主表ID
				payDetailMap.put("saleRetrunReqMainID", saleRetrunReqMainID);
				// 设置操作程序名称
				this.setUpdateInfoMapKey(payDetailMap);
			}
			// 插入销售支付方式构成表
			mqSA_Service.addSaleReturnPayList(payDetailList);
		}
		
		
		//查询用户表获得用户ID
		Map userMap = binBEMQMES99_Service.selUserByEempID(map);
		
		Map<String, Object> newMap1 = new HashMap<String, Object>();
		newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SA);//业务类型
		newMap1.put(CherryConstants.OS_MAINKEY_BILLID, saleRetrunReqMainID);//退货申请单ID
		newMap1.put("BIN_EmployeeID", map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		newMap1.put("CurrentUnit", "MQ");//当前机能ID
		newMap1.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        newMap1.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
        newMap1.put("BrandCode", map.get("brandCode"));//品牌编号

		
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
		newMap1.put("UserInfo", userInfo);
		
		newMap1.put("tradeDateTime", map.get("tradeTime"));//MQ单据的时间
		newMap1.put("BillIDKey", "BIN_SaleReturnRequestID");
		newMap1.put("BillNo", map.get("tradeNoIF"));
		//工作流开始
		binOLSTCM00_BL.StartOSWorkFlow(newMap1);
	}

	/**
	 * 接收数据写入MQ收发日志表
	 * @param map
	 * @throws Exception
	 */
	private void addMessageLog(Map<String, Object> map) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationInfoID", map.get("organizationInfoID"));
		paramMap.put("brandInfoID", map.get("brandInfoID"));
		paramMap.put("tradeType", map.get("tradeType"));
		paramMap.put("tradeNoIF", map.get("tradeNoIF"));
		paramMap.put("modifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		paramMap.put("counterCode", map.get("departCode"));
		paramMap.put("isPromotionFlag", map.get("isPromotionFlag"));
		paramMap.put("createdBy", "-2");
		paramMap.put("createPGM", "MqSA");
		paramMap.put("updatedBy", "-2");
		paramMap.put("updatePGM", "MqSA");
		// 插入MQ日志表（数据库SqlService）
        binBEMQMES99_Service.addMessageLog(paramMap);
	}
	
	private void addMongoDBBusLog(Map<String, Object> map) throws CherryMQException {
		// 插入MongoDB
        DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码，即品牌简称
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", map.get("tradeType"));
		// 单据号
		dbObject.put("TradeNoIF", map.get("tradeNoIF"));
		// 修改次数
		dbObject.put("ModifyCounts", map.get("modifyCounts")==null
				||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
		// 业务主体
	    dbObject.put("TradeEntity", "1");
	    // 柜台号
	    dbObject.put("CounterCode", map.get("departCode"));
		//员工代码
		dbObject.put("UserCode", map.get("employeeCode"));
		// 发生时间
		dbObject.put("OccurTime", (String)map.get("tradeTime"));
        // 日志正文
		dbObject.put("Content", "客户退货申请");
		// 
		map.put("addMongoDBFlag", "0");
	    binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	    map.put("addMongoDBFlag", "1");
		
	}
	
	

	/**
	 * 设置操作程序名称
	 * @param map
	 */
	private void setUpdateInfoMapKey(Map<String, Object> map) {
		map.put("createdBy", "-2");
		map.put("createPGM", "MqSA");
		map.put("updatedBy", "-2");
		map.put("updatePGM", "MqSA");
	}
	
	
	
	/**
	 * 校验并设置相关参数
	 * @param map
	 * @throws Exception
	 */
	private void checkAndSetData(Map<String, Object> map) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tradeNoIF", map.get("relevantNo"));
		Map<String, Object> saleRecordMap = mqSA_Service.getSaleRecordInfo(paramMap);
		paramMap.clear();
		paramMap=null;
		if(null == saleRecordMap || saleRecordMap.isEmpty()) {
			MessageUtil.addMessageWarning(map,"销售未成功时，又进行了关联退货业务！"+"关联单为\""+map.get("relevantNo")+"\"");
		}
		map.put("departCode", map.get("counterCode"));
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
		
		Map<String, Object> memberInfo = binBEMQMES99_Service.selMemberInfo(map);
		if (memberInfo != null && memberInfo.get("memberInfoID") != null) {
			// 设定会员ID
			map.put("memberInfoID", memberInfo.get("memberInfoID"));
			memberInfo.clear();
			memberInfo = null;
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
		// 设定交易时间
		map.put("tradeDateTime",map.get("tradeTime"));
		if("".equals(tradeDate)) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeDate"));
		} else if(!DateUtil.checkDate(tradeDate,"yyyy-MM-dd")){
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_75, "TradeDate","yyyy-MM-dd"));
		}
		
		if("".equals(tradeTime)) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_74, "TradeTime"));
		} else if(!DateUtil.checkDate(tradeTime,"yyyy-MM-dd HH:mm:ss")) {
			MessageUtil.addMessageWarning(map,String.format(MessageConstants.MSG_ERROR_75, "TradeTime","yyyy-MM-dd HH:mm:ss"));
		}
		

		// 校验明细中的产品及仓库相关信息
		List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailList");
		if(null != detailDataList && !detailDataList.isEmpty()) {
			for(int i=0; i < detailDataList.size(); i++) {
				Map<String, Object> detailDataMap = detailDataList.get(i);
				//当明细中的数量、金额为空时设为0
	            if("".equals(ConvertUtil.getString(detailDataMap.get("quantity")))){
	            	MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_76, "Quantity"));
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
				// 设定统一的产品、促销品ID的KEY
				String isPromotionFlag = ConvertUtil.getString(detailDataMap.get("isPromotionFlag"));
				String productIdKey = "0".equals(isPromotionFlag) ? "productVendorID" : "promotionProductVendorID";
				detailDataMap.put("productId", detailDataMap.get(productIdKey));
				
			}
		}
		
		if(map.containsKey("payList") && !"".equals(ConvertUtil.getString(map.get("payList")))) {
			// 校验支付方式明细
			List<Map<String, Object>> payDetailList = (List<Map<String, Object>>)map.get("payList");
			for(int i=0; i<payDetailList.size(); i++) {
				Map<String, Object> payDetailMap = payDetailList.get(i);
				//设置支付方式明细的序号
				payDetailMap.put("detailNo", i+1);
			}
			
		}
		
	}

}
