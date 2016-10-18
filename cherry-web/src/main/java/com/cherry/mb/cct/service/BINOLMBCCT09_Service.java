package com.cherry.mb.cct.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLMBCCT09_Service extends BaseService{
	/**
     * 获取来电记录List
     * 
     * @param map
     * @return
     * 		来电记录List
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCallLogList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT09.getCallLogList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 获取来电记录数量
     * 
     * @param map
     * @return
     * 		来电记录数量
     */
    public int getCallLogCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBCCT09.getCallLogCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
    }
}
