package com.cherry.ct.common.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLCTCOM07_Service extends BaseService{
	/**
     * 通过会员卡号获取会员ID
     * 
     * @param map
     * @return
     * 		会员ID
     */
	public String getMemberIdByCode(String memberCode) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("memberCode", memberCode);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM07.getMemberIdByCode");
        return (String) baseServiceImpl.get(paramMap);
    }
	
	/**
     * 通过会员ID获取会员信息
     * 
     * @param map
     * @return
     * 		会员ID
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfoById(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM07.getMemberInfoById");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
}
