package com.cherry.bs.sam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSSAM01_Service  extends BaseService{

	public int getScheduleCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM01.getScheduleCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public List<Map<String, Object>> getScheduleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM01.getScheduleList");
		return baseServiceImpl.getList(paramMap);
	}
}
