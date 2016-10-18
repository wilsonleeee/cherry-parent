/*	
 * @(#)BINOLSSPRM22_BL.java     1.0 2010/12/03		
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;
import com.cherry.ss.common.bl.BINOLSSCM02_BL;
import com.cherry.ss.prm.form.BINOLSSPRM22_Form;

public class BINOLSSPRM22_BL {
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;	
	@Resource
	private BINOLSSCM02_BL binOLSSCM02_BL;
	@Resource
	private BINOLSSCM01_BL binOLSSCM01_BL;
	
	/**
	 * 进行盘点处理
	 * @param form
	 */
	public int tran_stocktaking(BINOLSSPRM22_Form form,UserInfo userInfo){
		//盘点部门ID
		String organizationId =  userInfo.getCurrentOrganizationID();
		//盘点仓库ID
		String depotId = form.getDepot();		
	    //逻辑仓库ID
        int logicDepotId = CherryUtil.obj2int(form.getLogicDepot());
        
		//组织ID
		String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());		
		//品牌ID
		String brandInfoId = userInfo.getCurrentBrandInfoID();


		//盘点日期
		String deliverDate =CherryUtil.getSysDateTime("yyyy-MM-dd");
		
		//盘点原因
		String reasonAll = form.getReasonAll();

		//产品厂商编码
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();	
		
		//账面数量
		String[] bookCountArr = form.getBookCountArr();
		
		//盘点数量
		String[] quantityuArr =form.getQuantityuArr();
		
	    //盘差数量 
	     String[] gainCount = form.getGainCountArr();
	    
	    //盘差金额 
	    // String[] gainMoney = form.getGainMoneyArr();

		//促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();				
		
		//盘点原因
		String[] reasonArr = form.getReasonArr();

		//一次盘点的总盈亏数量
		int totalQuantity =0;
		//一次盘点的总盈亏金额
		double totalAmount =0;
		
		//整理画面数据,统计一单的总数量和总金额
		int lengthTotal = promotionProductVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int tempCount = CherryUtil.string2int(quantityuArr[i])-CherryUtil.string2int(bookCountArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
			totalAmount += money;
			totalQuantity += tempCount;	
		}
		
		Map<String, Object> mapInventory = new HashMap<String, Object>();
		//操作部门
		mapInventory.put("BIN_OrganizationID", organizationId);
		//操作员工
		mapInventory.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//总数量
		mapInventory.put("TotalQuantity", totalQuantity);
		//总金额
		mapInventory.put("TotalAmount", totalAmount);
		//审核区分  0：未审核	1：审核完了
		mapInventory.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		//业务类型 
		//1:仓库发货，2:仓库收货 3:仓库退库，4:接收退库，5:调入申请，6:调出确认
		//7:自由入库，8:自由出库 P：盘点，N:销售出库，R:销售入库
		mapInventory.put("TradeType", "P");
		//操作日期
		mapInventory.put("DeliverDate", deliverDate);
		//有效区分
		mapInventory.put("ValidFlag", "1");
		
		//数据来源渠道
		mapInventory.put("DataChannel", null);
		//物流ID
		mapInventory.put("BIN_LogisticInfoID", null);
		mapInventory.put("createdBy", userInfo.getLoginName());
		mapInventory.put("createPGM", "BINOLSSPRM22");
		mapInventory.put("updatedBy", userInfo.getLoginName());
		mapInventory.put("updatePGM", "BINOLSSPRM22");
		
		//insert  【促销品库存操作流水表】
		int bIN_PromotionInventoryLogID = binOLSSCM01_BL.insertPromotionInventoryLog(mapInventory);
		
		//取得单据号
		String stockTakingNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,userInfo.getLoginName(), "P");
		
		//拼装主表数据	
		HashMap<String,Object> tempMap = new HashMap<String,Object>();
		//主表      促销库存操作流水ID
		tempMap.put("BIN_PromotionInventoryLogID", bIN_PromotionInventoryLogID);
		//主表       组织ID
		tempMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());	
		//主表       品牌ID
		tempMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		//主表      盘点单号
		tempMap.put("StockTakingNo", stockTakingNo);
		//主表       接口盘点单号
		tempMap.put("StockTakingNoIF", stockTakingNo);			
		//主表     部门ID
		tempMap.put("BIN_OrganizationID", organizationId);
		//主表     盘点员工
		tempMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		//主表     总数量
		tempMap.put("TotalQuantity", totalQuantity);
		//主表     总金额
		tempMap.put("TotalAmount", totalAmount);
		//主表     业务类型
		tempMap.put("TradeType", "P");
		//主表     盘点类型
		tempMap.put("Type", "F3");
		//主表     关联单号
		tempMap.put("RelevantNo", null);
		//主表     盘点原因
		tempMap.put("Reason", reasonAll);
		//主表     
		tempMap.put("StockTakingDate", deliverDate);
		
		tempMap.put("CreatedBy", userInfo.getLoginName());
		tempMap.put("CreatePGM", "BINOLSSPRM22");
		tempMap.put("UpdatedBy", userInfo.getLoginName());
		tempMap.put("UpdatePGM", "BINOLSSPRM22");
		//insert 【促销产品盘点业务单据表】
		int promotionStockTakingID = binOLSSCM02_BL.insertPromotionStockTaking(tempMap);
		
		//拼装详细表数据
		List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
		int index =1;
		for(int i=0;i<lengthTotal;i++){
			//没有输入盘点数据的明细，忽略
			if("".equals(quantityuArr[i])||quantityuArr[i]==null){
				continue;	
			}
			int tempCount = CherryUtil.string2int(gainCount[i]);	
			//拼装明细表字段
			tempMap = new HashMap<String,Object>();				
			//明细       促销产品盘点ID
			tempMap.put("BIN_PromotionStockTakingID", promotionStockTakingID);
			//明细       产品厂商编码
			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);
			//明细       明细连番
			tempMap.put("DetailNo", index++);
			//明细       账面数量
			tempMap.put("Quantity", bookCountArr[i]);			
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
					
			//明细     盘点仓库ID
			tempMap.put("BIN_InventoryInfoID", depotId);
			//明细      TODO：盘点逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", logicDepotId);
			//明细      TODO：盘点仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     盘点处理方式			
			tempMap.put("HandleType", "2");
			//明细       盘差
			tempMap.put("GainQuantity", tempCount);							
			//明细     理由
			tempMap.put("Reason", reasonArr[i]);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     共通字段
			tempMap.put("CreatedBy", userInfo.getLoginName());
			tempMap.put("CreatePGM", "BINOLSSPRM22");
			tempMap.put("UpdatedBy", userInfo.getLoginName());
			tempMap.put("UpdatePGM", "BINOLSSPRM22");
			tempList.add(tempMap);	
		}
		//插入【促销产品盘点业务单据明细表】
		binOLSSCM02_BL.insertPromotionTakingDetail(tempList);
		//【促销产品入出库表】【促销产品入出库明细表】
		userInfo.setCurrentUnit("BINOLSSPRM22");
		binOLSSCM02_BL.insertStockInOutAndDetail(new int[]{promotionStockTakingID},userInfo);
		return bIN_PromotionInventoryLogID;
	}	
}
