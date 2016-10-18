package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBCCT05_Service extends BaseService{
	/**
	 * 新增非会员资料
	 * 
	 * @param map
	 * @return 
	 */
	public void insertCustomer(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBCCT05.insertCustomer");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 修改非会员资料
	 * 
	 * @param map
	 * @return 
	 */
	public void updateCustomer(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBCCT05.updateCustomer");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 获取非会员资料信息
	 * @param map
	 * @return 非会员信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCustomerInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT05.getCustomerInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
}
