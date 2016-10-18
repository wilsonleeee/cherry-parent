/*	
 * @(#)BINOLSSCM05_Service.java     1.0 2010/10/29		
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
package com.cherry.ss.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSSCM05_Service extends BaseService{
	
	/**
	 * 发货单ID取得收发货单据及其详细信息
	 * @param promotionInventoryLogID
	 * @return
	 */
	public List<Map<String, Object>> getPromotionDeliverAllGroupByID(int promotionDeliverID){
		Map<String,Object> pramap = new HashMap<String,Object>();
		pramap.put("BIN_PromotionDeliverID", promotionDeliverID);
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM05.getPromotionDeliverAllGroupByID");
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
		pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM05.getOrganizationType");
		return (String) baseServiceImpl.get(pramap);		
	}
	
	/**
	 * 取得促销品发货单主表信息主要取各种code
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPromotionDeliverForMQ(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM05.getPromotionDeliverForMQ");
		return (Map)baseServiceImpl.get(map);
	}
	
    /**
     * 入库单ID取得入库单据及其详细信息
     * @param prmInDepotID
     * @return
     */
    public List<Map<String, Object>> getPrmInDepotAllGroupByID(int prmInDepotID){
        Map<String,Object> pramap = new HashMap<String,Object>();
        pramap.put("BIN_PrmInDepotID", prmInDepotID);
        pramap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM05.getPrmInDepotAllGroupByID");
        List<Map<String, Object>>  ret = baseServiceImpl.getList(pramap);
        return ret;
    }
    
    /**
     * 取得促销品入库单主表信息主要取各种code
     * @param map
     * @return
     */
    public Map<String,Object> getPrmInDepotForMQ(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSCM05.getPrmInDepotForMQ");
        return (Map)baseServiceImpl.get(map);
    }
}
