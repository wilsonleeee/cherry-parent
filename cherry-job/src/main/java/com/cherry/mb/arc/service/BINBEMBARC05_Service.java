package com.cherry.mb.arc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 汇美舍官网奖励积分导入处理Service
 * @author menghao
 *
 */
public class BINBEMBARC05_Service extends BaseService {
	
	/**
	 * 更新数据同步控制的状态
	 * @param map
	 * @return
	 */
	public int updateGetStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC05.updateGetStatus");
		return ifServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得接口表中处于处理中的积分奖励数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPointChangeInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBARC05.getPointChangeInfo");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 批量更新处理过的积分奖励接口数据的状态
	 * @param list
	 */
	public void updateGetStatusEnd(List<Map<String, Object>> list) {
		ifServiceImpl.updateAll(list, "BINBEMBARC05.updateGetStatusEnd");
	}

}
