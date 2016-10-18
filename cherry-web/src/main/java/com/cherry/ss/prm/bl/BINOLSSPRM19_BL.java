/*	
 * @(#)BINOLSSPRM19_BL.java     1.0 2010/12/09		
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.form.BINOLSSPRM19_Form;

/**
 * @author dingyc
 * 
 */
public class BINOLSSPRM19_BL {

	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	@Resource
	private BINOLSSCM00_BL binolsscm00_bl;

	@Resource
	private BINOLSSCM01_BL binOLSSCM01_BL;

	@Resource
	private BINOLSSCM03_BL binOLSSCM03_BL;

	// @Resource
	// private PromotionAllocationJbpm promotionAllocationJbpm;

	/**
	 * 进行调拨处理
	 * 
	 * @param form
	 * @throws Exception
	 */
	public int tran_sendAllocation(BINOLSSPRM19_Form form, UserInfo userInfo)
			throws Exception {

		// 调入部门ID
		String inOrganizationId = form.getInOrganizationId();
		// 调入仓库
		String inDepotId = form.getInDepotId();
		// 调入逻辑仓库
		String inLoginDepotId = form.getInLoginDepotId();
		// 申请调拨日期
		String allocationDate = CherryUtil.getSysDateTime("yyyy-MM-dd");
		// 调拨理由（总）
		String reasonALl = form.getReasonAll();
		// 产品厂商编码
		String[] promotionProductVendorIDArr = form
				.getPromotionProductVendorIDArr();
		// 促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();
		// 调出部门
		String outOrganizationID = form.getOutOrganizationId();
		// 调拨数量
		String[] quantityuArr = form.getQuantityuArr();
		// 调拨原因
		String[] reasonArr = form.getReasonArr();

		// 一次调拨操作的总数量（始终为正）
		int totalQuantity = 0;
		// 总金额
		double totalAmount = 0;

		// 计算一次操作的总金额,并进行拆分
		Map<String, List<HashMap<String, Object>>> splitMap = new HashMap<String, List<HashMap<String, Object>>>();
		HashMap<String, Object> tempMap = null;
		String splitKey = "";
		int lengthTotal = promotionProductVendorIDArr.length;
		for (int i = 0; i < lengthTotal; i++) {
			int tempCount = CherryUtil.string2int(quantityuArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])
					* tempCount;
			totalAmount += money;
			totalQuantity += tempCount;

			// 进行表单拆分
			tempMap = new HashMap<String, Object>();
			// 明细 促销产品厂商ID
			tempMap.put("BIN_PromotionProductVendorID",
					promotionProductVendorIDArr[i]);
			// 明细 调拨数量
			tempMap.put("Quantity", tempCount);
			// 明细 价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			// 明细 TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			// 明细 调拨仓库ID
			tempMap.put("BIN_InventoryInfoID", inDepotId);
			// 明细 TODO：调拨逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", inLoginDepotId);
			// 明细 TODO：调拨仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID", 0);
			// 明细 有效区分
			tempMap.put("ValidFlag", "1");
			// 明细 理由
			tempMap.put("Reason", reasonArr[i]);
			// 明细 共通字段
			tempMap.put("CreatedBy", userInfo.getLoginName());
			tempMap.put("CreatePGM", "BINOLSSPRM19");
			tempMap.put("UpdatedBy", userInfo.getLoginName());
			tempMap.put("UpdatePGM", "BINOLSSPRM19");

			// 主表 接口调拨单号
			tempMap.put("AllocationNoIF", null);
			// 主表 组织ID
			tempMap.put("BIN_OrganizationInfoID",
					userInfo.getBIN_OrganizationInfoID());
			// 主表 品牌ID
			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			// 主表 申请调拨部门
			tempMap.put("BIN_OrganizationID", inOrganizationId);
			// 主表 接受调拨部门
			tempMap.put("BIN_OrganizationIDAccept", outOrganizationID);
			// 主表 制单员工
			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
			// 主表 审核区分
			tempMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_SUBMIT);
			// 主表 业务类型
			tempMap.put("TradeType",
					CherryConstants.BUSINESS_TYPE_ALLOCATION_REQUEST);
			// 主表 调拨区分
			tempMap.put("AllocationFlag", "I");
			// 主表 关联单号
			tempMap.put("RelevantNo", null);
			// 主表 调拨理由
			tempMap.put("ReasonALl", reasonALl);
			// 主表 调拨日期
			tempMap.put("AllocationDate", allocationDate);
			tempMap.put("TradeStatus", CherryConstants.BILLTYPE_PRM_BG_UNRES);
			// 主表计算用 一条明细的总金额
			tempMap.put("Money", money);
			// 主表 工作流实例ID
			tempMap.put("WorkFlowID", null);

