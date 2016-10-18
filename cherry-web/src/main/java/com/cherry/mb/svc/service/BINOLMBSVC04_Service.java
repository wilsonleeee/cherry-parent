package com.cherry.mb.svc.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBSVC04_Service extends BaseService{
	
	public Map<String,Object> getRangeInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC04.getRangeInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}

	
	public void updateRangeInfo(Map<String,Object> params){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(params);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC04.updateRangeInfo");
		 baseServiceImpl.save(paramMap);
	}
	
	public void insertRangeInfo(Map<String,Object> params){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(params);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC04.insertRangeInfo");
		 baseServiceImpl.save(paramMap);
	}
}
