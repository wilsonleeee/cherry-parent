/*
 * @(#)BINOLSTSFH05_Service.java     1.0 2011/09/14
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
package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH06_Service extends BaseService{
	
	
	/**
	 * 根据品牌ID获取品牌CODE
	 * 
	 * */
	public String getBrandCode(int brandInfoID){
		return (String) baseServiceImpl.get(brandInfoID,"BINOLSTSFH06.getBrandCode");
	}
	
	public String getDepart(Map<String,Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.getDepart");
        return  (String) baseServiceImpl.get(parameterMap);
    }
	
	/**
	 * 取得产品List
	 * 
	 * @param map
	 * @return
	 */
	public List searchProductList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.searchProductList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取订货参数表(全局) 
	 * @param map
	 * @return
	 */
    public List<Map<String, Object>> getOrderParameterGlobal(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.getOrderParameterGlobal");
        return baseServiceImpl.getList(map);
    }
	
//    /**
//     * 根据部门ID取实体仓库
//     * @param map
//     * @return
//     */
//    public List<Map<String, Object>> getDepotInfo(Map<String, Object> map) {
//        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.getDepotInfo");
//        return baseServiceImpl.getList(map);
//    }
    
    /**
     * LotionSPA杭州瑞合 建议发货单List
     * @param map
     * @return
     */
    public List<Map<String, Object>> searchSuggetstList_LSRH(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH06.searchSuggetstList_LSRH");
        return baseServiceImpl.getList(map);
    }
    
    
    /**
	 * 查询产品批次库存表数据（根据实体仓库ID，逻辑仓库ID，产品厂商ID查询）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProNewBatchStockList (Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH06.getProNewBatchStockList");
		return baseServiceImpl.getList(paramMap);
	}
}
