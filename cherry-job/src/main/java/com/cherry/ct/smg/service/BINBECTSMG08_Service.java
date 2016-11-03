package com.cherry.ct.smg.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBECTSMG08_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCouponByPhone(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBECTSMG08.getCouponByPhone");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
	
}
