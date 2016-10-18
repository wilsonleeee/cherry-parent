/*	
 * @(#)BINOLSSPRM51_BL.java     1.0 2010/10/27		
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.prm.form.BINOLSSPRM51_Form;
import com.cherry.ss.prm.service.BINOLSSPRM51_Service;

/**
 * 收货
 * @author dingyc
 *
 */
public class BINOLSSPRM51_BL {
//	
//	@Resource
//	private BINOLSSPRM51_Service binOLSSPRM51_Service;
//	
//	@Resource
//	private BINOLCM03_BL binOLCM03_BL;
//	
//	@Resource
//	private BINOLSSCM01_BL binOLSSCM01_BL;
//	
//	@Resource
//	private BINOLSSCM04_BL binOLSSCM04_BL;
//	
//	/**
//	 * 取得发货单概要信息列表
//	 * @param organizationId
//	 * @param userInfo
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List getDeliverDataList(String organizationId,UserInfo userInfo ){
//		List retList = new ArrayList();
//
//			//使用工作流
//			//任务接收者
//			List list = new ArrayList();
//			list.add("organizationID"+organizationId);
//			//list.add("positionID"+userInfo.getCurrentPositionID());
//			//list.add("userID"+organizationId);
//			
//			JbpmContext context = JbpmConfiguration.getInstance().createJbpmContext();
//			//接收到该部门所有的任务，过滤出发货任务
//			List<TaskInstance> taskList = context.getGroupTaskList(list);
//			Map<String ,Object> praMap;
//			for(int i=0;i<taskList.size();i++){
//				TaskInstance task = taskList.get(i);
//				if(CherryConstants.JBPM_PROM_SEND_REC.equals(String.valueOf(task.getVariable(CherryConstants.JBPM_BUSSINESS_CODE)))){
//					//如果任务是收货
//					String deliverID = String.valueOf(task.getVariable(CherryConstants.JBPM_MAIN_ID));
//					praMap = new HashMap<String ,Object>();
//					praMap.put("language", userInfo.getLanguage());
//					praMap.put("BIN_PromotionDeliverID", deliverID);
//					praMap.put("TaskInstanceID", task.getId());
//					//审核区分   审核通过
//					praMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//					//入出库区分   已发货
//					praMap.put("StockInFlag", CherryConstants.PROM_DELIVER_SENT);
//					//业务类型   发货
//					praMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
//					retList.addAll(binOLSSPRM51_Service.getDeliverDataListJbpm(praMap));
//				}
//			}
//			context.close();
//		return retList;
//	}
//	
//	/**
//	 * 取得多条发货单的明细信息
//	 * @param deliverID
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List getDeliverDataDetailList(String[] deliverIDArr,UserInfo userInfo){
//		List retList = new ArrayList();
//		Map<String ,Object> praMap; 
//		for(int i=0;i<deliverIDArr.length;i++){
//			praMap = new HashMap<String ,Object>();
//			//参数格式：发货单ID + 任务ID
//			String[] tempArr = deliverIDArr[i].split("_");
//			praMap.put("BIN_PromotionDeliverID", tempArr[0]);
//			praMap.put("TaskInstanceID", tempArr[1]);
//			praMap.put("language", userInfo.getLanguage());
//			retList.addAll(binOLSSPRM51_Service.getDeliverDataDetailList(praMap));
//		}		
//		return retList;
//	}
//
//	/**
//	 * 进行收货处理
//	 * @param form
//	 * @throws Exception 
//	 */
//	@SuppressWarnings("unchecked")
//	public int tran_receiving(BINOLSSPRM51_Form form,UserInfo userInfo) throws Exception{
//		//收货部门ID
//		String inOrganizationId =  form.getInOrganizationId();		
//		//收货仓库
//		String inDepotId = form.getInDepot();
//		
//		//组织ID
//		String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
//		//品牌ID
//		String brandInfoId = userInfo.getCurrentBrandInfoID();	
//		//userInfo.setCurrentOrganizationInfoID(organizationInfoId);
//		userInfo.setCurrentBrandInfoID(brandInfoId);		
//		userInfo.setCurrentUnit("BINOLSSPRM51");
//		//收货日期
//		String deliverDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
//		//发货部门ID
//		String[] outOrganizationIdArr = form.getOutOrganizationIDArr();
//		//发货单据ID
//		String[] promotionDeliverIDArr =form.getPromotionDeliverIDArr();
//		//发货单据号
//		String[] deliverNoArrIF = form.getDeliverNoArrIF();
//		//产品厂商ID
//		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
//		//促销品单价
//		String[] priceUnitArr = form.getPriceUnitArr();			
//		//收货数量
//		String[] quantityuArr =form.getQuantityuArr();		
//		//原因 备注
//		String[] reasonArr = form.getReasonArr();		
//		//更新时间
//		String[] outUpdateTimeArr=form.getOutUpdateTimeArr(); 
//		//更新次数
//		String[] outModifyCountArr = form.getOutModifyCountArr();
//		
//		//任务实例ID
//		String[] taskInstanceIDArr = form.getTaskInstanceIDArr();
//		
//		//一次发货操作的总数量（始终为正）
//		int totalQuantity =0;
//		//总金额
//		double totalAmount =0;
//		
//		//计算一次操作的总金额,并进行拆分
//		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
//		HashMap<String,Object> tempMap=null;
//		String splitKey="";
//		int lengthTotal = promotionDeliverIDArr.length;
//		for(int i=0;i<lengthTotal;i++){
//			int tempCount = CherryUtil.string2int(quantityuArr[i]);
//			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
//			totalAmount += money;
//			totalQuantity += tempCount;
//			
//			//进行表单拆分
//			tempMap = new HashMap<String,Object>();	
//			//明细       产品厂商编码
//			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
//			//明细       发货数量
//			tempMap.put("Quantity", tempCount);
//			//明细       价格
//			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
//			//明细       TODO：包装类型ID
//			tempMap.put("BIN_ProductVendorPackageID", 0);
//			//明细      收货仓库ID
//			tempMap.put("BIN_InventoryInfoID", inDepotId);
//			//明细      TODO：收货逻辑仓库ID
//			tempMap.put("BIN_LogicInventoryInfoID", 0);
//			//明细      TODO：收货仓库库位ID
//			tempMap.put("BIN_StorageLocationInfoID",0);
//			//明细     有效区分
//			tempMap.put("ValidFlag", "1");
//			//明细     理由
//			tempMap.put("Reason", reasonArr[i]);
//			//明细     共通字段
//			tempMap.put("CreatedBy", userInfo.getLoginName());
//			tempMap.put("CreatePGM", "BINOLSSPRM51");
//			tempMap.put("UpdatedBy", userInfo.getLoginName());
//			tempMap.put("UpdatePGM", "BINOLSSPRM51");
//			
//			//主表       接口收发货单号
//			tempMap.put("DeliverReceiveNoIF", null);
//			//主表       组织ID
//			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
//			//主表       品牌ID
//			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());	
//			//主表      发货部门
//			tempMap.put("BIN_OrganizationID", outOrganizationIdArr[i]);
//			//主表     收货部门
//			tempMap.put("BIN_OrganizationIDReceive", inOrganizationId);
//			//主表     制单员工
//			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//			//主表     入库区分
//			tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_RECEIVE);
//			//主表     业务类型
//			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_RECEIVE);
//			//主表     关联单号
//			tempMap.put("RelevanceNo", deliverNoArrIF[i]);	
//			//主表     收货日期
//			tempMap.put("DeliverDate", deliverDate);
//			
//			//主表    发货单的更新时间（排他用）
//			tempMap.put("outUpdateTime", outUpdateTimeArr[i]);
//			//主表    发货单的更新次数（排他用）
//			tempMap.put("outModifyCount", outModifyCountArr[i]);
//			
//			//主表计算用    一条明细的总金额
//			tempMap.put("Money", money);
//			
//			//工作流用   
//			tempMap.put("TaskInstanceID", taskInstanceIDArr[i]);
//			
//			splitKey = promotionDeliverIDArr[i];
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
//		//insert【促销品库存操作流水表】
//		Map<String, Object> mapInventory = new HashMap<String, Object>();
//		//操作部门
//		mapInventory.put("BIN_OrganizationID", inOrganizationId);
//		//操作员工
//		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
//		//总数量
//		mapInventory.put("TotalQuantity", totalQuantity);
//		//总金额
//		mapInventory.put("TotalAmount", totalAmount);
//		//审核区分 
//		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//		//业务类型  1:仓库发货，2:仓库收货
//		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_RECEIVE);
//		//操作日期
//		mapInventory.put("DeliverDate", deliverDate);
//		//有效区分
//		mapInventory.put("ValidFlag", "1");
//		
//		//数据来源渠道
//		mapInventory.put("DataChannel", 0);
//		//物流ID
//		mapInventory.put("BIN_LogisticInfoID", 0);
//		mapInventory.put("createdBy", userInfo.getLoginName());
//		mapInventory.put("createPGM", "BINOLSSPRM51");
//		mapInventory.put("updatedBy", userInfo.getLoginName());
//		mapInventory.put("updatePGM", "BINOLSSPRM51");
//		
//		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);		
//		
//		Iterator it = splitMap.keySet().iterator();
//		long[] taskInstanceID = new long[splitMap.size()];
//		int index =0;
//		while(it.hasNext()){
//			String key = it.next().toString();
//			List<HashMap<String,Object>> list = splitMap.get(key);
//			
//			HashMap<String,Object> praMap = list.get(0);
//			//更新原发货单据为已收货，带排他处理
//			HashMap<String,Object> updateMap = new HashMap<String,Object> ();
//			updateMap.put("BIN_PromotionDeliverID", key);
//			updateMap.put("outUpdateTime", praMap.get("outUpdateTime"));
//			updateMap.put("outModifyCount", praMap.get("outModifyCount"));
//			updateMap.put("StockInFlag", CherryConstants.PROM_DELIVER_RECEIVE);
//			updateMap.put("UpdatedBy", userInfo.getLoginName());
//			updateMap.put("UpdatePGM", "BINOLSSPRM51");
//			int count = binOLSSCM04_BL.updateDeliverMainHT(updateMap);
//			if(count<1){
//				throw new CherryException("ESS00017",new String[]{praMap.get("RelevanceNo").toString()});
//			}
//			//取得单据号
//			String deliverNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,userInfo.getLoginName(), CherryConstants.BUSINESS_TYPE_DELIVER_RECEIVE);
//			//把收货数据插入到促销产品收发货业务单据表和明细表
//			int maindeliverid = insertPromotionDeliverAll(bIN_PromotionInventoryLogID,deliverNo,list);
//			
//			//将收货数据写入入出库表
//			int stockid = binOLSSCM01_BL.insertStockInOutByDeliverID(maindeliverid,  "BINOLSSPRM51",userInfo.getBIN_UserID());
//			
//			//更新库存
//			binOLSSCM01_BL.updatePromotionStockByInOutID(stockid,  "BINOLSSPRM51",userInfo.getBIN_UserID());
//			
//			//更新原发货单据为已入库,不做排他
//			updateMap = new HashMap<String,Object>();
//			updateMap.put("BIN_PromotionDeliverID", key);
//			updateMap.put("StockInFlag", CherryConstants.PROM_DELIVER_STOCK_IN);
//			updateMap.put("UpdatedBy", userInfo.getLoginName());
//			updateMap.put("UpdatePGM", "BINOLSSPRM51");
//			binOLSSCM04_BL.updateDeliverMainHT(updateMap);
//			
//			//更新收货单据为审核通过，已入库,不做排他
//			updateMap = new HashMap<String,Object>();
//			updateMap.put("BIN_PromotionDeliverID", maindeliverid);
//			updateMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
//			updateMap.put("StockInFlag", CherryConstants.PROM_DELIVER_STOCK_IN);
//			updateMap.put("UpdatedBy", userInfo.getLoginName());
//			updateMap.put("UpdatePGM", "BINOLSSPRM51");
//			binOLSSCM04_BL.updateDeliverMainHT(updateMap);
//
//			taskInstanceID[index]= Long.parseLong(String.valueOf(praMap.get("TaskInstanceID")));
//			index++;
//		}
//		
//			//结束工作流中的任务
//			JbpmContext context = JbpmConfiguration.getInstance().createJbpmContext();
//			for(int i=0;i<taskInstanceID.length;i++){
//				TaskInstance task = context.getTaskInstance(taskInstanceID[i]);
//				task.end();
//			}
//			context.close();
//		
//		return bIN_PromotionInventoryLogID;
//	} 
//	
//	
//	/**
//	 * 插入一单收货数据插入到【促销产品收发货业务单据表】和【促销产品收发货业务单据明细表】
//	 * @param seqnum 库存操作流水号
//	 * @param argList 收货明细数据
//	 */
//	public int insertPromotionDeliverAll(int seqnum,String deliverReceiveNo,List<HashMap<String,Object>> argList){
//		//循环一单里的所有明细，以生成发货主表数据
//		int totalCount =0;
//		double totalMoney = 0.00;
//		for(int i=0;i<argList.size();i++){
//			HashMap<String,Object> map = argList.get(i);
//			totalCount +=  CherryUtil.string2int(map.get("Quantity").toString());
//			totalMoney +=	CherryUtil.string2double(map.get("Money").toString());
//		}
//		HashMap<String,Object> tempMap = argList.get(0);
//		HashMap<String,Object> deliverMap = new HashMap<String,Object>();
//		
//		//促销库存操作流水ID
//		deliverMap.put("BIN_PromotionInventoryLogID", seqnum);
//		
//		//主表       组织信息ID
//		deliverMap.put("BIN_OrganizationInfoID", tempMap.get("BIN_OrganizationInfoID"));	
//		//主表       品牌ID
//		deliverMap.put("BIN_BrandInfoID", tempMap.get("BIN_BrandInfoID"));
//		//收发货单号
//		deliverMap.put("DeliverReceiveNo", deliverReceiveNo);
//		//发货部门
//		deliverMap.put("BIN_OrganizationID", tempMap.get("BIN_OrganizationID"));
//		//收货部门
//		deliverMap.put("BIN_OrganizationIDReceive", tempMap.get("BIN_OrganizationIDReceive"));
//		//制单员工
//		deliverMap.put("BIN_EmployeeID", tempMap.get("BIN_EmployeeID"));
//		//总数量
//		deliverMap.put("TotalQuantity", totalCount);
//		//总金额
//		deliverMap.put("TotalAmount", totalMoney);
//		//审核区分
//		deliverMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
//		//业务类型
//		deliverMap.put("TradeType", tempMap.get("TradeType"));
//		//关联单号
//		deliverMap.put("RelevanceNo", tempMap.get("RelevanceNo"));
//		//关联接口单号
//		deliverMap.put("DeliverReceiveNoIF", deliverReceiveNo);
//		//TODO：物流ID
//		deliverMap.put("BIN_LogisticInfoID", 0);
//		//收发货理由
//		deliverMap.put("Reason", null);
//		//收发货日期
//		deliverMap.put("DeliverDate", tempMap.get("DeliverDate"));
//		//入库区分 表示对方是否已经根据发货单入库
//		deliverMap.put("StockInFlag", tempMap.get("StockInFlag"));
//		//有效区分
//		deliverMap.put("ValidFlag", "1");	
//		
//		deliverMap.put("CreatedBy", tempMap.get("CreatedBy"));
//		deliverMap.put("CreatePGM", tempMap.get("CreatePGM"));
//		deliverMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
//		deliverMap.put("UpdatePGM", tempMap.get("UpdatePGM"));
//		
//		//insert 【促销产品收发货业务单据表】
//		int bIN_PromotionDeliverID = binOLSSCM04_BL.insertPromotionDeliverMain(deliverMap);
//		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
//		for(int i=0;i<argList.size();i++){
//			Map<String,Object> map = argList.get(i);
//			//促销产品发货ID
//			map.put("BIN_PromotionDeliverID", bIN_PromotionDeliverID);
//			//明细连番
//			map.put("DetailNo", i+1);
//			detailList.add(map);
//		}
//		//insert 【促销产品收发货业务单据明细表】
//		binOLSSCM04_BL.insertPromotionDeliverDetail(detailList);
//		return bIN_PromotionDeliverID;
//	}
}