			splitKey = outOrganizationID;
			if (splitMap.containsKey(splitKey)) {
				List<HashMap<String, Object>> tempList = splitMap.get(splitKey);
				tempList.add(tempMap);
			} else {
				List<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
				tempList.add(tempMap);
				splitMap.put(splitKey, tempList);
			}
		}
		int ret = 0;
		// 插入 促销产品调拨表(已经被拆成每个收货地址一单,会带着多条明细)
		for (List<HashMap<String, Object>> list : splitMap.values()) {
			// 取得单据号
			String allocationNo = binOLCM03_BL
					.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),
							userInfo.getBIN_BrandInfoID(),
							userInfo.getLoginName(), "5");
			ret = insertAllocatonAllData(0, allocationNo, list, userInfo);

			if (ret == 0) {
				// 抛出自定义异常：操作失败！
				throw new CherryException("ISS00005");
			}

			// 准备参数，开始工作流
			Map<String, Object> pramMap = new HashMap<String, Object>();
			pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE,
					CherryConstants.OS_BILLTYPE_BG);
			pramMap.put(CherryConstants.OS_MAINKEY_BILLID, ret);
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER,
					userInfo.getBIN_UserID());
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION,
					userInfo.getBIN_PositionCategoryID());
			pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART,
					userInfo.getBIN_OrganizationID());
			pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE,
					userInfo.getBIN_EmployeeID());
			pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			pramMap.put("UserInfo", userInfo);
			pramMap.put("BIN_OrganizationIDAccept",
					list.get(0).get("BIN_OrganizationIDAccept"));
			binolsscm00_bl.StartOSWorkFlow(pramMap);
		}
		return ret;
	}

	/**
	 * 保存调拨单，不申请
	 * 
	 * @param form
	 * @throws Exception
	 */
	public int tran_saveAllocation(BINOLSSPRM19_Form form, UserInfo userInfo)
			throws Exception {

		// 调入部门ID
		String inOrganizationId = form.getInOrganizationId();
		// 调入仓库
		String inDepotId = form.getInDepotId();
		// 调入逻辑仓库
		String inLoginDepotId = form.getInLoginDepotId();
		// 申请调拨日期
		String allocationDate = CherryUtil.getSysDateTime("yyyy-MM-dd");
		// 调拨理由（总）
		String reasonALl = form.getReasonAll();
		// 产品厂商编码
		String[] promotionProductVendorIDArr = form
				.getPromotionProductVendorIDArr();
		// 促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();
		// 调出部门
		String outOrganizationID = form.getOutOrganizationId();
		// 调拨数量
		String[] quantityuArr = form.getQuantityuArr();
		// 调拨原因
		String[] reasonArr = form.getReasonArr();

		// 一次调拨操作的总数量（始终为正）
		int totalQuantity = 0;
		// 总金额
		double totalAmount = 0;

		// 计算一次操作的总金额,并进行拆分
		Map<String, List<HashMap<String, Object>>> splitMap = new HashMap<String, List<HashMap<String, Object>>>();
		HashMap<String, Object> tempMap = null;
		String splitKey = "";
		int lengthTotal = promotionProductVendorIDArr.length;
		for (int i = 0; i < lengthTotal; i++) {
			int tempCount = CherryUtil.string2int(quantityuArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])
					* tempCount;
			totalAmount += money;
			totalQuantity += tempCount;

			// 进行表单拆分
			tempMap = new HashMap<String, Object>();
			// 明细 促销产品厂商ID
			tempMap.put("BIN_PromotionProductVendorID",
					promotionProductVendorIDArr[i]);
			// 明细 调拨数量
			tempMap.put("Quantity", tempCount);
			// 明细 价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			// 明细 TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			// 明细 调拨仓库ID
			tempMap.put("BIN_InventoryInfoID", inDepotId);
			// 明细 TODO：调拨逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", inLoginDepotId);
			// 明细 TODO：调拨仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID", 0);
			// 明细 有效区分
			tempMap.put("ValidFlag", "1");
			// 明细 理由
			tempMap.put("Reason", reasonArr[i]);
			// 明细 共通字段
			tempMap.put("CreatedBy", userInfo.getLoginName());
			tempMap.put("CreatePGM", "BINOLSSPRM19");
			tempMap.put("UpdatedBy", userInfo.getLoginName());
			tempMap.put("UpdatePGM", "BINOLSSPRM19");

			// 主表 接口调拨单号
			tempMap.put("AllocationNoIF", null);
			// 主表 组织ID
			tempMap.put("BIN_OrganizationInfoID",
					userInfo.getBIN_OrganizationInfoID());
			// 主表 品牌ID
			tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			// 主表 申请调拨部门
			tempMap.put("BIN_OrganizationID", inOrganizationId);
			// 主表 接受调拨部门
			tempMap.put("BIN_OrganizationIDAccept", outOrganizationID);
			// 主表 制单员工
			tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
			// 主表 审核区分
			tempMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
			// 主表 业务类型
			tempMap.put("TradeType",
					CherryConstants.BUSINESS_TYPE_ALLOCATION_REQUEST);
			// 主表 调拨区分
			tempMap.put("AllocationFlag", "I");
			// 主表 关联单号
			tempMap.put("RelevantNo", null);
			// 主表 调拨理由
			tempMap.put("ReasonALl", reasonALl);
			// 主表 调拨日期
			tempMap.put("AllocationDate", allocationDate);
			tempMap.put("TradeStatus", CherryConstants.BILLTYPE_PRM_BG_UNRES);
			// 主表计算用 一条明细的总金额
			tempMap.put("Money", money);

			splitKey = outOrganizationID;
			if (splitMap.containsKey(splitKey)) {
				List<HashMap<String, Object>> tempList = splitMap.get(splitKey);
				tempList.add(tempMap);
			} else {
				List<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
				tempList.add(tempMap);
				splitMap.put(splitKey, tempList);
			}
		}

		// 插入 促销品库存操作流水表
		Map<String, Object> mapInventory = new HashMap<String, Object>();
		// 操作部门
		mapInventory.put("BIN_OrganizationID", inOrganizationId);
		// 操作员工
		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		// 总数量
		mapInventory.put("TotalQuantity", totalQuantity);
		// 总金额
		mapInventory.put("TotalAmount", totalAmount);
		// 审核区分
		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		// 业务类型
		// 1:仓库调拨，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
		// 7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
		mapInventory.put("TradeType",
				CherryConstants.BUSINESS_TYPE_ALLOCATION_REQUEST);
		// 操作日期
		mapInventory.put("DeliverDate", allocationDate);
		// 有效区分
		mapInventory.put("ValidFlag", "1");

		// 数据来源渠道
		mapInventory.put("DataChannel", 0);
		// 物流ID
		mapInventory.put("BIN_LogisticInfoID", 0);
		mapInventory.put("createdBy", userInfo.getLoginName());
		mapInventory.put("createPGM", "BINOLSSPRM19");
		mapInventory.put("updatedBy", userInfo.getLoginName());
		mapInventory.put("updatePGM", "BINOLSSPRM19");

		int bIN_PromotionInventoryLogID = binOLSSCM01_BL
				.insertPromotionInventoryLog(mapInventory);

		// 插入 促销产品调拨表(已经被拆成每个收货地址一单,会带着多条明细)

		for (List<HashMap<String, Object>> list : splitMap.values()) {
			// 取得单据号
			String allocationNo = binOLCM03_BL
					.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),
							userInfo.getBIN_BrandInfoID(),
							userInfo.getLoginName(), "5");
			insertAllocatonAllData(bIN_PromotionInventoryLogID, allocationNo,
					list, userInfo);
		}
		// Iterator<String> it = splitMap.keySet().iterator();
		// while(it.hasNext()){
		// String key = it.next();
		// List<HashMap<String,Object>> list = splitMap.get(key);
		// //取得单据号
		// String allocationNo =
		// binOLCM03_BL.getTicketNumber(userInfo.getBIN_OrganizationInfoID(),userInfo.getBIN_BrandInfoID(),userInfo.getLoginName(),
		// "5");
		// insertAllocatonAllData(bIN_PromotionInventoryLogID,allocationNo,list,userInfo);
		// }
		return bIN_PromotionInventoryLogID;
	}

	/**
	 * 插入一单调拨申请数据到【促销产品调拨业务单据表】和【促销产品调拨业务单据明细表】
	 * 
	 * @param seqnum
	 *            库存操作流水号
	 * @param argList
	 *            明细数据
	 * @throws InterruptedException
	 */
	public int insertAllocatonAllData(int seqnum, String allocationNo,
			List<HashMap<String, Object>> argList, UserInfo userInfo)
			throws InterruptedException {

		// 循环一单里的所有明细，以调拨主表数据
		int totalCount = 0;
		double totalMoney = 0.00;
		for (int i = 0; i < argList.size(); i++) {
			HashMap<String, Object> map = argList.get(i);
			totalCount += CherryUtil.string2int(map.get("Quantity").toString());
			totalMoney += CherryUtil.string2double(map.get("Money").toString());
		}
		HashMap<String, Object> tempMap = argList.get(0);
		HashMap<String, Object> allocationMap = new HashMap<String, Object>();

		// 促销库存操作流水ID
		allocationMap.put("BIN_PromotionInventoryLogID", seqnum);
		// 主表 组织信息ID
		allocationMap.put("BIN_OrganizationInfoID",
				tempMap.get("BIN_OrganizationInfoID"));
		// 主表 品牌ID
		allocationMap.put("BIN_BrandInfoID", tempMap.get("BIN_BrandInfoID"));
		// 主表 调拨单号
		allocationMap.put("AllocationNo", allocationNo);
		// 主表 接口调拨单号
		allocationMap.put("AllocationNoIF", allocationNo);
		// 主表 申请调拨部门
		allocationMap.put("BIN_OrganizationID",
				tempMap.get("BIN_OrganizationID"));
		// 主表 接受调拨部门
		allocationMap.put("BIN_OrganizationIDAccept",
				tempMap.get("BIN_OrganizationIDAccept"));
		// 主表 制单员工
		allocationMap.put("BIN_EmployeeID", tempMap.get("BIN_EmployeeID"));
		// 主表 总数量
		allocationMap.put("TotalQuantity", totalCount);
		// 主表 总金额
		allocationMap.put("TotalAmount", totalMoney);
		// 主表 审核区分
		allocationMap.put("VerifiedFlag", tempMap.get("VerifiedFlag"));
		// 主表 业务类型
		allocationMap.put("TradeType", tempMap.get("TradeType"));
		// 主表 调拨区分
		allocationMap.put("AllocationFlag", tempMap.get("AllocationFlag"));
		// 主表 关联单号
		allocationMap.put("RelevanceNo", tempMap.get("RelevanceNo"));
		// 主表 TODO：物流ID
		allocationMap.put("BIN_LogisticInfoID", 0);
		// 主表 收发货理由
		allocationMap.put("Reason", tempMap.get("ReasonALl"));
		// 主表 调拨日期
		allocationMap.put("AllocationDate", tempMap.get("AllocationDate"));
		allocationMap.put("TradeStatus", tempMap.get("TradeStatus"));
		// 主表 有效区分
		allocationMap.put("ValidFlag", "1");

		allocationMap.put("CreatedBy", tempMap.get("CreatedBy"));
		allocationMap.put("CreatePGM", tempMap.get("CreatePGM"));
		allocationMap.put("UpdatedBy", tempMap.get("UpdatedBy"));
		allocationMap.put("UpdatePGM", tempMap.get("UpdatePGM"));

		// insert 【促销产品调拨业务单据表】
		int bIN_PromotionAllocationID = binOLSSCM03_BL
				.insertAllocationMain(allocationMap);
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < argList.size(); i++) {
			Map<String, Object> map = argList.get(i);
			// 促销产品发货ID
			map.put("BIN_PromotionAllocationID", bIN_PromotionAllocationID);
			// 明细连番
			map.put("DetailNo", i + 1);
			detailList.add(map);
		}
		// insert 【促销产品调拨业务单据明细表】
		binOLSSCM03_BL.insertAllocationDetail(detailList);
		return bIN_PromotionAllocationID;
	}
}
