
/*  
 * @(#)BINOLSTCM11_BL.java    1.0 2011-12-2     
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
package com.cherry.st.common.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM11_IF;
import com.cherry.st.common.service.BINOLSTCM11_Service;

/**
 * 产品收货共通处理
 * 
 * @author zhanggl
 * @date 2011-12-2
 * 
 * */

public class BINOLSTCM11_BL implements BINOLSTCM11_IF {

	@Resource
    private BINOLCM03_BL binOLCM03_BL;
	@Resource
	private BINOLSTCM11_Service binOLSTCM11_Service;
	@Resource
    private BINOLSTCM01_IF binOLSTCM01_BL;
	
	
	
	@Override
	public int insertProductReceiveAll(Map<String, Object> mainData,
			List<Map<String, Object>> detailList) {
		// TODO Auto-generated method stub
		int productReceiveId = 0;
        String organizationInfoId = ConvertUtil.getString(mainData.get("BIN_OrganizationInfoID"));
        String brandInfoId = ConvertUtil.getString(mainData.get("BIN_BrandInfoID"));
        String receiveNo = ConvertUtil.getString(mainData.get("ReceiveNo"));
        String receiveNoIF=ConvertUtil.getString(mainData.get("ReceiveNoIF"));
        String createdBy = ConvertUtil.getString(mainData.get("CreatedBy"));
        String bussType = "RD";
        //如果deliverNo不存在调用共通生成单据号
        if(null==receiveNo || "".equals(receiveNo)){
        	receiveNo = binOLCM03_BL.getTicketNumber(organizationInfoId,brandInfoId,createdBy,bussType);
            mainData.put("ReceiveNo", receiveNo);
        }
        if(null==receiveNoIF || "".equals(receiveNoIF)){
            mainData.put("ReceiveNoIF", receiveNo);
        }
        
        //如果没有指明逻辑仓库以及库位信息,则默认为0
        if(null == mainData.get("BIN_LogicInventoryInfoID")){
            mainData.put("BIN_LogicInventoryInfoID", 0);
        }
        if(null == mainData.get("BIN_StorageLocationInfoID")){
            mainData.put("BIN_StorageLocationInfoID", 0);
        }
        
        //物流ID默认为0
        if(null == mainData.get("BIN_LogisticInfoID")){
        	mainData.put("BIN_LogisticInfoID", 0);
        }
        
        if(ConvertUtil.getString(mainData.get("BIN_OrganizationIDDX")).equals("")){
            mainData.put("BIN_OrganizationIDDX", mainData.get("BIN_OrganizationID"));
        }
        if(ConvertUtil.getString(mainData.get("BIN_EmployeeIDDX")).equals("")){
            mainData.put("BIN_EmployeeIDDX", mainData.get("BIN_EmployeeID"));
        }
        
        //插入收货主表,并返回主表ID
        productReceiveId = binOLSTCM11_Service.insertProductReceiveMain(mainData);
        
        //遍历明细
        for(int index=0; index<detailList.size(); index++){
        	Map<String,Object> detailMap = detailList.get(index);
        	//主表ID
        	detailMap.put("BIN_ProductReceiveID", productReceiveId);
        	
            if("".equals(ConvertUtil.getString(detailMap.get("ReferencePrice")))){
                detailMap.put("ReferencePrice", detailMap.get("Price"));
            }
        	
        	if(null == detailMap.get("BIN_LogicInventoryInfoID")){
        		detailMap.put("BIN_LogicInventoryInfoID", 0);
        	}
        	if(null == detailMap.get("BIN_StorageLocationInfoID")){
        		detailMap.put("BIN_StorageLocationInfoID", 0);
        	}
        	
        }
        binOLSTCM11_Service.insertProductReceiveDetail(detailList);
        
		return productReceiveId;
	}


	/**
     * 给定收货单主表ID,生成入出库单据,并更改库存
     *  @param praMap
     * @return 返回出入库记录的主表ID
     * 
     * */
    public int createProductInOutByReceiveID(Map<String,Object> praMap){
    	int productReceiveID = CherryUtil.obj2int(praMap.get("BIN_ProductReceiveID"));
        String createdBy = ConvertUtil.getString(praMap.get("CreatedBy"));
        String createPGM =ConvertUtil.getString(praMap.get("CreatePGM"));
        //取得收货单概要
        Map<String,Object> mainData = getProductReceiveMainData(productReceiveID,null);
        mainData.put("RelevanceNo", mainData.get("ReceiveNoIF"));
        mainData.put("BIN_OrganizationID", mainData.get("BIN_OrganizationIDReceive"));
        mainData.put("StockType","0");
        mainData.put("TradeType","RD");
        mainData.put("CreatedBy", createdBy);
        mainData.put("CreatePGM", createPGM);
        mainData.put("UpdatedBy", createdBy);
        mainData.put("UpdatePGM", createPGM);
        //取得收货单明细
        List<Map<String,Object>> detailList = getProductReceiveDetailData(productReceiveID,null);
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = detailList.get(i);
            temp.put("StockType", "0");
            temp.put("CreatedBy", createdBy);
            temp.put("CreatePGM", createPGM);
            temp.put("UpdatedBy", createdBy);
            temp.put("UpdatePGM", createPGM);
            detailList.set(i, temp);
        }
        //创建入出库单据
        int productInOutId = binOLSTCM01_BL.insertProductInOutAll(mainData,detailList);
        
        mainData.put("BIN_ProductInOutID", productInOutId);
        //更改库存
        binOLSTCM01_BL.changeStock(mainData);
        return productInOutId;
    }

	@Override
	public List<Map<String, Object>> getProductReceiveDetailData(
			int productReceiveID, String language) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("BIN_ProductReceiveID", productReceiveID);
		paramMap.put("language", language);
		return binOLSTCM11_Service.getProductReceiveDetailData(paramMap);
	}



	@Override
	public Map<String, Object> getProductReceiveMainData(int productReceiveID,
			String language) {
		// TODO Auto-generated method stub
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("BIN_ProductReceiveID", productReceiveID);
		paramMap.put("language", language);
		return binOLSTCM11_Service.getProductReceiveMainData(paramMap);
	}

    /**
     * 修改收货主表数据。
     * @param praMap
     * @return
     */
    @Override
    public int updateProductReceiveMain(Map<String, Object> praMap) {
        return binOLSTCM11_Service.updateProductReceiveMain(praMap);
    }
}
