package com.cherry.wp.wr.krp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 库存报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/09/24
 */
public class BINOLWRKRP99_Service extends BaseService {
	
	/**
	 * 获取产品库存数量
	 * 
	 * @param map 查询条件
	 * @return 产品库存数量
	 */
	public int getProStockCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRKRP99.getProStockCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取产品库存统计信息
	 * 
	 * @param map 查询条件
	 * @return 产品库存统计信息
	 */
	public Map<String, Object> getProStockCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRKRP99.getProStockCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取产品库存List
	 * 
	 * @param map 查询条件
	 * @return 产品库存List
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRKRP99.getProStockList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据部门ID取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCouInfoByCouId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRKRP99.getCouInfoByCouId");
		return (Map)baseServiceImpl.get(paramMap);
	}

}
