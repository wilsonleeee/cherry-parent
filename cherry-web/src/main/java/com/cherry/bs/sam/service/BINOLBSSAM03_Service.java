package com.cherry.bs.sam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLBSSAM03_Service extends BaseService{

	public int getPayrollCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM03.getPayrollCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public List<Map<String, Object>> getPayrollList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSSAM03.getPayrollList");
		return baseServiceImpl.getList(paramMap);
	}
}
