package com.cherry.ct.tpl.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLCTTPL02_Service extends BaseService {
	/**
	 * 插入沟通模板信息
	 * 
	 * @param map
	 * @return 
	 */
	public void insertTemplate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTTPL02.insertTemplate");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新沟通模板信息
	 * 
	 * @param map
	 * @return 
	 */
	public void updateTemplate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCTTPL02.updateTemplate");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 根据模板编号查询模板信息
	 * @param map
	 * @return 模板信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTemplateInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTTPL02.getTemplateInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
}
