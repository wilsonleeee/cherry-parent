/*	
 * @(#)BINOLSSPRM52_BL.java     1.0 2010/11/29		
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
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.bl.BINOLSSCM04_BL;
import com.cherry.ss.common.bl.BINOLSSCM05_BL;
import com.cherry.ss.common.service.BINOLSSCM04_Service;
import com.cherry.ss.prm.form.BINOLSSPRM52_Form;

/**
 * 发货单查询编辑修改审核
 * 
 * @author dingyc
 * @version 1.0 2010.11.29
 * 
 */
public class BINOLSSPRM52_BL {
	@Resource(name="binOLSSCM04_Service")
	private BINOLSSCM04_Service binolsscm04_service;
	
	@Resource(name="binOLSSCM04_BL")
	private BINOLSSCM04_BL binolsscm04_bl;
	
	@Resource(name="binOLSSCM05_BL")
	private BINOLSSCM05_BL binolsscm05_bl;

	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binolsscm00_bl;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binolcm03_bl;
	
	/**
	 * 工作流中读取出来的按钮会通过action调用此方法
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public void tran_doaction(BINOLSSPRM52_Form form, UserInfo userInfo) throws Exception {
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put("entryID", form.getEntryID());
		pramMap.put("actionID", form.getActionID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("BINOLSSPRM52_Form", form);
		pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		binolsscm00_bl.DoAction(pramMap);
	}
 
	
	/**
	 * 发货
	 * 会启动工作流
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public void tran_sendDeliver(BINOLSSPRM52_Form form, UserInfo userInfo) throws Exception {
		//保存发货单
		boolean ret = saveDeliverByForm(form, "BINOLSSPRM52",userInfo.getBIN_UserID());
		if(!ret){
			throw new CherryException("ECM00038");
		}
		
		//如果收货部门是柜台，则要发送MQ消息
		boolean isCounter = binolsscm05_bl.checkOrganizationType(form.getInOrganizationID());
		
		//准备参数，开始工作流
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, form.getDeliverId());
		pramMap.put("BIN_EmployeeID", userInfo.getBIN_EmployeeID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userInfo.getBIN_UserID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, userInfo.getBIN_PositionCategoryID());
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, userInfo.getBIN_OrganizationID());
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, userInfo.getBIN_EmployeeID());
		pramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
		pramMap.put("SendToCounter", isCounter?"YES":"NO");
		pramMap.put("UserInfo", userInfo);
		pramMap.put("BIN_OrganizationIDReceive", form.getInOrganizationID());
		binolsscm00_bl.StartOSWorkFlow(pramMap);	
	}

	/**
	 * 制单员编辑修改发货单，非工作流
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public boolean tran_saveDeliver(BINOLSSPRM52_Form form,UserInfo userInfo){
		return this.saveDeliverByForm(form, "BINOLSSPRM52",userInfo.getBIN_UserID());
	}
	/**
	 * 
	 * 制单员删除发货单，非工作流
	 * @param form
	 * @param userInfo
	 * @throws Exception
	 */
	public void tran_deleteDeliver(BINOLSSPRM52_Form form, UserInfo userInfo) throws Exception {

		//更新主表  有效区分置为0
		Map<String,Object> mainMap =new HashMap<String,Object>();
		mainMap.put("ValidFlag", "0");
		mainMap.put("OldUpdateTime", form.getUpdateTime());
		mainMap.put("OldModifyCount", form.getModifyCount());
		mainMap.put("BIN_PromotionDeliverID", form.getDeliverId());
		mainMap.put("UpdatedBy", userInfo.getBIN_UserID());
		mainMap.put("UpdatePGM", "BINOLSSPRM52");
		int cnt = binolsscm04_bl.updatePrmDeliverMain(mainMap);
		if (cnt < 1) {
			throw new CherryException("ECM00038");
		}	
	}
	/**
	 * 保存发货单,保存成功返回true，否则返回false
	 * @param form
	 * @throws Exception 
	 */
	public boolean saveDeliverByForm(BINOLSSPRM52_Form form,String currentUnit,int userID){

		//发货仓库
		String outDepotId = form.getOutDepotId();
		//发货逻辑窗口
		int outLogicInventoryInfoID = CherryUtil.obj2int(form.getOutLoginDepotId());
		//发货理由（总）
		String reasonALl = form.getReasonAll();
		//产品厂商编码
		String[] promotionProductVendorIDArr = form.getPromotionProductVendorIDArr();
		//促销品单价
		String[] priceUnitArr = form.getPriceUnitArr();		
		//发货数量
		String[] quantityuArr =form.getQuantityuArr();		
		//发货原因
		String[] reasonArr = form.getReasonArr();

		//一次发货操作的总数量（始终为正）
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
			//明细       促销产品发货ID
			tempMap.put("BIN_PromotionDeliverID", form.getDeliverId());
			//明细       促销产品厂商ID
			tempMap.put("BIN_PromotionProductVendorID", promotionProductVendorIDArr[i]);
			//明细连番
			tempMap.put("DetailNo", i+1);
			//明细       发货数量
			tempMap.put("Quantity", tempCount);
			//明细       价格
			tempMap.put("Price", CherryUtil.string2double(priceUnitArr[i]));
			//明细       TODO：包装类型ID
			tempMap.put("BIN_ProductVendorPackageID", 0);
			//明细      发货仓库ID
			tempMap.put("BIN_InventoryInfoID", outDepotId);
			//明细      TODO：发货逻辑仓库ID
			tempMap.put("BIN_LogicInventoryInfoID", outLogicInventoryInfoID);
			//明细      TODO：发货仓库库位ID
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
		Map<String,Object> mainMap =new HashMap<String,Object>();
		mainMap.put("Reason", reasonALl);
		mainMap.put("TotalQuantity", totalQuantity);
		mainMap.put("TotalAmount", totalAmount);
		mainMap.put("StockInFlag", form.getStockInFlag());
		mainMap.put("VerifiedFlag", form.getVerifiedFlag());
		mainMap.put("OldUpdateTime", form.getUpdateTime());
		mainMap.put("OldModifyCount", form.getModifyCount());
		mainMap.put("BIN_PromotionDeliverID", form.getDeliverId());
        if(ConvertUtil.getString(form.getPlanArriveDate()).equals("")){
            //预计到货日期清空
            mainMap.put("EmptyPlanArriveDate","true");
        }else{
            //预计到货日期
            mainMap.put("PlanArriveDate",form.getPlanArriveDate());
        }
		mainMap.put("UpdatedBy", userID);
		mainMap.put("UpdatePGM", "BINOLSSPRM52");
		int cnt = binolsscm04_bl.updatePrmDeliverMain(mainMap);
		if(cnt<1){
			return false;
		}		
		//删除原有明细
		binolsscm04_bl.deleteDeliverDetailPhysical(mainMap);
		//插入新明细
		binolsscm04_service.insertPromotionDeliverDetail(detailList);
		return true;
	}	
	/**
	 * 进行收货处理
	 * @param form
	 * @throws Exception 
	 */
	public int receiveDeliverByForm(BINOLSSPRM52_Form form,String currentUnit,int userID,int employeeID){
		/**
    	 * 修改receiveDeliverByForm方法,收货的实体仓库和逻辑仓库都从页面上取得
    	 * 
    	 * @author zhanggl
    	 * @date 2011-12-09
    	 * 
    	 * */		
		//收货实体仓库
		String inDepotId = form.getInDepotId();
		//收货逻辑仓库
		String inLogicDepotId = form.getInLogicDepotId();
		
		String billID = form.getDeliverId();
		
		Map<String, Object> parmap = new HashMap<String, Object>();
		parmap.put("BIN_PromotionDeliverID", billID);
		Map<String, Object> mainMap = binolsscm04_bl.getPromotionDeliverMain(parmap);
		List<Map<String, Object>> detailList = binolsscm04_bl.getDeliverDetailList(parmap);
		
		//直接用查询出来的发货单mainMap作为参数map 修改一下，即可作为收货单插入（大部分结构相同，小部分需要修改）

		//促销库存操作流水ID
		mainMap.put("BIN_PromotionInventoryLogID", 0);	
		//收货部门
		//mainMap.put("BIN_OrganizationID", inOrganizationId);
		//收发货单号
		String orgInfoId = String.valueOf(mainMap.get("BIN_OrganizationInfoID"));
		String brandId = String.valueOf(mainMap.get("BIN_BrandInfoID"));
		String deliverNo = binolcm03_bl.getTicketNumber(orgInfoId,brandId,String.valueOf(userID), CherryConstants.BUSINESS_TYPE_RD);
		mainMap.put("DeliverReceiveNo", deliverNo);
		//关联单号
		mainMap.put("RelevanceNo", mainMap.get("DeliverReceiveNoIF"));
		//关联接口单号
		mainMap.put("DeliverReceiveNoIF", deliverNo);
		//制单员工
		mainMap.put("BIN_EmployeeID", employeeID);
		//审核区分
		mainMap.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		//业务类型
		mainMap.put("TradeType", "2");
		//TODO：物流ID
		mainMap.put("BIN_LogisticInfoID", 0);
		//收发货理由
		mainMap.put("Reason", "");
		//收发货日期
		mainMap.put("DeliverDate", CherryUtil.getSysDateTime("yyyy-MM-dd"));
		//入库区分 表示对方是否已经根据发货单入库
		mainMap.put("StockInFlag", "4");
		//有效区分
		mainMap.put("ValidFlag", "1");	
		
		mainMap.put("CreatedBy", userID);
		mainMap.put("CreatePGM", currentUnit);
		mainMap.put("UpdatedBy", userID);
		mainMap.put("UpdatePGM", currentUnit);
		
		//insert 【促销产品收发货业务单据表】
		int bIN_PromotionDeliverID = binolsscm04_bl.insertPromotionDeliverMain(mainMap);
		
		for(int i=0;i<detailList.size();i++){
			Map<String,Object> map = detailList.get(i);
			map.put("BIN_PromotionDeliverID", bIN_PromotionDeliverID);
			//TODO:逻辑仓库
			map.put("BIN_LogicInventoryInfoID", inLogicDepotId);
			map.put("BIN_StorageLocationInfoID", null);
			map.put("BIN_InventoryInfoID", inDepotId);
			//有效区分
			map.put("ValidFlag", "1");	
			map.put("CreatedBy", userID);
			map.put("CreatePGM", currentUnit);
			map.put("UpdatedBy", userID);
			map.put("UpdatePGM", currentUnit);
		}
		//插入收货单据明细
		binolsscm04_bl.insertPromotionDeliverDetail(detailList);		
		return bIN_PromotionDeliverID;
	} 
}
