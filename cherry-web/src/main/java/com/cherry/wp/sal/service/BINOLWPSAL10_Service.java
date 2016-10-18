package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWPSAL10_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCouponOrderProduct(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL10.getCouponOrderProduct");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCouponOrderInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL10.getCouponOrderInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
}
