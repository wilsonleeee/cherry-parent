/*	
 * @(#)BINOLSSPRM56_BL.java     1.0 2010/11/29		
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
import com.cherry.ss.common.bl.BINOLSSCM03_BL;
import com.cherry.ss.prm.form.BINOLSSPRM56_Form;

/**
 * 调拨记录查询修改审核
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 * 
 */
public class BINOLSSPRM56_BL {
	@Resource(name="binOLSSCM03_BL")
	private BINOLSSCM03_BL binolsscm03_BL;

	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binolcm03_bl;
	
	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binolsscm00_bl;
	/**
	 * 工作流中读取出来的按钮会通过action调用此方法
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public void tran_doaction(BINOLSSPRM56_Form form, UserInfo userInfo) throws Exception {
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put("entryID", form.getEntryID());
		pramMap.put("actionID", form.getActionID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("BINOLSSPRM56_Form", form);
		pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		binolsscm00_bl.DoAction(pramMap);
	}
	
	
	
	/**
	 * 保存调拨单,非工作流
	 * @param form
	 * @throws Exception 
	 */	
	public void tran_saveAllocation(BINOLSSPRM56_Form form,UserInfo userInfo) throws Exception{
		boolean ret = saveAllocationByForm(form,"BINOLSSPRM56",userInfo.getBIN_UserID());
		if(!ret){
			throw new CherryException("ECM00038");
		}
	}
	
