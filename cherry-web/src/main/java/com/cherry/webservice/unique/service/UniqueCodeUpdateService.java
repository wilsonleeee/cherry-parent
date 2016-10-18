package com.cherry.webservice.unique.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 保存扫码关注信息 接口Service
 * 
 * @author zhouwei
 * @version 2016-06-12 1.0.0
 */
public class UniqueCodeUpdateService extends BaseService {

	/**
	 * 查询积分唯一码是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrtUnique(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,  "UniqueCodeUpdate.getPrtUnique");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新新后台的唯一码使用状态等信息
	 * @param map
	 * @return
	 */
	public int updPrtUniqueInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "UniqueCodeUpdate.UpdPrtUniqueInfo");
		return baseServiceImpl.update(paramMap);
	}
	
}
