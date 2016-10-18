package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWPSAL13_Service extends BaseService{

	public Integer getCardId(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL13.getCardId");
        return baseServiceImpl.getSum(paramMap);
    }

	public void relation(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL13.relation");
        baseServiceImpl.update(paramMap);
	}

	public Map<String, Object> getMemberIdByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL13.getMemberIdByCode");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}

}
