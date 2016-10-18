package com.cherry.st.bil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

@SuppressWarnings("unchecked")
public class BINOLSTBIL07_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;

	/**
	 * 取得总数
	 * 
	 * @param map
	 * @return
	 */
	public int getShiftCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getShiftCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得List
	 * 
	 * @param map
	 * @return
	 */
	public List getShiftList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getShiftList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取产品移库单记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getExportDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品移库单详细导出List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtShiftExportList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL07.getPrtShiftExportList");
		return baseServiceImpl.getList(parameterMap);
	}
}
