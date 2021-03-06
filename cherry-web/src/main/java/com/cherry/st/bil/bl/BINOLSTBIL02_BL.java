
/*  
 * @(#)BINOLSTBIL02_BL.java    1.0 2011-10-21     
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
package com.cherry.st.bil.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.bil.interfaces.BINOLSTBIL02_IF;
import com.cherry.st.bil.service.BINOLSTBIL02_Service;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.service.BINOLSTCM08_Service;
import com.cherry.st.ios.service.BINOLSTIOS01_Service;

@SuppressWarnings("unchecked")
public class BINOLSTBIL02_BL implements BINOLSTBIL02_IF {
	
	@Resource
    private BINOLSTIOS01_Service binOLSTIOS01_Service;
	@Resource
    private BINOLSTBIL02_Service binOLSTBIL02_Service;
	@Resource
	private BINOLSTCM08_Service binOLSTCM08_Service;
	@Resource
	private BINOLSTCM00_IF binOLSTCM00_BL;

	/**工作流中的各种动作
	 * 
	 */
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
		pramMap.put("CurrentUnit", "BINOLSTBIL02");
		pramMap.put("BIN_BrandInfoID", sessionMap.get("BIN_BrandInfoID"));
		pramMap.put("BIN_UserID", sessionMap.get("BIN_EmployeeID"));
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID, mainData.get("BIN_ProductInDepotID"));
		UserInfo userInfo = (UserInfo) sessionMap.get("UserInfo");
        pramMap.put("BrandCode", userInfo.getBrandCode());
        pramMap.put("OpComments", sessionMap.get("OpComments"));
		binOLSTCM00_BL.DoAction(pramMap);
	}

	/**
	 * 非工作流模式下保存入库单
	 * 
	 * */
	@Override
	public void tran_save(Map<String, Object> sessionMap,
			Map<String, Object> mainData, List<String[]> detailList)
			throws Exception {
		this.saveInDepot(sessionMap, mainData, detailList);
	}

	/**
	 * 提交入库单
	 * 
	 * */
	@Override
	public void tran_submit(Map<String, Object> sessionMap,
			Map<String, Object> mainData, List<String[]> detailList)
			throws Exception {
		int ret = 0;
		//先保存单据
		ret = this.saveInDepot(sessionMap, mainData, detailList);
		//如果无法更新单据数据抛出异常
		if(ret == 0){
			throw new CherryException("ECM00038");
		}
		
		//准备参数，开始工作流
		Map<String,Object> pramMap = new HashMap<String,Object>();
		pramMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_GR);
		pramMap.put(CherryConstants.OS_MAINKEY_BILLID,mainData.get("BIN_ProductInDepotID"));
		pramMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, sessionMap.get("BIN_EmployeeID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_USER, sessionMap.get("userID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, sessionMap.get("positionID"));
		pramMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, sessionMap.get("BIN_OrganizationID"));
		pramMap.put("CurrentUnit", "BINOLSTBIL02");
		pramMap.put("BIN_BrandInfoID", sessionMap.get("BIN_BrandInfoID"));
		pramMap.put("UserInfo", sessionMap.get("UserInfo"));
		UserInfo userInfo = (UserInfo) sessionMap.get("UserInfo");
		pramMap.put("BrandCode", userInfo.getBrandCode());
		//是否由云POS画面提交
	    pramMap.put("WEBPOS_SUBMIT", ConvertUtil.getString(sessionMap.get("WEBPOS_SUBMIT")));
		binOLSTCM00_BL.StartOSWorkFlow(pramMap);
	}
	
	/**
	 * 保存入库单
	 * 
	 * */
	public int saveInDepot(Map<String, Object> sessionMap,
			Map<String, Object> mainData, List<String[]> detailList){
		
		//存放插入入库明细表的数据
		List<Map<String,Object>> insertList = new ArrayList<Map<String,Object>>();
		
		 //从list中获取各种参数数组
        String[] productVendorIdArr = detailList.get(0);
        String[] batchNoArr = detailList.get(1);
        String[] quantityArr = detailList.get(2);
        String[] reasonArr = detailList.get(3);
        String[] priceArr = detailList.get(4);
        String[] referencePriceArr = detailList.get(5);
        
        //总金额
        double totalAmount = 0.0;
        //总数量
        int totalQuantity = 0;
        
        int detailNo = 0;
        for(int i = 0 ; i < productVendorIdArr.length ; i++){
        	if(null == productVendorIdArr[i] || ("").equals(productVendorIdArr[i])){
        		continue;
        	}
        	detailNo = detailNo+1;
        	Map<String,Object> productDetail = new HashMap<String,Object>();
            productDetail.putAll(sessionMap);
            productDetail.put("BIN_ProductInDepotID", mainData.get("BIN_ProductInDepotID"));
            //产品厂商ID
            productDetail.put("BIN_ProductVendorID", productVendorIdArr[i]);
            //明细连番
            productDetail.put("DetailNo", detailNo);
            //数量
            productDetail.put("Quantity", quantityArr[i]);
            //申请数量
            productDetail.put("PreQuantity", quantityArr[i]);
            //参考价
            productDetail.put("ReferencePrice", referencePriceArr[i]);
            //价格
            productDetail.put("Price", priceArr[i]);
            //包装类型ID
            productDetail.put("BIN_ProductVendorPackageID", "0");
            //实体仓库ID
            productDetail.put("BIN_InventoryInfoID", mainData.get("depotInfoId"));
            //逻辑仓库ID
            productDetail.put("BIN_LogicInventoryInfoID", CherryUtil.obj2int(mainData.get("logicInventoryInfoId")));
            //仓库库位ID
            productDetail.put("BIN_StorageLocationInfoID", "0");
            //备注
            productDetail.put("Comments", reasonArr[i]);
            //入出库区分
            productDetail.put("StockType", "0");
            //批次号
            productDetail.put("BatchNo", batchNoArr[i]);
            if(null != batchNoArr[i] && !"".equals(batchNoArr[i])){
                //取得批次ID
                Map<String,Object> productBatch = binOLSTIOS01_Service.getProductBatchId(productDetail);
                int productBatchId = 0;
                if(null != productBatch){
                    productBatchId= CherryUtil.string2int(ConvertUtil.getString(productBatch.get("BIN_ProductBatchID")));
                }
                if(0 == productBatchId){
                    productBatchId = binOLSTIOS01_Service.insertProductBatch(productDetail);
                }
                //批次ID
                productDetail.put("BIN_ProductBatchID", productBatchId);
            }

            totalQuantity += Integer.parseInt(quantityArr[i]);
            totalAmount += Double.parseDouble(priceArr[i])*Integer.parseInt(quantityArr[i]);
            insertList.add(productDetail);
        }
        
        //设定主表信息
        mainData.putAll(sessionMap);
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("PreTotalQuantity", totalQuantity);
        mainData.put("PreTotalAmount", totalAmount);
        
        binOLSTBIL02_Service.deletePrtInDepotDetail(mainData);
        binOLSTCM08_Service.insertProductInDepotDetail(insertList);
        
		return binOLSTCM08_Service.updateProductInDepotMain(mainData);
	}

}
