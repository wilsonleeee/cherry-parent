package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSSPRM15_Service extends BaseService{
	
	/**
	 * 取得促销活动List
	 * @param map
	 * @return List
	 */
	public List getActiveList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM15.getActiveList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得已下发促销活动总数
	 * @param map
	 * @return int
	 */
	public int getActiveCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM15.getActiveCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return List
	 */
	public List indSearchPrmCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM15.getCounterList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得促销品信息
	 * @param map
	 * @return List
	 */
	public List indSearchPrmPrt(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM15.getPrmPrtList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得促销活动信息
	 * @param map
	 * @return List
	 */
	public List indSearchPrmActName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM15.getPrmActNameList");
		return baseServiceImpl.getList(map);
	}
}
