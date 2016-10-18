package com.cherry.cm.gadget.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class GadgetService extends BaseService {
	
	/**
	 * 取得用户自定义配置的小工具List
	 * 
	 * @param map 查询条件
	 * @return 小工具List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUserGadgetInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "gadget.getUserGadgetInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得系统默认配置的小工具List
	 * 
	 * @param map 查询条件
	 * @return 小工具List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSysGadgetInfoList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "gadget.getSysGadgetInfoList");
		return baseServiceImpl.getList(map);
	}

}
