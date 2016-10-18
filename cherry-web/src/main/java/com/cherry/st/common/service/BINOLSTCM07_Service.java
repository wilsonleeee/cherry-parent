/*	
 * @(#)BINOLSTCM07_Service.java     1.0 2010/10/29		
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
package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTCM07_Service extends BaseService{
	
	/**
	 * 发货单ID取得收发货单据及其详细信息
	 * @param promotionInventoryLogID
	 * @return
	 */
	public List<Map<String, Object>> getProDeliverAllGroupByID(int proDeliverID,String orgInfoID,String brandInfoID){
		Map<String,Object> pramap = new HashMap<String,Object>();
		pramap.put("BIN_ProductDeliverID", proDeliverID);
		pramap.put("BIN_OrganizationInfoID", orgInfoID);
		pramap.put("BIN_BrandInfoID", brandInfoID);
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM07.getProDeliverAllGroupByID");
		List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
		return ret;		
	}
	
	/**
     * 取得指定ID的发货单明细数量（合并同一产品）
     * @param promotionInventoryLogID
     * @return
     */
    public List<Map<String, Object>> getProDeliverDetailQuantityByID(int proDeliverID){
        Map<String,Object> pramap = new HashMap<String,Object>();
        pramap.put("BIN_ProductDeliverID", proDeliverID);
        pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM07.getProDeliverDetailQuantityByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
        return ret;     
    }
	
	/**
	 * 取得部门类型
	 * @param promotionInventoryLogID
	 * @return
	 */
	public String getOrganizationType(String organizationId){
		Map<String,Object> pramap = new HashMap<String,Object>();
		pramap.put("organizationId", organizationId);
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM07.getOrganizationType");
		return (String) baseServiceImpl.get(pramap);		
	}
	
    /**
     * 入库单ID取得入库单据及其详细信息
     * @param promotionInventoryLogID
     * @return
     */
    public List<Map<String, Object>> getProInDepotAllGroupByID(int productInDepotID,String orgInfoID,String brandInfoID){
        Map<String,Object> pramap = new HashMap<String,Object>();
        pramap.put("BIN_ProductInDepotID", productInDepotID);
        pramap.put("BIN_OrganizationInfoID", orgInfoID);
        pramap.put("BIN_BrandInfoID", brandInfoID);
        pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM07.getProInDepotAllGroupByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
        return ret;     
    }
    
    /**
     * 取得指定ID的入库单明细数量（合并同一产品）
     * @param promotionInventoryLogID
     * @return
     */
    public List<Map<String, Object>> getProInDepotDetailQuantityByID(int productInDepotID){
        Map<String,Object> pramap = new HashMap<String,Object>();
        pramap.put("BIN_ProductInDepotID", productInDepotID);
        pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM07.getProInDepotDetailQuantityByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
        return ret;     
    }
}
