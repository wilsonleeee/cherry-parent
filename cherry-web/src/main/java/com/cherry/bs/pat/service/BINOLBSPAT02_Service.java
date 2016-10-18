package com.cherry.bs.pat.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;

public class BINOLBSPAT02_Service {
	@Resource
	private BaseServiceImpl baseServiceImpl;
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> partnerDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT02.partnerDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	public void addPartner(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT02.addPartner");
		baseServiceImpl.save(map);
	}

	public String getCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT02.getCount");
		return (String)baseServiceImpl.get(parameterMap);
	}
	public String checkCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT02.checkCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	public void tran_Partnersave(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPAT02.updatePartner");
		baseServiceImpl.save(map);
	}
	
}