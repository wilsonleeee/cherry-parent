package com.cherry.ct.pms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLCTPMS01_Service  extends BaseService{

	public Map<String, Object> getParamCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPMS01.getParamCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	public List<Map<String, Object>> getParamList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPMS01.getParamList");
		return baseServiceImpl.getList(paramMap);
	}
	
	public int updateParam(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPMS01.updateParam");
		return baseServiceImpl.update(paramMap);
	}
	
	public int checkParam(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPMS01.checkParam");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public void insertParam(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTPMS01.insertParam");
		baseServiceImpl.save(paramMap);
	}
}
