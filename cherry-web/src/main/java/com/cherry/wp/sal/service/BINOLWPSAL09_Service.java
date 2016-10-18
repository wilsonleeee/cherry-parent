package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLWPSAL09_Service extends BaseService{
	
	public int checkCounterPassword(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL09.checkCounterPassword");
		return CherryUtil.obj2int(baseServiceImpl.get(paramMap));
	}
	
	public void bindCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL09.bindCounter");
		baseServiceImpl.save(map);
	}
	
	public int unBindCounter(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL09.unBindCounter");
		return baseServiceImpl.update(map);
	}
}
