package com.cherry.ct.common.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLCTCOM09_Service{
	
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBrandAndOrgByLonginName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM09.getBrandAndOrgByLonginName");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
}
