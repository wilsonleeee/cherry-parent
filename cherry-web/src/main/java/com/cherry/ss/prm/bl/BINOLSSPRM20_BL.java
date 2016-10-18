/*	
 * @(#)BINOLSSPRM20_BL.java     1.0 2010/12/16		
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
package com.cherry.ss.prm.bl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import org.jbpm.JbpmConfiguration;
//import org.jbpm.JbpmContext;
//import org.jbpm.taskmgmt.exe.TaskInstance;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.form.BINOLSSPRM20_Form;
import com.cherry.ss.prm.service.BINOLSSPRM20_Service;

/**
 * @author dingyc
 *
 */
public class BINOLSSPRM20_BL {
//	
//	@Resource
//	private BINOLCM03_BL binOLCM03_BL;
//	@Resource
//	private BINOLSSCM01_BL binOLSSCM01_BL;
//	@Resource
//	private BINOLSSCM03_BL binOLSSCM03_BL;
//	@Resource
//	private BINOLSSPRM20_Service binOLSSPRM20_Service;
//	
//	/**
//	 * 进行调拨确认处理
//	 * @param form
//	 * @throws Exception 
//	 */
//	public int tran_allocation(BINOLSSPRM20_Form form,UserInfo userInfo) throws Exception{
//		
//		//调出部门ID
//		String outOrganizationId = form.getOutOrganizationId();
//		//调出仓库
//		String outDepotId = form.getOutDepotId();
//		//申请调拨日期
//		String allocationDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
//		//调拨理由（总）
//		String reasonALl = form.getReasonAll();
//		//产品厂商编码
//		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
//		//促销品单价
//		String[] priceUnitArr = form.getPriceUnitArr();	
//		//调入部门
//		String[] inOrganizationIDArr =form.getInOrganizationIDArr();	
//		//调入仓库
//		String[] inventoryIDArr =form.getInventoryIDArr();		
//		//调拨数量
//		String[] quantityuArr =form.getQuantityuArr();		
//		//调拨原因
//		String[] reasonArr = form.getReasonArr();
//		//调拨申请单号
//		String[] relevanceNoArrIF = form.getRelevanceNoArrIF();
//		//调拨申请单ID
//		String[] allocationIDArr = form.getAllocationIDArr();
//		 //更新时间
//	    String[]  outUpdateTimeArr = form.getOutUpdateTimeArr();	    
//	    //更新次数
//	    String[]  outModifyCountArr = form.getOutModifyCountArr();
//	    
//		//任务实例ID
//		String[] taskInstanceIDArr = form.getTaskInstanceIDArr();
//
//		String[] auditActors = null;
//		
//		//审核区分
//		String verifiedFlag =CherryConstants.AUDIT_FLAG_AGREE;
//		//业务状态区分
//		String statusFlag = CherryConstants.PROM_ALLOCATION_RES;
//		
//		//查询调出确认单是否开启了审核
////		if(binOLCM14_BL.isAuditOpen("1000", "203",userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID())){
////			//如果开启了审核，查询调出确认单的审核人
////			 auditActors = binOLCM10_BL.getActors(CherryConstants.JBPM_PROM_ALLO_OUT_AUDIT,userInfo);
////			 if(auditActors!=null&&auditActors.length>0){
////				 verifiedFlag =CherryConstants.AUDIT_FLAG_SUBMIT;
////				 statusFlag = CherryConstants.PROM_ALLOCATION_RES;
////			 }
////		}
//	
//		//调出确认单是否需要收货
////		if(binOLCM14_BL.isConfigOpen("1003", userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID())){
////			statusFlag = CherryConstants.PROM_ALLOCATION_RES;
////		}
//		
//		//一次调拨操作的总数量（始终为正）
//		int totalQuantity =0;
//		//总金额
//		double totalAmount =0;
//		
//		//计算一次操作的总金额,并进行拆分
//		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
//		HashMap<String,Object> tempMap=null;
//		String splitKey="";
//		int lengthTotal = promotionProductVendorIDArr.length;
//		for(int i=0;i<lengthTotal;i++){
//			int tempCount = CherryUtil.string2int(quantityuArr[i]);
//			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//			totalAmount += money;
//			totalQuantity += tempCount;
//			
//			//进行表单拆分
//			tempMap = new HashMap<String,Object>();		
//			//明细       促销产品厂商ID
//			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
//			//明细       调拨数量
//			tempMap.put("Quantity", tempCount);
//			//明细       价格
//			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//			//明细       TODO：包装类型ID
//			tempMap.put("BIN_ProductVendorPackageID", 0);
//			//明细      调出仓库ID
//			tempMap.put("BIN_InventoryInfoID", outDepotId);
//			//明细      TODO：调拨逻辑仓库ID
//			tempMap.put("BIN_LogicInventoryInfoID", 0);
//			//明细      TODO：调拨仓库库位ID
//			tempMap.put("BIN_StorageLocationInfoID",0);
//			//明细     有效区分
//			tempMap.put("ValidFlag", "1");
//			//明细     理由
//			tempMap.put("Reason", reasonArr[i]);
//			//明细     共通字段
//			tempMap.put("CreatedBy", userInfo.getLoginName());
//			tempMap.put("CreatePGM", "BINOLSSPRM20");
//			tempMap.put("UpdatedBy", userInfo.getLoginName());
//			tempMap.put("UpdatePGM", "BINOLSSPRM20");
//			
//			//主表       接口调拨单号
//			tempMap.put("AllocationNoIF", null);
//			//主表       组织ID
//			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
//			//主表       品牌ID
//			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//			//主表      申请调拨部门
//			tempMap.put("BIN_OrganizationID", inOrganizationIDArr[i]);
//			//主表     接受调拨部门
//			tempMap.put("BIN_OrganizationIDAccept", outOrganizationId);
//			//主表     制单员工
//			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());	
//			//主表     审核区分
//			tempMap.put("VerifiedFlag", verifiedFlag);
//			//主表     业务类型
//			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_ALLOCATION_RESPONSE);
//			//主表     业务状态
//			tempMap.put("TradeStatus", statusFlag);
//			//主表     调拨区分
//			tempMap.put("AllocationFlag", "O");
//			//主表     关联单号
//			tempMap.put("RelevanceNo", relevanceNoArrIF[i]);
//			//主表     调拨理由
//			tempMap.put("ReasonALl", reasonALl);
//			//主表    调拨日期
//			tempMap.put("AllocationDate", allocationDate);
//			//主表计算用    一条明细的总金额
//			tempMap.put("Money", money);
//			
//			tempMap.put("AllocationID", allocationIDArr[i]);
//			tempMap.put("outUpdateTime", outUpdateTimeArr[i]);
//			tempMap.put("outModifyCount", outModifyCountArr[i]);
//			tempMap.put("inInventoryID", inventoryIDArr[i]);
//			
//			//工作流用   
//			tempMap.put("TaskInstanceID", taskInstanceIDArr[i]);
//			
//			splitKey = allocationIDArr[i];
//			if(splitMap.containsKey(splitKey)){
//				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
//				tempList.add(tempMap);
//			}else{
//				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
//				tempList.add(tempMap);
//				splitMap.put(splitKey, tempList);
//			}			
//		}
//		
//		//插入  促销品库存操作流水表
//		Map<String, Object> mapInventory = new HashMap<String, Object>();
//		//操作部门
//		mapInventory.put("BIN_OrganizationID", outOrganizationId);
//		//操作员工
//		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//		//总数量
//		mapInventory.put("TotalQuantity", totalQuantity);
//		//总金额
//		mapInventory.put("TotalAmount", totalAmount);
//		//审核区分  0：未审核	1：审核完了
//		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE );
//		//业务类型 
//		//1:仓库调拨，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
//		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
//		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_ALLOCATION_RESPONSE);
//		//操作日期
//		mapInventory.put("DeliverDate", allocationDate);
//		//有效区分
//		mapInventory.put("ValidFlag", "1");
//		
//		//数据来源渠道
//		mapInventory.put("DataChannel", 0);
//		//物流ID
//		mapInventory.put("BIN_LogisticInfoID", 0);
//		mapInventory.put("createdBy", userInfo.getLoginName());
//		mapInventory.put("createPGM", "BINOLSSPRM20");
//		mapInventory.put("updatedBy", userInfo.getLoginName());
//		mapInventory.put("updatePGM", "BINOLSSPRM20");
//		
//		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
//		
//		//插入 促销产品调拨表(已经被拆成每个收货地址一单,会带着多条明细)
//		long[] taskInstanceID = new long[splitMap.size()];
//		int index =0;
//		for(List<HashMap<String,Object>> list:splitMap.values()){
//			//更新原申请单，插入确认单的主表明细表数据，插入入出库表，更新库存表
//			insertAllocatonAllData2(bIN_PromotionInventoryLogID,list,userInfo);
//			taskInstanceID[index]= Long.parseLong(String.valueOf(list.get(0).get("TaskInstanceID")));
//			index++;
//		}		
////		Iterator<String> it = splitMap.keySet().iterator();		
////		while(it.hasNext()){
////			String key = it.next();
////			List<HashMap<String,Object>> list = splitMap.get(key);
////			//更新原申请单，插入确认单的主表明细表数据，插入入出库表，更新库存表
////			insertAllocatonAllData2(bIN_PromotionInventoryLogID,list,userInfo);
////			taskInstanceID[index]= Long.parseLong(String.valueOf(list.get(0).get("TaskInstanceID")));
////			index++;
////		}
//		
//		
//		//读取配置文件，工作流是否开启
////		String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
////		if(jbpmFlag.equals("true")){
//			//结束工作流中的任务
//			JbpmContext context = JbpmConfiguration.getInstance().createJbpmContext();
//			for(int i=0;i<taskInstanceID.length;i++){
//				TaskInstance task = context.getTaskInstance(taskInstanceID[i]);
//				task.end();
//			}
//			context.close();
////		}
//		
//		return bIN_PromotionInventoryLogID;
//	}	
//	
//	/**
//	 * 进行调拨确认处理(首页工作流弹出画面)
//	 * @param form
//	 * @throws Exception 
//	 */
//	public int tran_allocationJBPM(BINOLSSPRM20_Form form,UserInfo userInfo) throws Exception{
//		
//		//调出部门ID
//		String outOrganizationId = form.getOutOrganizationId();
//		//调出仓库
//		String outDepotId = form.getOutDepotId();
//		//申请调拨日期
//		String allocationDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
//		//调拨理由（总）
//		String reasonALl = form.getReasonAll();
//		//产品厂商编码
//		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
//		//促销品单价
//		String[] priceUnitArr = form.getPriceUnitArr();	
//		//调入部门
//		String[] inOrganizationIDArr =form.getInOrganizationIDArr();	
//		//调入仓库
//		String[] inventoryIDArr =form.getInventoryIDArr();		
//		//调拨数量
//		String[] quantityuArr =form.getQuantityuArr();		
//		//调拨原因
//		String[] reasonArr = form.getReasonArr();
//		//调拨申请单号
//		String[] relevanceNoArrIF = form.getRelevanceNoArrIF();
//		//调拨申请单ID
//		String[] allocationIDArr = form.getAllocationIDArr();
//		 //更新时间
//	    String[]  outUpdateTimeArr = form.getOutUpdateTimeArr();	    
//	    //更新次数
//	    String[]  outModifyCountArr = form.getOutModifyCountArr();
//	    
//		//任务实例ID
//		String[] taskInstanceIDArr = form.getTaskInstanceIDArr();
//
//		String[] auditActors = null;
//		
//		//审核区分
//		String verifiedFlag =CherryConstants.AUDIT_FLAG_AGREE;
//		//业务状态区分
//		String statusFlag = CherryConstants.PROM_ALLOCATION_RES;
//		
//		//查询调出确认单是否开启了审核
////		if(binOLCM14_BL.isAuditOpen("1000", "203",userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID())){
////			//如果开启了审核，查询调出确认单的审核人
////			 auditActors = binOLCM10_BL.getActors(CherryConstants.JBPM_PROM_ALLO_OUT_AUDIT,userInfo);
////			 if(auditActors!=null&&auditActors.length>0){
////				 verifiedFlag =CherryConstants.AUDIT_FLAG_SUBMIT;
////				 statusFlag = CherryConstants.PROM_ALLOCATION_RES;
////			 }
////		}
////	
////		//调出确认单是否需要收货
////		if(binOLCM14_BL.isConfigOpen("1003", userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID())){
////			statusFlag = CherryConstants.PROM_ALLOCATION_RES;
////		}
//		
//		//一次调拨操作的总数量（始终为正）
//		int totalQuantity =0;
//		//总金额
//		double totalAmount =0;
//		
//		//计算一次操作的总金额,并进行拆分
//		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
//		HashMap<String,Object> tempMap=null;
//		String splitKey="";
//		int lengthTotal = promotionProductVendorIDArr.length;
//		for(int i=0;i<lengthTotal;i++){
//			int tempCount = CherryUtil.string2int(quantityuArr[i]);
//			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//			totalAmount += money;
//			totalQuantity += tempCount;
//			
//			//进行表单拆分
//			tempMap = new HashMap<String,Object>();		
//			//明细       促销产品厂商ID
//			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
//			//明细       调拨数量
//			tempMap.put("Quantity", tempCount);
//			//明细       价格
//			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//			//明细       TODO：包装类型ID
//			tempMap.put("BIN_ProductVendorPackageID", 0);
//			//明细      调出仓库ID
//			tempMap.put("BIN_InventoryInfoID", outDepotId);
//			//明细      TODO：调拨逻辑仓库ID
//			tempMap.put("BIN_LogicInventoryInfoID", 0);
//			//明细      TODO：调拨仓库库位ID
//			tempMap.put("BIN_StorageLocationInfoID",0);
//			//明细     有效区分
//			tempMap.put("ValidFlag", "1");
//			//明细     理由
//			tempMap.put("Reason", reasonArr[i]);
//			//明细     共通字段
//			tempMap.put("CreatedBy", userInfo.getLoginName());
//			tempMap.put("CreatePGM", "BINOLSSPRM20");
//			tempMap.put("UpdatedBy", userInfo.getLoginName());
//			tempMap.put("UpdatePGM", "BINOLSSPRM20");
//			
//			//主表       接口调拨单号
//			tempMap.put("AllocationNoIF", null);
//			//主表       组织ID
//			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
//			//主表       品牌ID
//			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
//			//主表      申请调拨部门
//			tempMap.put("BIN_OrganizationID", inOrganizationIDArr[i]);
//			//主表     接受调拨部门
//			tempMap.put("BIN_OrganizationIDAccept", outOrganizationId);
//			//主表     制单员工
//			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());	
//			//主表     审核区分
//			tempMap.put("VerifiedFlag", verifiedFlag);
//			//主表     业务类型
//			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_ALLOCATION_RESPONSE);
//			//主表     业务状态
//			tempMap.put("TradeStatus", statusFlag);
//			//主表     调拨区分
//			tempMap.put("AllocationFlag", "O");
//			//主表     关联单号
//			tempMap.put("RelevanceNo", relevanceNoArrIF[i]);
//			//主表     调拨理由
//			tempMap.put("ReasonALl", reasonALl);
//			//主表    调拨日期
//			tempMap.put("AllocationDate", allocationDate);
//			//主表计算用    一条明细的总金额
//			tempMap.put("Money", money);
//			
//			tempMap.put("AllocationID", allocationIDArr[i]);
//			tempMap.put("outUpdateTime", outUpdateTimeArr[i]);
//			tempMap.put("outModifyCount", outModifyCountArr[i]);
//			tempMap.put("inInventoryID", inventoryIDArr[i]);
//			
//			//工作流用   
//			tempMap.put("TaskInstanceID", taskInstanceIDArr[i]);
//			
//			splitKey = allocationIDArr[i];
//			if(splitMap.containsKey(splitKey)){
//				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
//				tempList.add(tempMap);
//			}else{
//				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
//				tempList.add(tempMap);
//				splitMap.put(splitKey, tempList);
//			}			
//		}
//		
//		//插入  促销品库存操作流水表
//		Map<String, Object> mapInventory = new HashMap<String, Object>();
//		//操作部门
//		mapInventory.put("BIN_OrganizationID", outOrganizationId);
//		//操作员工
//		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//		//总数量
//		mapInventory.put("TotalQuantity", totalQuantity);
//		//总金额
//		mapInventory.put("TotalAmount", totalAmount);
//		//审核区分  0：未审核	1：审核完了
//		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE );
//		//业务类型 
//		//1:仓库调拨，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
//		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
//		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_ALLOCATION_RESPONSE);
//		//操作日期
//		mapInventory.put("DeliverDate", allocationDate);
//		//有效区分
//		mapInventory.put("ValidFlag", "1");
//		
//		//数据来源渠道
//		mapInventory.put("DataChannel", 0);
//		//物流ID
//		mapInventory.put("BIN_LogisticInfoID", 0);
//		mapInventory.put("createdBy", userInfo.getLoginName());
//		mapInventory.put("createPGM", "BINOLSSPRM20");
//		mapInventory.put("updatedBy", userInfo.getLoginName());
//		mapInventory.put("updatePGM", "BINOLSSPRM20");
//		
//		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
//		
//		//插入 促销产品调拨表(已经被拆成每个收货地址一单,会带着多条明细)
//		long[] taskInstanceID = new long[splitMap.size()];
//		int index =0;
//		for(List<HashMap<String,Object>> list:splitMap.values()){
//			//更新原申请单，插入确认单的主表明细表数据，插入入出库表，更新库存表
//			insertAllocatonAllData2(bIN_PromotionInventoryLogID,list,userInfo);
//			taskInstanceID[index]= Long.parseLong(String.valueOf(list.get(0).get("TaskInstanceID")));
//			index++;
//		}		
////		Iterator<String> it = splitMap.keySet().iterator();		
////		while(it.hasNext()){
////			String key = it.next();
////			List<HashMap<String,Object>> list = splitMap.get(key);
////			//更新原申请单，插入确认单的主表明细表数据，插入入出库表，更新库存表
////			insertAllocatonAllData2(bIN_PromotionInventoryLogID,list,userInfo);
////			taskInstanceID[index]= Long.parseLong(String.valueOf(list.get(0).get("TaskInstanceID")));
////			index++;
////		}
//		
//		
//		//读取配置文件，工作流是否开启
////		String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
////		if(jbpmFlag.equals("true")){
//			//结束工作流中的任务
//			JbpmContext context = JbpmConfiguration.getInstance().createJbpmContext();
//			for(int i=0;i<taskInstanceID.length;i++){
//				TaskInstance task = context.getTaskInstance(taskInstanceID[i]);
//				task.end();
//			}
//			context.close();
////		}
//		
//		return bIN_PromotionInventoryLogID;
//	}	
//	
//	@SuppressWarnings("unchecked")
//	public List<Map<String,Object>> getAllOrder(String organizationID,UserInfo userInfo){
//		
//		List retList = new ArrayList();
//		
//		//读取配置文件，工作流是否开启
////		String jbpmFlag =  PropertiesUtil.pps.getProperty("JBPM.OpenFlag", "false");
////		if(jbpmFlag.equals("true")){
//			//使用工作流
//			//任务接收者
//			List list = new ArrayList();
//			list.add("organizationID"+organizationID);
//			//list.add("positionID"+userInfo.getCurrentPositionID());
//			//list.add("userID"+userInfo.getBIN_UserID());
//			
//			JbpmContext context = JbpmConfiguration.getInstance().createJbpmContext();
//			//接收到该部门所有的任务，过滤出发货任务
//			List<TaskInstance> taskList = context.getGroupTaskList(list);
//			Map<String ,Object> praMap;
//			for(int i=0;i<taskList.size();i++){
//				TaskInstance task = taskList.get(i);
//				if(CherryConstants.JBPM_PROM_ALLO_OUT_CONFIRM.equals(String.valueOf(task.getVariable(CherryConstants.JBPM_BUSSINESS_CODE)))){
//					//如果任务是调出确认
//					String allocationID = String.valueOf(task.getVariable(CherryConstants.JBPM_MAIN_ID));
//					praMap = new HashMap<String ,Object>();
//					praMap.put("BIN_PromotionAllocationID", allocationID);
//					praMap.put("TaskInstanceID", task.getId());
//					praMap.put("language", userInfo.getLanguage());
//					retList.addAll(binOLSSPRM20_Service.getAllOrderJbpm(praMap));
//				}
//			}
//			context.close();
////		}else{
////			//不使用工作流
////			Map<String,Object> map =new HashMap<String,Object>();			
////			map.put("language", userInfo.getLanguage());
////			map.put("BIN_OrganizationIDAccept", organizationID);
////			retList = binOLSSPRM20_Service.getAllOrder(map);
////		}
//		return retList;		
//	}
//	
//	/**
//	 * 取得调拨明细
//	 * @param allocationIDArr
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Map<String ,Object>> getAllocationDetailList(String[] allocationIDArr,UserInfo userInfo){
//		List<Map<String ,Object>> retList = new ArrayList<Map<String ,Object>>();
//		Map<String ,Object> praMap; 
//		for(int i=0;i<allocationIDArr.length;i++){
//			praMap = new HashMap<String ,Object>();
//			//参数格式：调拨单ID + 任务ID
//			String[] tempArr = allocationIDArr[i].split("_");
//			praMap.put("BIN_PromotionAllocationID", tempArr[0]);
//			praMap.put("TaskInstanceID", tempArr[1]);
//			praMap.put("language", userInfo.getLanguage());
//			retList.addAll(binOLSSPRM20_Service.getAllocationDetailList(praMap));
//		}		
//		return retList;
//	}
//	
//	/**
//	 * 取得调拨单信息
//	 * 
//	 * @param map
//	 * @return
//	 */
//	public Map<String, Object> getAllocationInfo(Map<String, Object> map) {
//		return binOLSSPRM20_Service.getAllocationInfo(map);
//	}
//	
//	/**
//	 * 插入一单调拨确认单数据到【促销产品调拨业务单据表】和【促销产品调拨业务单据明细表】
//	 * @param seqnum 库存操作流水号
//	 * @param argList 明细数据
//	 * @throws Exception 
//	 */
//	public int insertAllocatonAllData2(int seqnum,List<HashMap<String,Object>> argList,UserInfo userInfo) throws Exception{
//		HashMap<String,Object> tempMap = argList.get(0);	
//		String oldBIN_PromotionAllocationID = String.valueOf(tempMap.get("AllocationID"));
//			
//		HashMap<String,Object> praMap = new HashMap<String,Object>();		
//		praMap.put("BIN_PromotionAllocationID", tempMap.get("AllocationID"));
//		praMap.put("TradeStatus",CherryConstants.PROM_ALLOCATION_RES);
//		praMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
//		praMap.put("UpdatePGM", tempMap.get("UpdatePGM"));
//		praMap.put("outUpdateTime", tempMap.get("outUpdateTime"));
//		praMap.put("outModifyCount", tempMap.get("outModifyCount"));
//		
//		//更新原申请单
//		int cnt = binOLSSCM03_BL.updateAllocationMainHT(praMap);
//		if(cnt==0){
//			throw new CherryException("ESS00029");
//		}
//		
//		//取得单据号
//		String allocationNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID(),userInfo.getLoginName(), "6");
//		//循环一单里的所有明细，以调拨主表数据
//		int totalCount =0;
//		double totalMoney = 0.00;
//		for(int i=0;i<argList.size();i++){
//			HashMap<String,Object> map = argList.get(i);
//			totalCount +=  CherryUtil.string2int(map.get("Quantity").toString());
//			totalMoney +=	CherryUtil.string2double(map.get("Money").toString());
//		}
//		
//		HashMap<String,Object> allocationMap = new HashMap<String,Object>();		
//		
//		//促销库存操作流水ID
//		allocationMap.put("BIN_PromotionInventoryLogID", seqnum);
//		//调拨单号
//		allocationMap.put("AllocationNo", allocationNo);
//		//主表       组织信息ID
//		allocationMap.put("BIN_OrganizationInfoID", tempMap.get("BIN_OrganizationInfoID"));	
//		//主表       品牌ID
//		allocationMap.put("BIN_BrandInfoID", tempMap.get("BIN_BrandInfoID"));
//		//主表       接口调拨单号
//		allocationMap.put("AllocationNoIF", allocationNo);
//		//主表      申请调拨部门
//		allocationMap.put("BIN_OrganizationID", tempMap.get("BIN_OrganizationID"));
//		//主表     接受调拨部门
//		allocationMap.put("BIN_OrganizationIDAccept",tempMap.get("BIN_OrganizationIDAccept"));
//		//主表    制单员工
//		allocationMap.put("BIN_EmployeeID", tempMap.get("BIN_EmployeeID"));
//		//主表    总数量
//		allocationMap.put("TotalQuantity", totalCount);
//		//主表    总金额
//		allocationMap.put("TotalAmount", totalMoney);
//		//主表    审核区分
//		allocationMap.put("VerifiedFlag", tempMap.get("VerifiedFlag"));
//		//主表    业务类型
//		allocationMap.put("TradeType", tempMap.get("TradeType"));
//		//主表     调拨区分
//		allocationMap.put("AllocationFlag", tempMap.get("AllocationFlag"));
//		//主表    关联单号
//		allocationMap.put("RelevanceNo", tempMap.get("RelevanceNo"));
//		//主表    TODO：物流ID
//		allocationMap.put("BIN_LogisticInfoID", 0);
//		//主表    收发货理由
//		allocationMap.put("Reason", tempMap.get("ReasonALl"));
//		//主表    调拨日期
//		allocationMap.put("AllocationDate", tempMap.get("AllocationDate"));
//		allocationMap.put("TradeStatus", tempMap.get("TradeStatus"));
//		//主表    有效区分
//		allocationMap.put("ValidFlag", "1");	
//		
//		allocationMap.put("CreatedBy", tempMap.get("CreatedBy"));
//		allocationMap.put("CreatePGM", tempMap.get("CreatePGM"));
//		allocationMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
//		allocationMap.put("UpdatePGM", tempMap.get("UpdatePGM"));		
//		//insert 【促销产品调拨业务单据表】
//		int bIN_PromotionAllocationID = binOLSSCM03_BL.insertAllocationMain(allocationMap);
//		
//		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
//		for(int i=0;i<argList.size();i++){
//			Map<String,Object> map = argList.get(i);
//			//促销产品发货ID
//			map.put("BIN_PromotionAllocationID", bIN_PromotionAllocationID);
//			//明细连番
//			map.put("DetailNo", i+1);
//			detailList.add(map);
//		}
//		//insert 【促销产品调拨业务单据明细表】
//		binOLSSCM03_BL.insertAllocationDetail(detailList);
//		
//		String statusFlag = String.valueOf(tempMap.get("TradeStatus"));
//		String verifiedFlag = String.valueOf(tempMap.get("VerifiedFlag"));
//		
//		if(verifiedFlag.equals(CherryConstants.AUDIT_FLAG_AGREE)&&statusFlag.equals(CherryConstants.PROM_ALLOCATION_RES)){
//			//出库方 操作入出库表，库存表
//			int stockOutID = binOLSSCM01_BL.insertStockInOutByAllocationIDOut(bIN_PromotionAllocationID,userInfo);
//			binOLSSCM01_BL.updatePromotionStockByInOutID(stockOutID, "BINOLSSPRM20",userInfo.getBIN_UserID());
//			
//			//入库方 操作入出库表，库存表
//			int stockInID = binOLSSCM01_BL.insertStockInOutByAllocationIDIn(Integer.parseInt(oldBIN_PromotionAllocationID),userInfo);
//			binOLSSCM01_BL.updatePromotionStockByInOutID(stockOutID, "BINOLSSPRM20",userInfo.getBIN_UserID());
//		}
//		return bIN_PromotionAllocationID;
//	}
}