	/**
	 *提交调入单，启动工作流
	 * @param form
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void tran_sendAllocation(BINOLSSPRM56_Form form,UserInfo userInfo) throws Exception{
		
		//保存调入单
		boolean ret = saveAllocationByForm(form, "BINOLSSPRM52",userInfo.getBIN_UserID());
		if(!ret){
			throw new CherryException("ECM00038");
		}
   		//准备参数，开始工作流
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_BG);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getProAllocationId());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("UserInfo", userInfo);
		pramMap.put("BIN_OrganizationIDAccept", form.getOutOrganizationId());
		binolsscm00_bl.StartOSWorkFlow(pramMap);
	}
	
	/**
	 * 删除调拨单,非工作流
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public void tran_deleteAllocation(BINOLSSPRM56_Form form,UserInfo userInfo) throws Exception{
		
		//更新主表 置为无效(逻辑删除)
		Map<String,Object> mainMap =new HashMap<String,Object>();
		mainMap.put("OldUpdateTime", form.getUpdateTime());
		mainMap.put("ValidFlag", "0");
		mainMap.put("OldModifyCount", form.getModifyCount());
		mainMap.put("BIN_PromotionAllocationID", form.getProAllocationId());
		int cnt = binolsscm03_BL.updateAllocationMain(mainMap);
		if(cnt<1){
			throw new CherryException("ESS00029");
		}
	}
	/**
	 * 保存调拨单
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public boolean saveAllocationByForm(BINOLSSPRM56_Form form,String currentUnit,int userID){
		
		//调入仓库
		String inDepotId = form.getInDepotId();
		//调拨理由（总）
		String reasonALl = form.getReasonAll();
		//产品厂商编码
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
		//促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//调拨数量
		String[] quantityuArr =form.getQuantityuArr();		
		//调拨原因
		String[] reasonArr = form.getReasonArr();

		//一次调拨操作的总数量（始终为正）
		int totalQuantity =0;
		//总金额
		double totalAmount =0;
		
		//明细列表
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		HashMap<String,Object> tempMap=null;
		//String splitKey="";
		int lengthTotal = promotionProductVendorIDArr.length;
		for(int i=0;i<lengthTotal;i++){
			int tempCount = CherryUtil.string2int(quantityuArr[i]);
			double money = CherryUtil.string2double(priceUnitArr[i])*tempCount;
			totalAmount += money;
			totalQuantity += tempCount;
			
			//进行表单拆分
			tempMap = new HashMap<String,Object>();	
			//明细       促销产品调拨ID
			tempMap.put("BIN_PromotionAllocationID", form.getProAllocationId());
			//明细       促销产品厂商ID
			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);
			//明细连番
			tempMap.put("DetailNo", i+1);
			//明细       调拨数量
			tempMap.put("Quantity", tempCount);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      调拨仓库ID
			tempMap.put("BIN_InventoryInfoID", inDepotId);
			//明细
			tempMap.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getInLogicId()));
			//明细      TODO：调拨仓库库位ID
			tempMap.put("BIN_StorageLocationInfoID",0);
			//明细     有效区分
			tempMap.put("ValidFlag", "1");
			//明细     理由
			tempMap.put("Reason", reasonArr[i]);
			//明细     共通字段
			tempMap.put("CreatedBy", userID);
			tempMap.put("CreatePGM", currentUnit);
			tempMap.put("UpdatedBy", userID);
			tempMap.put("UpdatePGM", currentUnit);
			
			detailList.add(tempMap);
		}
		//更新主表
		HashMap<String,Object> mainMap =new HashMap<String,Object>();
		mainMap.put("Reason", reasonALl);
		mainMap.put("VerifiedFlag", form.getVerifiedFlag());
		mainMap.put("TotalQuantity", totalQuantity);
		mainMap.put("TotalAmount", totalAmount);
		mainMap.put("OldUpdateTime", form.getUpdateTime());
		mainMap.put("OldModifyCount", form.getModifyCount());
		mainMap.put("UpdatedBy", userID);
		mainMap.put("UpdatePGM", currentUnit);
		mainMap.put("BIN_PromotionAllocationID", form.getProAllocationId());
		int cnt = binolsscm03_BL.updateAllocationMain(mainMap);
		if(cnt<1){
			return false;
		}		
		//删除原有明细
		binolsscm03_BL.deleteAllocationDetailPhysical(mainMap);
		//插入新明细
		binolsscm03_BL.insertAllocationDetail(detailList);		
		return true;
	}
	/**
	 * 工作流中做调出确认，会有工作流的类来调用此方法
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public int allocationOutByForm(BINOLSSPRM56_Form form,String currentUnit,int userID,int employeeID){
		
		//调出仓库
		String outDepotId = form.getOutDepotId();
		String billID = form.getProAllocationId();
		
		Map<String, Object> parmap = new HashMap<String, Object>();
		parmap.put("BIN_PromotionAllocationID", billID);
		Map<String, Object> mainMap = binolsscm03_BL.getPromotionAllocationMain(parmap);
		List<Map<String, Object>> detailList = binolsscm03_BL.getPromotionAllocationDetail(parmap);
		
		//直接用查询出来的发货单mainMap作为参数map 修改一下，即可作为收货单插入（大部分结构相同，小部分需要修改）

		//促销库存操作流水ID
		mainMap.put("BIN_PromotionInventoryLogID", 0);	
		//单号
		String orgInfoId = String.valueOf(mainMap.get("BIN_OrganizationInfoID"));
		String brandId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
		String billNO = binolcm03_bl.getTicketNumber(orgInfoId,brandId,String.valueOf(userID), CherryConstants.BUSINESS_TYPE_LG);
		mainMap.put("AllocationNo", billNO);
		//关联单号
		mainMap.put("RelevanceNo", mainMap.get("DeliverReceiveNoIF"));
		//接口单号
		mainMap.put("AllocationNoIF", billNO);
		//制单员工
		mainMap.put("BIN_EmployeeID", employeeID);
		//审核区分
		mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		//业务类型
		mainMap.put("TradeType", "6");
		//TODO：物流ID
		mainMap.put("BIN_LogisticInfoID", 0);
		//理由
		mainMap.put("Reason", "");
		//日期
		mainMap.put("AllocationDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));
		mainMap.put("AllocationFlag", "O");
		//
		mainMap.put("TradeStatus", "2");
		//有效区分
		mainMap.put("ValidFlag", "1");	
		
		mainMap.put("CreatedBy", userID);
		mainMap.put("CreatePGM", currentUnit);
		mainMap.put("UpdatedBy", userID);
		mainMap.put("UpdatePGM", currentUnit);
		
		//insert 调拨单主表
		int id = binolsscm03_BL.insertAllocationMain(mainMap);
		
		for(int i=0;i<detailList.size();i++){
			Map<String,Object> map = detailList.get(i);
			map.put("BIN_PromotionAllocationID", id);
			map.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(form.getOutLogicId()));
			map.put("BIN_StorageLocationInfoID", 0);
			map.put("BIN_InventoryInfoID", outDepotId);
			//有效区分
			map.put("ValidFlag", "1");	
			map.put("CreatedBy", userID);
			map.put("CreatePGM", currentUnit);
			map.put("UpdatedBy", userID);
			map.put("UpdatePGM", currentUnit);
		}
		//插入调拨单据明细
		binolsscm03_BL.insertAllocationDetail(detailList);		
		return id;
	}
	
}
