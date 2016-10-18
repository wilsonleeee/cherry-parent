package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

/**
 * 
 * 发货单查询Service
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.03
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS03_Service {
	
	@Resource(name="baseServiceImpl")
	private BaseServiceImpl baseServiceImpl;
	
	/**
	 * 取得某一产品的总数量和总金额
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLPTRPS03.getSumInfo");
	}
	
	/**
	 * 取得发货单List
	 * 
	 * @param map
	 * @return
	 */
	public List getProductList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
						"BINOLPTRPS03.getProductList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得发货单信息
	 * 
	 * @param map
	 * @return
	 */
	public Map getDeliverInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS03.getDeliverInfo");
		return (Map) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得发货单明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getDeliverDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map.get(CherryConstants.SESSION_LANGUAGE));
		// 促销产品收发货ID
		paramMap.put("deliverId", map.get("deliverId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS03.getDeliverDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
