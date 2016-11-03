package com.cherry.sld.sc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBESLDSC01_Service  extends BaseService{

	public List<Map<String, Object>> getMessageInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESLDSC01.getMessageInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	public void tran_insertUpdEmpSalary(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBESLDSC01.insertUpdEmpSalary");
        baseServiceImpl.save(parameterMap);
	}
}
