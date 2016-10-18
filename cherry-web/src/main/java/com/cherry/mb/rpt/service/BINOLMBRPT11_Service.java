package com.cherry.mb.rpt.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 扫码关注报表Service
 * 
 * @author Hujh
 * @version 1.0 2015/11/11
 */
public class BINOLMBRPT11_Service extends BaseService {
	
	/**
	 * 扫码关注数量
	 * @param map
	 * @return
	 */
	public int getSubscribeCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT11.getSubscribeCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 扫码关注List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubscribeList(Map<String, Object> map) {
		
		return baseServiceImpl.getList(map, "BINOLMBRPT11.getSubscribeList");
	}

	 /**
     * 取得柜台主管名称List
     * @param map
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterBAS(Map<String, Object> map) {

		return baseServiceImpl.getList(map, "BINOLMBRPT11.getCounterBAS");
	}

}
