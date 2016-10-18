/*	
 * @(#)BINOLSSPRM17_BL.java     1.0 2010/10/27		
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
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.ss.prm.form.BINOLSSPRM17_Form;
@SuppressWarnings("unchecked")
public class BINOLSSPRM17_BL {
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	@Resource
	private BINOLSSCM04_BL binOLSSCM04_BL;
	
	@Resource
	private BINOLSSCM05_BL binOLSSCM05_BL;
	
	@Resource
	private BINOLSSCM00_BL binOLSSCM00_BL;
	
	@Resource
	private BINOLCM01_BL binolcm01BL;
	
	/**
	 * 进行发货处理
	 * @param form
	 * @throws Exception 
	 * @throws Exception 
	 */
	public int tran_deliver(BINOLSSPRM17_Form form,UserInfo userInfo) throws Exception{
		
		//发货部门ID
		String outOrganizationId =  form.getOutOrganizationId();
		
		String outDepotId = form.getOutDepot();
		
	    //发货逻辑仓库
        int outLoginDepotId = CherryUtil.obj2int(form.getOutLoginDepotId());
        
		//完善用户信息
		binolcm01BL.completeUserInfo(userInfo, outOrganizationId,"BINOLSSPRM17");
		//产品厂商编码
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();		
		//促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//收货部门 
		String[] inOrganizationIDArr =form.getInOrganizationIDArr();		
		//发货数量
		String[] quantityuArr =form.getQuantityuArr();		
		//发货原因
		String[] reasonArr = form.getReasonArr();

		//一次发货操作的总数量（始终为正）
		int totalQuantity =0;
		//总金额
		double totalAmount =0;
		
		//审核区分，如果不需要审核，则直接置为1 已审核通过
		//查询发货单的审核者		
//		String[] auditActors = null;
		String verifiedFlag=CherryConstants.AUDIT_FLAG_SUBMIT;
		String stockInFlag = CherryConstants.PROM_DELIVER_UNSEND;
		
		//计算一次操作的总金额,并进行拆分
		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
		HashMap<String,Object> tempMap=null;
		String splitKey="";
		int lengthTotal = promotionProductVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int tempCount = CherryUtil.string2int(quantityuArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
			totalAmount += money;
			totalQuantity += tempCount;
			
			//进行表单拆分
			tempMap = new HashMap<String,Object>();		
			//明细       产品厂商编码
			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
			//明细       发货数量
			tempMap.put("Quantity", tempCount);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      发货仓库ID
			tempMap.put("BIN_InventoryInfoID", outDepotId);
			//明细      TODO：发货逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", outLoginDepotId);
			//明细      TODO：发货仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     理由
			tempMap.put("Reason", reasonArr[i]);
			//明细     共通字段
			tempMap.put("CreatedBy", userInfo.getBIN_UserID());
			tempMap.put("CreatePGM", "BINOLSSPRM17");
			tempMap.put("UpdatedBy", userInfo.getBIN_UserID());
			tempMap.put("UpdatePGM", "BINOLSSPRM17");
			
			//主表       接口收发货单号
			tempMap.put("DeliverReceiveNoIF", null);	
			//主表       组织ID
			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
			//主表       品牌ID
			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());	
			//主表      发货部门
			tempMap.put("BIN_OrganizationID", outOrganizationId);
			//主表     收货部门
			tempMap.put("BIN_OrganizationIDReceive", inOrganizationIDArr[i]);
			//主表     制单员工
			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
			//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
			tempMap.put("StockInFlag", stockInFlag);			
			//主表     业务类型
			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
			//主表     关联单号
			tempMap.put("RelevantNo", null);	
			//主表计算用    一条明细的总金额
			tempMap.put("Money", money);
			//主表 预计到货日期
			tempMap.put("PlanArriveDate", form.getPlanArriveDate());
			
			//主表    原因
			tempMap.put("ReasonAll", form.getReasonAll());
			
			splitKey = inOrganizationIDArr[i];
			if(splitMap.containsKey(splitKey)){
				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
				tempList.add(tempMap);
			}else{
				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
				tempList.add(tempMap);
				splitMap.put(splitKey, tempList);
			}
		}
		
		int bIN_PromotionInventoryLogID = 0;
		//binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);

		//(已经被拆成每个收货地址一单,会带着多条明细)
		Iterator it = splitMap.keySet().iterator();
		int deliverMainID = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			List<HashMap<String, Object>> list = splitMap.get(key);
			// 取得单据号
			String deliverNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(), userInfo
					.getBIN_BrandInfoID(), userInfo.getLoginName(), "1");
			// 插入【 促销产品收发货主表】
			deliverMainID = this.insertPromotionDeliverMain(bIN_PromotionInventoryLogID, deliverNo, verifiedFlag,
					list);
			
			if(deliverMainID == 0){
				//抛出自定义异常：操作失败！
            	throw new CherryException("ISS00005");
			}

			// 插入【 促销产品收发货明细表】
			this.insertPromotionDeliverDetail(deliverMainID, list);
			HashMap<String, Object> map0 = list.get(0);
			String receiveOrganizationID = String.valueOf(map0.get("BIN_OrganizationIDReceive"));

			// 如果收货部门是柜台，则要发送MQ消息
			boolean isCounter = binOLSSCM05_BL.checkOrganizationType(receiveOrganizationID);

			// 准备参数，开始工作流
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
			pramMap.put(CherryConstants.OS_MAINKEY_BILLID, deliverMainID);
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
			pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
			pramMap.put("CurrentUnit", "BINOLSSPRM17");
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("SendToCounter", isCounter ? "YES" : "NO");
			pramMap.put("UserInfo", userInfo);
			pramMap.put("BIN_OrganizationIDReceive", receiveOrganizationID);
			binOLSSCM00_BL.StartOSWorkFlow(pramMap);
		}
		
		return deliverMainID;
	}
	
	
	
	
	/**
	 * 保存发货单
	 * @param form
	 * @throws Exception 
	 */
	public int tran_saveDeliver(BINOLSSPRM17_Form form,UserInfo userInfo) throws Exception{
		//发货部门ID
		String outOrganizationId =  form.getOutOrganizationId();
		//发货逻辑仓库
		String outLoginDepotId = form.getOutLoginDepotId();
		
		String outDepotId = form.getOutDepot();		
		//完善用户信息
		binolcm01BL.completeUserInfo(userInfo, outOrganizationId,"BINOLSSPRM17");
		//产品厂商编码
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();		
		//促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//收货部门 
		String[] inOrganizationIDArr =form.getInOrganizationIDArr();		
		//发货数量
		String[] quantityuArr =form.getQuantityuArr();		
		//发货原因
		String[] reasonArr = form.getReasonArr();

		//一次发货操作的总数量（始终为正）
		int totalQuantity =0;
		//总金额
		double totalAmount =0;
		
		//计算一次操作的总金额,并进行拆分
		Map<String,List<HashMap<String,Object>>> splitMap = new HashMap<String,List<HashMap<String,Object>>>();
		HashMap<String,Object> tempMap=null;
		String splitKey="";
		int lengthTotal = promotionProductVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int tempCount = CherryUtil.string2int(quantityuArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
			totalAmount += money;
			totalQuantity += tempCount;
			
			//进行表单拆分
			tempMap = new HashMap<String,Object>();		
			//明细       产品厂商编码
			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);			
			//明细       发货数量
			tempMap.put("Quantity", tempCount);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      发货仓库ID
			tempMap.put("BIN_InventoryInfoID", outDepotId);
			//明细      TODO：发货逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", outLoginDepotId);
			//明细      TODO：发货仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     理由
			tempMap.put("Reason", reasonArr[i]);
			//明细     共通字段
			tempMap.put("CreatedBy", userInfo.getLoginName());
			tempMap.put("CreatePGM", "BINOLSSPRM17");
			tempMap.put("UpdatedBy", userInfo.getLoginName());
			tempMap.put("UpdatePGM", "BINOLSSPRM17");
			
			//主表       接口收发货单号
			tempMap.put("DeliverReceiveNoIF", null);		
			//主表       组织ID
			tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
			//主表       品牌ID
			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			//主表      发货部门
			tempMap.put("BIN_OrganizationID", outOrganizationId);
			//主表     收货部门
			tempMap.put("BIN_OrganizationIDReceive", inOrganizationIDArr[i]);
			//主表     制单员工
			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
			//主表     入库区分 1：未发货 2：已发货 3：已收货 4：已入库			
			tempMap.put("StockInFlag", CherryConstants.PROM_DELIVER_UNSEND);			
			//主表     业务类型
			tempMap.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
			//主表     关联单号
			tempMap.put("RelevantNo", null);	
			//主表计算用    一条明细的总金额
			tempMap.put("Money", money);
            //主表 预计到货日期
            tempMap.put("PlanArriveDate", form.getPlanArriveDate());
			
			//主表    原因
			tempMap.put("ReasonAll", form.getReasonAll());
			
			splitKey = inOrganizationIDArr[i];
			if(splitMap.containsKey(splitKey)){
				List<HashMap<String,Object>> tempList = splitMap.get(splitKey);
				tempList.add(tempMap);
			}else{
				List<HashMap<String,Object>> tempList = new ArrayList<HashMap<String,Object>>();
				tempList.add(tempMap);
				splitMap.put(splitKey, tempList);
			}
		}
		
		//插入  促销品库存操作流水表
		Map<String, Object> mapInventory = new HashMap<String, Object>();
		//操作部门
		mapInventory.put("BIN_OrganizationID", outOrganizationId);
		//操作员工
		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//总数量
		mapInventory.put("TotalQuantity", totalQuantity);
		//总金额
		mapInventory.put("TotalAmount", totalAmount);
		//审核区分  
		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		//业务类型 
		//1:仓库发货，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
		mapInventory.put("TradeType", CherryConstants.BUSINESS_TYPE_DELIVER_SEND);
		//有效区分
		mapInventory.put("ValidFlag", "1");
		
		//数据来源渠道
		mapInventory.put("DataChannel", 0);
		//物流ID
		mapInventory.put("BIN_LogisticInfoID", 0);
		mapInventory.put("createdBy", userInfo.getLoginName());
		mapInventory.put("createPGM", "BINOLSSPRM17");
		mapInventory.put("updatedBy", userInfo.getLoginName());
		mapInventory.put("updatePGM", "BINOLSSPRM17");
		
		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
		
		//审核区分  未提交审核
		String verifiedFlag =CherryConstants.AUDIT_FLAG_UNSUBMIT;
		
		//(已经被拆成每个收货地址一单,会带着多条明细)
		Iterator it = splitMap.keySet().iterator();
		
		while(it.hasNext()){
			String key = it.next().toString();
			List<HashMap<String,Object>> list = splitMap.get(key);
			//取得单据号
			String deliverNo = binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID(),userInfo.getLoginName(), "1");
			//插入【 促销产品收发货主表】
			int deliverMainID = this.insertPromotionDeliverMain(bIN_PromotionInventoryLogID, deliverNo,verifiedFlag, list);
			
			//插入【 促销产品收发货明细表】
			this.insertPromotionDeliverDetail(deliverMainID, list);			
		}
		return bIN_PromotionInventoryLogID;
	}
	
	/**
	 * 将发货数据插入发货单主表
	 * @param id
	 * @param argList
	 * @return
	 */
	private int insertPromotionDeliverMain(int seqnum,String deliverReceiveNo,String verifiedFlag,List<HashMap<String,Object>> argList){
		//循环一单里的所有明细，以生成发货主表数据
		int totalCount =0;
		double totalMoney = 0.00;
		for(int i=0;i<argList.size();i++){
			HashMap<String,Object> map = argList.get(i);
			map.put("DeliverReceiveNoIF", deliverReceiveNo);
			totalCount +=  CherryUtil.string2int(map.get("Quantity").toString());
			totalMoney +=	CherryUtil.string2double(map.get("Money").toString());
		}
		HashMap<String,Object> tempMap = argList.get(0);
		HashMap<String,Object> deliverMap = new HashMap<String,Object>();
		
		//促销库存操作流水ID
		deliverMap.put("BIN_PromotionInventoryLogID", seqnum);
		//主表       组织信息ID
		deliverMap.put("BIN_OrganizationInfoID", tempMap.get("BIN_OrganizationInfoID"));	
		//主表       品牌ID
		deliverMap.put("BIN_BrandInfoID", tempMap.get("BIN_BrandInfoID"));
		//收发货单号
		deliverMap.put("DeliverReceiveNo", deliverReceiveNo);
		//发货部门
		deliverMap.put("BIN_OrganizationID", tempMap.get("BIN_OrganizationID"));
		//收货部门
		deliverMap.put("BIN_OrganizationIDReceive", tempMap.get("BIN_OrganizationIDReceive"));
		//制单员工
		deliverMap.put("BIN_EmployeeID", tempMap.get("BIN_EmployeeID"));
		//总数量
		deliverMap.put("TotalQuantity", totalCount);
		//总金额
		deliverMap.put("TotalAmount", totalMoney);
		//审核区分
		deliverMap.put("VerifiedFlag", verifiedFlag);
		//业务类型
		deliverMap.put("TradeType", tempMap.get("TradeType"));
		//关联单号
		deliverMap.put("RelevanceNo", tempMap.get("RelevanceNo"));
		//关联接口单号
		deliverMap.put("DeliverReceiveNoIF", tempMap.get("DeliverReceiveNoIF"));
		//TODO：物流ID
		deliverMap.put("BIN_LogisticInfoID", 0);
		//收发货理由
		deliverMap.put("Reason", tempMap.get("ReasonAll"));
		//收发货日期
		deliverMap.put("DeliverDate", tempMap.get("DeliverDate"));
		//预计到货日期
		deliverMap.put("PlanArriveDate", tempMap.get("PlanArriveDate"));
		//入库区分 表示对方是否已经根据发货单入库
		deliverMap.put("StockInFlag", tempMap.get("StockInFlag"));
		//有效区分
		deliverMap.put("ValidFlag", "1");	
		//工作流实例ID
		deliverMap.put("WorkFlowID", null);
		
		deliverMap.put("CreatedBy", tempMap.get("CreatedBy"));
		deliverMap.put("CreatePGM", tempMap.get("CreatePGM"));
		deliverMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
		deliverMap.put("UpdatePGM", tempMap.get("UpdatePGM"));
		
		//insert 【促销产品收发货业务单据表】
		int bIN_PromotionDeliverID = binOLSSCM04_BL.insertPromotionDeliverMain(deliverMap);
		return bIN_PromotionDeliverID;
	}
	
	/**将发货数据插入发货单明细表
	 * @param id
	 * @param argList
	 * @return
	 */
	private void insertPromotionDeliverDetail(int id,List<HashMap<String,Object>> argList){
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<argList.size();i++){
			Map<String,Object> map = argList.get(i);
			//促销产品发货ID
			map.put("BIN_PromotionDeliverID", id);
			//明细连番
			map.put("DetailNo", i+1);
			detailList.add(map);
		}
		//insert 【促销产品收发货业务单据明细表】
		binOLSSCM04_BL.insertPromotionDeliverDetail(detailList);
	}
}
