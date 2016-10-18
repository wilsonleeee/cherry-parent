package com.cherry.bs.wem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLBSWEM05_Service extends BaseService {

	/**
     * 获取分成统计List
     * 
     * @param map
     * @return
     * 		分成统计List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBonusList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM05.getBonusList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取分成微商数量
     * 
     * @param map
     * @return
     * 		分成微商数量
     */
	public int getBonusCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM05.getBonusCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
}
