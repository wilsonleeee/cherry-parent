/*
 * @(#)BINOLSTBIL10_Service.java     1.0 2010/11/05
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
package com.cherry.st.bil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 盘点单明细Service
 * 
 * 
 * 
 * @author weisc
 * @version 1.0 2010.11.05
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL10_Service extends BaseService{
	/**
	 * 取得盘点单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getTakingInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 产品盘点ID
		paramMap.put("billId", map.get("billId"));
		// 盈亏
		paramMap.put("profitKbn", map.get("profitKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.getTakingInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得盘点单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 产品盘点ID
		paramMap.put("billId", map.get("billId"));
		// 排序字段
		paramMap.put("detailOrderBy", map.get("detailOrderBy"));
		// 盈亏区分 
		paramMap.put("profitKbn", map.get("profitKbn"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.getTakingDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.getSumInfo");
        return (Map<String, Object>)baseServiceImpl.get(map);
    }
    /**
     * 删除【产品盘点单据表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductStockTakingLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.deleteProductStockTakingLogic");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 删除【产品盘点单据明细表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductTakingDetailLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.deleteProductTakingDetailLogic");
        return baseServiceImpl.update(map);
    }
    /**
     * 删除【产品盘点单据明细表】
     * @param map
     * @return
     */
    public int deleteProductTakingDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.deleteProductTakingDetail");
        return baseServiceImpl.update(map);
    }

    /**
     * 获取产品分类信息
     * @param paramsMap
     * @return
     */
	public List<Map<String, Object>> getPrtCateinfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL10.getPrtCateinfo");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
}
