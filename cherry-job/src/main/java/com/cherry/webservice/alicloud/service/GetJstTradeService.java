package com.cherry.webservice.alicloud.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 获取聚石塔订单数据
 * 
 * @author jijw
 *
 * @version  2015-9-22
 *
 */
public class GetJstTradeService extends BaseService {
	
	/**
	 * 取得聚石塔订单信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getJstTradeList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"GetJstTrade.getJstTradeList");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 *  取得聚石塔订单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getJstTradeCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "GetJstTrade.getJstTradeCount");
		return baseServiceImpl.getSum(map);
	}
}
