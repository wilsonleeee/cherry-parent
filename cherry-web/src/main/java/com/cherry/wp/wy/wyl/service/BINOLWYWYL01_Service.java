package com.cherry.wp.wy.wyl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLWYWYL01_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubCampaignList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWYWYL01.getSubCampaignList");
        return baseServiceImpl.getList(paramMap);
    }
}
