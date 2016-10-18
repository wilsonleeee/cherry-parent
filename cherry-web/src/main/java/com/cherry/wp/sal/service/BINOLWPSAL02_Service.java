package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWPSAL02_Service extends BaseService{
		
	public String getUserBindCounterCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL02.getUserBindCounterCode");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	public String getCounterPhone(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL02.getCounterPhone");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	public String getCounterAddress(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL02.getCounterAddress");
		return (String)baseServiceImpl.get(paramMap);
	}
}
