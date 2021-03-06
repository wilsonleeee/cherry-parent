package com.cherry.st.bil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
@SuppressWarnings("unchecked")
public class BINOLSTBIL01_Service extends BaseService {
    /**
     * 取得入库单总数
     * 
     * @param map
     * @return 
     */
	public int getPrtInDepotCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL01.getPrtInDepotCount");
		return (Integer) baseServiceImpl.get(map);
	}
	
	/**
	 * 取得入库单list
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPrtInDepotList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL01.getPrtInDepotList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL01.getSumInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品入库单记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL01.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品入库单详细导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtInDepotExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL01.getPrtInDepotExportList");
		return baseServiceImpl.getList(parameterMap);
	}
}
