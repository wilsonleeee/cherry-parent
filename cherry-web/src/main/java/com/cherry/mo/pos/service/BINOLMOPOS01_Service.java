package com.cherry.mo.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOPOS01_Service extends BaseService {
	
	public List<Map<String, Object>> getStorePayConfigList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPOS01.getStorePayConfigList");
		return baseServiceImpl.getList(paramMap);
	}

	public int getListCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPOS01.getListCount");
		return baseServiceImpl.getSum(paramMap);
	}

	public void addStorePayConfig(List<Map<String,Object>> plist) {
		baseServiceImpl.saveAll(plist, "BINOLMOPOS01.addStorePayConfig");
	}

	public List<Map<String, Object>> getStorePayCodeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPOS01.getStorePayCodeList");
		return baseServiceImpl.getList(paramMap);
	}

	public void delStorePayConfig(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPOS01.delStorePayConfig");
		baseServiceImpl.update(paramMap);
	}

	public void editStorePayConfig(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLMOPOS01.editStorePayConfig");
	}

	public void editDefaultPay(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOPOS01.editDefaultPay");
		baseServiceImpl.update(paramMap);
	}

}
