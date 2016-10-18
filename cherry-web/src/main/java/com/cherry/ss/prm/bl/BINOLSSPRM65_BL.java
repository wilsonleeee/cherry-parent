/*
 * @(#)BINOLSSPRM65_BL.java     1.0 2013/01/25
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
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.ss.common.bl.BINOLSSCM00_BL;
import com.cherry.ss.common.service.BINOLSSCM09_Service;
import com.cherry.ss.prm.interfaces.BINOLSSPRM65_IF;
import com.cherry.ss.prm.service.BINOLSSPRM65_Service;

/**
 * 
 * 入库明细BL
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */public class BINOLSSPRM65_BL implements BINOLSSPRM65_IF {
		
	@Resource(name="binOLSSPRM65_Service")
    private BINOLSSPRM65_Service binOLSSPRM65_Service;
	
	@Resource(name="binOLSSCM09_Service")
	private BINOLSSCM09_Service binOLSSCM09_Service;
	
	@Resource(name="binOLSSCM00_BL")
	private BINOLSSCM00_BL binOLSSCM00_BL;

	/**
	 * 工作流中的各种动作
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void tran_doaction(Map<String, Object> sessionMap,Map<String,Object> billInformation) throws Exception {
		//单据主表信息
		Map<String,Object> mainData = (Map<String, Object>) billInformation.get("mainData");
		//单据明细信息
		List<String[]> detailList = (List<String[]>) billInformation.get("detailList");
		int ret = 0;
		//先保存单据
		ret = this.saveInDepot(sessionMap, mainData, detailList);
		//如果无法更新单据数据抛出异常
		if(ret == 0){
			throw new CherryException("ECM00038");
		}
		Map<String, Object> pramMap = new HashMap<String, Object>();
		pramMap.put("entryID", mainData.get("entryID"));
		pramMap.put("actionID", mainData.get("actionID"));
		pramMap.put("BIN_EmployeeID", sessionMap.get("BIN_EmployeeID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, sessionMap.get("userID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, sessionMap.get("positionID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, sessionMap.get("BIN_OrganizationID"));
		pramMap.put("CurrentUnit", "BINOLSSPRM65");
		pramMap.put("BIN_BrandInfoID", sessionMap.get("BIN_BrandInfoID"));
		pramMap.put("BIN_UserID", sessionMap.get("BIN_EmployeeID"));
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, mainData.get("BIN_PrmInDepotID"));
		pramMap.put("OpComments", billInformation.get("opComments"));
	    UserInfo userInfo = (UserInfo) sessionMap.get("UserInfo");
	    pramMap.put("BrandCode", userInfo.getBrandCode());
		binOLSSCM00_BL.DoAction(pramMap);
	}

	/**
	 * 非工作流模式下保存入库单
	 */
	@Override
	public void tran_save(Map<String, Object> sessionMap,Map<String, Object> mainData, List<String[]> detailList) throws Exception {
		this.saveInDepot(sessionMap, mainData, detailList);
	}

//	/**
//	 * 提交入库单
//	 */
//	@Override
//	public void tran_submit(Map<String, Object> sessionMap,Map<String, Object> mainData, List<String[]> detailList) throws Exception {
//		int ret = 0;
//		//先保存单据
//		ret = this.saveInDepot(sessionMap, mainData, detailList);
//		//如果无法更新单据数据抛出异常
//		if(ret == 0){
//			throw new CherryException("ECM00038");
//		}
//		
//		//准备参数，开始工作流
//		Map<String,Object> pramMap = new HashMap<String,Object>();
//		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);
//		pramMap.put(CherryConstants.OS_MAINKEY_BILLID,mainData.get("BIN_ProductInDepotID"));
//		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, sessionMap.get("BIN_EmployeeID"));
//		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, sessionMap.get("userID"));
//		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, sessionMap.get("positionID"));
//		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, sessionMap.get("BIN_OrganizationID"));
//		pramMap.put("CurrentUnit", "BINOLSTBIL02");
//		pramMap.put("BIN_BrandInfoID", sessionMap.get("BIN_BrandInfoID"));
//		pramMap.put("UserInfo", sessionMap.get("UserInfo"));
//		binOLSTCM00_BL.StartOSWorkFlow(pramMap);
//	}
	
	/**
	 * 保存入库单
	 * @param sessionMap
	 * @param mainData
	 * @param detailList
	 * @return
	 */
	public int saveInDepot(Map<String, Object> sessionMap,Map<String, Object> mainData, List<String[]> detailList){
		//存放插入入库明细表的数据
		List<Map<String,Object>> insertList = new ArrayList<Map<String,Object>>();
		
		//从list中获取各种参数数组
        String[] prmVendorIdArr = detailList.get(0);
//        String[] batchNoArr = detailList.get(1);
        String[] quantityArr = detailList.get(1);
        String[] reasonArr = detailList.get(2);
        String[] priceArr = detailList.get(3);
        
        //总金额
        double totalAmount = 0.0;
        //总数量
        int totalQuantity = 0;
        
        for(int i = 0 ; i < prmVendorIdArr.length ; i++){
        	if(null == prmVendorIdArr[i] || ("").equals(prmVendorIdArr[i])){
        		continue;
        	}
        	Map<String,Object> prmDetail = new HashMap<String,Object>();
            prmDetail.putAll(sessionMap);
            prmDetail.put("BIN_PrmInDepotID", mainData.get("BIN_PrmInDepotID"));
            //促销品厂商ID
            prmDetail.put("BIN_PromotionProductVendorID", prmVendorIdArr[i]);
            //明细连番
            prmDetail.put("DetailNo", i+1);
            //数量
            prmDetail.put("Quantity", quantityArr[i]);
            //申请数量
            prmDetail.put("PreQuantity", quantityArr[i]);
            //价格
            prmDetail.put("Price", priceArr[i]);
            //包装类型ID
//            productDetail.put("BIN_ProductVendorPackageID", "0");
            //实体仓库ID
            prmDetail.put("BIN_InventoryInfoID", mainData.get("depotInfoId"));
            //逻辑仓库ID
            prmDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(mainData.get("logicInventoryInfoId")));
            //仓库库位ID
            prmDetail.put("BIN_StorageLocationInfoID", "0");
            //备注
            prmDetail.put("Comments", reasonArr[i]);
            //入出库区分
            prmDetail.put("StockType", CherryConstants.STOCK_TYPE_IN);

            totalQuantity += Integer.parseInt(quantityArr[i]);
            totalAmount += Double.parseDouble(priceArr[i])*Integer.parseInt(quantityArr[i]);
            insertList.add(prmDetail);
        }
        
        //设定主表信息
        mainData.putAll(sessionMap);
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("PreTotalQuantity", totalQuantity);
        mainData.put("PreTotalAmount", totalAmount);
        
        binOLSSPRM65_Service.deletePrmInDepotDetail(mainData);
        binOLSSCM09_Service.insertPrmInDepotDetail(insertList);
        
		return binOLSSCM09_Service.updatePrmInDepotMain(mainData);
	}
}